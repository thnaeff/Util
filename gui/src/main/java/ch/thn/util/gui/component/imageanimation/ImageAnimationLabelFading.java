package ch.thn.util.gui.component.imageanimation;

import ch.thn.util.gui.effects.imageanimation.ImageAnimationFading;
import ch.thn.util.valuerange.ImageAlphaGradient;

import javax.swing.Icon;

/**
 * A label with an image which fades in and out.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageAnimationLabelFading extends ImageAnimationLabel<ImageAnimationFading> {
  private static final long serialVersionUID = 3864075241985202132L;

  /**
   *
   *
   */
  public ImageAnimationLabelFading() {
    super();
  }

  /**
   *
   *
   * @param icon
   */
  public ImageAnimationLabelFading(Icon icon) {
    super(icon);
  }

  /**
   *
   *
   * @param icon
   * @param horizontalAlignment
   * @param imageAnimation
   */
  public ImageAnimationLabelFading(Icon icon, int horizontalAlignment) {
    super(icon, horizontalAlignment);
  }

  /**
   *
   *
   * @param text
   * @param icon
   * @param horizontalAlignment
   */
  public ImageAnimationLabelFading(String text, Icon icon, int horizontalAlignment) {
    super(text, icon, horizontalAlignment);
  }

  /**
   *
   *
   * @param text
   * @param imageAnimation
   */
  public ImageAnimationLabelFading(String text) {
    super(text);
  }

  /**
   *
   *
   * @param text
   * @param horizontalAlignment
   */
  public ImageAnimationLabelFading(String text, int horizontalAlignment) {
    super(text, horizontalAlignment);
  }


  /**
   *
   *
   */
  @Override
  protected void init() {
    setImageAnimation(new ImageAnimationFading(this, null));

    super.init();
  }


  /**
   *
   *
   * @param gradient
   * @param timeout
   * @param delay
   * @param repeat
   */
  public void addStep(ImageAlphaGradient gradient, long timeout, long delay, int repeat) {

    getImageAnimation().addStep(getImageIcon().getImage(), gradient, timeout, delay, repeat);

  }


  @Override
  public void animate(int loops) {

    if (hasIconChanged()) {
      // Reset the image for all steps if the image has changed
      for (int i = 0; i < getImageAnimation().stepCount(); i++) {
        getImageAnimation().getAnimationStep(i).getImageManipulation()
            .setImage1(getImageIcon().getImage());
      }

    }

    super.animate(loops);
  }


}
