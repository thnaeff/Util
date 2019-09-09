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
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.thn.util.gui.component.CollapsiblePane;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class CollapsiblePaneTest extends JPanel {
  private static final long serialVersionUID = -9078262441317059420L;

  private CollapsiblePane collapsiblePane0 = null;
  private CollapsiblePane collapsiblePane1 = null;

  /**
   * 
   */
  public CollapsiblePaneTest() {

    setLayout(new BorderLayout());

    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel p0 = new JPanel();
    p0.setLayout(new BorderLayout());
    p0.add(new JLabel("asdfsadfsdaaaaaa"), BorderLayout.WEST);
    p0.setBackground(Color.white);

    JPanel p1 = new JPanel();
    p1.setLayout(new BorderLayout());
    p1.add(new JLabel("asdfsadfsd"), BorderLayout.WEST);
    p1.setBackground(Color.yellow);
    p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    collapsiblePane0 = new CollapsiblePane("Collapsible pane", p0, false);
    collapsiblePane1 = new CollapsiblePane("Collapsible pane", p1, true);


    JPanel p2 = new JPanel();
    p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
    p2.add(collapsiblePane0);
    p2.add(collapsiblePane1);

    add(p2, BorderLayout.NORTH);

  }



}
