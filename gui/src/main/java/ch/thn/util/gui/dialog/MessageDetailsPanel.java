package ch.thn.util.gui.dialog;

import ch.thn.util.gui.component.CollapsiblePane;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A {@link JPanel} with two components: a text area on top to show the message, and a
 * {@link CollapsiblePane} below to show the message details
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class MessageDetailsPanel extends JPanel {
  private static final long serialVersionUID = -4566929387975388883L;


  private CollapsiblePane collapsiblePane = null;


  /**
   * 
   * 
   * @param message
   * @param details
   * @param detailsShown
   */
  public MessageDetailsPanel(String message, String details, boolean detailsShown) {

    setLayout(new BorderLayout());

    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

    JTextArea taMessage = new JTextArea(message);
    taMessage.setEditable(false);
    taMessage.setBackground(getBackground());

    JTextArea taDetails = new JTextArea(details);
    taDetails.setEditable(false);
    taDetails.setBackground(getBackground());

    JScrollPane spDetails = new JScrollPane(taDetails);
    spDetails.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    spDetails.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


    collapsiblePane = new CollapsiblePane("Details:", spDetails, !detailsShown);

    p.add(taMessage);
    p.add(Box.createVerticalStrut(5));
    p.add(collapsiblePane);

    add(p, BorderLayout.NORTH);
  }



}
