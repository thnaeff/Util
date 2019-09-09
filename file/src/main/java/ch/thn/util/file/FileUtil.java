package ch.thn.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class FileUtil {


  /**
   * Returns the separator which separates files/folders in a path (backslash "\" for windows, slash
   * "/" for unix, ...).
   *
   * @return The file system path separator
   */
  public static String getPathSeparator() {
    return File.separator;
  }

  /**
   * Returns the available filesystem roots.<br />
   * On an unix system this would return "/", whereas on a Windows system it returns "A:/", "C:/"
   * etc. depending on the available drives.
   *
   * @return An array with all file system roots
   * @see File#listRoots()
   */
  public static File[] getFileSystemRoots() {
    return File.listRoots();
  }

  /**
   * Checks the given path if it starts with any of the available file system roots.<br />
   * For more information about file system roots, see {@link File#listRoots()}
   *
   * @param path The file system root to look for. Checks all file system roots if they start with
   *        the given path
   * @return Whether a file system root starts with the given path or not
   */
  public static boolean hasFileSystemRoot(String path) {
    File[] roots = File.listRoots();
    for (int i = 0; i < roots.length; i++) {
      if (path.startsWith(roots[i].getPath())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Adds the path separator to the end of the path if not there yet.
   *
   * @param path The path to check
   * @return The checked and, if necessary, corrected path
   */
  public static String addPathSeparator(String path) {
    // Add path separator if not there
    if (path != null && !path.endsWith(File.separator)) {
      path = path + File.separator;
    }

    return path;
  }

  /**
   * Returns the current working directory
   *
   * @return The current working directory
   */
  public static String getWorkingDirectory() {
    // Since System.getProperty("user.dir") and user.home etc. can be confusing
    // and I haven't quite figured out if they really are reliable on all systems,
    // I chose a little more complicated approach. This is exactly the same
    // like if a file is created relatively, like new File("testfile.txt"), which
    // would be relative to the working directory.

    File f = new File(".");

    try {
      return addPathSeparator(f.getCanonicalPath());
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Returns the next existing parent directory of the given file.<br />
   * <br />
   * Example:<br />
   * /home/me/somefolder/file.test and "somefolder" does not exist, it returns /home/me/<br />
   * /home/me/somefolder/file.test and "somefolder" does exist, it returns
   * /home/me/somefolder/<br />
   *
   * @param file The file in whose path to look for the existing parent directory
   * @return The parent directory
   */
  public static File getNextExistingParentDirectory(File file) {

    file = file.getParentFile();

    while (file != null && !file.isDirectory()) {
      file = file.getParentFile();
    }

    return file;
  }


  /**
   * Deletes all files and sub-directories on the given path. The path can be a file or directory.
   * If it is a file, only the file is deleted (if <code>includePath=true</code>).
   *
   * @param path The path and all its sub-directories/files to delete
   * @param includePath If set to <code>true</code>, the directory the given path points to is
   *        deleted as well. If set to <code>false</code>, only files/directories within the path
   *        are deleted
   * @throws IOException When deleting of any directory or file on the path fails
   */
  public static void deleteAll(File path, boolean includePath) throws IOException {

    Files.walkFileTree(path.toPath(), new FileTreeWalker(FileTreeWalker.Action.DELETE));

    if (includePath && path.exists() && !path.delete()) {
      throw new IOException("Failed to delete directory or file "
          + path);
    }

  }

  /**
   * Copies all files and sub-directories on the given path, including the <code>path</code>
   * parameter directory. If <code>path</code> is just a file, only the file is copied.
   *
   * @param source The source directory
   * @param dest The destination directory
   * @param replaceExisting If set to <code>true</code>, existing files/directories are replaced
   * @throws IOException When copying of any directory or file on the path fails
   */
  public static void copyAll(File source, File dest, boolean replaceExisting) throws IOException {

    Files.walkFileTree(source.toPath(), new FileTreeWalker(FileTreeWalker.Action.COPY,
        source.toPath(), dest.toPath(), replaceExisting));

  }

  /************************************************************************************
   * Some code from the copy example in:
   * https://docs.oracle.com/javase/tutorial/essential/io/walk.html
   *
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  private static class FileTreeWalker extends SimpleFileVisitor<Path> {

    private enum Action {
      DELETE,
      COPY,
      MOVE;
    }

    private Action action = null;
    private Path source = null;
    private Path target = null;
    private CopyOption[] copyMoveOptions = null;

    /**
     * A new file tree walker for deleting.
     *
     * @param action The {@link Action} to use this walker for
     */
    public FileTreeWalker(Action action) {
      this(action, null, null, false);
    }

    /**
     * A new file tree walker for copying or moving.
     *
     * @param action The {@link Action} to use this walker for
     * @param source The source directory
     * @param target The target directory
     * @param replaceExisting If set to <code>true</code>, existing files/directories are replaced
     */
    public FileTreeWalker(Action action, Path source, Path target, boolean replaceExisting) {
      this.action = action;
      this.source = source;
      this.target = target;

      if (action == Action.COPY || action == Action.MOVE) {
        List<CopyOption> copyMoveOptionsTemp = new ArrayList<CopyOption>();
        copyMoveOptionsTemp.add(LinkOption.NOFOLLOW_LINKS);
        copyMoveOptionsTemp.add(StandardCopyOption.ATOMIC_MOVE);

        if (replaceExisting) {
          copyMoveOptionsTemp.add(StandardCopyOption.REPLACE_EXISTING);
        }

        copyMoveOptions = copyMoveOptionsTemp.toArray(new CopyOption[copyMoveOptionsTemp.size()]);
      }

    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException {

      if (action == Action.COPY || action == Action.MOVE) {
        copyOrMove(action, dir);
      }

      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

      if (action == Action.DELETE) {
        if (!file.toFile().delete()) {
          throw new IOException("Failed to delete directory or file "
              + file);
        }

        return FileVisitResult.CONTINUE;
      } else if (action == Action.COPY || action == Action.MOVE) {

        copyOrMove(action, file);

        return FileVisitResult.CONTINUE;
      }

      return FileVisitResult.TERMINATE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

      if (action == Action.COPY || action == Action.MOVE) {
        // fix up modification time of directory when done
        if (exc == null) {
          Path newdir = target.resolve(source.relativize(dir));
          try {
            FileTime time = Files.getLastModifiedTime(dir);
            Files.setLastModifiedTime(newdir, time);
          } catch (IOException e) {
            throw new IOException("Unable to copy all attributes to "
                + newdir, e);
          }
        }
      } else if (action == Action.DELETE) {
        // Delete visited directory

        if (!dir.toFile().delete()) {
          throw new IOException("Failed to delete directory "
              + dir);
        }
      }

      return FileVisitResult.CONTINUE;
    }

    /**
     * Copies or moves a directory or file. Retries if atomic operation does not work and if atomic
     * operation fails, the option is taken out of the list.
     *
     * @param action The copy or move action
     * @param dir The directory to copy
     * @throws IOException If copying or moving failed
     */
    private void copyOrMove(Action action, Path dir) throws IOException {

      try {
        if (action == Action.COPY) {
          Path newdir = target.resolve(source.relativize(dir));
          Files.copy(dir, newdir, copyMoveOptions);
        } else if (action == Action.MOVE) {
          Path newdir = target.resolve(source.relativize(dir));
          Files.move(dir, newdir, copyMoveOptions);
        }
      } catch (AtomicMoveNotSupportedException e) {
        // Remove the atomic move from the list and try again
        List<CopyOption> optionsTemp = Arrays.asList(copyMoveOptions);
        optionsTemp.remove(StandardCopyOption.ATOMIC_MOVE);
        copyMoveOptions = optionsTemp.toArray(new CopyOption[optionsTemp.size()]);

        copyOrMove(action, dir);
      }

    }

  }

}
