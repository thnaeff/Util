package ch.thn.util.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * A panel with a specific number of columns where label-component pairs can be added to. It also
 * offers parameters to configure the vertical, horizontal and column space and if there should be
 * divider lines shown.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class LabelledComponentPanel extends JPanel {
  private static final long serialVersionUID = 6955100393791654848L;

  private static int COL_WIDTH = 4;
  private static int LABEL = 0;
  private static int COMPONENT = 1;
  private static int SPACERVERTICAL = 2;

  private JPanel pContent = null;

  private int[] rows = null;

  private int hgap = 0;
  private int vgap = 0;
  private int colspace = 0;

  private boolean labelAlignmentRight = true;
  private boolean componentAlignmentRight = false;
  private boolean verticalDivider = true;
  private boolean horizontalDivider = true;

  /**
   * 
   * 
   * @param numberOfColumns
   */
  public LabelledComponentPanel(int numberOfColumns) {
    this(numberOfColumns, 5, 2, 10, true, false, false, false);
  }

  /**
   * 
   * 
   * @param numberOfColumns
   * @param verticalDivider
   * @param horizontalDivider
   */
  public LabelledComponentPanel(int numberOfColumns, boolean verticalDivider,
      boolean horizontalDivider) {
    this(numberOfColumns, 5, 2, 10, true, false, verticalDivider, horizontalDivider);
  }

  /**
   * 
   * 
   * @param numberOfColumns
   * @param hgap
   * @param vgap
   * @param colspace
   * @param labelAlignmentRight
   * @param componentAlignmentRight
   * @param verticalDivider
   * @param horizontalDivider
   */
  public LabelledComponentPanel(int numberOfColumns, int hgap, int vgap, int colspace,
      boolean labelAlignmentRight, boolean componentAlignmentRight, boolean verticalDivider,
      boolean horizontalDivider) {
    this.hgap = hgap;
    this.vgap = vgap;
    this.colspace = colspace;
    this.labelAlignmentRight = labelAlignmentRight;
    this.componentAlignmentRight = componentAlignmentRight;
    this.verticalDivider = verticalDivider;
    this.horizontalDivider = horizontalDivider;

    rows = new int[numberOfColumns];

    pContent = new JPanel(new GridBagLayout());

    setLayout(new BorderLayout());
    super.add(pContent, BorderLayout.NORTH);

  }

  @Override
  public void removeAll() {
    pContent.removeAll();
  }

  @Override
  public void remove(Component comp) {
    throw new UnsupportedOperationException("Operation not implemented");
  }

  @Override
  public void remove(int index) {
    throw new UnsupportedOperationException("Operation not implemented");
  }

  @Override
  public Component add(Component comp) {
    throw new UnsupportedOperationException(
        "Operation not supported. Use addLabelComponent instead.");
  }

  @Override
  public Component add(Component comp, int index) {
    throw new UnsupportedOperationException(
        "Operation not supported. Use addLabelComponent instead.");
  }

  @Override
  public void add(Component comp, Object constraints) {
    throw new UnsupportedOperationException(
        "Operation not supported. Use addLabelComponent instead.");
  }

  @Override
  public void add(Component comp, Object constraints, int index) {
    throw new UnsupportedOperationException(
        "Operation not supported. Use addLabelComponent instead.");
  }

  @Override
  public Component add(String name, Component comp) {
    throw new UnsupportedOperationException(
        "Operation not supported. Use addLabelComponent instead.");
  }

  /**
   * 
   * 
   * @param label
   * @param comp
   */
  public void addLabelComponent(String label, JComponent comp) {
    addLabelComponent(0, new JLabel(label), comp, false);
  }

  /**
   * 
   * 
   * @param label
   * @param comp
   */
  public void addLabelComponent(JLabel label, JComponent comp) {
    addLabelComponent(0, label, comp, false);
  }

  /**
   * 
   * 
   * @param column
   * @param label
   * @param comp
   */
  public void addLabelComponent(int column, String label, JComponent comp) {
    addLabelComponent(column, new JLabel(label), comp, false);
  }

  /**
   * 
   * 
   * @param column
   * @param label
   * @param comp
   */
  public void addLabelComponent(int column, JLabel label, JComponent comp) {
    addLabelComponent(column, label, comp, false);
  }

  /**
   * 
   * 
   * @param label
   * @param comp
   * @param fill
   */
  public void addLabelComponent(String label, JComponent comp, boolean fill) {
    addLabelComponent(0, new JLabel(label), comp, fill);
  }

  /**
   * 
   * 
   * @param label
   * @param comp
   * @param fill
   */
  public void addLabelComponent(JLabel label, JComponent comp, boolean fill) {
    addLabelComponent(0, label, comp, fill);
  }

  /**
   * 
   * 
   * @param column
   * @param label
   * @param comp
   * @param fill
   */
  public void addLabelComponent(int column, String label, JComponent comp, boolean fill) {
    addLabelComponent(column, new JLabel(label), comp, fill);
  }

  /**
   * 
   * 
   * @param column
   * @param label
   * @param comp
   * @param fill Ignores left/right alignment and fills entire space with given component
   */
  public void addLabelComponent(int column, JLabel label, JComponent comp, boolean fill) {

    if (column > rows.length - 1) {
      throw new LabelledComponentPanelError("Column "
          + column
          + " out of bounds. "
          + "Number of available columns: "
          + rows.length);
    }

    if (horizontalDivider && rows[column] > 0) {
      // There is already a row -> add the spacers
      pContent.add(createSeparator(JSeparator.HORIZONTAL), createConstraints(LABEL, column, true));
      pContent.add(createSeparator(JSeparator.HORIZONTAL),
          createConstraints(COMPONENT, column, true));

      if (verticalDivider && column < rows.length - 1) {
        pContent.add(createSeparator(JSeparator.VERTICAL),
            createConstraints(SPACERVERTICAL, column, true));
      }

      rows[column]++;
    }

    JPanel p1 =
        new JPanel(new FlowLayout(labelAlignmentRight ? FlowLayout.RIGHT : FlowLayout.LEFT));
    p1.add(label);

    JPanel p2 = new JPanel();
    p2.setLayout(new BorderLayout());

    if (fill) {
      p2.add(comp, BorderLayout.CENTER);
    } else {
      if (componentAlignmentRight) {
        p2.add(comp, BorderLayout.EAST);
      } else {
        p2.add(comp, BorderLayout.WEST);
      }
    }

    pContent.add(p1, createConstraints(LABEL, column, false));
    pContent.add(p2, createConstraints(COMPONENT, column, false));

    if (verticalDivider && column < rows.length - 1) {
      pContent.add(createSeparator(JSeparator.VERTICAL),
          createConstraints(SPACERVERTICAL, column, true));
    }

    rows[column]++;

  }


  /**
   * 
   * 
   * @param label
   * @param column
   * @param spacer
   * @return
   */
  private GridBagConstraints createConstraints(int x, int column, boolean spacer) {
    GridBagConstraints c = new GridBagConstraints();

    boolean label = x == LABEL;
    boolean component = x == COMPONENT;

    int top = 0;
    int left = 0;
    int bottom = 0;
    int right = 0;

    c.gridx = x + column * COL_WIDTH;
    c.gridy = rows[column];
    c.gridwidth = 1;
    c.gridheight = 1;

    if (label || component) {
      // A label or component position

      c.anchor = label ? GridBagConstraints.WEST : GridBagConstraints.EAST;
      c.fill = GridBagConstraints.HORIZONTAL;

      if (!component && !spacer) {
        right = hgap;
      }

      if (spacer) {
        top = vgap / 2;
        bottom = vgap / 2;
      }

      // Do not adjust the label width
      c.weightx = label ? 0 : 1.0;
      c.weighty = 1.0;
    } else {
      // Vertical spacer position

      c.weightx = 0;
      c.weighty = 0;
      c.fill = GridBagConstraints.VERTICAL;

      // Distribute the space equally to both sides of the spacer
      if (spacer) {
        left = colspace / 2;
        right = colspace / 2;
      }
    }

    c.insets = new Insets(top, left, bottom, right);

    return c;
  }


  /**
   * 
   * 
   * @param orientation
   * @return
   */
  private JSeparator createSeparator(int orientation) {
    JSeparator s = new JSeparator(orientation);

    s.setForeground(Color.lightGray);

    return s;
  }


  /*************************************************************************
   * 
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  private class LabelledComponentPanelError extends Error {
    private static final long serialVersionUID = -1467270182957413906L;

    /**
     * 
     * 
     * @param message
     */
    public LabelledComponentPanelError(String message) {
      super(message);
    }


  }

}
