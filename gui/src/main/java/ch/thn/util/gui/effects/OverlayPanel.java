package ch.thn.util.gui.effects;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * A panel which lays over all the other components with a semi transparent background.<br />
 * <br />
 * The layered pane can be obtained from a JFrame for example with getLayeredPane().<br>
 *
 * @author Thomas Naeff (github.com/thnaeff) Naeff
 * @since
 *
 */
public class OverlayPanel extends JPanel {
  private static final long serialVersionUID = 1623359440962114967L;


  private JLayeredPane layeredPane = null;

  private final boolean showTransparentBackground = true;

  private boolean fullPassThrough = false;
  private boolean firstPaint = true;

  private Color cBackground = new Color(0, 0, 0, 110);

  public static final Color cSemiTransparent = new Color(0, 0, 0, 110);
  public static final Color cTransparent = new Color(0, 0, 0, 0);

  /**
   * A panel over all the other panels with a semi transparent gray background. Positions the
   * layered pane as "high" as possible (in front of all other layered panes), with a value of 999.
   * 
   * 
   * @param layeredPane
   * @param visible
   * @since
   */
  public OverlayPanel(JLayeredPane layeredPane, boolean visible) {
    this(layeredPane, visible, 999);
  }

  /**
   * A panel over all the other panels with a semi transparent gray background. The default
   * background color is Color(0, 0, 0, 110).
   * 
   * 
   * @param layeredPane
   * @param visible
   * @param layerDepth The layer depth, e.g. {@link JLayeredPane#POPUP_LAYER}
   * @since
   */
  public OverlayPanel(JLayeredPane layeredPane, boolean visible, int layerDepth) {
    this.layeredPane = layeredPane;

    // Just define a certain size to make the paint-method work
    setBounds(0, 0, 1, 1);

    // Some style
    // setBorder(BorderFactory.createLineBorder(Color.darkGray, 5));

    // Not opaque to see background components shine through
    setOpaque(false);

    setEventPassThrough(false);

    setVisible(visible);

    layeredPane.add(this, new Integer(layerDepth));

  }

  /**
   * Deactivates this overly panel by removing it from the layered pane
   * 
   */
  public void deactivate() {
    remove(this);
  }

  /**
   * Sets the color of the overlay panel.<br />
   * To create a translucent color, use an alpha value, e.g. Color(0, 0, 0, 110)<br />
   * Setting the color to <code>null</code> creates a completely transparent overlay panel.
   * 
   * @param color
   */
  public void setColor(Color color) {
    if (color == null) {
      cBackground = cTransparent;
    } else {
      cBackground = color;
    }
  }

  /**
   * If this is set to <code>true</code>, events like mouse or key events are passed through to the
   * underlying components and are not consumed by the overlay panel. This means that underlying
   * components can be controlled through this overlay panel. If set to <code>false</code>,
   * underlying components can not be controlled.
   * 
   * @param eventPassThrough
   */
  public void setEventPassThrough(boolean eventPassThrough) {
    this.setEventPassThrough(eventPassThrough, false);
  }

  /**
   * If eventPassThrough is set to <code>true</code>, events like mouse or key events are passed
   * through to the underlying components and are not consumed by the overlay panel. This means that
   * underlying components can be controlled through this overlay panel. If eventPassThrough is set
   * to <code>false</code>, underlying components can not be controlled.
   * 
   * @param eventPassThrough
   * @param fullPassThrough If set to <code>true</code>, all component events on the overlay panel
   *        are ignored and passed to the underlying components. This means that any components on
   *        the overlay panel can not be selected or controlled, but it also means that for example
   *        the mouse cursor changes according to the underlying components. Only available if
   *        eventPassThrough=<code>true</code>
   */
  public void setEventPassThrough(boolean eventPassThrough, boolean fullPassThrough) {

    if (!eventPassThrough) {
      // Block against mouse and key events
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
      enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
      enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
      setFocusable(true);
      setFocusCycleRoot(true);
      // Make sure the overlay panel has focus
      requestFocusInWindow();
    } else {
      this.fullPassThrough = fullPassThrough;

      disableEvents(AWTEvent.MOUSE_EVENT_MASK);
      disableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
      disableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
      // setFocusable(false);
      setFocusCycleRoot(false);
    }
  }


  @Override
  public void setVisible(boolean visible) {


    super.setVisible(visible);

    layeredPane.moveToFront(this);

    // Make sure the overlay panel gets the focus, otherwise an element below the
    // overlay panel might still have the focus and can still be controlled
    // with the keys
    if (visible) {
      requestFocusInWindow();
    }

    // Validate again
    layeredPane.validate();

  }

  @Override
  public boolean contains(int x, int y) {
    if (fullPassThrough) {
      return false;
    }

    return super.contains(x, y);
  }

  @Override
  public boolean contains(Point p) {
    if (fullPassThrough) {
      return false;
    }

    return super.contains(p);
  }

  @Override
  public void paint(Graphics g) {

    // Stretch the panel to cover the whole layered pane
    setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());

    if (showTransparentBackground) {
      // Show a translucent gray background
      g.setColor(cBackground);
      g.fillRect(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
    }

    super.paint(g);

    if (firstPaint) {
      validate();
      firstPaint = false;
    }

  }



}
