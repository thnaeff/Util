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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ch.thn.util.gui.input.PathTextField;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class PathTextFieldTest extends JPanel implements ActionListener {
  private static final long serialVersionUID = 8646762548881946278L;


  private JFileChooser fileChooser = null;

  private PathTextField ptf = null;


  public PathTextFieldTest(JLayeredPane lp) {

    setLayout(new BorderLayout());


    ptf = new PathTextField("/home", 20);
    ptf.addButtonActionListener(this);
    ptf.activatePathWatcher(true);
    fileChooser = new JFileChooser(ptf.getText());
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    add(ptf, BorderLayout.NORTH);

    // JComponentDebugger debugger = new JComponentDebugger(ptf);
    // debugger.setLayeredPane(lp);

    ptf.getBorder();
  }



  @Override
  public void actionPerformed(ActionEvent e) {
    fileChooser.setCurrentDirectory(new File(ptf.getText()));
    // fileChooser.setSelectedFile(new File(ptf.getText()));

    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      ptf.setText(fileChooser.getSelectedFile().getAbsolutePath());
    }
  }

}
