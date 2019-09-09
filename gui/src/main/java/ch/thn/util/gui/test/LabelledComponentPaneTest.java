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

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.thn.util.gui.component.LabelledComponentPanel;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class LabelledComponentPaneTest extends JPanel {
  private static final long serialVersionUID = -7692399082056416715L;

  LabelledComponentPanel lPanel = null;


  /**
   * 
   */
  public LabelledComponentPaneTest() {

    setLayout(new BorderLayout());

    lPanel = new LabelledComponentPanel(2, 5, 10, 20, true, false, true, true);


    lPanel.addLabelComponent(new JLabel("Label1:"), new JLabel("Component1"), true);
    lPanel.addLabelComponent(new JLabel("aaaaaaa Label2:"), new JLabel("bbbbbbbbbb Component2"));

    lPanel.addLabelComponent(1, new JLabel("Long1:"),
        new JLabel(
            "abcdefg ddddddddddddddddddddddddddddddddddddddddddddd asd fasd fasdf asdf asdfasd"),
        true);
    lPanel.addLabelComponent(1, new JLabel("Long2:"),
        new JLabel("abcdefg dddddddddddddddddddddddddddd asd fasd fasdf asdf asdfasd"));


    add(lPanel, BorderLayout.NORTH);
  }



}
