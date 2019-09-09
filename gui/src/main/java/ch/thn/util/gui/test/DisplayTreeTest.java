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

import ch.thn.util.gui.tree.UITreeModel;
import ch.thn.util.thread.ControlledRunnable;
import ch.thn.util.tree.KeyListTreeNode;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class DisplayTreeTest extends JPanel {
  private static final long serialVersionUID = -7694599082056416715L;

  private JTree guiTree = null;

  private JScrollPane spContent = null;

  private KeyListTreeNode<String, String> tree = null;

  private KeyListTreeNode<String, String> node21 = null;
  private KeyListTreeNode<String, String> node221 = null;

  /**
   *
   */
  public DisplayTreeTest() {

    setLayout(new BorderLayout());


    tree = new KeyListTreeNode<>("0", "A test for DisplayTree");

    tree.addChildNode("1", "Node 1");

    node21 = tree.addChildNode("2", "Node 2").addChildNode("2.1", "Node 2.1");

    node221 =
        node21.getParentNode().addChildNode("2.2", "Node 2.2").addChildNode("2.2.1", "Node 2.2.1");

    tree.addChildNode("3", "Node 3");


    ControlledRunnable c = new ControlledRunnable(false, false) {

      @Override
      public void run() {
        Object o = new Object();

        try {
          synchronized (o) {
            o.wait(1000);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        int count = 0;
        boolean stop = false;

        while (true) {

          switch (count) {
            case 0:
              System.out.println("- add: Node 4");
              tree.addChildNode("4", "Node 4");
              break;
            case 1:
              System.out.println("- remove: "
                  + tree.getChildNodes("3"));
              tree.removeChildNodes("3");
              break;
            case 2:
              System.out.println("- add: Node 5");
              tree.addChildNode("5", "Node 5");
              break;
            case 3:
              System.out.println("- remove "
                  + node21.toString());
              node21.removeNode();
              break;
            case 4:
              System.out.println("- add: Node 2211 ");
              node221.addChildNode("2.2.1.1", "Node 2.2.1.1");
              break;
            case 5:
              System.out.println("- replace: Node 1");
              tree.getChildNode(0).replaceNode("1r", "Node 1 replaced");
              break;
            case 6:
              System.out.println("- replace: Node 2");
              tree.getChildNode("2", 0).replaceNode("2r", "Node 2 replaced");
              break;
            case 7:
              System.out.println("- change value: *");
              for (KeyListTreeNode<String, String> node : tree) {
                node.setNodeValue(node.getNodeValue()
                    + "*");
              }
              break;
            default:
              stop = true;
              break;
          }

          count++;

          if (stop) {
            break;
          }

          try {
            synchronized (o) {
              o.wait(500);
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

      }
    };

    Thread t = new Thread(c);
    t.start();


    guiTree = new JTree(new UITreeModel<KeyListTreeNode<String, String>>(tree));

    spContent = new JScrollPane(guiTree);


    add(spContent, BorderLayout.CENTER);
  }



}
