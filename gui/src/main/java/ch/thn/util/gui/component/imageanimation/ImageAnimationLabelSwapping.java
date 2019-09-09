package ch.thn.util.gui.component.imageanimation;

import ch.thn.thread.controlledrunnable.ControlledRunnableEvent;
import ch.thn.thread.controlledrunnable.ControlledRunnableListener;
import ch.thn.util.gui.effects.imageanimation.ImageAnimationSwapping;

import java.awt.Image;

import javax.swing.Icon;

/**
 * A label with swapping images.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageAnimationLabelSwapping extends ImageAnimationLabel<ImageAnimationSwapping>
    implements ControlledRunnableListener {
  private static final long serialVersionUID = 3864075241985202132L;

  /**
   *
   *
   */
  public ImageAnimationLabelSwapping() {
    super();
  }

  /**
   *
   *
   * @param icon
   */
  public ImageAnimationLabelSwapping(Icon icon) {
    super(icon);
  }

  /**
   *
   *
   * @param icon
   * @param horizontalAlignment
   * @param imageAnimation
   */
  public ImageAnimationLabelSwapping(Icon icon, int horizontalAlignment) {
    super(icon, horizontalAlignment);
  }

  /**
   *
   *
   * @param text
   * @param icon
   * @param horizontalAlignment
   */
  public ImageAnimationLabelSwapping(String text, Icon icon, int horizontalAlignment) {
    super(text, icon, horizontalAlignment);
  }

  /**
   *
   *
   * @param text
   * @param imageAnimation
   */
  public ImageAnimationLabelSwapping(String text) {
    super(text);
  }

  /**
   *
   *
   * @param text
   * @param horizontalAlignment
   */
  public ImageAnimationLabelSwapping(String text, int horizontalAlignment) {
    super(text, horizontalAlignment);
  }


  /**
   *
   *
   */
  @Override
  protected void init() {
    setImageAnimation(new ImageAnimationSwapping(this, null));

    getImageAnimation().addControlledRunnableListener(this);

    super.init();
  }


  /**
   *
   *
   * @param image
   * @param timeout The timeout after swapping the image
   * @param delay The delay for the swap to start
   */
  public void addStep(Image image, long timeout, long delay) {

    getImageAnimation().addStep(image, timeout, delay);

  }

  /**
   *
   *
   * @param images
   * @param timeout
   * @param delay
   */
  public void addSteps(Image[] images, long timeout, long delay) {
    for (Image image : images) {
      addStep(image, timeout, delay);
    }
  }

  /**
   * Defines the icon this component will display when not running. Setting a icon here will stop
   * the animation.<br />
   * This icon will only be shown when the animation is not running.
   *
   */
  @Override
  public void setIcon(Icon icon) {
    super.setIcon(icon);
  }

  @Override
  public void runnableStateChanged(ControlledRunnableEvent e) {

    if (e.getStateType() == ControlledRunnableEvent.StateType.PAUSE) {
      if (getImageAnimation().isPaused()) {
        setIcon(getOriginalIcon());
      }
    }

  }

}
