package ch.thn.util.gui.component.extension;

import ch.thn.util.gui.component.MouseCursorArea;
import ch.thn.util.gui.component.ToolTipArea;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;

import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Draws images in the given component. The specialty of this class is that the image can also be
 * drawn outside of the components borders. The class will automatically extend the border (using an
 * invisible outer border) to draw the image.<br>
 * The location of the images can be set with one of the constants<br>
 * <ul>
 * <li>{@link #NORTH_EAST}</li>
 * <li>{@link #NORTH_WEST}</li>
 * <li>{@link #SOUTH_EAST}</li>
 * <li>{@link #SOUTH_WEST}</li>
 * </ul>
 * <br>
 * These constants set the location of the image in their corner. Using the
 * {@link #addImage(Image, int, int, int)} method gives the possibility to move the image out of the
 * corner.<br>
 * <br>
 * ====== USAGE ======<br>
 * <br>
 * To use the {@link BorderImage} class the following steps are needed:<br>
 * <ul>
 * <li>In your class, create an instance of {@link BorderImage}</li>
 * <li>Add images to your {@link BorderImage} object, using the {@link #addImage(Image, int)} or
 * {@link #addImage(Image, int, int, int)} method.</li>
 * <li>Overwrite the paintBorder-method in your class. Add the call to
 * {@link #paintImages(Graphics)} <b>after</b> the super-call super.paintBorder(g) (the images need
 * to be painted on top of everything else, thus maybe paintChildren needs to be overwritten in case
 * any components are painted over the image).</li>
 * </ul>
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class BorderImage implements AncestorListener, PropertyChangeListener, ComponentListener {

  public static final int NORTH_EAST = 0;
  public static final int NORTH_WEST = 1;
  public static final int SOUTH_EAST = 2;
  public static final int SOUTH_WEST = 3;

  private JComponent component = null;

  private ComponentPadding borderExtension = null;

  private LinkedHashMap<Image, AddedImage> images = null;

  private int top = 0;
  private int right = 0;
  private int bottom = 0;
  private int left = 0;
  private int componentWidth = 0;
  private int componentHeight = 0;

  /**
   *
   * @param component
   */
  public BorderImage(JComponent component) {
    this.component = component;

    images = new LinkedHashMap<Image, AddedImage>();

    borderExtension = new ComponentPadding(component);

    component.addAncestorListener(this);
    component.addPropertyChangeListener(this);
    component.addComponentListener(this);
  }

  /**
   * Adds an image in the corner specified with the given position.<br>
   * Valid position values are:<br>
   * <ul>
   * <li>NORTH_EAST</li>
   * <li>NORTH_WEST</li>
   * <li>SOUTH_EAST</li>
   * <li>SOUTH_WEST</li>
   * </ul>
   *
   * @param image
   * @param position
   */
  public void addImage(Image image, int position) {
    addImage(image, position, 0, 0);
  }

  /**
   * Adds an image in the corner specified with the given position.<br>
   * Valid position values are:<br>
   * <ul>
   * <li>NORTH_EAST</li>
   * <li>NORTH_WEST</li>
   * <li>SOUTH_EAST</li>
   * <li>SOUTH_WEST</li>
   * </ul>
   * The position of the image can be adjusted with the offset-parameters. If the position of the
   * image is outside of the component, the border will be extended so that the image can be drawn.
   *
   *
   * @param image
   * @param position
   * @param offsetX
   * @param offsetY
   */
  public void addImage(Image image, int position, int offsetX, int offsetY) {
    images.put(image, new AddedImage(image, position, offsetX, offsetY));
  }

  /**
   * Sets a tool tip message for the given image. The image needs to be added before with one of the
   * addImage-methods.
   *
   * @param image
   * @param toolTipString
   */
  public void setToolTip(Image image, String toolTipString) {
    if (!images.containsKey(image)) {
      throw new ComponentExtensionError("Image "
          + image
          + " not found!");
    }

    images.get(image).setToolTip(toolTipString);
  }

  /**
   * Sets a mouse cursor for the given image. The image needs to be added before with one of the
   * addImage-methods.
   *
   * @param image
   * @param cursor
   */
  public void setMouseCursor(Image image, int cursor) {
    if (!images.containsKey(image)) {
      throw new ComponentExtensionError("Image "
          + image
          + " not found!");
    }

    images.get(image).setMouseCursor(cursor);
  }

  /**
   *
   */
  public void removeAllImages() {
    images.clear();
  }

  /**
   *
   * @param show
   */
  public void showAll(boolean show) {
    for (AddedImage i : images.values()) {
      i.show(show);
    }
  }

  /**
   *
   * @param image
   */
  public void removeImage(Image image) {
    images.remove(image);
  }

  /**
   * Marks an image to be shown. The image needs to be added before with one of the
   * addImage-methods.
   *
   * @param image
   * @param show
   */
  public void show(Image image, boolean show) {
    if (!images.containsKey(image)) {
      throw new ComponentExtensionError("Image "
          + image
          + " not found!");
    }

    images.get(image).show(show);
  }

  /**
   *
   * @param image
   * @return
   */
  public boolean isShowing(Image image) {
    if (!images.containsKey(image)) {
      throw new ComponentExtensionError("Image "
          + image
          + " not found!");
    }

    return images.get(image).isShowing();
  }

  /**
   * Draws all the images which are set to show=true.
   *
   * @param g
   */
  public void paintImages(Graphics g) {

    for (AddedImage i : images.values()) {
      if (i.isShowing()) {
        g.drawImage(i.getImage(), i.getX(), i.getY(), component);
      }
    }

  }

  /**
   * Updates all the images and recalculates their position and the size of the border
   */
  private void update() {
    PaddingBorder border = borderExtension.getExtendedBorder();
    int[] thickness = border.getThickness();

    componentWidth = (int) component.getSize().getWidth() - thickness[ComponentPadding.EAST]
        - thickness[ComponentPadding.WEST];
    componentHeight = (int) component.getSize().getHeight() - thickness[ComponentPadding.NORTH]
        - thickness[ComponentPadding.SOUTH];

    top = 0;
    right = 0;
    bottom = 0;
    left = 0;

    for (AddedImage i : images.values()) {
      i.recalculate();
    }

    for (AddedImage i : images.values()) {
      i.adjust();
    }

    borderExtension.setThickness(top, right, bottom, left);

    // TODO necessary to validate?
    // component.getParent().validate();
  }


  @Override
  public void ancestorAdded(AncestorEvent event) {
    update();
  }

  @Override
  public void ancestorMoved(AncestorEvent event) {}

  @Override
  public void ancestorRemoved(AncestorEvent event) {}

  @Override
  public void propertyChange(PropertyChangeEvent evt) {

    if (evt.getPropertyName().equals("border")) {
      update();
    }

  }

  @Override
  public void componentHidden(ComponentEvent e) {}

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void componentResized(ComponentEvent e) {
    update();
  }

  @Override
  public void componentShown(ComponentEvent e) {}



  /**
   * Holds the added image and its position (with tool tip message and cursor type)
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  private class AddedImage {

    private ToolTipArea toolTip = null;

    private MouseCursorArea cursorArea = null;

    private Image image = null;

    private int imageWidth = 0;
    private int imageHeight = 0;
    private int position = 0;
    private int offsetX = 0;
    private int offsetY = 0;
    private int x = 0;
    private int y = 0;

    private boolean show = true;


    /**
     *
     * @param image
     * @param position
     * @param offsetX
     * @param offsetY
     */
    public AddedImage(Image image, int position, int offsetX, int offsetY) {
      this.image = image;
      this.position = position;
      this.offsetX = offsetX;
      this.offsetY = offsetY;

      imageWidth = image.getWidth(component);
      imageHeight = image.getHeight(component);

      toolTip = new ToolTipArea(component);
      cursorArea = new MouseCursorArea(component);
    }

    /**
     *
     * @param show
     */
    public void show(boolean show) {
      this.show = show;
      toolTip.active(show);
      cursorArea.active(show);
    }

    /**
     *
     * @return
     */
    public boolean isShowing() {
      return show;
    }

    /**
     *
     * @param toolTipMessage
     */
    public void setToolTip(String toolTipMessage) {
      toolTip.setMessage(toolTipMessage, x - 1, y - 1, imageWidth + 2, imageHeight + 2);
    }

    /**
     *
     * @param cursor
     */
    public void setMouseCursor(int cursor) {
      cursorArea.setCursor(cursor, x - 1, y - 1, imageWidth + 2, imageHeight + 2);
    }

    /**
     * @return the x
     */
    public int getX() {
      return x;
    }

    /**
     *
     */
    public int getY() {
      return y;
    }

    /**
     * @return the image
     */
    public Image getImage() {
      return image;
    }

    /**
     * Recalculate the position of the image and the border needed (if the image is outside of the
     * component). The coordinates are measured from the top left corner, thus since the borders
     * might grow the coordinates have to be adjusted after recalculating all the image positions
     * using {@link #recalculate()}.
     */
    protected void recalculate() {

      switch (position) {
        case NORTH_EAST: // top right
          x = componentWidth - imageWidth + offsetX;
          y = offsetY;
          break;
        case NORTH_WEST: // top left
          x = offsetX;
          y = offsetY;
          break;
        case SOUTH_EAST: // bottom right
          x = componentWidth - imageWidth + offsetX;
          y = componentHeight - imageHeight + offsetY;
          break;
        case SOUTH_WEST: // bottom left
          x = offsetX;
          y = componentHeight - imageHeight + offsetY;
          break;
        default:
          break;
      }

      if (x < 0) {
        int newLeft = Math.abs(x);
        if (newLeft > left) {
          // Extend the left border by x
          left = newLeft;
        }
      }

      if (x + imageWidth > componentWidth) {
        int newRight = x + imageWidth - componentWidth - right;
        if (newRight > right) {
          right = newRight;
        }
      }

      if (y < 0) {
        int newTop = Math.abs(y);
        if (newTop > top) {
          top = newTop;
        }
      }

      if (y + imageHeight > componentHeight) {
        int newBottom = y + imageHeight - componentHeight;
        if (newBottom > bottom) {
          bottom = newBottom;
        }
      }

    }

    /**
     * Adjusts the coordinates. Has to be called for all the {@link AddedImage}s after the
     * recalculation with {@link #recalculate()}.
     */
    private void adjust() {
      x += left;
      y += top;
      toolTip.setArea(x, y, imageWidth, imageHeight);
      cursorArea.setArea(x, y, imageWidth, imageHeight);
    }


  }

}
