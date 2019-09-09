package ch.thn.util.gui.component;

import java.awt.event.MouseEvent;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public interface ActionAreaListener {

  /**
   * The mouse pointer entered the defined area
   * 
   * @param e The mouse event which caused the listeners to be notified
   */
  public void mouseEnteredArea(MouseEvent e);

  /**
   * The mouse pointer exited the defined area
   * 
   * @param e The mouse event which caused the listeners to be notified
   */
  public void mouseExitedArea(MouseEvent e);

}
