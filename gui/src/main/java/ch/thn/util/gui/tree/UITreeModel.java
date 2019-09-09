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
package ch.thn.util.gui.tree;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A tree model which holds the data for a {@link JTree}. The {@link JTree} is updated automatically
 * when the data changes.<br />
 * The backing data is a list tree (e.g. any tree node implementing {@link ListTreeNodeInterface}),
 * for example {@link KeyListTreeNode} or {@link ListTreeNode}.
 *
 *
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class UITreeModel<N extends ListTreeNodeInterface<?, N>>
    implements TreeModel, TreeNodeListener<N> {

  private N treeNode = null;

  private List<TreeModelListener> listeners = null;


  /**
   *
   *
   * @param treeNode
   */
  public UITreeModel(N treeNode) {
    this.treeNode = treeNode;

    // Add the listener to all nodes
    for (N node : treeNode) {
      node.addTreeNodeListener(this);
    }

    listeners = new ArrayList<>();

  }

  @Override
  public Object getChild(Object parent, int index) {
    return ((ListTreeNodeInterface<?, ?>) parent).getChildNode(index);
  }

  @Override
  public int getChildCount(Object parent) {
    return ((ListTreeNodeInterface<?, ?>) parent).getChildNodesCount();
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    return ((ListTreeNodeInterface<?, ?>) child).getNodeIndex();
  }

  @Override
  public Object getRoot() {
    return treeNode;
  }

  @Override
  public boolean isLeaf(Object node) {
    return ((ListTreeNodeInterface<?, ?>) node).isLeafNode();
  }


  @Override
  public void addTreeModelListener(TreeModelListener l) {
    listeners.add(l);
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {
    listeners.remove(l);
  }

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {



  }

  /**
   *
   *
   * @param node
   * @param toParent
   * @return
   */
  private TreePath getTreePath(N node, boolean toParent) {
    List<N> path = new ArrayList<>();

    if (!toParent) {
      path.add(node);
    }

    while (node.getParentNode() != null) {
      node = node.getParentNode();
      path.add(0, node);
    }

    return new TreePath(path.toArray());
  }

  /**
   *
   *
   * @param source
   * @param parent
   * @param sourceNodeIndex
   */
  private void fireNodeChanged(N source, N parent, int sourceNodeIndex) {
    int[] childIndices = new int[] {};
    Object[] children = new Object[] {};

    // From TreeModelListener.treeNodesChanged javadoc:
    // "To indicate the root has changed, childIndices and children will be null."
    if (parent == null) {
      childIndices = null;
      children = null;
    }

    final TreeModelEvent e =
        new TreeModelEvent(source, getTreePath(source, false), childIndices, children);

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        for (TreeModelListener l : listeners) {
          l.treeNodesChanged(e);
        }
      }
    });

  }

  /**
   *
   *
   * @param source
   * @param parent
   * @param sourceNodeIndex
   */
  private void fireNodeInserted(N source, N parent, int sourceNodeIndex) {
    int[] childIndices = new int[] {sourceNodeIndex};
    Object[] children = new Object[] {source};

    final TreeModelEvent e =
        new TreeModelEvent(source, getTreePath(source, true), childIndices, children);

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        for (TreeModelListener l : listeners) {
          l.treeNodesInserted(e);
        }
      }
    });

  }

  /**
   *
   *
   * @param source
   * @param parent
   * @param sourceNodeIndex
   */
  private void fireNodeRemoved(N source, N parent, int sourceNodeIndex) {
    int[] childIndices = new int[] {sourceNodeIndex};
    Object[] children = new Object[] {source};

    final TreeModelEvent e =
        new TreeModelEvent(source, getTreePath(parent, false), childIndices, children);

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        for (TreeModelListener l : listeners) {
          l.treeNodesRemoved(e);
        }
      }
    });

  }

  /**
   *
   *
   * @param source
   * @param parent
   * @param sourceNodeIndex
   */
  private void fireStructureChanged(N source, N parent, int sourceNodeIndex) {
    final TreeModelEvent e = new TreeModelEvent(source, getTreePath(source, false));

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        for (TreeModelListener l : listeners) {
          l.treeStructureChanged(e);
        }
      }
    });

  }


  @Override
  public void nodeRemoved(TreeNodeEvent<N> e) {
    System.out.println(e.getSource()
        + " -> removed: "
        + e.getNode());
    N node = e.getNode();

    node.removeTreeNodeListener(this);
    fireNodeRemoved(node, e.getParentNodeOfRemoved(), e.getNodeIndexOfRemoved());
  }

  @Override
  public void nodeAdded(TreeNodeEvent<N> e) {
    System.out.println(e.getSource()
        + " -> added: "
        + e.getNode());
    N node = e.getNode();

    node.addTreeNodeListener(this);
    fireNodeInserted(node, node.getParentNode(), node.getNodeIndex());
  }

  @Override
  public void nodeReplaced(TreeNodeEvent<N> e) {
    System.out.println(e.getSource()
        + " -> replaced: "
        + e.getOldNode().toString()
        + "=>"
        + e.getNode());
    e.getOldNode().removeTreeNodeListener(this);
    fireNodeRemoved(e.getOldNode(), e.getParentNodeOfRemoved(), e.getNodeIndexOfRemoved());

    N node = e.getNode();
    node.addTreeNodeListener(this);
    fireNodeInserted(node, node.getParentNode(), node.getNodeIndex());
  }

  @Override
  public void nodeValueChanged(TreeNodeEvent<N> e) {
    System.out.println(e.getSource()
        + " -> value changed "
        + e.getNode());
    N node = e.getNode();
    fireNodeChanged(e.getNode(), node.getParentNode(), node.getNodeIndex());
  }

}
