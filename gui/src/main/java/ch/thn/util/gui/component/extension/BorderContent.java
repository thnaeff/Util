package ch.thn.util.gui.component.extension;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;


/**
 * This border content class can be used to give any {@link JComponent} the ability to have one or
 * more other {@link JComponent}s within the border.<br>
 * <br>
 * Lets look at the example of a {@link JTextField}. This is about how an original
 * {@link JTextField} is set up (the visible border with "-" and "|", and the actual invisible text
 * field bounds with "="):
 *
 * <pre>
 * |---Text field border---|
 * | ===================== |
 * | = Text field        = |
 * | ===================== |
 * |-----------------------|
 * </pre>
 *
 * <br>
 * Now, how about we want to add a component (a {@link JButton} for example) to the right of the
 * text field, but we want the button to appear within the border? One solution would be to create a
 * class that extends {@link JPanel} with a border layout, then add the {@link JTextField} and the
 * {@link JButton} to that border layout, remove the border from the {@link JTextField} and add the
 * border to the {@link JPanel}. This would look just fine and exactly as expected.<br>
 * But the drawback would be that we have to deal with a {@link JPanel} now (since the
 * {@link JTextField} is within that {@link JPanel}). All the {@link JTextField} methods (and
 * inherited methods) which are needed would have to be implemented in the new class.<br>
 * <br>
 * The problem is solved with this {@link BorderContent} class. It class allows the adding of any
 * {@link JComponent} which is then placed between the border and the actual object.<br>
 * This is how adding a {@link JButton} "But" to a {@link JTextField} would look like when the
 * {@link BorderContent} class is used:
 *
 * <pre>
 * <code>
 * |-----Text field border-------|
 * | ===================== |---| |
 * | = Text field        = |But| |
 * | ===================== |---| |
 * |-----------------------------|
 * </code>
 * </pre>
 *
 * Now we have the benefit of still having a {@link JTextField} object, only that it appears with
 * the added {@link JButton}.<br>
 * <br>
 * ====== USAGE ======<br>
 * <br>
 * To use the {@link BorderContent} class the following steps are needed:<br>
 * <ul>
 * <li>In your class, create an instance of {@link BorderContent}</li>
 * <li>Add components to your {@link BorderContent} object, using the
 * {@link #addComponent(JComponent, int)} method.</li>
 * </ul>
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class BorderContent implements AncestorListener, PropertyChangeListener, ComponentListener {

  public static final int NORTH = 0;
  public static final int EAST = 1;
  public static final int SOUTH = 2;
  public static final int WEST = 3;

  public static final int ORIENTATIONS_MAX = 3;

  private JComponent extendedComponent = null;

  private final JComponent[] components = new JComponent[ORIENTATIONS_MAX + 1];

  private final int[] margins = new int[ORIENTATIONS_MAX + 1];

  private PaddingBorder extendedBorder = null;

  private final Insets borderInsets = new Insets(0, 0, 0, 0);


  /**
   * Creates a new {@link BorderContent} with a value of 0 for all margins.
   *
   * @param component
   */
  public BorderContent(JComponent component) {
    this(component, 0, 0, 0, 0);
  }

  /**
   * Creates a new {@link BorderContent} using the given margins which are applied around the
   * extended component.
   *
   * @param component
   * @param marginNorth
   * @param marginEast
   * @param marginSouth
   * @param marginWest
   */
  public BorderContent(JComponent component, int marginNorth, int marginEast, int marginSouth,
      int marginWest) {
    initialize(component, marginNorth, marginEast, marginSouth, marginWest);
  }

  /**
   *
   *
   * @param panel The panel to add content within its border. Only a panel with a BorderLayout can
   *        be used.
   * @param marginNorth
   * @param marginEast
   * @param marginSouth
   * @param marginWest
   */
  public BorderContent(JPanel panel, int marginNorth, int marginEast, int marginSouth,
      int marginWest) {
    if (!(panel.getLayout() instanceof BorderLayout)) {
      throw new ComponentExtensionError("Only a JPanel with BorderLayout can be used.");
    }

    initialize(panel, marginNorth, marginEast, marginSouth, marginWest);
  }

  /**
   *
   *
   * @param component
   * @param marginNorth
   * @param marginEast
   * @param marginSouth
   * @param marginWest
   */
  private void initialize(JComponent component, int marginNorth, int marginEast, int marginSouth,
      int marginWest) {
    this.extendedComponent = component;
    setMargins(marginNorth, marginEast, marginSouth, marginWest);

    extendedBorder = new PaddingBorder(extendedComponent);

    extendedComponent.addAncestorListener(this);
    extendedComponent.addPropertyChangeListener(this);
    extendedComponent.addComponentListener(this);

    updateBorder();
    updateInsets();
    updateComponents();
  }


  /**
   *
   * @param marginNorth
   * @param marginEast
   * @param marginSouth
   * @param marginWest
   */
  public void setMargins(int marginNorth, int marginEast, int marginSouth, int marginWest) {
    this.margins[NORTH] = marginNorth;
    this.margins[EAST] = marginEast;
    this.margins[SOUTH] = marginSouth;
    this.margins[WEST] = marginWest;
  }

  /**
   * Adds a new component to the object of which this {@link BorderContent} is for. The position of
   * the component has to be specified with one of the available positions:<br>
   * {@link BorderContent}.{@link #NORTH}<br>
   * {@link BorderContent}.{@link #EAST}<br>
   * {@link BorderContent}.{@link #SOUTH}<br>
   * {@link BorderContent}.{@link #WEST}<br>
   *
   * @param component
   * @param orientation
   */
  public void addComponent(JComponent component, int orientation) {
    if (extendedComponent == null) {
      throw new ComponentExtensionError(this.getClass().getCanonicalName()
          + " has not yet been initialized.");
    }

    if (orientation > ORIENTATIONS_MAX || orientation < 0) {
      throw new ComponentExtensionError("Orientation "
          + orientation
          + " does not exist. Use NORTH, EAST, SOUTH or WEST.");
    }

    if (component == null) {
      return;
    }

    if (components[orientation] != null) {
      // Remove any old component
      this.extendedComponent.remove(components[orientation]);
    }

    components[orientation] = component;

    this.extendedComponent.add(component);

    // If the component is added to a JTextField for example, the mouse
    // pointer over the added component would have the text cursor
    component.setCursor(Cursor.getDefaultCursor());

    component.addComponentListener(this);
  }

  /**
   * Removes the given component
   *
   * @param component
   */
  public void removeComponent(JComponent component) {
    if (extendedComponent == null) {
      throw new ComponentExtensionError(this.getClass().getCanonicalName()
          + " has not yet been initialized.");
    }

    if (component == null) {
      return;
    }

    // Look for the component in all the orientations
    for (int i = 0; i < ORIENTATIONS_MAX; i++) {
      if (components[i] != null && components[i].equals(component)) {
        components[i] = null;
      }
    }

    extendedComponent.remove(component);

    component.removeComponentListener(this);

    updateInsets();
  }

  /**
   * Returns the actual border without modifications (the additional insets etc.)
   *
   * @return
   */
  public Border getBorder() {
    Border b = extendedComponent.getBorder();
    if (b instanceof ExtensionBorder) {
      return ((ExtensionBorder) b).getBorder();
    } else {
      return b;
    }
  }

  /**
   *
   */
  private void updateInsets() {
    // Don't do anything if the component is not even showing
    if (!extendedComponent.isShowing()) {
      return;
    }

    borderInsets.top = margins[NORTH];
    borderInsets.right = margins[EAST];
    borderInsets.bottom = margins[SOUTH];
    borderInsets.left = margins[WEST];

    if (components[NORTH] != null) {

      borderInsets.top += getComponentHeight(NORTH);
    }

    if (components[EAST] != null) {

      borderInsets.right += getComponentWidth(EAST);
    }

    if (components[SOUTH] != null) {

      borderInsets.bottom += getComponentHeight(SOUTH);

    }

    if (components[WEST] != null) {

      borderInsets.left += getComponentWidth(WEST);

    }

    extendedBorder.setInsets(borderInsets);
  }

  /**
   *
   */
  private void updateComponents() {
    // Don't do anything if the component is not even showing
    if (!extendedComponent.isShowing()) {
      return;
    }

    if (components[NORTH] != null) {
      int x = getLeftEdge();
      int y = getTopEdge();
      int width = getWidth();
      int height = getComponentHeight(NORTH);

      components[NORTH].setBounds(x, y, width, height);
    }

    if (components[EAST] != null) {
      int x = getLeftEdge() + getWidth() - getComponentWidth(EAST);
      int y = getTopEdge() + borderInsets.top - margins[NORTH];
      int width = getComponentWidth(EAST);
      int height = getHeight() - getComponentHeight(NORTH) - getComponentHeight(SOUTH);

      components[EAST].setBounds(x, y, width, height);
    }

    if (components[SOUTH] != null) {
      int x = getLeftEdge();
      int y = getTopEdge() + getHeight() - getComponentHeight(SOUTH);
      int width = getWidth();
      int height = getComponentHeight(SOUTH);

      components[SOUTH].setBounds(x, y, width, height);
    }

    if (components[WEST] != null) {
      int x = getLeftEdge();
      int y = getTopEdge() + borderInsets.top - margins[NORTH];
      int width = getComponentWidth(WEST);
      int height = getHeight() - getComponentHeight(NORTH) - getComponentHeight(SOUTH);

      components[WEST].setBounds(x, y, width, height);
    }


    extendedComponent.validate();
  }

  /**
   * Returns the width of the component at the given position
   *
   * @param position
   * @return
   */
  private int getComponentHeight(int position) {
    if (components[position] == null) {
      return 0;
    }

    Dimension dim = components[position].getPreferredSize();
    Dimension dimMax = components[position].getMaximumSize();
    Dimension dimMin = components[position].getMinimumSize();

    int height = (int) dim.getHeight();

    // Obey max/min dimension
    if (height > dimMax.getHeight()) {
      height = (int) dimMax.getHeight();
    } else if (height < dimMin.getHeight()) {
      height = (int) dimMin.getHeight();
    }

    return height;
  }

  /**
   * Returns the height of the component at the given position
   *
   * @param position
   * @return
   */
  private int getComponentWidth(int position) {
    if (components[position] == null) {
      return 0;
    }

    Dimension dim = components[position].getPreferredSize();
    Dimension dimMax = components[position].getMaximumSize();
    Dimension dimMin = components[position].getMinimumSize();

    int width = (int) dim.getWidth();

    // Obey max/min dimension
    if (width > dimMax.getWidth()) {
      width = (int) dimMax.getWidth();
    } else if (width < dimMin.getWidth()) {
      width = (int) dimMin.getWidth();
    }

    return width;

  }

  /**
   * Returns the left edge within the border
   *
   * @return
   */
  private int getLeftEdge() {
    Insets originalBorderInstes = extendedBorder.getOriginalInsets();
    return originalBorderInstes.left;
  }

  /**
   * Returns the top edge within the border
   *
   * @return
   */
  private int getTopEdge() {
    Insets originalBorderInstes = extendedBorder.getOriginalInsets();
    return originalBorderInstes.top;
  }

  /**
   * Returns the width of the space within the borders
   *
   * @return
   */
  private int getWidth() {
    Dimension componentDimensions = extendedComponent.getSize();
    Insets originalBorderInstes = extendedBorder.getOriginalInsets();

    return (int) componentDimensions.getWidth() - originalBorderInstes.left
        - originalBorderInstes.right;
  }

  /**
   * Returns the height of the space within the borders
   *
   * @return
   */
  private int getHeight() {
    Dimension componentDimensions = extendedComponent.getSize();
    Insets originalBorderInstes = extendedBorder.getOriginalInsets();

    return (int) componentDimensions.getHeight() - originalBorderInstes.top
        - originalBorderInstes.bottom;
  }

  /**
   *
   */
  private void updateBorder() {
    Border b = extendedComponent.getBorder();

    if (b != null && b instanceof PaddingBorder) {
      // An extended border has already been set from somewhere else. Make sure
      // it has all the values we need and use it from now on.

      PaddingBorder existing = (PaddingBorder) b;

      extendedBorder = existing;
    } else {
      // Extended border is not set yet
      extendedBorder.setBorder(b);

      extendedComponent.setBorder(extendedBorder);
    }

  }

  /**
   * Updates the layout of the border content.<br>
   * If one of the border components is changed but the change does not trigger one of the events
   * which would update the border content (ancestor, component or property events), then this
   * method could help updating the bounds of the border components.
   */
  public void updateContent() {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        updateInsets();
        updateComponents();
      }
    });
  }

  @Override
  public void ancestorAdded(AncestorEvent event) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        updateBorder();
        updateInsets();
        updateComponents();
      }
    });
  }

  @Override
  public void ancestorMoved(AncestorEvent event) {}

  @Override
  public void ancestorRemoved(AncestorEvent event) {}

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("border")) {
      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          updateBorder();
          updateInsets();
          updateComponents();
        }
      });
    } else {
      updateContent();
    }


  }

  @Override
  public void componentHidden(ComponentEvent e) {
    updateContent();
  }

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void componentResized(ComponentEvent e) {
    updateContent();
  }

  @Override
  public void componentShown(ComponentEvent e) {
    updateContent();
  }

}
