package ch.thn.util.gui.component.table;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public interface SimpleTableColumnListener {

  /**
   * Is fired if anything on a {@link SimpleTableColumn} (title, renderer, ...) has changed
   *
   * @param column The column which has changed
   */
  public void columnChanged(SimpleTableColumn column);

}
