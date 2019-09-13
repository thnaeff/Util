package ch.thn.util.string;

/**
 * An error on the {@link StringUtil} class.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class StringUtilError extends Error {
  private static final long serialVersionUID = 6684988586075624559L;

  public StringUtilError(String message) {
    super(message);

  }

}
