package ch.thn.util.gui.component.table;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * A {@link JTable} with additional functionality. This table takes a {@link SimpleTableModel} which
 * is used to set up the table.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class SimpleTable extends JTable {
  private static final long serialVersionUID = 4868306360112011436L;

  private SimpleTableModel tableModel = null;

  /**
   *
   *
   * @param columns
   */
  public SimpleTable(AbstractTableModel model) {
    super(model);

    setModel(model);

    // TODO just set a custom table header for now. Maybe there are fun things to do with it...
    getTableHeader().setDefaultRenderer(new SimpleTableHeaderCellRenderer(model));

    setFillsViewportHeight(true);


    // http://www.chka.de/swing/table/faq.html
    // "Why is JTable so slow and why does it ask the renderer every time the mouse is moved?"
    // ToolTipManager.sharedInstance().unregisterComponent(this);
    // ToolTipManager.sharedInstance().unregisterComponent(getTableHeader());
  }

  @Override
  public void setModel(TableModel dataModel) {
    if (tableModel != null) {
      // Make sure the column model is not connected to the old table model any more
      tableModel.setColumnModel(null);
    }

    if (dataModel instanceof SimpleTableModel) {
      tableModel = (SimpleTableModel) dataModel;
      tableModel.setColumnModel(getColumnModel());
    } else {
      tableModel = null;
    }

    super.setModel(dataModel);
  }

  @Override
  public TableCellRenderer getCellRenderer(int row, int column) {

    if (tableModel != null) {
      int modelColumn = convertColumnIndexToModel(column);
      int modelRow = convertRowIndexToModel(row);
      Object value = tableModel.getValueAt(modelRow, modelColumn);

      if (value != null && tableModel.getColumn(modelColumn).hasRenderer(value.getClass())) {
        // Return a column specific renderer
        return tableModel.getColumn(modelColumn).getRenderer(value.getClass());
      }

    }

    // Returns the default cell renderer which matches the class returned with
    // TableModel.getColumnClass
    // Row and column seem to be converted to the model view internally
    return super.getCellRenderer(row, column);
  }

  @Override
  public TableCellEditor getCellEditor(int row, int column) {

    if (tableModel != null) {
      int modelColumn = convertColumnIndexToModel(column);
      int modelRow = convertRowIndexToModel(row);
      Object value = tableModel.getValueAt(modelRow, modelColumn);

      if (value != null && tableModel.getColumn(modelColumn).hasEditor(value.getClass())) {
        // Return a column specific renderer
        return tableModel.getColumn(modelColumn).getEditor(value.getClass());
      }

    }

    // Returns the default cell editor which matches the class returned with
    // TableModel.getColumnClass
    // Row and column seem to be converted to the model view internally
    return super.getCellEditor(row, column);
  }



}
