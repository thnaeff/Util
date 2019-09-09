package ch.thn.util.gui.component;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * This class gives the possibility to define an area within the component which informs the added
 * {@link ActionAreaListener}s when the mouse has entered/exited that area.<br>
 * <br>
 * The {@link MouseCursorArea} and {@link ToolTipArea} classes are built on the base of this class.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ActionArea implements MouseMotionListener, MouseListener {

  private JComponent component = null;

  private Rectangle rect = null;

  private ArrayList<ActionAreaListener> listeners = null;

  private boolean withinArea = false;
  private boolean isActive = true;

  /**
   * 
   * @param component
   */
  public ActionArea(JComponent component) {
    this.component = component;

    listeners = new ArrayList<>();
  }

  /**
   * 
   * @return
   */
  public JComponent getComponent() {
    return component;
  }

  /**
   * 
   * 
   * @param x
   * @param y
   * @param width
   * @param height
   */
  public void setArea(int x, int y, int width, int height) {
    rect = new Rectangle(x, y, width, height);
  }

  /**
   * 
   * @param l
   */
  public void addActionAreaListener(ActionAreaListener l) {
    listeners.add(l);
  }

  /**
   * 
   * @param l
   */
  public void removeActionAreaListener(ActionAreaListener l) {
    listeners.remove(l);
  }

  /**
   * 
   * @param entered
   */
  private void fireActionAreaListener(boolean entered, MouseEvent e) {
    if (entered) {
      for (ActionAreaListener l : listeners) {
        l.mouseEnteredArea(e);
      }
    } else {
      for (ActionAreaListener l : listeners) {
        l.mouseExitedArea(e);
      }
    }
  }

  /**
   * 
   * @param active
   */
  public void active(boolean active) {
    isActive = active;

    if (active) {
      component.addMouseMotionListener(this);
      component.addMouseListener(this);
    } else {
      component.removeMouseMotionListener(this);
      component.removeMouseListener(this);
    }
  }

  /**
   * 
   * @return
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * 
   * @return
   */
  public boolean isWithinArea() {
    return withinArea;
  }

  /**
   * 
   * @param p
   */
  private void checkArea(MouseEvent e) {
    if (rect.contains(e.getPoint())) {
      if (!withinArea) {
        // Was not within the area before but now it is
        fireActionAreaListener(true, e);
        withinArea = true;
      }
    } else {
      if (withinArea) {
        // Was within the area before but now it is not any more
        fireActionAreaListener(false, e);
        withinArea = false;
      }
    }
  }


  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {
    checkArea(e);
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {
    checkArea(e);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    checkArea(e);
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}



}
