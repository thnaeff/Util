package ch.thn.util.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Some kind of button menu which has a panel assigned for each button and switches to that panel if
 * its button is clicked.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class PanelSwitcher implements ActionListener {

  private int visible = -1;
  private int orientation = HORIZONTAL;
  private float xalignment = XLEFT;
  private float yalignment = YTOP;
  private int buttonSpace = 2;

  public static int HORIZONTAL = 0;
  public static int VERTICAL = 1;

  public static float XLEFT = Component.LEFT_ALIGNMENT;
  public static float XRIGHT = Component.RIGHT_ALIGNMENT;
  public static float YTOP = Component.TOP_ALIGNMENT;
  public static float YBOTTOM = Component.BOTTOM_ALIGNMENT;

  private JPanel contentPanel = null;
  private JPanel buttonPanel = null;

  private Border panelBorder = null;

  private Color cBorder = null;

  private Dimension buttonDim = null;

  private Vector<SwitcherComponent> sc = null;


  /**
   * 
   */
  public PanelSwitcher() {

    sc = new Vector<SwitcherComponent>();

    cBorder = new Color(8, 72, 172);

    contentPanel = new JPanel();
    contentPanel.setLayout(new BorderLayout());

    buttonPanel = new JPanel();

    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

  }

  /**
   * 
   * @param orientation
   */
  public void setButtonOrientation(int orientation) {
    this.orientation = orientation;

    if (orientation == HORIZONTAL) {
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    } else {
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    }

  }

  /**
   * 
   * @return
   */
  public int getButtonOrientation() {
    return orientation;
  }

  /**
   * 
   * @param space
   */
  public void setButtonSpace(int space) {
    buttonSpace = space;
  }

  /**
   * 
   * @return
   */
  public int getButtonSpace() {
    return buttonSpace;
  }


  /**
   * Sets the dimension for all newly added buttons. If set to null, the button dimensions are not
   * changed.
   * 
   * @param dim A dimension object with width and height
   */
  public void setDefaultButtonDimension(Dimension dim) {
    buttonDim = dim;
  }

  /**
   * 
   * @return
   */
  public Dimension getDefaultButtonDimension() {
    return buttonDim;
  }

  /**
   * 
   * @param alignment
   */
  public void setXAlignment(float alignment) {
    xalignment = alignment;
  }

  /**
   * 
   * @return
   */
  public float getXAlignment() {
    return xalignment;
  }

  /**
   * 
   * @param alignment
   */
  public void setYAlignment(float alignment) {
    yalignment = alignment;
  }

  /**
   * 
   * @return
   */
  public float getYAlignment() {
    return yalignment;
  }



  /**
   * 
   * @param comp
   */
  public void add(SwitcherComponent comp) {

    sc.add(comp);

    comp.getButton().addActionListener(this);

    if (buttonDim != null) {
      comp.setButtonDimension(buttonDim);
    }

    comp.setButtonSpace(buttonSpace);
    comp.setButtonOrientation(orientation);

    comp.getButton().setAlignmentX(xalignment);
    comp.getButton().setAlignmentY(yalignment);

    buttonPanel.add(comp.getButtonPanel());

  }

  /**
   * Remove a switcher component
   * 
   * @param comp The switcher component
   * @return True if the component was removed
   */
  public boolean remove(SwitcherComponent comp) {
    int tempVisible = visible;

    if (comp == null) {
      return false;
    }

    if (visible >= 0) {
      if ((visible + 1) < sc.size()) {
        // If there is another one after the removed one, switch to that one
        switchTo(visible + 1);
      } else if ((visible - 1) >= 0) {
        // Otherwise check if there is one in front of the removed one
        switchTo(visible - 1);
      } else {
        // If there is only one tab left, switch to -1 which means
        // that the current panel will be hidden and none will be shown
        switchTo(-1);
      }
    }


    comp.getButton().removeActionListener(this);
    buttonPanel.remove(comp.getButtonPanel());

    buttonPanel.validate();
    buttonPanel.repaint();


    boolean ret = sc.remove(comp);

    if (tempVisible >= sc.size()) {
      visible = sc.size() - 1;
    } else {
      visible = tempVisible;
    }

    return ret;

  }

  /**
   * Remove a switcher component which contains the specified panel
   * 
   * @param panel The panel of the switcher component
   * @return True if the panes was found and removed
   */
  public boolean remove(JPanel panel) {
    return remove(getSwitcherComponent(panel));
  }

  /**
   * 
   * @param panel
   * @return
   */
  public SwitcherComponent getSwitcherComponent(JPanel panel) {
    for (int i = 0; i < sc.size(); i++) {
      if (sc.get(i).getPanel() == panel) {
        return sc.get(i);
      }
    }

    return null;
  }

  /**
   * 
   * @param button
   * @return
   */
  public SwitcherComponent getSwitcherComponent(JButton button) {
    for (int i = 0; i < sc.size(); i++) {
      if (sc.get(i).getButton() == button) {
        return sc.get(i);
      }
    }

    return null;
  }

  /**
   * 
   * @return
   */
  public JPanel getContentPanel() {
    return contentPanel;
  }

  /**
   * 
   * @return
   */
  public JPanel getButtonPanel() {
    return buttonPanel;
  }

  /**
   * Returns the number of panels
   * 
   * @return The number of panels
   */
  public int getPanelCount() {
    return sc.size();
  }

  /**
   * Returns the number of the visible component
   * 
   * @return The number of the component
   */
  public int getVisibleNr() {
    return visible;
  }

  /**
   * Returns the SwitcherComponent of the visible component
   * 
   * @return The SwitcherComponent of the component, or null if no component is visible
   */
  public SwitcherComponent getVisibleComponent() {
    if ((visible >= 0) && (visible < sc.size())) {
      return sc.get(visible);
    } else {
      return null;
    }
  }

  /**
   * Returns the panel of the visible component
   * 
   * @return The panel of the component, or null if no panel is visible
   */
  public JPanel getVisiblePanel() {
    if ((visible >= 0) && (visible < sc.size())) {
      return sc.get(visible).getPanel();
    } else {
      return null;
    }
  }


  /**
   * Switch to a different panel.<br>
   * If switching to componentNr -1, the current panel will be hidden and no new panel will be
   * shown.
   * 
   * @param componentNr The panel number
   * @return True if switching was ok, false if not
   */
  public boolean switchTo(int componentNr) {
    String title = "";

    if ((componentNr < -1) || (componentNr >= sc.size())) {
      contentPanel.repaint();
      contentPanel.revalidate();
      buttonPanel.repaint();
      buttonPanel.revalidate();
      return false;
    }

    // Hide the visible one
    if (visible >= 0) {
      sc.get(visible).deactivate();
      contentPanel.remove(sc.get(visible).getPanel());
    }

    visible = componentNr;

    // Show the new one
    if (componentNr >= 0) {
      sc.get(componentNr).activate();
      contentPanel.add(sc.get(componentNr).getPanel());

      title = sc.get(componentNr).getPanelTitle();
    } else {
      title = "";
    }

    // Add border and title if set
    if ((componentNr >= 0) && (sc.get(componentNr).getPanelTitle() != null)) {
      panelBorder =
          BorderFactory.createTitledBorder(BorderFactory.createLineBorder(cBorder), title);
      contentPanel.setBorder(panelBorder);
    } else {
      contentPanel.setBorder(null);
      panelBorder = null;
    }


    contentPanel.repaint();
    contentPanel.revalidate();

    return true;
  }



  @Override
  public void actionPerformed(ActionEvent arg0) {
    Object o = arg0.getSource();

    if (o instanceof JButton) {
      JButton b = (JButton) o;

      for (int i = 0; i < sc.size(); i++) {
        if (sc.get(i).getButton() == b) {
          switchTo(i);
          break;
        }
      }
    }

  }


}
