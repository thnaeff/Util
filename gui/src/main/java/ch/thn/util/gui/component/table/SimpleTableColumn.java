package ch.thn.util.gui.component.table;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * When setting up a table, it is defined per column how data has to be displayed (with a table cell
 * renderer) and how data has to be edited (with a table cell editor). This column class is used to
 * do these configurations for a column.<br />
 * <br />
 * <br />
 * In addition, this table column implementation offers the functionality to define multiple
 * renderers and editors, one for each value class type (e.g. all strings should use a certain
 * editor, whereas all boolean values should use another one). This allows for different data types
 * in one table column (which is against the regular table implementation where each column has one
 * data type).
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class SimpleTableColumn {

  private HashMap<Class<?>, TableCellRenderer> renderers = null;
  private HashMap<Class<?>, TableCellEditor> editors = null;

  /**
   * The first defined class is stored here so that something can be returned for
   * {@link #getColumnClass()}
   */
  private Class<?> firstClass = null;

  private ArrayList<SimpleTableColumnListener> listeners = null;

  private Object columnTitle = null;
  private Object defaultValue = null;

  private boolean editable = false;

  private int minWidth = 0;
  private int maxWidth = 0;


  /**
   *
   *
   * @param columnTitle
   */
  public SimpleTableColumn(Object columnTitle) {
    this(columnTitle, String.class, null, null, null, false, 30, 0);
  }

  /**
   *
   *
   * @param columnTitle
   * @param columnClass
   */
  public SimpleTableColumn(Object columnTitle, Class<?> columnClass) {
    this(columnTitle, columnClass, null, null, null, false, 30, 0);
  }


  /**
   * Creates a new table column object which contains the preferences of a column.<br>
   * <br>
   * Any changes done to a {@link SimpleTableColumn} object notifies the {@link SimpleTableModel} of
   * all the tables in which the column object is in (yes, it is possible to have one
   * {@link SimpleTableColumn} object in multiple tables). The table model then calls
   * <code>fireTableStructureChanged</code> to notify the table of an update.
   *
   * @param columnTitle The title of the column.
   * @param columnClass The class of which the cell content of this column has to be.
   * @param defaultValue The default value to be set in a cell of this column when a new empty row
   *        or this column is added.
   * @param columnRenderer A renderer for the given class
   * @param columnEditor An editor for the given class
   * @param editable If set to true, values in this column are editable per default
   */
  public SimpleTableColumn(Object columnTitle, Class<?> columnClass, Object defaultValue,
      TableCellRenderer columnRenderer, TableCellEditor columnEditor, boolean editable,
      int minWidth, int maxWidth) {
    this.columnTitle = columnTitle;
    this.defaultValue = defaultValue;
    this.editable = editable;
    this.minWidth = minWidth;
    this.maxWidth = maxWidth;

    renderers = new HashMap<Class<?>, TableCellRenderer>();
    editors = new HashMap<Class<?>, TableCellEditor>();
    listeners = new ArrayList<>();

    if (columnRenderer != null) {
      renderers.put(columnClass, columnRenderer);
    }

    if (columnEditor != null) {
      editors.put(columnClass, columnEditor);
    }

    firstClass = columnClass;
  }

  /**
   * Used internally for {@link SimpleTableModel} to register a listener which receives events about
   * column changes
   *
   * @param l
   */
  protected void addSimpleTableColumnListener(SimpleTableColumnListener l) {
    listeners.add(l);
  }

  /**
   * Used internally for {@link SimpleTableModel} to unregister a listener which received events
   * about column changes
   *
   * @param l
   */
  protected void removeSimpleTableColumnListener(SimpleTableColumnListener l) {
    listeners.remove(l);
  }

  /**
   * Used internally by {@link SimpleTableModel}.<br>
   * Notifies the registered objects about changes in the column
   */
  private void fireColumnChanged() {
    for (SimpleTableColumnListener l : listeners) {
      l.columnChanged(this);
    }
  }

  /**
   * Sets the given renderer for the given class. If data with the given class is added to this
   * column, then the renderer which is defined here is used to show the data.
   *
   * @param c
   * @param r
   */
  public void setRenderer(Class<?> c, TableCellRenderer r) {
    renderers.put(c, r);

    if (firstClass == null) {
      firstClass = c;
    }

    fireColumnChanged();
  }

  /**
   *
   *
   * @param c
   * @return
   */
  public TableCellRenderer getRenderer(Class<?> c) {
    return renderers.get(c);
  }

  /**
   *
   *
   * @param c
   * @return
   */
  public boolean hasRenderer(Class<?> c) {
    return renderers.containsKey(c);
  }

  /**
   *
   *
   * @param c
   * @param e
   */
  public void setEditor(Class<?> c, TableCellEditor e) {
    editors.put(c, e);

    if (firstClass == null) {
      firstClass = c;
    }

    fireColumnChanged();
  }

  /**
   *
   *
   * @param c
   * @return
   */
  public boolean hasEditor(Class<?> c) {
    return editors.containsKey(c);
  }

  /**
   *
   *
   * @param c
   * @return
   */
  public TableCellEditor getEditor(Class<?> c) {
    return editors.get(c);
  }

  /**
   *
   *
   * @param editable
   */
  public void setEditable(boolean editable) {
    this.editable = editable;
    fireColumnChanged();
  }

  /**
   *
   *
   * @return
   */
  public boolean isEditable() {
    return editable;
  }

  /**
   * @return the columnTitle
   */
  public Object getColumnTitle() {
    return columnTitle;
  }

  /**
   * @param columnTitle the columnTitle to set
   */
  public void setColumnTitle(Object columnTitle) {
    this.columnTitle = columnTitle;
    fireColumnChanged();
  }

  /**
   * @return the defaultValue
   */
  public Object getDefaultValue() {
    return defaultValue;
  }

  /**
   * @param defaultValue the defaultValue to set
   */
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * @param minWidth the minWidth to set
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
    fireColumnChanged();
  }

  /**
   * @return the minWidth
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * @param maxWidth the maxWidth to set
   */
  public void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
    fireColumnChanged();
  }

  /**
   * @return the maxWidth
   */
  public int getMaxWidth() {
    return maxWidth;
  }


  /**
   *
   *
   * @return
   */
  protected Class<?> getColumnClass() {
    return firstClass;
  }

}
