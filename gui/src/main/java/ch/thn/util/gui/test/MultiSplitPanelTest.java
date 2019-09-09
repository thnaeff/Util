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

import javax.swing.JLabel;

import ch.thn.util.gui.component.MultiSplitPanel;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class MultiSplitPanelTest extends MultiSplitPanel {
  private static final long serialVersionUID = -5368689969785345463L;

  /**
   * 
   */
  public MultiSplitPanelTest() {
    super(ORIENTATION_VERTICAL_UP);


    addSplitComponent(new JLabel("comp 1"));
    addSplitComponent(new JLabel("comp 2"));
    addSplitComponent(new JLabel("comp 3"));
    addSplitComponent(new JLabel("comp 4"));

  }



}