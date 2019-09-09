package ch.thn.util.gui.effects.imageanimation;

import ch.thn.thread.controlledrunnable.ControlledRunnableEvent;
import ch.thn.thread.controlledrunnable.ControlledRunnableListener;
import ch.thn.thread.controlledrunnable.RepeatingRunnable;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JLabel;


/**
 * Base class for image animation. See {@link ImageAnimationFading} as example of an implementation.
 *
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 * @param <S>
 */
public abstract class ImageAnimation<M extends ImageManipulation> extends RepeatingRunnable
    implements ControlledRunnableListener {

  private LinkedList<AnimationStep> animationSteps = null;

  private BufferedImage imageOut = null;
  private Graphics2D graphicsOut = null;

  private Component componentToRepaint = null;

  private Rectangle repaintArea = null;

  private volatile boolean isAnimating = false;


  /**
   *
   *
   * @param componentToRepaint The component on which the image is being drawn
   * @param width The width of the animated image(s)
   * @param height The height of the animated image(s)
   */
  public ImageAnimation(Component componentToRepaint, int width, int height) {
    super();
    init(componentToRepaint, null, null, width, height);
  }

  /**
   *
   * @param componentToRepaint The component on which the image is being drawn
   * @param imageOut The image where the animations should be drawn on
   */
  public ImageAnimation(Component componentToRepaint, BufferedImage imageOut) {
    super();
    init(componentToRepaint, null, imageOut, 0, 0);
  }

  /**
   *
   *
   * @param componentToRepaint The component on which the image is being drawn
   * @param repaintArea The area of on the image component which should be updated after painting a
   *        new image
   * @param width The width of the animated image(s)
   * @param height The height of the animated image(s)
   */
  public ImageAnimation(Component componentToRepaint, Rectangle repaintArea, int width,
      int height) {
    super();
    init(componentToRepaint, repaintArea, null, width, height);
  }

  /**
   *
   * @param componentToRepaint The component on which the image is being drawn
   * @param repaintArea The area which should be updated after painting a new image
   * @param imageOut The image where the animations should be drawn on
   */
  public ImageAnimation(Component componentToRepaint, Rectangle repaintArea,
      BufferedImage imageOut) {
    super();
    init(componentToRepaint, repaintArea, imageOut, 0, 0);
  }

  /**
   *
   * @param componentToRepaint The component on which the image is being drawn
   * @param repaintArea The area which should be updated after painting a new image
   * @param imageOut The image where the animations should be drawn on
   * @param width The size of the image. Only needed if imageOut=<code>null</code>
   * @param height The size of the image. Only needed if imageOut=<code>null</code>
   */
  private void init(Component componentToRepaint, Rectangle repaintArea, BufferedImage imageOut,
      int width, int height) {
    this.componentToRepaint = componentToRepaint;
    this.repaintArea = repaintArea;

    addControlledRunnableListener(this);

    animationSteps = new LinkedList<>();

    if (imageOut == null) {
      // If no width and height are given, the buffered image is not created here
      // and it has to be set manually wit the setAnimatedImage method
      if (width != 0 || height != 0) {
        setOutputImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
      }
    } else {
      setOutputImage(imageOut);
    }

    pause(true);
  }

  /**
   * Returns the image which is used to draw the animated images.<br>
   * Draw this image on a component (or add it to a {@link JLabel} with new JLabel(new
   * ImageIcon(animatedImage)) for example) to use the animation.
   *
   * @return
   */
  public BufferedImage getOutputImage() {
    return imageOut;
  }

  /**
   *
   *
   * @return
   */
  public Graphics2D getOutputGraphics() {
    return graphicsOut;
  }

  /**
   * Sets the given image as image to draw on
   *
   * @param image
   */
  public void setOutputImage(BufferedImage image) {
    this.imageOut = image;
    this.graphicsOut = image.createGraphics();

    // Set the new image also for all the already defined steps
    for (AnimationStep animationStep : getAnimationSteps()) {
      animationStep.getImageManipulation().setManipulatingImage(image, graphicsOut);
    }
  }

  /**
   * Set a specific area (the area within which the image is being drawn) which needs to be updated
   * after drawing a new image. If set to null, the whole component is updated.
   *
   * @param rect
   */
  public void setRepaintArea(Rectangle rect) {
    repaintArea = rect;
  }

  /**
   * Removes all animation steps
   *
   */
  public void clearSteps() {
    animationSteps.clear();
  }

  /**
   * The number of animation steps
   *
   */
  public int stepCount() {
    return animationSteps.size();
  }

  /**
   *
   *
   * @param stepIndex
   * @return
   */
  public AnimationStep getAnimationStep(int stepIndex) {
    return animationSteps.get(stepIndex);
  }

  /**
   * Resets the fading and starts at the beginning with a cleared image.
   */
  @Override
  public void reset() {
    super.reset();
    clear();
  }

  /**
   * Clears the image
   */
  protected abstract void clear();

  /**
   * Returns the list with all the animation steps
   *
   * @return
   */
  protected LinkedList<AnimationStep> getAnimationSteps() {
    return animationSteps;
  }

  /**
   * Returns <code>true</code> if the animation is currently running, <code>false</code> if the
   * animation is paused, stopped or not yet started
   *
   * @return
   */
  public boolean isAnimating() {
    return isAnimating;
  }

  /**
   *
   *
   * @param animationStep
   * @return
   */
  public int addAnimationStep(AnimationStep animationStep) {
    animationSteps.add(animationStep);
    return animationSteps.size() - 1;
  }

  /**
   *
   *
   * @param animationStep
   * @param animationIndex
   * @param animationRepeat
   */
  protected void animation(AnimationStep animationStep, int animationIndex, int animationRepeat) {
    ImageManipulation imageManipulation = animationStep.imageManipulation;

    imageManipulation.reset();

    // All steps until done
    while (!imageManipulation.isDone() && !isResetRequested() && !isStopRequested()) {

      if (isPauseRequested()) {
        runPause(true);
      }

      imageManipulation.manipulate();

      repaintComponentOrArea();

      if (animationStep.timeout != 0) {
        controlledWait(animationStep.timeout);
      }

    } // End all steps until done

  }

  @Override
  public boolean execute() {

    // Each animation step
    for (int animationIndex = 0; animationIndex < animationSteps.size() && !isResetRequested()
        && !isStopRequested(); animationIndex++) {

      AnimationStep animationStep = animationSteps.get(animationIndex);

      // Step repeats
      for (int stepRepeat = 0; stepRepeat <= animationStep.repeat && !isResetRequested()
          && !isStopRequested(); stepRepeat++) {

        if (animationStep.delay > 0) {
          controlledWait(animationStep.delay);
        }

        animation(animationStep, animationIndex, stepRepeat);

      } // End step repeats

    } // End each animation step

    // Not done. The number of loops has to be reached.
    return false;
  }


  /**
   * Repaints the component which was set for this {@link ImageAnimation} or the given area on that
   * component. This method should be called after each fading step or whenever the output image
   * changes.
   *
   */
  protected void repaintComponentOrArea() {
    if (componentToRepaint != null) {
      if (repaintArea != null) {
        componentToRepaint.repaint((int) repaintArea.getX(), (int) repaintArea.getY(),
            (int) repaintArea.getWidth(), (int) repaintArea.getHeight());
      } else {
        componentToRepaint.repaint();
      }
    }
  }

  @Override
  public void runnableStateChanged(ControlledRunnableEvent e) {

    if (e.getStateType() == ControlledRunnableEvent.StateType.PAUSE) {
      isAnimating = !isPaused();
    }

  }



  /**************************************************************************
   *
   *
   *
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  public class AnimationStep {

    private M imageManipulation = null;

    private long timeout = 0;
    private long delay = 0;
    private long repeat = 0;

    /**
     *
     *
     * @param imageManipulation
     * @param timeout
     * @param delay
     * @param repeat
     */
    public AnimationStep(M imageManipulation, long timeout, long delay, int repeat) {
      this.imageManipulation = imageManipulation;
      this.timeout = timeout;
      this.delay = delay;
      this.repeat = repeat;

    }

    /**
     *
     *
     * @return
     */
    public M getImageManipulation() {
      return imageManipulation;
    }

    /**
     *
     *
     * @return
     */
    public long getTimeout() {
      return timeout;
    }

    /**
     *
     *
     * @return
     */
    public long getDelay() {
      return delay;
    }

    /**
     *
     *
     * @return
     */
    public long getRepeat() {
      return repeat;
    }


  }


}
