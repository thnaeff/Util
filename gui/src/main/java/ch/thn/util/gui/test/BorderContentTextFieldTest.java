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
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.thn.util.gui.component.extension.BorderContent;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class BorderContentTextFieldTest extends JTextField {
  private static final long serialVersionUID = -6759906697066833000L;

  private BorderContent bc = null;


  /**
   * 
   */
  public BorderContentTextFieldTest() {
    super(10);

    bc = new BorderContent(this, 10, 10, 10, 10);


    // setBorder(BorderFactory.createLineBorder(Color.blue));
    // setBorder(BorderFactory.createLineBorder(Color.red, 10));
    // setBorder(BorderFactory.createEtchedBorder(Color.black, Color.magenta));
    setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red, 5),
        BorderFactory.createLineBorder(Color.cyan, 5)));

    JPanel p0 = new JPanel(new BorderLayout());
    p0.setBorder(BorderFactory.createLineBorder(Color.orange));
    p0.add(getButton("N"), BorderLayout.WEST);
    p0.add(new JLabel("This is a test aölksjdflkajsölfdjlsadjflsdf"));
    p0.setPreferredSize(new Dimension(100, 30));
    p0.setMinimumSize(new Dimension(100, 30));
    p0.setMaximumSize(new Dimension(100, 30));

    bc.addComponent(p0, BorderContent.NORTH);
    bc.addComponent(getButton("E"), BorderContent.EAST);
    bc.addComponent(getButton("S"), BorderContent.SOUTH);
    bc.addComponent(getButton("W"), BorderContent.WEST);


  }


  private JButton getButton(String s) {
    JButton b = new JButton(s);
    b.setPreferredSize(new Dimension(100, 300));
    // b.setMinimumSize(new Dimension(50, 100));
    // b.setMaximumSize(new Dimension(200, 400));
    return b;
  }


}
