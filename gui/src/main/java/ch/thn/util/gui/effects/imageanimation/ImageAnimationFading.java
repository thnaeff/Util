package ch.thn.util.gui.effects.imageanimation;

import ch.thn.util.valuerange.ImageAlphaGradient;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


/**
 * Automated image fading which uses the {@link ImageFading} class and automatically performs the
 * fading steps. Multiple images, gradients and animation speeds can be added after each other. It
 * can be defined for each step how many times the step should be repeated.
 *
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageAnimationFading extends ImageAnimation<ImageFading> {


  /**
   *
   *
   * @param componentToRepaint
   * @param repaintArea
   * @param imageOut
   */
  public ImageAnimationFading(Component componentToRepaint, Rectangle repaintArea,
      BufferedImage imageOut) {
    super(componentToRepaint, repaintArea, imageOut);
  }

  /**
   *
   *
   * @param componentToRepaint
   * @param repaintArea
   * @param width
   * @param height
   */
  public ImageAnimationFading(Component componentToRepaint, Rectangle repaintArea, int width,
      int height) {
    super(componentToRepaint, repaintArea, width, height);
  }

  /**
   *
   *
   * @param componentToRepaint
   * @param width
   * @param height
   */
  public ImageAnimationFading(Component componentToRepaint, int width, int height) {
    super(componentToRepaint, width, height);
  }

  /**
   *
   *
   * @param componentToRepaint
   * @param imageOut
   */
  public ImageAnimationFading(Component componentToRepaint, BufferedImage imageOut) {
    super(componentToRepaint, imageOut);
  }


  /**
   * Adds a new step to the animation. This step fades from one image to the other using the given
   * gradients.
   *
   * @param image1 The fist image (fading "from" this one)
   * @param image2 The second image (fading "to" this one)
   * @param gradient1 The gradient for the first image
   * @param gradient2 The gradient for the second image
   * @param timeout The timeout between each image gradient step (determines the speed of the
   *        animation)
   * @param delay The delay for the animation to start
   * @param repeat The number of times this animation step should be repeated. A repeat of 1 means
   *        the step will be animated twice (once plus 1 repeat).
   * @return The step index
   */
  public int addStep(Image image1, Image image2, ImageAlphaGradient gradient1,
      ImageAlphaGradient gradient2, long timeout, long delay, int repeat) {

    return addAnimationStep(new AnimationStep(new ImageFading(getOutputImage(), getOutputGraphics(),
        image1, image2, gradient1, gradient2), timeout, delay, repeat));

  }

  /**
   * Adds a new step to the animation. This step fades only the one image using the given gradient.
   *
   * @param image
   * @param gradient The gradient for the image
   * @param timeout The timeout between each image gradient step (determines the speed of the
   *        animation)
   * @param delay The delay for the animation to start
   * @param repeat The number of times this animation step should be repeated. A repeat of 1 means
   *        the step will be animated twice (once plus 1 repeat).
   * @return The step index
   */
  public int addStep(Image image, ImageAlphaGradient gradient, long timeout, long delay,
      int repeat) {

    return addAnimationStep(new AnimationStep(
        new ImageFading(getOutputImage(), getOutputGraphics(), image, null, gradient, null),
        timeout, delay, repeat));

  }


  @Override
  protected void clear() {
    // Since all the animation steps use the same buffered image, only one
    // has to be cleared
    if (getAnimationSteps().size() > 0) {
      getAnimationSteps().get(0).getImageManipulation().clear();
      repaintComponentOrArea();
    }
  }

}
