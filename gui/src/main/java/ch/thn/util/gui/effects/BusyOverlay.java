package ch.thn.util.gui.effects;

import ch.thn.util.ColorUtil;
import ch.thn.util.gui.Loader;
import ch.thn.util.gui.component.CenteredPanel;
import ch.thn.util.gui.component.PlainButton;
import ch.thn.util.html.HtmlUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * An {@link OverlayPanel} which shows a loading/busy animation.
 *
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class BusyOverlay extends OverlayPanel implements ActionListener, MouseListener {
  private static final long serialVersionUID = 1013412074114453291L;


  private static boolean isVisible = false;
  private static boolean setVisible = false;

  private JLabel lBusy = null;

  private CenteredPanel pCentered = null;
  private JPanel pContent = null;

  private PlainButton bCancel = null;

  private Timer tSetVisible = null;

  private static final ImageIcon icon = Loader.loadIcon("/32x32/loading.gif");

  private String busyText = null;

  /**
   * An overlay panel at a layer depth of 9999 to be above all other components
   *
   *
   * @param rootPane
   * @param busyText
   * @param cancelButton
   */
  public BusyOverlay(JLayeredPane rootPane, String busyText, boolean cancelButton) {
    super(rootPane, false, 9999);

    setLayout(new BorderLayout());

    pCentered = new CenteredPanel();
    pCentered.setOpaque(false);

    pContent = new JPanel(new BorderLayout());
    pContent.setOpaque(false);
    // A maximum size, needed for the CenteredPanel
    pContent.setMaximumSize(new Dimension(500, 300));

    tSetVisible = new Timer(0, this);

    lBusy = new JLabel(icon);
    lBusy.setVerticalTextPosition(JLabel.BOTTOM);
    lBusy.setHorizontalTextPosition(JLabel.CENTER);

    bCancel = new PlainButton("[Cancel]");
    bCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    bCancel.addMouseListener(this);


    pContent.add(lBusy, BorderLayout.CENTER);

    if (cancelButton) {
      pContent.add(bCancel, BorderLayout.SOUTH);
    }

    pCentered.setCenteredContent(pContent);
    add(pCentered, BorderLayout.CENTER);

    setBusyText(busyText);
  }

  /**
   * Sets the busy icon. This is typically a GIF image to show a busy animation.
   *
   * @param icon
   */
  public void setBusyIcon(ImageIcon icon) {
    lBusy.setIcon(icon);
  }

  /**
   * Sets the text shown under the busy icon. The text is formatted with some html/css, thus a
   * linebreak can be added with &lt;br&gt;.
   *
   * @param busyText
   */
  public void setBusyText(String busyText) {
    this.busyText = busyText;
    lBusy.setText(HtmlUtil
        .textHtml(textFormatted(busyText, 4, ColorUtil.colorToHex(Color.white), true, true)));
  }

  /**
   *
   *
   * @return
   */
  public String getBusyText() {
    return busyText;
  }

  /**
   *
   *
   * @param hide
   */
  public void hideBusyIcon(boolean hide) {
    if (hide) {
      lBusy.setIcon(null);
    } else {
      lBusy.setIcon(icon);
    }
  }

  /**
   * Puts the text between font-tags with the given options
   *
   * @param text The text to put between the font-tags
   * @param size The text size, or 0 for not text size definition
   * @param color The text color (for example #75ba45), or null for not text color definition
   * @param alignCenter
   * @return The text within the font-tags and the options
   */
  private String textFormatted(String text, int size, String color, boolean bold,
      boolean alignCenter) {
    String s = "<font";

    if (size != 0) {
      s = s
          + " size=\""
          + size
          + "\"";
    }

    if (color != null) {
      s = s
          + " color=\""
          + color
          + "\"";
    }

    if (bold) {
      s = s
          + " weight=\"bold\"";
    }

    s = s
        + ">"
        + text
        + "</font>";

    return s;
  }


  /**
   * Shows the busy overlay. A delay can be given so that the busy animation is now shown right away
   * in case the operation finished within a short time and the busy overlay does not need to be
   * shown.
   *
   * @param visible
   * @param delay The delay in ms to execute the setVisible action
   */
  public void setVisible(boolean visible, int delay) {
    setVisible = visible;

    tSetVisible.setDelay(delay);
    tSetVisible.setInitialDelay(delay);
    tSetVisible.start();

    if (!visible) {
      setVisible(false);
    }
  }

  /**
   * Returns the cancel button which is shown in the overlay. An action listener should be added to
   * this button to allow the user to cancel the operation and to avoid that the loading overlay
   * locks the application from being used.
   *
   * @return
   */
  public JButton getCancelButton() {
    return bCancel;
  }

  /**
   *
   * @param visible
   */
  @Override
  public void setVisible(boolean visible) {
    if (tSetVisible != null) {
      tSetVisible.stop();
    }

    // If one loading overlay is already visible, do not show a new one
    if (isVisible && visible) {
      return;
    }

    isVisible = visible;

    super.setVisible(visible);

  }


  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == tSetVisible) {
      tSetVisible.stop();

      setVisible(setVisible);
    }

  }


  @Override
  public void mouseClicked(MouseEvent e) {}


  @Override
  public void mouseEntered(MouseEvent e) {
    if (e.getSource() == bCancel) {
      bCancel.setForeground(Color.white);
    }
  }


  @Override
  public void mouseExited(MouseEvent e) {
    if (e.getSource() == bCancel) {
      bCancel.setForeground(Color.black);
    }
  }


  @Override
  public void mousePressed(MouseEvent e) {}


  @Override
  public void mouseReleased(MouseEvent e) {}


}
