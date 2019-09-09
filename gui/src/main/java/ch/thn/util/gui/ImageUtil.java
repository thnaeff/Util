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
package ch.thn.util.gui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ImageUtil {


  /**
   * Turns any {@link Icon} into an {@link ImageIcon}. Creates a new image if the given {@link Icon}
   * is not an instance of {@link ImageIcon}. Returns the casted {@link Icon} if it is an instance
   * of {@link ImageIcon};
   * 
   * @param icon
   * @return
   */
  public static ImageIcon iconToImageIcon(Icon icon) {
    if (icon == null) {
      return null;
    }

    if (icon instanceof ImageIcon) {
      return (ImageIcon) icon;
    } else {
      int w = icon.getIconWidth();
      int h = icon.getIconHeight();

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice gd = ge.getDefaultScreenDevice();
      GraphicsConfiguration gc = gd.getDefaultConfiguration();
      BufferedImage image = gc.createCompatibleImage(w, h);
      Graphics2D g = image.createGraphics();

      icon.paintIcon(null, g, 0, 0);

      g.dispose();

      return new ImageIcon(image);
    }
  }



}
