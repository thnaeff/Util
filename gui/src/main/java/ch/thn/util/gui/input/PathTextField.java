/**
 * Copyright 2014 Thomas Naeff (github.com/thnaeff)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package ch.thn.util.gui.input;


import ch.thn.file.filesystemwatcher.FileSystemWatcher;
import ch.thn.file.filesystemwatcher.PathWatcherListener;
import ch.thn.util.file.FileUtil;
import ch.thn.util.gui.GuiUtilError;
import ch.thn.util.gui.Loader;
import ch.thn.util.gui.component.ClippingLabel;
import ch.thn.util.gui.component.PlainButton;
import ch.thn.util.gui.component.extension.BorderContent;
import ch.thn.util.gui.component.extension.BorderImage;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A text field for entering and selecting a file path, with browsing button, validation, the
 * possibility to set (and show) a base path and optional file system watcher which watches the path
 * for changes.<br>
 * <br>
 * The validation is triggered when the path in the text field changes, when the text field gets the
 * focus and when the text field looses the focus. In addition, the path watcher (if activated) can
 * trigger the validation.<br>
 * The browsing button is shown on the right of the text field as folder icon. To use the browsing
 * button, add an {@link ActionListener} to this text field using
 * {@link #addButtonActionListener(ActionListener)}.<br>
 * The validation can be turned on and off with {@link #showStatus(boolean)}. When on, a small icon
 * is added to the top left corner to show the validation status.<br>
 * The base path can be made visible with {@link #showBasePath(boolean)} and its length in the path
 * text field can be set with {@link #setBasePathMaxWidth(int, boolean)}. When the base path is set,
 * the path entered in the text field is always relative to that base path.<br>
 * The file system watcher can be turned on with {@link #activatePathWatcher(boolean)}. When turned
 * on, the path is automatically validated when the path text field does not have the focus and any
 * directory or its content within the current path gets changed.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class PathTextField extends InfoTextTextField implements DocumentListener, ComponentListener,
    AncestorListener, PathWatcherListener, FocusListener {
  private static final long serialVersionUID = 5554169796153183463L;

  private BorderContent borderContent = null;
  private BorderImage borderImage = null;

  private PlainButton bIcon = null;

  private ClippingLabel lBasePath = null;

  private FileNameExtensionFilter fileFilter = null;

  private FileSystemWatcher pathWatcher = null;
  private Thread pathWatcherThread = null;

  private ImageIcon currentIcon = null;
  private final ImageIcon iInit = Loader.loadIcon("/10x10/spacer.png");
  private final ImageIcon iFile = Loader.loadIcon("/10x10/file.png");
  private final ImageIcon iFolder = Loader.loadIcon("/10x10/folder.png");
  private final ImageIcon iNew = Loader.loadIcon("/10x10/add_new.png");
  private final ImageIcon iInvalid = Loader.loadIcon("/10x10/invalid.png");

  private boolean allowDirectory = true;
  private boolean allowFile = true;
  private boolean allowNew = true;
  private boolean allowNewDirectory = true;
  private boolean showInvalidOnly = false;
  private boolean initOk = false;

  private String basePath = "";

  private int basePathPercentage = 0;

  /**
   *
   *
   * @param text
   * @param columns
   */
  public PathTextField(String text, int columns) {
    this(text, columns, true, true, true, true);

  }

  /**
   *
   *
   * @param columns
   */
  public PathTextField(int columns) {
    this("", columns, true, true, true, true);

  }

  /**
   *
   *
   * @param columns
   * @param allowDirectory
   * @param allowFile
   * @param allowNew
   * @param allowNewDirectory
   */
  public PathTextField(int columns, boolean allowDirectory, boolean allowFile, boolean allowNew,
      boolean allowNewDirectory) {
    this("", columns, allowDirectory, allowFile, allowNew, allowNewDirectory);
  }

  /**
   *
   *
   * @param text
   * @param columns
   * @param allowDirectory
   * @param allowFile
   * @param allowNew
   * @param allowNewDirectory
   */
  public PathTextField(String text, int columns, boolean allowDirectory, boolean allowFile,
      boolean allowNew, boolean allowNewDirectory) {
    super(text, columns);

    this.allowDirectory = allowDirectory;
    this.allowFile = allowFile;
    this.allowNew = allowNew;
    this.allowNewDirectory = allowNewDirectory;

    lBasePath = new ClippingLabel();

    setInfoText("Enter path (or browse and select)");
    getDocument().addDocumentListener(this);
    addComponentListener(this);
    addAncestorListener(this);
    addFocusListener(this);

    bIcon = new PlainButton(Loader.loadIcon("/16x16/folder.png"),
        Loader.loadIcon("/16x16/folder_explore.png"));
    bIcon.setToolTipText("Browse");

    borderContent = new BorderContent(this, 0, 5, 0, 0);
    borderContent.addComponent(bIcon, BorderContent.EAST);

    borderImage = new BorderImage(this);
    borderImage.addImage(iInit.getImage(), BorderImage.NORTH_WEST, -4, -3);
    borderImage.addImage(iFile.getImage(), BorderImage.NORTH_WEST, -4, -3);
    borderImage.addImage(iFolder.getImage(), BorderImage.NORTH_WEST, -4, -3);
    borderImage.addImage(iNew.getImage(), BorderImage.NORTH_WEST, -4, -3);
    borderImage.addImage(iInvalid.getImage(), BorderImage.NORTH_WEST, -4, -3);

    borderImage.setMouseCursor(iInit.getImage(), Cursor.DEFAULT_CURSOR);
    borderImage.setMouseCursor(iFile.getImage(), Cursor.DEFAULT_CURSOR);
    borderImage.setMouseCursor(iFolder.getImage(), Cursor.DEFAULT_CURSOR);
    borderImage.setMouseCursor(iNew.getImage(), Cursor.DEFAULT_CURSOR);
    borderImage.setMouseCursor(iInvalid.getImage(), Cursor.DEFAULT_CURSOR);

    initOk = true;

    updatePathWatcher();
    checkPath();

  }

  /**
   * Sets the font and color of the label showing the base path
   *
   * @param font The font to use. The font will not be changed if set to <code>null</code>
   * @param color The color to use. The color will not be changed if set to <code>null</code>
   */
  public void setBasePathLabelAttributes(Font font, Color color) {
    if (color != null) {
      lBasePath.setForeground(color);
    }

    if (font != null) {
      lBasePath.setFont(font);
    }
  }

  /**
   * Activates the path watcher thread to watch for file system changes on the current path.
   *
   * @param activate
   */
  public void activatePathWatcher(boolean activate) {
    if (activate) {
      if (pathWatcher == null) {
        pathWatcher = new FileSystemWatcher();
        pathWatcher.addPathWatcherListener(this);

        pathWatcherThread = new Thread(pathWatcher);
        pathWatcherThread.start();
      }
    } else {
      pathWatcher.stop();
      pathWatcher = null;
      pathWatcherThread = null;
    }
  }

  /**
   *
   * @param fileFilter
   */
  public void setFileFilter(FileNameExtensionFilter fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   *
   * @param invalidOnly
   */
  public void showInvalidOnly(boolean invalidOnly) {
    showInvalidOnly = invalidOnly;
  }

  /**
   * Sets the base path which is always added in front of the path visible in the text field
   *
   * @param basePath
   */
  public void setBasePath(String basePath) {
    this.basePath = FileUtil.addPathSeparator(basePath);

    lBasePath.setText(this.basePath);
    borderContent.updateContent();
  }

  /**
   * Enables/disables showing the base path
   *
   * @param show
   */
  public void showBasePath(boolean show) {
    if (show) {
      borderContent.addComponent(lBasePath, BorderContent.WEST);
    } else {
      borderContent.removeComponent(lBasePath);
    }

  }

  /**
   * Sets the max width of the base path text label, either in pixel or as percentage of the whole
   * path text field.
   *
   * @param width
   * @param percentageOfField True if the width value is a percentage value
   */
  public void setBasePathMaxWidth(int width, boolean percentageOfField) {
    if (width <= 0) {
      throw new GuiUtilError("Base path max width should be > 0");
    }

    if (percentageOfField) {
      basePathPercentage = width;
    } else {
      basePathPercentage = 0;
      lBasePath.setMaximumSize(new Dimension(width, (int) lBasePath.getMaximumSize().getHeight()));
    }

  }

  /**
   * Returns true if the entered path is invalid, and false if the path is not valid.
   *
   * @return
   */
  public boolean isPathValid() {
    return !currentIcon.equals(iInvalid);
  }

  /**
   *
   * @param allow
   */
  public void allowDirectory(boolean allow) {
    this.allowDirectory = allow;
  }

  /**
   *
   * @return
   */
  public boolean allowDirectory() {
    return this.allowDirectory;
  }

  /**
   *
   * @param allow
   */
  public void allowFile(boolean allow) {
    this.allowFile = allow;
  }

  /**
   *
   * @return
   */
  public boolean allowFile() {
    return this.allowFile;
  }

  /**
   *
   * @param allow
   */
  public void allowNew(boolean allow) {
    this.allowNew = allow;
  }

  /**
   *
   * @return
   */
  public boolean allowNew() {
    return this.allowNew;
  }

  /**
   * Adds an action listener to the button
   *
   * @param l
   */
  public void addButtonActionListener(ActionListener l) {
    bIcon.addActionListener(l);
  }

  /**
   * Removes the action listener from the button
   *
   * @param l
   */
  public void removeButtonActionListener(ActionListener l) {
    bIcon.removeActionListener(l);
  }

  /**
   * Enable/disable the button
   *
   * @param b
   */
  public void setButtonEnabled(boolean b) {
    bIcon.setEnabled(b);
  }

  /**
   * Show/hide the button
   *
   * @param b
   */
  public void setButtonVisible(boolean b) {
    bIcon.setVisible(b);
  }

  /**
   * Returns the entered path including the base path. If the entered path is a relative path (not
   * starting with a path separator), then the working directory is added in front of the path.
   *
   * @return
   */
  public String getWholePath() {
    // Set the base path to the working directory if the entered path is
    // relative
    if (!isInfoShown()) {
      if ((basePath == null || basePath.length() == 0) && getText().length() > 0
          && !FileUtil.hasFileSystemRoot(getText())) {
        return FileUtil.getWorkingDirectory() + getText();
      } else {
        return basePath + getText();
      }
    } else {
      return basePath;
    }

  }

  /**
   * Since the action listener is attached to the internal browse button, the source of the
   * actionPerformed event will be that button. This method can be used to test if the
   * actionPerformed source is from this text field.
   *
   * @return
   */
  public boolean isActionListenerSource(Object source) {
    return source.equals(bIcon);
  }

  /**
   * Checks the path and shows the info icon according to the status of the path
   */
  private void checkPath() {
    String path = getWholePath();

    if (currentIcon != null) {
      // Hide the old image
      borderImage.show(currentIcon.getImage(), false);
    } else {
      borderImage.showAll(false);
    }

    currentIcon = iInit;

    if (path == null || path.length() == 0) {
      return;
    }

    File f = new File(path);
    String toolTip = "";

    if (f.isDirectory()) {
      // -- EXISTING DIRECTORY --
      if (allowDirectory) {
        if (!showInvalidOnly) {
          toolTip = "Existing directory";
          currentIcon = iFolder;
        }
      } else {
        toolTip = "Invalid input (existing directory not allowed)";
        currentIcon = iInvalid;
      }
    } else if (f.isFile()) {
      // -- EXISTING FILE --
      if (allowFile) {
        // Also check file extension
        if (fileFilter == null || fileFilter.accept(f)) {
          if (!showInvalidOnly) {
            toolTip = "Existing file";
            currentIcon = iFile;
          }
        } else {
          toolTip = "Invalid file extension. Only "
              + fileFilter.getDescription()
              + " allowed";
          currentIcon = iInvalid;
        }
      } else {
        toolTip = "Invalid input (existing file not allowed)";
        currentIcon = iInvalid;
      }
    } else {
      if (path.endsWith(FileUtil.getPathSeparator())) {
        // -- NEW DIRECTORY --
        if (allowNewDirectory) {
          if (!showInvalidOnly) {
            toolTip = "New directory";
            currentIcon = iNew;
          }
        } else {
          toolTip = "Invalid input (new directory not allowed)";
          currentIcon = iInvalid;
        }
      } else {
        // -- NEW FILE/DIRECTORY --
        if (!allowNewDirectory) {
          // Check the current path and the parent path, because the last
          // path piece could be a file or a directory -> if it is a file
          // its parent has to be checked to be an existing directory.
          // => the current path has already been checked with f.isDirectory()
          File parent = f.getParentFile();
          if (parent != null) {
            if (!parent.isDirectory()) {
              // The parent directory is a new one -> invalid
              toolTip = "Invalid input (parent path to does not exist)";
              currentIcon = iInvalid;
            }
          }
        }

        // Only continue if the path is not already set to invalid
        if (currentIcon != iInvalid) {
          if (allowNew) {
            if (fileFilter != null) {
              // A file is expected
              if (fileFilter.accept(f)) {
                if (!showInvalidOnly) {
                  toolTip = "New file/directory";
                  currentIcon = iNew;
                }
              } else {
                toolTip = "Invalid file extension. Only "
                    + fileFilter.getDescription()
                    + " allowed";
                currentIcon = iInvalid;
              }
            } else {
              if (!showInvalidOnly) {
                // New file (or it could also be a directory)
                toolTip = "New file/directory";
                currentIcon = iNew;
              }
            }
          } else {
            // New file (or it could also be a directory)
            toolTip = "Invalid input (path does not exist)";
            currentIcon = iInvalid;
          }
        }

      }


    }

    // Show in the tool tip message if it is a relative path
    if ((basePath == null || basePath.length() == 0) && !FileUtil.hasFileSystemRoot(getText())) {
      toolTip += " relative to: "
          + FileUtil.getWorkingDirectory();
    }

    borderImage.setToolTip(currentIcon.getImage(), toolTip);
    borderImage.show(currentIcon.getImage(), true);

    // Repaint to make sure the icon gets updated
    repaint();
  }

  /**
   *
   */
  private void updateBasePathField() {
    if (basePathPercentage > 0) {
      int width = (int) getSize().getWidth() / 100 * basePathPercentage;

      lBasePath.setMaximumSize(new Dimension(width, (int) lBasePath.getMaximumSize().getHeight()));
    }
  }

  /**
   *
   */
  private void updatePathWatcher() {
    if (pathWatcher != null) {
      pathWatcher.pause(false);
      pathWatcher.clearAllRegisteredPaths();

      // Always watch the working directory, because the entered path could
      // be a relative path
      pathWatcher.registerPath(FileUtil.getWorkingDirectory(), false, true);

      pathWatcher.registerPath(getWholePath(), false, true);
    }
  }

  @Override
  public void setText(String t) {
    super.setText(t);

    if (initOk) {
      updatePathWatcher();
      checkPath();
    }
  }

  @Override
  public void paintChildren(Graphics g) {
    super.paintChildren(g);
    borderImage.paintImages(g);

    // if (pathWatcher != null) {
    // g.setColor(Color.orange);
    // g.drawRect(0, (int)getBounds().getHeight() - 3, 2, 2);
    // }

  }


  @Override
  public void changedUpdate(DocumentEvent e) {
    checkPath();
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    checkPath();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    checkPath();
  }

  @Override
  public void ancestorAdded(AncestorEvent event) {
    updateBasePathField();
  }

  @Override
  public void ancestorMoved(AncestorEvent event) {}

  @Override
  public void ancestorRemoved(AncestorEvent event) {}

  @Override
  public void componentHidden(ComponentEvent e) {}

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void componentResized(ComponentEvent e) {
    updateBasePathField();
  }

  @Override
  public void componentShown(ComponentEvent e) {}

  @Override
  public void focusGained(FocusEvent e) {
    super.focusGained(e);

    if (pathWatcher != null) {
      pathWatcher.pause(true);
    }

    checkPath();
  }

  @Override
  public void focusLost(FocusEvent e) {
    super.focusLost(e);
    updatePathWatcher();
    checkPath();
  }

  @Override
  public void newPathWatched(Path path) {}

  @Override
  public void pathChanged(Path path, Path context, boolean overflow) {
    checkPath();
  }

  @Override
  public void directoryCreated(Path path, Path created) {
    Path p = Paths.get(getWholePath());

    // If the created path is within the current path, then register it
    if (p.startsWith(created)) {
      pathWatcher.registerPath(created.toString(), false, false);
    }
  }

  @Override
  public void directoryDeleted(Path path, Path deleted) {}

  @Override
  public void directoryModified(Path path, Path modified) {}


}
