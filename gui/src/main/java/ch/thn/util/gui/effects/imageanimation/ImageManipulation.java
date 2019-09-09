package ch.thn.util.gui.effects.imageanimation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A base class for image manipulations. Contains some common variables and methods.
 *
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public abstract class ImageManipulation {

  protected BufferedImage imageManipulated = null;

  /** The output graphics where the manipulated image is drawn */
  protected Graphics2D graphicsManipulated = null;

  /** The width of the whole output image */
  protected int imageManipulatedWidth = 0;
  /** The height of the whole output image */
  protected int imageManipulatedHeight = 0;

  /** The X coordinate of the center */
  protected int centerX = 0;
  /** The Y coordinate of the center */
  protected int centerY = 0;

  private boolean graphicsCreatedInternally = false;

  public static final Color cClear = new Color(0, 0, 0, 0);


  /**
   * 
   */
  public ImageManipulation() {


  }



  /**
   * 
   * 
   * @param imageToDrawOn
   * @param graphicsToDrawOn
   */
  public ImageManipulation(BufferedImage imageToDrawOn, Graphics2D graphicsToDrawOn) {

    setManipulatingImage(imageToDrawOn, graphicsToDrawOn);

  }

  /**
   * 
   * 
   * @param width
   * @param height
   */
  public ImageManipulation(int width, int height) {

    setManipulatingImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB), null);

  }



  /**
   * Sets the image and graphics which should be used to draw the manipulated images on. The
   * graphics should be taken from imageToDrawOn, they can be <code>null</code> to have them created
   * internally. The parameter graphicsToDrawOn is only available so that the same graphics object
   * can be shared between multiple image manipulation instances.
   * 
   * @param graphicsToDrawOn
   */
  protected void setManipulatingImage(BufferedImage imageToDrawOn, Graphics2D graphicsToDrawOn) {
    this.imageManipulated = imageToDrawOn;

    if (graphicsToDrawOn != null) {
      graphicsCreatedInternally = false;
      this.graphicsManipulated = graphicsToDrawOn;
    } else {
      graphicsCreatedInternally = true;
      this.graphicsManipulated = imageToDrawOn.createGraphics();
    }

    imageManipulatedWidth = imageToDrawOn.getWidth();
    imageManipulatedHeight = imageManipulated.getHeight();
  }

  /**
   * Calculates centerWidth and centerHeight to center an image with the given dimensions.
   * 
   * @param imageWidth
   * @param imageHeight
   */
  protected void calcCenter(int imageWidth, int imageHeight) {
    this.centerX = imageManipulatedWidth / 2 - imageWidth / 2;
    this.centerY = imageManipulatedHeight / 2 - imageHeight / 2;
  }

  /**
   * Clears the image by filling it with the given color
   */
  public synchronized void clear(Color c) {
    graphicsManipulated.setBackground(c);
    try {
      graphicsManipulated.clearRect(0, 0, imageManipulatedWidth, imageManipulatedHeight);
    } catch (NullPointerException e) {
      // Not sure yet why clearRect throws a null pointer exception here,
      // the parameters are not NULL...
    }
  }

  /**
   * Clears the image by filling it with a default color with alpha=0
   */
  public void clear() {
    clear(cClear);
  }

  /**
   * Resets the image
   */
  public void reset() {
    if (graphicsCreatedInternally) {
      // Also "reset" the graphics object by creating a new one
      graphicsManipulated = this.imageManipulated.createGraphics();
    }
  }


  /**
   * Checks if there are any more steps or if it is done.
   * 
   * @return <code>true</code> if all steps are performed, <code>false</code> if more steps are
   *         available.
   */
  public abstract boolean isDone();


  /**
   * 
   * 
   * @return
   */
  protected abstract BufferedImage manipulate();

}
