package ch.thn.util.gui.component;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A {@link JPanel} with one function: to display horizontally and vertically centered content. One
 * method is needed: {@link #setCenteredContent(JComponent)}. This method clears any components from
 * the panel and adds the given component only.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class CenteredPanel extends JPanel {
  private static final long serialVersionUID = 4745412439809719776L;

  /**
   * Removes all components from the panel and adds the given content in the center of the panel.
   * The content should have at least a maximum size set, otherwise it will expand.
   * 
   * @param content
   */
  public void setCenteredContent(JComponent content) {
    removeAll();

    setLayout(new BorderLayout());

    Box box = new Box(BoxLayout.Y_AXIS);

    box.add(Box.createVerticalGlue());
    box.add(content);
    box.add(Box.createVerticalGlue());

    add(box, BorderLayout.CENTER);
  }

}
