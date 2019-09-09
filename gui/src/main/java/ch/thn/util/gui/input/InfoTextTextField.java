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

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * A {@link JTextField} which has the option to show an info text in the text field in light gray.
 * The info text is shown when the text field does not have focus and when there is no text entered.
 * The info text is hidden when the text field has focus or when text is entered.
 * 
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class InfoTextTextField extends JTextField implements FocusListener {
  private static final long serialVersionUID = -5660483010013238986L;


  private String sInfo = null;

  private Color cOriginal = null;

  private boolean infoShown = false;
  private boolean hasFocus = false;

  /**
   * @see JTextField#JTextField()
   */
  public InfoTextTextField() {
    super();

    init();
  }

  /**
   * 
   * @param columns
   * @see JTextField#JTextField(int)
   */
  public InfoTextTextField(int columns) {
    super(columns);

    init();
  }

  /**
   * 
   * @param text
   * @see JTextField#JTextField(String)
   */
  public InfoTextTextField(String text) {
    super(text);

    init();
  }

  /**
   * 
   * @param text
   * @param columns
   * @see JTextField#JTextField(String, int)
   */
  public InfoTextTextField(String text, int columns) {
    super(text, columns);

    init();
  }

  /**
   * 
   * @param doc
   * @param text
   * @param columns
   * @see JTextField#JTextField(Document, String, int)
   */
  public InfoTextTextField(Document doc, String text, int columns) {
    super(doc, text, columns);

    init();
  }

  /**
   * Initialize the info text behavior
   */
  private void init() {
    addFocusListener(this);
  }

  /**
   * Shows the info text when the text field does not have focus and there is no text entered. Hides
   * the info text when the text field has focus or when there is text entered.
   * 
   */
  private void updateInfoText() {

    if (!hasFocus && getText().length() == 0) {
      if (sInfo == null) {
        return;
      }

      // The flag needs to be set before the text is set so that any listeners
      // can use #isInfoShown()
      infoShown = true;

      cOriginal = getForeground();
      setForeground(Color.lightGray);
      super.setText(" "
          + sInfo);
      setCaretPosition(0);
    } else if (hasFocus && infoShown) {
      reset("");
    }
  }

  /**
   * Resets the text field (clears the info text and resets the foreground color)
   * 
   * @param
   */
  private void reset(String text) {
    // The flag needs to be set before the text is set so that any listeners
    // can use #isInfoShown()
    infoShown = false;

    setForeground(cOriginal);
    super.setText(text);
  }

  /**
   * Sets the info text which is shown when the text field does not have focus
   * 
   * @param text
   */
  public void setInfoText(String text) {
    sInfo = text;

    // If the info text is currently showing, reset the text field so that
    // it gets updated with the new info text
    if (infoShown) {
      reset("");
    }

    updateInfoText();
  }

  /**
   * Returns the current info text
   * 
   * @return
   */
  public String getInfoText() {
    return sInfo;
  }

  /**
   * Returns true if the info text is currently showing
   * 
   * @return
   */
  public boolean isInfoShown() {
    return infoShown;
  }

  @Override
  public String getText() {
    if (infoShown) {
      // Nothing to return if info text is shown
      return "";
    } else {
      return super.getText();
    }
  }

  @Override
  public String getText(int offs, int len) throws BadLocationException {
    if (infoShown) {
      // Nothing to return if info text is shown
      return "";
    } else {
      return super.getText(offs, len);
    }
  }

  @Override
  public void setText(String t) {
    reset(t);
  }


  @Override
  public void focusGained(FocusEvent e) {
    hasFocus = true;
    updateInfoText();
  }

  @Override
  public void focusLost(FocusEvent e) {
    hasFocus = false;
    updateInfoText();
  }


}
