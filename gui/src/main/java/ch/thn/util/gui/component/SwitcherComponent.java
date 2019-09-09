package ch.thn.util.gui.component;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class SwitcherComponent {


  private int buttonSpace = 2;

  private JPanel panel = null;
  private JPanel bPanel = null;
  private JButton button = null;

  private String panelTitle = null;

  private Color cBorderActivated = null;
  private Color cBorderDeactivated = null;
  private Color cBackgroundActivated = null;
  private Color cBackgroundDeactivated = null;

  private Border borderActivated = null;
  private Border borderDeactivated = null;

  public static int HORIZONTAL = 0;
  public static int VERTICAL = 1;


  /**
   * The PanelSwitcher component which contains the panel and the corresponding button (with text
   * and/or icon) to switch to the panel
   * 
   * @param panel The Panel
   * @param buttonIcon An icon for the button
   * @param buttonText A text for the button
   */
  public SwitcherComponent(JPanel panel, Icon buttonIcon, String buttonText) {

    cBorderActivated = new Color(8, 72, 172);
    cBorderDeactivated = new Color(112, 137, 175);
    cBackgroundActivated = new Color(128, 178, 255);
    cBackgroundDeactivated = new Color(171, 171, 171);


    borderActivated = BorderFactory.createLineBorder(cBorderActivated, 1);
    borderDeactivated = BorderFactory.createLineBorder(cBorderDeactivated, 1);

    bPanel = new JPanel();
    button = new JButton();

    bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
    bPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

    bPanel.add(button);

    setPanel(panel);
    setButtonIcon(buttonIcon);
    setButtonText(buttonText);

    deactivate();
  }

  /**
   * The PanelSwitcher component which contains the panel and the corresponding button (with text
   * and/or icon) to switch to the panel
   * 
   * @param panel The Panel
   * @param buttonText A text for the button
   */
  public SwitcherComponent(JPanel panel, String buttonText) {
    this(panel, null, buttonText);
  }

  /**
   * The PanelSwitcher component which contains the panel and the corresponding button (with text
   * and/or icon) to switch to the panel
   * 
   * @param panel The Panel
   * @param buttonIcon An icon for the button
   */
  public SwitcherComponent(JPanel panel, Icon buttonIcon) {
    this(panel, buttonIcon, null);
  }

  /**
   * Set the panel which is connected to the button
   * 
   * @param panel A panel
   */
  public void setPanel(JPanel panel) {
    this.panel = panel;
  }

  /**
   * Returns the panel which is connected to the button
   * 
   * @return A panel
   */
  public JPanel getPanel() {
    return panel;
  }

  /**
   * The button which is used to switch the panel
   * 
   * @return A button
   */
  public JButton getButton() {
    return button;
  }

  /**
   * The panel with the button. The panel is used to add a invisible border to set the space between
   * buttons.
   * 
   * @return The panel which contains the button
   */
  protected JPanel getButtonPanel() {
    return bPanel;
  }

  /**
   * Set the text displayed in the button. If set to null, no text is shown
   * 
   * @param text Some text
   */
  public void setButtonText(String text) {
    button.setText(text);
  }

  /**
   * Returns the text displayed in the button. If set to null, no text is shown
   * 
   * @return Some text
   */
  public String getButtonText() {
    return button.getText();
  }

  /**
   * Set the icon displayed in the button. If set to null, no icon is shown
   * 
   * @param icon An icon
   */
  public void setButtonIcon(Icon icon) {
    button.setIcon(icon);
  }

  /**
   * Returns the icon displayed in the button. If set to null, no icon is shown
   * 
   * @return An icon
   */
  public Icon getButtonIcon() {
    return button.getIcon();
  }

  /**
   * Sets the tool tip text which is shown when hovering the button
   * 
   * @param txt The tool tip text
   */
  public void setButtonToolTipText(String txt) {
    button.setToolTipText(txt);
  }

  /**
   * Returns the tool tip text which is shown when hovering the button
   * 
   * @return The tool tip text
   */
  public String getButtonToolTipText() {
    return button.getToolTipText();
  }

  /**
   * If set to something not null, the Panel gets a frame and a title
   * 
   * @param title A title string
   */
  public void setPanelTitle(String title) {
    panelTitle = title;
  }

  /**
   * If set to something not null, the Panel gets a frame and a title
   * 
   * @return A title string
   */
  public String getPanelTitle() {
    return panelTitle;
  }

  /**
   * Sets the dimension of the button
   * 
   * @param dim A dimension object
   */
  public void setButtonDimension(Dimension dim) {
    button.setPreferredSize(dim);
    button.setMaximumSize(dim);
    button.setMinimumSize(dim);
  }

  /**
   * 
   * @param space
   */
  protected void setButtonSpace(int space) {
    buttonSpace = space;
  }

  /**
   * 
   * @return
   */
  protected int getButtonSpace() {
    return buttonSpace;
  }

  /**
   * Returns the dimension of the button
   * 
   * @return A dimension object
   */
  public Dimension getButtonDimension() {
    return button.getPreferredSize();
  }

  /**
   * 
   * @param orientation
   */
  public void setButtonOrientation(int orientation) {
    if (orientation == HORIZONTAL) {
      bPanel.setBorder(BorderFactory.createEmptyBorder(0, buttonSpace, 0, 0));
    } else if (orientation == VERTICAL) {
      bPanel.setBorder(BorderFactory.createEmptyBorder(buttonSpace, 0, 0, 0));
    }
  }

  /**
   * Activates this component.<br>
   * This means that the button is highlighted and the panel is set to visible.
   */
  public void activate() {
    button.setBackground(cBackgroundActivated);

    button.setBorder(borderActivated);
  }

  /**
   * Deactivates this component.<br>
   * This means that the highlighted button is set back to normal and the panel is set to invisible.
   */
  public void deactivate() {
    button.setBackground(cBackgroundDeactivated);

    button.setBorder(borderDeactivated);
  }



}
