package ch.thn.util.gui.input.filechooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * <b>Important:</b> Only works if given in the constructor of {@link JFileChooser}!
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class DirectoryRestrictionFileSystemView extends FileSystemView {

  private File[] rootDirectories = null;

  public DirectoryRestrictionFileSystemView(File rootDirectory) {
    super();
    setRootDirectory(rootDirectory);
  }

  public DirectoryRestrictionFileSystemView(File[] rootDirectories) {
    super();
    this.rootDirectories = rootDirectories;
  }

  public void setRootDirectory(File rootDirectory) {
    this.rootDirectories = new File[] {rootDirectory};
  }

  public void setRootDirectories(File[] rootDirectories) {
    this.rootDirectories = rootDirectories;
  }

  @Override
  public File createNewFolder(File containingDir) {
    File folder = new File(containingDir, "New Folder");
    folder.mkdir();
    return folder;
  }

  @Override
  public File[] getRoots() {
    return rootDirectories;
  }

  @Override
  public File getDefaultDirectory() {
    return rootDirectories[0];
  }

  @Override
  public File getHomeDirectory() {
    return rootDirectories[0];
  }

}
