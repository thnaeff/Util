package ch.thn.util.gui.effects.imageanimation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageRotating extends ImageManipulation {


  private Image image = null;

  private int imageWidth = 0;
  private int imageHeight = 0;

  private double radiansPerStep = 0;

  private int degreesPerStep = 0;
  private int degreesRotated = 0;
  private int degreesToRotate = 0;


  /**
   * 
   * 
   * @param image
   * @param degreesToRotate
   * @param degreesPerStep
   */
  public ImageRotating(Image image, int degreesToRotate, int degreesPerStep) {
    this(null, null, image, degreesToRotate, degreesPerStep);
  }


  /**
   * 
   * 
   * @param imageToDrawOn
   * @param graphicsToDrawOn
   * @param image
   * @param degreesToRotate
   * @param degreesPerStep
   */
  public ImageRotating(BufferedImage imageToDrawOn, Graphics2D graphicsToDrawOn, Image image,
      int degreesToRotate, int degreesPerStep) {
    this.degreesToRotate = degreesToRotate;

    setDegreesPerStep(degreesPerStep);
    setImage(image);


    if (imageToDrawOn == null) {
      setManipulatingImage(new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB),
          null);
    } else {
      setManipulatingImage(imageToDrawOn, graphicsToDrawOn);
    }


  }

  @Override
  protected void setManipulatingImage(BufferedImage imageToDrawOn, Graphics2D graphicsToDrawOn) {
    super.setManipulatingImage(imageToDrawOn, graphicsToDrawOn);

    graphicsManipulated.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    calcCenter(imageWidth, imageHeight);
  }

  /**
   * 
   * 
   * @param image
   */
  public void setImage(Image image) {
    this.image = image;

    if (image != null) {
      this.imageWidth = image.getWidth(null);
      this.imageHeight = image.getHeight(null);
    } else {
      this.imageWidth = 0;
      this.imageHeight = 0;
    }

    calcCenter(imageWidth, imageHeight);
  }

  /**
   * 
   * 
   * @param degreesPerStep The degreesPerStep to set
   */
  public void setDegreesPerStep(int degreesPerStep) {
    this.degreesPerStep = degreesPerStep;
    this.radiansPerStep = Math.toRadians(degreesPerStep);
  }

  /**
   * 0 = infinite rotation.
   * 
   * @param degreesToRotate
   */
  public void setDegreesToRotate(int degreesToRotate) {
    this.degreesToRotate = degreesToRotate;
  }


  @Override
  public boolean isDone() {
    return degreesToRotate != 0 && degreesRotated >= degreesToRotate;
  }

  @Override
  public void reset() {
    super.reset();
    degreesRotated = 0;
  }


  @Override
  protected BufferedImage manipulate() {
    return rotate();
  }


  public BufferedImage rotate() {
    clear();

    double radians = radiansPerStep;

    // Correct the angle if it would go over the maximal angle
    if (degreesToRotate != 0 && degreesRotated + Math.abs(degreesPerStep) > degreesToRotate) {
      radians = Math.toRadians(degreesToRotate - degreesRotated);
    }

    graphicsManipulated.rotate(radians, imageManipulatedWidth / 2, imageManipulatedHeight / 2);
    graphicsManipulated.drawImage(image, centerX, centerY, null);
    // graphicsManipulated.drawRect(0, 0, imageManipulatedWidth-1, imageManipulatedWidth-1);

    degreesRotated += Math.abs(degreesPerStep);

    return imageManipulated;
  }


}
