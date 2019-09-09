package ch.thn.util.gui.component;

import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Just a {@link JButton} which is formatted so that no borders or background is visible. Only the
 * icon/text is visible.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class PlainButton extends JButton implements FocusListener {
  private static final long serialVersionUID = -5957190073582338443L;

  private Icon buttonIcon = null;
  private Icon buttonIconRollover = null;

  /**
   * Creates a {@link JButton} with only an icon
   * 
   * @param buttonIcon
   * @param buttonIconRollover
   */
  public PlainButton(Icon buttonIcon, Icon buttonIconRollover) {
    super();
    init(buttonIcon, buttonIconRollover);
  }

  /**
   * Creates a {@link JButton} with only an icon
   * 
   * @param a
   * @param buttonIcon
   * @param buttonIconRollover
   */
  public PlainButton(Action a, Icon buttonIcon, Icon buttonIconRollover) {
    super(a);
    init(buttonIcon, buttonIconRollover);
  }

  /**
   * Creates a {@link JButton} with only an icon
   * 
   * @param buttonIcon
   */
  public PlainButton(Icon buttonIcon) {
    super(buttonIcon);
    init(buttonIcon, null);
  }

  /**
   * 
   * 
   * @param s
   * @param buttonIcon
   * @param buttonIconRollover
   */
  public PlainButton(String s, Icon buttonIcon, Icon buttonIconRollover) {
    super(s);
    init(buttonIcon, buttonIconRollover);
  }

  /**
   * 
   * 
   * @param s
   * @param buttonIcon
   */
  public PlainButton(String s, Icon buttonIcon) {
    super(s, buttonIcon);
    init(buttonIcon, null);
  }

  /**
   * 
   * 
   * @param s
   */
  public PlainButton(String s) {
    super(s);
    init((Icon) null, null);
  }


  /**
   * Sets the button properties so that only the icon is shown
   * 
   * @param buttonIcon
   * @param buttonIconRollover
   */
  private void init(Icon buttonIcon, Icon buttonIconRollover) {
    this.buttonIcon = buttonIcon;
    this.buttonIconRollover = buttonIconRollover;

    setBorderPainted(false);
    setFocusPainted(true);
    setIcon(buttonIcon);
    setContentAreaFilled(false);

    if (buttonIconRollover != null) {
      setRolloverIcon(buttonIconRollover);
      setPressedIcon(buttonIcon);
    }

    setMargin(new Insets(0, 0, 0, 0));

    addFocusListener(this);
  }

  @Override
  public void focusGained(FocusEvent e) {
    if (buttonIconRollover != null) {
      setIcon(buttonIconRollover);
    }
  }

  @Override
  public void focusLost(FocusEvent e) {
    setIcon(buttonIcon);
  }


}
