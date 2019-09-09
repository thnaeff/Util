package ch.thn.util.gui.component;

import ch.thn.util.gui.Loader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

/**
 * A text area in a scroll pane to display a certain number of messages (with or without timestamp).
 * Once the message limit is reached the oldest message gets removed if a new one is added.
 *
 *
 * @author Thomas Naeff (github.com/thnaeff) Naeff
 *
 */
public class MessageBox extends JPanel implements ActionListener {
  private static final long serialVersionUID = 2141338881134198286L;

  private JTextArea textArea = null;
  private JScrollPane scrollPane = null;

  private PlainButton bClear = null;

  private SimpleDateFormat timeAndDateFormat = new SimpleDateFormat("dd.MM.-kk:mm:ss");

  private int numberOfLines = 0;
  private int maxNumberOfLines = 0;
  private int rows = 0;

  private boolean showTimestamp = true;


  /**
   * A text area where new message can be added at the end, including a timestamp
   * 
   * @param title
   * @param text
   * @param rows
   * @param columns
   * @since
   */
  public MessageBox(String title, String text, int rows, int columns, int maxNumberOfLines) {
    this.rows = rows;
    this.maxNumberOfLines = maxNumberOfLines;

    if (title == null || title.length() == 0) {
      // If there is not title, the clear-button can't be shown because it
      // does not have the space -> set an empty title
      title = " ";
    }

    setLayout(new BorderLayout());

    bClear = new PlainButton(Loader.loadIcon("/16x16/clear.png"));
    bClear.setToolTipText("Clear messages");
    bClear.addActionListener(this);

    textArea = new JTextArea(text, rows, columns);
    textArea.setEditable(false);

    scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setColumnHeaderView(new JLabel(title));
    scrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, bClear);


    add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * 
   * @param show
   */
  public void showTimestamp(boolean show) {
    this.showTimestamp = show;
  }

  /**
   * 
   * @param max
   */
  public void setMaxNumberOfLines(int max) {
    this.maxNumberOfLines = max;
  }

  /**
   * Appends a text to the text area and scrolls to the position of the new text
   * 
   * @param text
   */
  public void appendText(String text) {
    appendText(text, false);
  }

  /**
   * Just adds an empty line without timestamp or text
   */
  public void appendEmptyLine() {
    appendText(null, true);
  }

  /**
   * 
   * @param text
   * @param emptyLine If set to true, an empty line is added (without timestamp and text)
   */
  private synchronized void appendText(String text, boolean emptyLine) {
    if ((text == null) || (text.length() <= 0)) {
      return;
    }

    // Remove lines if there are too many
    while (maxNumberOfLines != 0 && numberOfLines > 0 && numberOfLines >= maxNumberOfLines) {
      // Too many lines. Remove the top one
      Element first = textArea.getDocument().getDefaultRootElement().getElement(0);
      if (first != null) {
        try {
          textArea.getDocument().remove(first.getStartOffset(), first.getEndOffset());
        } catch (BadLocationException e) {
          e.printStackTrace();
        }

        numberOfLines--;
      }

    }

    if (numberOfLines > 0) {
      textArea.append("\n");
    }

    numberOfLines++;

    if (!emptyLine) {
      if (showTimestamp) {
        Date dt = new Date();
        textArea.append(timeAndDateFormat.format(dt)
            + " | ");
      }

      textArea.append(text);
    }

    if (numberOfLines > rows) {
      Element last = textArea.getDocument().getDefaultRootElement().getElement(numberOfLines - 1);
      if (last != null) {
        // Make the last line visible
        textArea.setCaretPosition(last.getStartOffset());
      }
    }

  }


  /**
   * Set the title of the text area
   * 
   * @param title
   */
  public void setTitle(String title) {
    scrollPane.setColumnHeaderView(new JLabel(title));
  }

  /**
   * Removes all the text and shows a "CLEARED" message if showMessage=true
   * 
   * @param showMessage
   */
  public void clear(boolean showMessage) {
    Date dt = new Date();

    if (showMessage) {
      textArea.setText(timeAndDateFormat.format(dt)
          + " | [CLEARED]");
      numberOfLines = 1;
    } else {
      textArea.setText("");
      numberOfLines = 0;
    }

  }

  /**
   * Removes all the messages
   */
  public void clear() {
    clear(false);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    clear(true);
  }


}
