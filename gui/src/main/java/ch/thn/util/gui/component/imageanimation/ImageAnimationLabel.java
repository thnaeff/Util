package ch.thn.util.gui.component.imageanimation;

import ch.thn.util.gui.ImageUtil;
import ch.thn.util.gui.effects.imageanimation.ImageAnimation;

import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This is the base class for a {@link JLabel} whose icon can be animated.<br />
 * The icon has to be set with the standard {@link #setIcon(Icon)} method.<br />
 * See {@link ImageAnimationLabelFading} for an implementation example.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 * @param <A>
 *
 */
public abstract class ImageAnimationLabel<A extends ImageAnimation<?>> extends JLabel {
  private static final long serialVersionUID = -728553274740471716L;

  private A imageAnimation = null;

  private BufferedImage bufferedImage = null;

  private Icon originalIcon = null;
  private ImageIcon imageIcon = null;

  private boolean iconChanged = true;


  /**
   *
   *
   * @see JLabel
   */
  public ImageAnimationLabel() {
    super();
    init();
  }

  /**
   *
   *
   * @param icon
   * @see JLabel
   */
  public ImageAnimationLabel(Icon icon) {
    super(icon);
    init();
  }

  /**
   *
   *
   * @param text
   * @see JLabel
   */
  public ImageAnimationLabel(String text) {
    super(text);
    init();
  }

  /**
   *
   *
   * @param text
   * @param horizontalAlignment
   * @see JLabel
   */
  public ImageAnimationLabel(String text, int horizontalAlignment) {
    super(text, horizontalAlignment);
    init();
  }

  /**
   *
   *
   * @param icon
   * @param horizontalAlignment
   * @see JLabel
   */
  public ImageAnimationLabel(Icon icon, int horizontalAlignment) {
    super(icon, horizontalAlignment);
    init();
  }

  /**
   *
   *
   * @param text
   * @param icon
   * @param horizontalAlignment
   * @see JLabel
   */
  public ImageAnimationLabel(String text, Icon icon, int horizontalAlignment) {
    super(text, icon, horizontalAlignment);
    init();
  }

  // TODO
  public A getImageAnimation() {
    return imageAnimation;
  }

  /**
   *
   *
   * @param imageAnimation
   */
  protected void init() {

    if (imageAnimation == null) {
      throw new NullPointerException("Image animation not set");
    }

    Thread t = new Thread(imageAnimation);
    t.setName(this.getClass().getSimpleName());
    t.start();

  }

  /**
   *
   *
   * @param imageAnimation
   */
  protected void setImageAnimation(A imageAnimation) {
    this.imageAnimation = imageAnimation;
  }

  /**
   *
   *
   * @param loops
   */
  public void animate(int loops) {
    if (imageIcon == null) {
      return;
    }

    // Create a new image only if necessary
    if (bufferedImage == null || bufferedImage.getWidth() != imageIcon.getIconWidth()
        || bufferedImage.getHeight() != imageIcon.getIconHeight()) {
      bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
          BufferedImage.TYPE_INT_ARGB);

      imageAnimation.setOutputImage(bufferedImage);
    }

    if (iconChanged) {
      super.setIcon(new ImageIcon(imageAnimation.getOutputImage()));

      iconChanged = false;
    }

    imageAnimation.go(loops);
  }

  /**
   * Pauses the animation
   *
   * @param pause
   */
  public void pause(boolean pause) {
    imageAnimation.pause(pause);
  }

  /**
   * A flag which indicates if the icon has been changed with {@link #setIcon(Icon)}
   *
   * @return
   */
  protected boolean hasIconChanged() {
    return iconChanged;
  }

  /**
   * Defines the icon this component will display. Setting a icon will stop the animation.
   */
  @Override
  public void setIcon(Icon icon) {

    // setIcon is also called when constructing JLabel -> imageAnimation might not
    // be initialized. Only pause when initialized and not paused yet.
    if (imageAnimation != null && !imageAnimation.isPaused()) {
      // Pause the animation wherever it is
      imageAnimation.pause(true);
    }

    // Save icon to be able to return it with getIcon
    this.originalIcon = icon;

    // This ImageIcon is used for drawing
    this.imageIcon = ImageUtil.iconToImageIcon(icon);

    iconChanged = true;

    super.setIcon(icon);
  }

  /**
   * Returns the original icon which has been set with {@link #setIcon(Icon)}
   *
   * @return
   */
  public Icon getOriginalIcon() {
    return originalIcon;
  }

  /**
   * Image to animate
   *
   * @return
   */
  protected ImageIcon getImageIcon() {
    return imageIcon;
  }

  /**
   * Returns the icon that the label displays.<br />
   * <br />
   * Note: This icon is not the same object as the one set with {@link #setIcon(Icon)}. This
   * {@link ImageAnimationLabel} uses an internally created buffered image to draw the animations
   * on. User {@link #getOriginalIcon()} to get the icon which has been set with
   * {@link #setIcon(Icon)}
   */
  @Override
  public Icon getIcon() {
    // Just a method override to provide additional javadoc information

    return super.getIcon();
  }

}
