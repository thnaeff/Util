package ch.thn.util.gui.component;

import ch.thn.util.gui.GuiUtilError;
import ch.thn.util.string.StringUtil;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * This is a {@link JLabel} which does various clipping of its text (replacing exceeding characters
 * with "...").<br>
 * <br>
 * <b>Info:</b> The tool-tip text of this {@link JLabel} is used to display the full text when the
 * text had to be clipped. Thus, {@link #setToolTipText(String)} on this {@link ClippingLabel} will
 * be overwritten internally whenever the clipping is updated.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ClippingLabel extends JLabel implements AncestorListener, ComponentListener {
  private static final long serialVersionUID = -1882748996656722889L;

  public static final int CLIP_LEFT = 0;
  public static final int CLIP_RIGHT = 1;
  public static final int CLIP_CENTER = 2;

  private String originalText = getText();

  private int clippingMode = CLIP_LEFT;

  private boolean isInitialized = false;
  private boolean isClipped = false;



  /**
   *
   * @see JLabel#JLabel()
   */
  public ClippingLabel() {
    super();
    init();
  }

  /**
   *
   * @param icon
   * @see JLabel#JLabel(Icon)
   */
  public ClippingLabel(Icon icon) {
    super(icon);
    init();
  }

  /**
   *
   * @param string
   * @see JLabel#JLabel(String)
   */
  public ClippingLabel(String string) {
    super(string);
    init();
  }

  /**
   *
   * @param icon
   * @param hotizontalAlignment
   * @see JLabel#JLabel(Icon, int)
   */
  public ClippingLabel(Icon icon, int hotizontalAlignment) {
    super(icon, hotizontalAlignment);
    init();
  }

  /**
   *
   * @param string
   * @param hotizontalAlignment
   * @see JLabel#JLabel(String, int)
   */
  public ClippingLabel(String string, int hotizontalAlignment) {
    super(string, hotizontalAlignment);
    init();
  }

  /**
   *
   * @param string
   * @param icon
   * @param hotizontalAlignment
   * @see JLabel#JLabel(String, Icon, int)
   */
  public ClippingLabel(String string, Icon icon, int hotizontalAlignment) {
    super(string, icon, hotizontalAlignment);
    init();
  }


  /**
   *
   */
  private void init() {
    addAncestorListener(this);
    addComponentListener(this);
    isInitialized = true;
  }

  /**
   * Returns true if the current shown text is clipped
   */
  public boolean isClipped() {
    return isClipped;
  }

  /**
   * Set the position of the clipping to left/right/center.
   *
   * @param clippingMode
   */
  public void setClippingMode(int clippingMode) {
    if (clippingMode < 0 || clippingMode > 2) {
      throw new GuiUtilError("Unknown clipping mode "
          + clippingMode);
    }

    this.clippingMode = clippingMode;
  }

  @Override
  public void setText(String text) {
    // This method is called when the super call in the constructor is called.
    // This means that the originalText variable is not even initialized and
    // will be overwritten with NULL when it is initialized.

    if (isInitialized) {
      originalText = text;
      updateClipping();
    } else {
      super.setText(text);
    }
  }

  /**
   * Returns the original text without any clipping
   *
   * @return
   */
  public String getOriginalText() {
    return originalText;
  }

  /**
   * Updates the clipped string.<br>
   * Hint: the getText-method needs to return the clipped string since that is the string which is
   * displayed in the label.
   */
  private void updateClipping() {

    if (getFont() != null && originalText != null) {
      FontMetrics fm = getFontMetrics(getFont());
      int width = (int) getSize().getWidth();

      if (width > 0 && fm.stringWidth(originalText) > width) {
        // Needs clipping
        switch (clippingMode) {
          case CLIP_LEFT:
            super.setText(StringUtil.clipStringLeft(originalText, fm, width));
            break;
          case CLIP_RIGHT:
            // Clipping on the right side is done automatically
            // super.setText(originalText);
            // However, to know if the text is clipped or not it is done here
            // so that the texts can be compared
            super.setText(StringUtil.clipStringRight(originalText, fm, width));
            break;
          case CLIP_CENTER:
            super.setText(StringUtil.clipStringCenter(originalText, fm, width));
            break;
          default:
            break;
        }

        if (getText().equals(originalText)) {
          isClipped = false;
          setToolTipText("");
        } else {
          isClipped = true;
          setToolTipText(originalText);
        }
      } else {
        super.setText(originalText);
        setToolTipText("");
      }
    } else {
      super.setText(originalText);
      setToolTipText("");
    }

  }

  @Override
  public void setMaximumSize(Dimension maximumSize) {
    super.setMaximumSize(maximumSize);
    updateClipping();
  }

  @Override
  public void setMinimumSize(Dimension minimumSize) {
    super.setMinimumSize(minimumSize);
    updateClipping();
  }

  @Override
  public void setSize(Dimension d) {
    super.setSize(d);
    updateClipping();
  }

  @Override
  public void setPreferredSize(Dimension preferredSize) {
    super.setPreferredSize(preferredSize);
    updateClipping();
  }

  @Override
  public void componentHidden(ComponentEvent e) {}

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void componentResized(ComponentEvent e) {
    updateClipping();
  }

  @Override
  public void componentShown(ComponentEvent e) {
    updateClipping();
  }

  @Override
  public void ancestorAdded(AncestorEvent event) {
    updateClipping();
  }

  @Override
  public void ancestorMoved(AncestorEvent event) {}

  @Override
  public void ancestorRemoved(AncestorEvent event) {}



}
