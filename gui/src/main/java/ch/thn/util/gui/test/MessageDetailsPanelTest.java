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
package ch.thn.util.gui.test;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import ch.thn.util.gui.dialog.MessageDetailsPanel;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class MessageDetailsPanelTest extends JPanel {
  private static final long serialVersionUID = -8303244081925623714L;


  private MessageDetailsPanel panel = null;


  /**
   * 
   */
  public MessageDetailsPanelTest() {

    setLayout(new BorderLayout());

    panel = new MessageDetailsPanel("abc", "def", false);

    add(panel);

  }



}
