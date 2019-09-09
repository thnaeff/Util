package ch.thn.util.file;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class FileUtilError extends Error {
  private static final long serialVersionUID = -8903217344668976982L;



  public FileUtilError(String message) {
    super(message);
  }

  public FileUtilError(String message, Throwable cause) {
    super(message, cause);
  }

}
