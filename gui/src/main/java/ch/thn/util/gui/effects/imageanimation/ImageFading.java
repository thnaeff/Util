package ch.thn.util.gui.effects.imageanimation;

import ch.thn.util.valuerange.ImageAlphaGradient;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Fading a single image or fading from one image to the other with given gradients. Repeated
 * calling the {@link #fade()} method performs each fading step.<br />
 * A {@link BufferedImage} can be passed on or one will be created internally if not provided. For
 * each fading step, the image is drawn on that buffered image.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageFading extends ImageManipulation {

  private Image image1 = null;
  private Image image2 = null;

  private ImageAlphaGradient gradient1 = null;
  private ImageAlphaGradient gradient2 = null;

  private Float nextAlpha1 = null;
  private Float nextAlpha2 = null;

  private int image1Width = 0;
  private int image1Height = 0;

  /**
   *
   *
   * @param image1
   * @param image2
   * @param gradient1
   * @param gradient2
   */
  public ImageFading(Image image1, Image image2, ImageAlphaGradient gradient1,
      ImageAlphaGradient gradient2) {
    this(null, null, image1, image2, gradient1, gradient2);
  }

  /**
   *
   *
   * @param image1
   * @param gradient1
   */
  public ImageFading(Image image1, ImageAlphaGradient gradient1) {
    this(null, null, image1, null, gradient1, null);
  }

  /**
   *
   *
   * @param imageToDrawOn
   * @param graphicsToDrawOn
   * @param image1
   * @param gradient1
   */
  public ImageFading(BufferedImage imageToDrawOn, Graphics2D graphicsToDrawOn, Image image1,
      ImageAlphaGradient gradient1) {
    this(imageToDrawOn, graphicsToDrawOn, image1, null, gradient1, null);
  }

  /**
   *
   *
   * @param imageToDrawOn
   * @param graphicsToDrawOn
   * @param image1
   * @param image2
   * @param gradient1
   * @param gradient2
   */
  public ImageFading(BufferedImage imageToDrawOn, Graphics2D graphicsToDrawOn, Image image1,
      Image image2, ImageAlphaGradient gradient1, ImageAlphaGradient gradient2) {
    super();

    setImage1(image1);
    setImage1(image1);

    this.gradient1 = gradient1;
    this.gradient2 = gradient2;

    int width = 0;
    int height = 0;

    if (image1 == null) {
      width = image2.getWidth(null);
      height = image2.getHeight(null);
    } else {
      width = image1.getWidth(null);
      height = image1.getHeight(null);
    }

    if (imageToDrawOn == null) {
      setManipulatingImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB), null);
    } else {
      setManipulatingImage(imageToDrawOn, graphicsToDrawOn);
    }

    reset();

  }


  @Override
  public void setManipulatingImage(BufferedImage imageToDrawOn, Graphics2D graphicsToDrawOn) {
    super.setManipulatingImage(imageToDrawOn, graphicsToDrawOn);
    calcCenter(image1Width, image1Height);
  };

  /**
   *
   *
   * @param image1
   */
  public void setImage1(Image image1) {
    this.image1 = image1;

    if (image1 != null) {
      image1Width = image1.getWidth(null);
      image1Height = image1.getHeight(null);
    }

    calcCenter(image1Width, image1Height);
  }

  /**
   *
   *
   * @param image1
   */
  public void setImage2(Image image2) {
    this.image2 = image2;
  }

  /**
   *
   *
   * @param gradient1
   */
  public void setGradient1(ImageAlphaGradient gradient1) {
    this.gradient1 = gradient1;
  }

  /**
   *
   *
   * @param gradient2
   */
  public void setGradient2(ImageAlphaGradient gradient2) {
    this.gradient2 = gradient2;
  }

  /**
   * Resets the gradients and continues with the first gradient value
   */
  @Override
  public void reset() {
    super.reset();

    if (gradient1 != null) {
      gradient1.reset();
      nextAlpha1 = gradient1.getNext().floatValue();
    }

    if (gradient2 != null) {
      gradient2.reset();
      nextAlpha2 = gradient2.getNext().floatValue();
    }


  }


  @Override
  public boolean isDone() {
    // The fading is done if there are no alpha values left any more
    if ((gradient1 == null || nextAlpha1 == null) && (gradient2 == null || nextAlpha2 == null)) {
      return true;
    }

    return false;
  }

  @Override
  protected BufferedImage manipulate() {
    return fade();
  }

  /**
   * Continues with the next fading step and returns the resulting image.
   *
   * @return
   */
  public BufferedImage fade() {
    clear();

    // --- image1: draw with current alpha value
    if (image1 != null) {
      // Don't draw anything if alpha value is 0
      if (nextAlpha1 != null && nextAlpha1 != 0.0) {
        graphicsManipulated
            .setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nextAlpha1));
        graphicsManipulated.drawImage(image1, centerX, centerY, null);
      }
    }

    // --- image1: get next alpha value
    if (gradient1 != null) {
      if (gradient1.hasNext()) {
        nextAlpha1 = gradient1.getNext().floatValue();
      } else {
        nextAlpha1 = null;
      }
    }


    // --- image2: draw with current alpha value
    if (image2 != null) {
      // Don't draw anything if alpha value is 0
      if (nextAlpha2 != null && nextAlpha2 != 0.0) {
        graphicsManipulated
            .setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nextAlpha2));
        graphicsManipulated.drawImage(image2, centerX, centerY, null);
      }
    }

    // --- image2: get next alpha value
    if (gradient2 != null) {
      if (gradient2.hasNext()) {
        nextAlpha2 = gradient2.getNext().floatValue();
      } else {
        nextAlpha2 = null;
      }
    }

    // graphicsManipulated.drawRect(0, 0, imageManipulatedWidth-1, imageManipulatedHeight-1);

    return imageManipulated;
  }


}
