package ch.thn.util.gui.component.imageanimation;

import ch.thn.util.gui.effects.imageanimation.ImageAnimationRotating;

import javax.swing.Icon;

/**
 * A label with a rotating image.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageAnimationLabelRotating extends ImageAnimationLabel<ImageAnimationRotating> {
  private static final long serialVersionUID = 3864075241985202132L;

  /**
   *
   *
   */
  public ImageAnimationLabelRotating() {
    super();
  }

  /**
   *
   *
   * @param icon
   */
  public ImageAnimationLabelRotating(Icon icon) {
    super(icon);
  }

  /**
   *
   *
   * @param icon
   * @param horizontalAlignment
   * @param imageAnimation
   */
  public ImageAnimationLabelRotating(Icon icon, int horizontalAlignment) {
    super(icon, horizontalAlignment);
  }

  /**
   *
   *
   * @param text
   * @param icon
   * @param horizontalAlignment
   */
  public ImageAnimationLabelRotating(String text, Icon icon, int horizontalAlignment) {
    super(text, icon, horizontalAlignment);
  }

  /**
   *
   *
   * @param text
   * @param imageAnimation
   */
  public ImageAnimationLabelRotating(String text) {
    super(text);
  }

  /**
   *
   *
   * @param text
   * @param horizontalAlignment
   */
  public ImageAnimationLabelRotating(String text, int horizontalAlignment) {
    super(text, horizontalAlignment);
  }


  /**
   *
   *
   */
  @Override
  protected void init() {
    setImageAnimation(new ImageAnimationRotating(this, null));

    super.init();
  }


  /**
   *
   *
   * @param degreesToRotate
   * @param degreesPerStep
   * @param timeout
   * @param delay
   * @param repeat
   */
  public void addStep(int degreesToRotate, int degreesPerStep, long timeout, long delay,
      int repeat) {

    getImageAnimation().addStep(getImageIcon().getImage(), degreesToRotate, degreesPerStep, timeout,
        delay, repeat);

  }


  @Override
  public void animate(int loops) {

    if (hasIconChanged()) {
      // Reset the image for all steps if the image has changed
      for (int i = 0; i < getImageAnimation().stepCount(); i++) {
        getImageAnimation().getAnimationStep(i).getImageManipulation()
            .setImage(getImageIcon().getImage());
      }

    }

    super.animate(loops);
  }


}
