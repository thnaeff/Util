package ch.thn.util.gui.component.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 * The table model holds the data and the column definitions of a table.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class SimpleTableModel extends AbstractTableModel implements SimpleTableColumnListener {
  private static final long serialVersionUID = -3646748986368264684L;


  private ArrayList<SimpleTableColumn> columns = null;

  /**
   * rows, columns
   */
  private ArrayList<ArrayList<Object>> data = null;

  private TableColumnModel columnModel = null;

  /**
   *
   *
   * @param columns
   */
  public SimpleTableModel(SimpleTableColumn[] columns) {

    this.columns = new ArrayList<SimpleTableColumn>(columns.length);
    this.data = new ArrayList<ArrayList<Object>>();


    if (columns != null) {
      for (int i = 0; i < columns.length; i++) {
        columns[i].addSimpleTableColumnListener(this);
        this.columns.add(columns[i]);
      }
    }


  }

  /**
   *
   *
   * @param columnModel
   */
  protected void setColumnModel(TableColumnModel columnModel) {
    this.columnModel = columnModel;

    updateColumnSizes();
  }

  /**
   *
   * @param column
   * @param columnIndex
   * @return
   */
  private boolean addColumnInternal(int columnIndex, SimpleTableColumn column) {
    if (columnIndex < 0 || column == null) {
      return false;
    }

    columns.add(columnIndex, column);

    column.addSimpleTableColumnListener(this);

    // Fill all the existing rows in the new column with the given default value
    for (int i = 0; i < data.size(); i++) {
      data.get(i).add(columnIndex, columns.get(columnIndex).getDefaultValue());
    }

    return true;
  }

  /**
   *
   * @param column
   */
  public void addColumn(SimpleTableColumn column) {
    if (addColumnInternal(columns.size(), column)) {
      fireTableStructureChanged();
    }
  }

  /**
   *
   *
   * @param column
   */
  public void addColumn(int columnIndex, SimpleTableColumn column) {
    if (addColumnInternal(columnIndex, column)) {
      fireTableStructureChanged();
    }
  }

  /**
   *
   *
   * @param column
   */
  public void addColumns(SimpleTableColumn... columns) {
    boolean atLeastOneAdded = false;

    for (int i = 0; i < columns.length; i++) {
      if (addColumnInternal(this.columns.size(), columns[i])) {
        atLeastOneAdded = true;
      }
    }

    if (atLeastOneAdded) {
      fireTableStructureChanged();
    }
  }

  /**
   *
   *
   * @param firstColumnIndex
   * @param columns
   */
  public void addColumns(int firstColumnIndex, SimpleTableColumn... columns) {
    boolean atLeastOneAdded = false;

    for (int i = 0; i < columns.length; i++) {
      if (addColumnInternal(firstColumnIndex++, columns[i])) {
        atLeastOneAdded = true;
      }
    }

    if (atLeastOneAdded) {
      fireTableStructureChanged();
    }
  }


  /**
   *
   *
   * @param rowIndex
   * @return
   */
  private boolean addRowInternal(int rowIndex) {
    if (rowIndex < 0) {
      return false;
    }

    ArrayList<Object> list = new ArrayList<Object>(columns.size());

    // Fill all the fields in the new row with the given default value
    for (int i = 0; i < columns.size(); i++) {
      list.add(columns.get(i).getDefaultValue());
    }

    data.add(rowIndex, list);
    return true;
  }

  /**
   * Adds a new row at the end. All cells are filled with the default value.
   *
   */
  public void addRow() {
    if (addRowInternal(data.size())) {
      fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
  }

  /**
   * Adds a new row at the given index. All cells are filled with the default value.
   *
   * @param rowIndex
   */
  public void addRow(int rowIndex) {
    if (addRowInternal(rowIndex)) {
      fireTableRowsInserted(rowIndex, rowIndex);
    }
  }

  /**
   * Adds the given numberOfRows at the end. All cells are filled with the default value.
   *
   * @param numberOfRows
   */
  public void addRows(int numberOfRows) {
    boolean atLeastOneAdded = false;

    for (int i = 0; i < numberOfRows; i++) {
      if (addRowInternal(data.size())) {
        atLeastOneAdded = true;
      }
    }

    if (atLeastOneAdded) {
      fireTableRowsInserted(data.size() - numberOfRows, data.size() - 1);
    }
  }

  /**
   * Adds the given numberOfRows at the given row index. All cells are filled with the default
   * value.
   *
   * @param rowIndex
   * @param numberOfRows
   */
  public void addRows(int rowIndex, int numberOfRows) {
    int oldIndex = rowIndex;
    boolean atLeastOneAdded = false;

    for (int i = 0; i < numberOfRows; i++) {
      if (addRowInternal(rowIndex++)) {
        atLeastOneAdded = true;
      } else {
        // If adding one fails stop immediately. Otherwise, the range for
        // fireTableRowsInserted would not be correct
        break;
      }
    }

    if (atLeastOneAdded) {
      fireTableRowsInserted(oldIndex, oldIndex + numberOfRows - 1);
    }
  }

  /**
   * Adds the given row values at the given index. If there are not enough values given, the default
   * value is set for the rest.
   *
   * @param rowIndex
   * @param values
   * @return <code>true</code> if adding was successful, <code>false</code> if not (because the row
   *         index was out of bounds or no values given)
   */
  private boolean addRowValuesInternal(int rowIndex, Object... values) {
    if (rowIndex < 0) {
      return false;
    }

    data.add(rowIndex, prepareRowValues(values));
    return true;
  }

  /**
   * Sets the given row values at the given index. If there are not enough values given, the default
   * value is set for the rest.
   *
   * @param rowIndex
   * @param values
   * @return <code>true</code> if setting was successful, <code>false</code> if not (because the row
   *         index was out of bounds or no values given)
   */
  private boolean setRowValuesInternal(int rowIndex, Object... values) {
    if (rowIndex < 0) {
      return false;
    }

    data.set(rowIndex, prepareRowValues(values));
    return true;
  }

  /**
   *
   *
   * @param values
   * @return
   */
  private ArrayList<Object> prepareRowValues(Object... values) {

    ArrayList<Object> list = new ArrayList<Object>(columns.size());

    // Fill all the fields in the new row with the given values. If there
    // are not enough values, fill the rest with the given default value
    for (int i = 0; i < columns.size(); i++) {
      if (i < values.length) {
        list.add(values[i]);
      } else {
        list.add(columns.get(i).getDefaultValue());
      }
    }

    return list;
  }

  /**
   *
   *
   * @param rowIndex
   * @param values
   */
  public void addRowAt(int rowIndex, Object... values) {
    if (addRowValuesInternal(rowIndex, values)) {
      fireTableRowsInserted(rowIndex, rowIndex);
    }
  }

  /**
   * Adds a new row to the table and fills the row with the given values.<br>
   * If there are more values than columns, the exceeding values are ignored. If there are less
   * values than columns, the missing values are filled with <code>null</code>.
   *
   * @param values
   */
  public void addRow(Object... values) {
    if (addRowValuesInternal(data.size(), values)) {
      fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
  }

  /**
   *
   *
   * @param columnIndex
   * @param
   */
  private boolean removeColumnInternal(int columnIndex) {
    if (columnIndex < 0) {
      return false;
    }

    SimpleTableColumn tc = columns.remove(columnIndex);

    if (tc == null) {
      return false;
    }

    tc.removeSimpleTableColumnListener(this);

    // Remove all the existing data in the column
    for (int i = 0; i < data.size(); i++) {
      data.get(i).remove(columnIndex);
    }

    return true;
  }

  /**
   *
   *
   * @param column
   */
  public void removeColumn(SimpleTableColumn column) {
    if (removeColumnInternal(columns.indexOf(column))) {
      fireTableStructureChanged();
    }
  }

  /**
   *
   *
   * @param columns
   */
  public void removeColumns(SimpleTableColumn... columns) {
    boolean atLeastOneRemoved = false;

    for (int i = 0; i < columns.length; i++) {
      if (removeColumnInternal(this.columns.indexOf(columns[i]))) {
        atLeastOneRemoved = true;
      }
    }

    if (atLeastOneRemoved) {
      fireTableStructureChanged();
    }
  }

  /**
   *
   *
   * @param columnIndex
   */
  public void removeColumn(int columnIndex) {
    if (removeColumnInternal(columnIndex)) {
      fireTableStructureChanged();
    }
  }

  /**
   *
   *
   * @param columnIndexes
   */
  public void removeColumns(int... columnIndexes) {
    boolean atLeastOneRemoved = false;
    Arrays.sort(columnIndexes);

    // Delete the column with the highest index first, because all the columns
    // with a higher index will shift one lower
    for (int i = columnIndexes.length - 1; i >= 0; i--) {
      if (removeColumnInternal(columnIndexes[i])) {
        atLeastOneRemoved = true;
      }
    }

    if (atLeastOneRemoved) {
      fireTableStructureChanged();
    }
  }

  /**
   *
   *
   * @param rowIndex
   */
  public void removeRow(int rowIndex) {
    if (data.remove(rowIndex) != null) {
      fireTableRowsDeleted(rowIndex, rowIndex);
    }
  }

  /**
   *
   *
   * @param rowIndexes
   */
  public void removeRows(int... rowIndexes) {
    boolean atLeastOneRemoved = false;

    Arrays.sort(rowIndexes);

    // Delete the row with the highest index first, because all the rows
    // with a higher index will shift one lower
    for (int i = rowIndexes.length - 1; i >= 0; i--) {
      if (data.remove(rowIndexes[i]) != null) {
        atLeastOneRemoved = true;
      }
    }

    if (atLeastOneRemoved) {
      // Assuming that the javadoc means "Notifies all listeners that some or all
      // rows in the given range have been deleted"
      fireTableRowsDeleted(rowIndexes[0], rowIndexes[rowIndexes.length - 1]);
    }
  }


  /**
   * Deletes all the rows within the given range (including the <code>from</code> and
   * <code>to</code> value). The range can be increasing (from < to) or decreasing (from > to).
   *
   * @param from
   * @param to
   */
  public void removeRowRange(int from, int to) {
    boolean atLeastOneRemoved = false;

    // Delete the row with the highest index first, because all the rows
    // with a higher index will shift one lower
    if (from <= to) {
      // "from" is lower than "to" or equal
      for (int i = to; i >= from; i--) {
        if (data.remove(i) != null) {
          atLeastOneRemoved = true;
        }
      }
    } else {
      // "to" is lower than "from"
      for (int i = from; i >= to; i--) {
        if (data.remove(i) != null) {
          atLeastOneRemoved = true;
        }
      }
    }

    if (atLeastOneRemoved) {
      fireTableRowsDeleted(from, to);
    }
  }

  /**
   * Returns the column at the given index.
   *
   * @param columnIndex
   * @return
   */
  public SimpleTableColumn getColumn(int columnIndex) {
    return columns.get(columnIndex);
  }

  /**
   * Returns the index of the given column
   *
   * @param column
   * @return
   */
  public int getColumnIndex(SimpleTableColumn column) {
    return columns.indexOf(column);
  }

  /**
   *
   *
   */
  public void clearData() {
    int size = data.size();
    data.clear();

    if (size > 0) {
      fireTableRowsDeleted(0, size - 1);
    }
  }

  /**
   *
   *
   */
  protected void updateColumnSizes() {
    if (columnModel != null) {
      for (int i = 0; i < columnModel.getColumnCount(); i++) {
        SimpleTableColumn column = columns.get(i);

        if (column.getMinWidth() != 0) {
          columnModel.getColumn(i).setMinWidth(column.getMinWidth());
        }

        if (column.getMaxWidth() != 0) {
          columnModel.getColumn(i).setMaxWidth(column.getMaxWidth());
        }
      }
    }
  }


  @Override
  public int getColumnCount() {
    return columns.size();
  }

  @Override
  public int getRowCount() {
    return data.size();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columns.get(columnIndex).isEditable();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (data.get(rowIndex) == null) {
      throw new IndexOutOfBoundsException("Row "
          + rowIndex
          + " does not exist");
    }

    return data.get(rowIndex).get(columnIndex);
  }

  /**
   * Returns the values for a entire row
   *
   * @param rowIndex
   * @return
   */
  public List<Object> getValues(int rowIndex) {
    return data.get(rowIndex);
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (data.size() < rowIndex || columns.size() < columnIndex) {
      return;
    }

    data.get(rowIndex).set(columnIndex, aValue);
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  /**
   * Sets the given values for the row at the given index in the table. If not enough values are
   * given (less than the number of columns), the default value is set for any missing ones.
   *
   * @param rowIndex
   * @param values
   */
  public void setRow(int rowIndex, Object... values) {
    setRowValuesInternal(rowIndex, values);
    fireTableRowsUpdated(rowIndex, rowIndex);
  }

  @Override
  public String getColumnName(int column) {
    return columns.get(column).getColumnTitle().toString();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return columns.get(columnIndex).getColumnClass();
  }



  @Override
  public void columnChanged(SimpleTableColumn column) {
    fireTableStructureChanged();
    // fireTableStructureChanged creates new columns (if setAutoCreateColumnsFromModel
    // is set to true, which it should be to let JTable create columns and update
    // their titles etc.). Thus, the widths of the columns are lost. Thats
    // why the updateColumnSizes has to be done after each fireTableStructureChanged
    updateColumnSizes();
  }

}
