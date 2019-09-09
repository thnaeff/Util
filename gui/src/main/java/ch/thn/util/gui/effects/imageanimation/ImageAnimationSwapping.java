package ch.thn.util.gui.effects.imageanimation;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageAnimationSwapping extends ImageAnimation<ImageSwapping> {


  /**
   * 
   * 
   * @param componentToRepaint
   * @param repaintArea
   * @param imageOut
   */
  public ImageAnimationSwapping(Component componentToRepaint, Rectangle repaintArea,
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
  public ImageAnimationSwapping(Component componentToRepaint, Rectangle repaintArea, int width,
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
  public ImageAnimationSwapping(Component componentToRepaint, int width, int height) {
    super(componentToRepaint, width, height);
  }

  /**
   * 
   * 
   * @param componentToRepaint
   * @param imageOut
   */
  public ImageAnimationSwapping(Component componentToRepaint, BufferedImage imageOut) {
    super(componentToRepaint, imageOut);

  }


  /**
   * 
   * 
   * @param image
   * @param timeout The timeout after swapping the image
   * @param delay The delay for the swap to start
   * @return The step index
   */
  public int addStep(Image image, long timeout, long delay) {

    return addAnimationStep(new AnimationStep(
        new ImageSwapping(getOutputImage(), getOutputGraphics(), image), timeout, delay, 0));

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
