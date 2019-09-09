package ch.thn.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Creates a zip archive. Single files or whole directories can be added to the archive.
 *
 *
 * @author Thomas Naeff (thnaeff@gmail.com)
 */
public class ZipArchive {
  private final Logger logger = LoggerFactory.getLogger(ZipArchive.class);


  private List<File> files = null;


  /**
   * Creates a new zip archive object, which supports adding files and creating the actual zip file.
   *
   * @param filesOrDirectories A list of files or directories to add to the zip file. Directories
   *        are added recursively.
   */
  public ZipArchive(List<File> filesOrDirectories) {
    this.files = filesOrDirectories;
  }

  /**
   * Creates a new zip archive object, which supports adding files and creating the actual zip file.
   *
   * @param fileOrDirectory A list of files or directories to add to the zip file. Directories are
   *        added recursively.
   */
  public ZipArchive(File fileOrDirectory) {
    this();
    this.files.add(fileOrDirectory);
  }

  /**
   * Creates a new zip archive object, which supports adding files and creating the actual zip file.
   *
   */
  public ZipArchive() {
    this.files = new ArrayList<File>();
  }

  /**
   * Adds a new file or directory to the archive.
   *
   * @param fileOrDirectory A file or directory to add to the zip file. Directories are added
   *        recursively.
   */
  public void add(File fileOrDirectory) {
    files.add(fileOrDirectory);
  }

  /**
   * Zips all the files into the archive defined by the given zip archive.
   *
   * @param zipArchive The zip file to create
   * @return The output stream which was used to create the zip file
   */
  public OutputStream create(File zipArchive) throws IOException {

    OutputStream out = new FileOutputStream(zipArchive);
    zip(out);

    return out;

  }

  /**
   * Zips all the files into the archive defined by the given output stream.
   *
   * @param zipDestination The streeam of the zip file to create
   * @throws IOException Throws this exception if creating the zip file fails
   */
  private void zip(OutputStream zipDestination) throws IOException {
    ZipOutputStream out = null;

    try {
      out = new ZipOutputStream(zipDestination);

      for (File f : files) {
        zipIt(out, "", f);
      }

    } finally {
      if (out != null) {
        out.close();
      }
    }

  }

  /**
   * Adds all the given files or directories to the zip archive. The files/directories are added
   * under the sub directory <code>zipSubdir</code>.
   *
   * @param zout The zip archive to write to
   * @param zipSubdir The files/directories are added under this sub director
   * @param filesOrDirectories Files or directories to add. Directories are added recursively
   * @throws IOException If writing any of the files fails
   */
  private void zipIt(ZipOutputStream zout, String zipSubdir, File... filesOrDirectories)
      throws IOException, RuntimeException {

    if (zipSubdir != null && zipSubdir.length() > 0 && !zipSubdir.endsWith("/")
        && !zipSubdir.endsWith("\\")) {
      // Zip file directories need a forward slash "/" as separator
      zipSubdir = zipSubdir
          + "/";
    }

    for (File f : filesOrDirectories) {

      if (f.isFile()) {
        addFileToZip(zout, zipSubdir, f);
      } else if (f.isDirectory()) {
        addDirectoryToZip(zout, zipSubdir, f);
        zipIt(zout, zipSubdir
            + f.getName()
            + "/", f.listFiles());
      } else {
        throw new IOException(f.getPath()
            + " is not a file or a directory");
      }

    }

  }

  /**
   * Adds a new file to the zip archive. The file is added in the sub directory
   * <code>zipSubdir</code>.
   *
   * @param zos The zip archive to write to
   * @param zipSubdir The sub directory in which the file should be added
   * @param file The file to add
   * @throws IOException If writing to the zip archive fails
   */
  private void addFileToZip(ZipOutputStream zos, String zipSubdir, File file) throws IOException {

    logger.debug("Writing '{}' to zip archive", file.getAbsolutePath());

    FileInputStream fis = new FileInputStream(file);
    zos.putNextEntry(new ZipEntry(zipSubdir + file.getName()));

    byte[] bytes = new byte[1024];
    int length;
    while ((length = fis.read(bytes)) >= 0) {
      zos.write(bytes, 0, length);
    }

    zos.closeEntry();
    fis.close();

  }

  /**
   * Adds a directory to the zip archive output stream.
   *
   * @param zos The zip archive stream
   * @param subdir A sub directory to which the new directory should be added
   * @param dir The directory to add
   * @throws IOException If adding the directory fails
   */
  private void addDirectoryToZip(ZipOutputStream zos, String subdir, File dir) throws IOException {

    zos.putNextEntry(new ZipEntry(subdir
        + dir.getName()
        + "/"));
    zos.closeEntry();

  }

}
