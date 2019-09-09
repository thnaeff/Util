package ch.thn.util.gui.component.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 *
 * Code idea from: http://tips4java.wordpress.com/2009/02/27/default-table-header-cell-renderer/<br>
 * Modified so that any component can be set as column title
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class SimpleTableHeaderCellRenderer implements TableCellRenderer {


  private AbstractTableModel tableModel = null;



  public SimpleTableHeaderCellRenderer(AbstractTableModel tableModel) {
    this.tableModel = tableModel;

  }

  /**
   * If the column is sorted, the appropriate icon is retrieved from the current Look and Feel, and
   * a border appropriate to a table header cell is applied.
   * <P>
   *
   * @param table the <code>JTable</code>.
   * @param value the value to assign to the header cell
   * @param isSelected This parameter is ignored.
   * @param hasFocus This parameter is ignored.
   * @param row This parameter is ignored.
   * @param column the column of the header cell to render
   * @return the default table header cell renderer
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {

    Object o = null;

    if (tableModel instanceof SimpleTableModel) {
      // Any component can be set as title when using the SimpleTableModel
      o = ((SimpleTableModel) tableModel).getColumn(table.convertColumnIndexToModel(column))
          .getColumnTitle();
    } else {
      o = tableModel.getColumnName(table.convertColumnIndexToModel(column));
    }

    JComponent c = null;

    if (o instanceof JComponent) {
      c = (JComponent) o;
    } else {
      c = new JLabel(o.toString());
      c.setFont(c.getFont().deriveFont(Font.BOLD));
      c.setToolTipText(o.toString());
    }

    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

    // Set the tool tip text to the panel because somehow it does not show the
    // tool tip of the component
    p.setToolTipText(c.getToolTipText());

    // Sorting icon
    p.add(new JLabel(getIcon(table, column)), BorderLayout.EAST);
    // Header component
    p.add(c, BorderLayout.CENTER);

    return p;
  }

  /**
   * Returns an icon suitable to the primary sorted column, or null if the column is not the primary
   * sort key.
   *
   * @param table the <code>JTable</code>.
   * @param column the column index.
   * @return the sort icon, or null if the column is unsorted.
   */
  private Icon getIcon(JTable table, int column) {
    SortKey sortKey = getSortKey(table, column);

    if (sortKey != null && table.convertColumnIndexToView(sortKey.getColumn()) == column) {
      switch (sortKey.getSortOrder()) {
        case ASCENDING:
          return UIManager.getIcon("Table.ascendingSortIcon");
        case DESCENDING:
          return UIManager.getIcon("Table.descendingSortIcon");
        case UNSORTED:
          break;
        default:
          break;
      }
    }

    return null;
  }

  /**
   * Returns the current sort key, or null if the column is unsorted.
   *
   * @param table the table
   * @param column the column index
   * @return the SortKey, or null if the column is unsorted
   */
  private SortKey getSortKey(JTable table, int column) {
    RowSorter<?> rowSorter = table.getRowSorter();

    if (rowSorter == null) {
      return null;
    }

    List<?> sortedColumns = rowSorter.getSortKeys();

    if (sortedColumns.size() > 0) {
      return (SortKey) sortedColumns.get(0);
    }

    return null;
  }

}
