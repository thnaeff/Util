package ch.thn.util.gui.component;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * A panel which separates each added component {@link #addSplitComponent(Component)} with a split
 * pane. The orientation can be configured horizontal (left/right) and vertical (up/down).
 *
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class MultiSplitPanel extends JPanel {
  private static final long serialVersionUID = -985645277499546356L;

  /** Vertical orientation, new components are added below previous ones */
  public static final int ORIENTATION_VERTICAL_DOWN = 0;
  /** Vertical orientation, new components are added above previous ones */
  public static final int ORIENTATION_VERTICAL_UP = 1;
  /** Horizontal orientation, new components are added to the right of previous ones */
  public static final int ORIENTATION_HORIZONTAL_RIGHT = 2;
  /** Horizontal orientation, new components are added to the left of previous ones */
  public static final int ORIENTATION_HORIZONTAL_LEFT = 3;


  private JSplitPane spLast = null;
  private Component compLast = null;

  private int orientation = ORIENTATION_HORIZONTAL_RIGHT;


  /**
   * 
   * 
   * @param orientation
   */
  public MultiSplitPanel(int orientation) {
    setLayout(new BorderLayout());

    this.orientation = orientation;

  }

  @Override
  public Component add(Component comp) {
    throw new UnsupportedOperationException(
        "Adding components only allowed through addSplitComponent(Component)");
  }

  @Override
  public Component add(Component comp, int index) {
    throw new UnsupportedOperationException(
        "Adding components only allowed through addSplitComponent(Component)");
  }

  @Override
  public void add(Component comp, Object constraints) {
    throw new UnsupportedOperationException(
        "Adding components only allowed through addSplitComponent(Component)");
  }

  @Override
  public void add(Component comp, Object constraints, int index) {
    throw new UnsupportedOperationException(
        "Adding components only allowed through addSplitComponent(Component)");
  }

  @Override
  public Component add(String name, Component comp) {
    throw new UnsupportedOperationException(
        "Adding components only allowed through addSplitComponent(Component)");
  }

  @Override
  public void remove(Component comp) {
    throw new UnsupportedOperationException("Removig components not supported");
  }

  @Override
  public void remove(int index) {
    throw new UnsupportedOperationException("Removig components not supported");
  }

  @Override
  public void removeAll() {
    throw new UnsupportedOperationException("Removig components not supported");
  }


  /**
   * 
   * 
   * @param component
   * @return The {@link JSplitPane} so it can be further configured
   */
  public JSplitPane addSplitComponent(Component component) {
    if (component == null) {
      return null;
    }

    if (compLast == null) {
      // The very first component. Just add it

      addFirstComponent(component);

      compLast = component;
      return null;
    } else if (spLast == null) {
      // Second component. Add the first split pane

      spLast = newSplitPane(compLast, component);
      addFirstComponent(spLast);

      compLast = component;

    } else {
      // Further components.

      JSplitPane spPrevious = spLast;
      spLast = newSplitPane(compLast, component);
      addSplitPane(spPrevious, spLast);

      compLast = component;
    }


    return spLast;
  }

  /**
   * Adds the component to the last panel in the right orientation
   * 
   * @param component
   */
  private void addFirstComponent(Component component) {

    super.add(component, BorderLayout.CENTER);

    // switch (orientation) {
    // case ORIENTATION_VERTICAL_DOWN:
    // add(component, BorderLayout.NORTH);
    // break;
    // case ORIENTATION_VERTICAL_UP:
    // add(component, BorderLayout.SOUTH);
    // break;
    // case ORIENTATION_HORIZONTAL_RIGHT:
    // add(component, BorderLayout.WEST);
    // break;
    // case ORIENTATION_HORIZONTAL_LEFT:
    // add(component, BorderLayout.EAST);
    // break;
    // default:
    // break;
    // }

  }

  /**
   * 
   * 
   * @param spAddToThis
   * @param sp
   */
  private void addSplitPane(JSplitPane spAddToThis, JSplitPane sp) {

    switch (orientation) {
      case ORIENTATION_VERTICAL_DOWN:
      case ORIENTATION_HORIZONTAL_RIGHT:
        spAddToThis.setRightComponent(sp);
        break;
      case ORIENTATION_VERTICAL_UP:
      case ORIENTATION_HORIZONTAL_LEFT:
        spAddToThis.setLeftComponent(sp);
        break;
      default:
        break;
    }

  }

  /**
   * 
   * 
   * 
   * @param first
   * @param second
   * @return
   */
  private JSplitPane newSplitPane(Component first, Component second) {

    switch (orientation) {
      case ORIENTATION_VERTICAL_DOWN:
        return new JSplitPane(JSplitPane.VERTICAL_SPLIT, first, second);
      case ORIENTATION_VERTICAL_UP:
        return new JSplitPane(JSplitPane.VERTICAL_SPLIT, second, first);
      case ORIENTATION_HORIZONTAL_RIGHT:
        return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, first, second);
      case ORIENTATION_HORIZONTAL_LEFT:
        return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, second, first);
      default:
        break;
    }

    return null;

  }

}
