package ch.thn.util.gui.component;

import ch.thn.util.gui.Loader;
import ch.thn.util.gui.component.extension.BorderContent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Very simple {@link JPanel} with button above the panel to expand/collapse the content component
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class CollapsiblePane extends JPanel implements ActionListener {
  private static final long serialVersionUID = -5693453971357300324L;


  private BorderContent borderContent = null;

  private PlainButton bToggle = null;

  private JPanel pComponent = null;

  /**
   * 
   * 
   * @param title The text of the expand/collapse button
   * @param component The component to expand/collapse
   * @param collapsed
   */
  public CollapsiblePane(String title, JComponent component, boolean collapsed) {

    setLayout(new BorderLayout());

    bToggle = new PlainButton(title);
    bToggle.addActionListener(this);
    bToggle.setHorizontalAlignment(JButton.LEFT);
    bToggle.setHorizontalTextPosition(JButton.LEFT);
    bToggle.setContentAreaFilled(true);
    bToggle.setBackground(Color.lightGray);
    bToggle.setBorderPainted(true);
    bToggle.setBorder(
        BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));

    pComponent = new JPanel(new BorderLayout());
    // pComponent.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    pComponent.add(component);


    borderContent = new BorderContent(this, 0, 0, 0, 0);
    borderContent.addComponent(bToggle, BorderContent.NORTH);

    add(pComponent);
    collapsed(collapsed);
  }

  private void collapsed(boolean collapsed) {
    pComponent.setVisible(!collapsed);
    bToggle.setIcon(collapsed ? Loader.loadIcon("/10x10/collapsed.png")
        : Loader.loadIcon("/10x10/expanded.png"));
  }

  /**
   * 
   * 
   */
  public void collapse() {
    collapsed(true);
  }

  /**
   * 
   * 
   */
  public void expand() {
    collapsed(false);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    collapsed(pComponent.isVisible());

  }

}
