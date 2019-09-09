/**
 * Copyright 2014 Thomas Naeff (github.com/thnaeff)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package ch.thn.util.gui.test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

import ch.thn.util.gui.component.table.SimpleTable;
import ch.thn.util.gui.component.table.SimpleTableColumn;
import ch.thn.util.gui.component.table.SimpleTableModel;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class SimpleTableTest extends JPanel implements TableModelListener {
  private static final long serialVersionUID = -1328522429741918090L;



  public SimpleTableTest() {
    setLayout(new BorderLayout());

    // SimpleTableColumn[] cols = new SimpleTableColumn[] {
    // new SimpleTableColumn("String 1", String.class),
    // new SimpleTableColumn("String 2", String.class),
    // new SimpleTableColumn("Boolean", Boolean.class)
    // };
    //
    // // cols[0].setRenderer(String.class, new StringCellRenderer());
    // // cols[1].setRenderer(String.class, new StringCellRenderer());
    // // cols[2].setRenderer(Boolean.class, new BooleanCellRenderer());
    //
    // SimpleTableModel tm = new SimpleTableModel(cols);
    // SimpleTable st = new SimpleTable(tm);
    //
    // tm.addRows(2);
    // tm.addRow("value 1", "value 2", true);

    // ---------------------------------------------------------------------



    JLabel lCol2 = new JLabel("Col2");
    lCol2.setToolTipText("tooltip");

    SimpleTableColumn[] cols = new SimpleTableColumn[] {
        new SimpleTableColumn("Col1", String.class, "default", null, null, false, 30, 0), // 0
        new SimpleTableColumn(lCol2, Boolean.class, null, null, null, true, 0, 0), // 1
        new SimpleTableColumn("Booleans", Boolean.class, null, null, null, true, 0, 0), // 2
        new SimpleTableColumn("Test", String.class, "test", null, null, true, 0, 0) // 3
    };

    cols[1].setRenderer(Boolean.class, new BooleanCellRenderer());
    cols[1].setRenderer(String.class, new StringCellRenderer());

    cols[2].setRenderer(Boolean.class, new BooleanCellRenderer());
    cols[2].setRenderer(String.class, new StringCellRenderer());

    cols[3].setRenderer(Boolean.class, new BooleanCellRenderer());
    cols[3].setRenderer(String.class, new StringCellRenderer());



    SimpleTableModel tm = new SimpleTableModel(cols);

    SimpleTable st = new SimpleTable(tm);

    st.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
    st.setShowVerticalLines(false);
    st.setIntercellSpacing(new Dimension(0, 1));

    // st.setAutoCreateColumnsFromModel(false);
    // st.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    cols[0].setColumnTitle("ABC");
    // cols[2].setMinWidth(50);
    // cols[3].setMinWidth(100);

    tm.addTableModelListener(this);
    tm.addRows(5);
    // tm.addColumn(new SimpleTableColumn("Test"));
    // tm.addColumn(0, new SimpleTableColumn("Test0"));
    // tm.addColumns(new SimpleTableColumn("Test2"));
    tm.addRow();
    tm.addRow(0);
    tm.addRow("Value1", "Value2", "Value3", "Value4");
    tm.addRowAt(3, "A", "B");
    tm.addRows(3);
    tm.addRows(0, 2);
    // tm.removeColumn(3);
    // tm.removeColumn(cols[1]);
    // tm.removeColumns(0, 1, 2);
    // tm.removeColumns(cols[0], cols[1]);
    // tm.removeRow(5);
    // tm.removeRowRange(0, 10);
    // tm.removeRows(2, 3, 4, 5, 6, 10);


    // tm.setValueAt("First row", 0, 0);
    // tm.setValueAt("Last row", 4, 0);
    //
    // tm.addRows(0, 5);
    //
    // tm.setValueAt(new Boolean(true), 1, 1);
    // tm.setValueAt(new Boolean(false), 2, 1);
    // tm.setValueAt("test", 2, 1);
    //
    // tm.removeRowRange(6, 8);
    //
    // tm.addRow("Value1", new Boolean(true));
    //
    // tm.addRows(2);

    // Want to color a entire row? get all renderers for a row and use them...
    for (int i = 0; i < tm.getColumnCount(); i++) {
      TableCellRenderer r = st.getCellRenderer(1, i);
      // ...
    }


    add(new JScrollPane(st), BorderLayout.CENTER);

  }



  @Override
  public void tableChanged(TableModelEvent e) {
    System.out.print("Table changed: firstRow="
        + e.getFirstRow()
        + ", lastRow="
        + e.getLastRow()
        + ", column="
        + e.getColumn()
        + ", type=");

    switch (e.getType()) {
      case TableModelEvent.DELETE:
        System.out.print("DELETE");
        break;
      case TableModelEvent.INSERT:
        System.out.print("INSERT");
        break;
      case TableModelEvent.UPDATE:
        System.out.print("UPDATE");
        break;
      default:
        System.out.print("?");
        break;
    }


    if (e.getFirstRow() >= 0 && e.getColumn() >= 0) {
      SimpleTableModel tm = (SimpleTableModel) e.getSource();
      System.out.println(", first value="
          + tm.getValueAt(e.getFirstRow(), e.getColumn()));
    } else {
      System.out.println(", first value=?");
    }

  }



}
