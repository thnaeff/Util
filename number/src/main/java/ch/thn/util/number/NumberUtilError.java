package ch.thn.util.number;

/**
 * An error caused by a {@link NumberUtil} operation.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class NumberUtilError extends Error {
  private static final long serialVersionUID = 7533654119310669203L;

  public NumberUtilError(String message) {
    super(message);
  }

}
