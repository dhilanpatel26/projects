package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Map implemented as an AVL Tree.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class AvlTreeMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;
  private int size;

  private Node<K, V> singleRightRotation(Node<K, V> node) {
    Node<K, V> median = node.left;
    node.left = median.right;
    median.right = node;
    updateHeight(node);
    updateHeight(median);
    return median;
  }

  private Node<K, V> singleLeftRotation(Node<K, V> node) {
    Node<K, V> median = node.right;
    node.right = median.left;
    median.left = node;
    updateHeight(node);
    updateHeight(median);
    return median;
  }

  private Node<K, V> doubleLeftRightRotation(Node<K, V> node) {
    node.left = singleLeftRotation(node.left);
    return singleRightRotation(node);
  }

  private Node<K, V> doubleRightLeftRotation(Node<K, V> node) {
    node.right = singleRightRotation(node.right);
    return singleLeftRotation(node);
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    root = insert(root, k, v);
    size++;
  }

  // Insert given key and value into subtree rooted at given node;
  // return changed subtree with a new node added.
  private Node<K, V> insert(Node<K, V> n, K k, V v) {
    if (n == null) {
      return new Node<>(k, v);
    }

    int cmp = k.compareTo(n.key);
    if (cmp < 0) {
      n.left = insert(n.left, k, v);
    } else if (cmp > 0) {
      n.right = insert(n.right, k, v);
    } else {
      throw new IllegalArgumentException("duplicate key " + k);
    }
    return balance(n);
  }

  // Updates the height of the node in constant time
  private void updateHeight(Node<K, V> n) {

    if (n.left == null && n.right == null) {
      n.height = 0;
      return;
    }

    if (n.left == null) {
      n.height = n.right.height + 1;
    } else if (n.right == null) {
      n.height = n.left.height + 1;
    } else {
      if (n.left.height > n.right.height) {
        n.height = n.left.height + 1;
      } else {
        n.height = n.right.height + 1;
      }
    }
  }

  // Calculates balance factor in constant time
  private int bf(Node<K, V> n) {
    int heightLeft = -1;
    int heightRight = -1;

    if (n.left != null) {
      heightLeft = n.left.height;
    }
    if (n.right != null) {
      heightRight = n.right.height;
    }
    return heightLeft - heightRight;
  }

  // Determines which rotation to apply
  private Node<K, V> balance(Node<K, V> n) {
    updateHeight(n);
    int bf = bf(n);
    Node<K, V> newNode = n;

    if (bf == 2) {
      if (bf(n.left) == -1) {
        newNode = doubleLeftRightRotation(n);
      } else {
        newNode = singleRightRotation(n);
      }
    } else if (bf == -2) {
      if (bf(n.right) == 1) {
        newNode = doubleRightLeftRotation(n);
      } else {
        newNode = singleLeftRotation(n);
      }
    }
    return newNode;
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    Node<K, V> node = findForSure(k);
    V value = node.value;
    root = remove(root, node);
    size--;
    return value;
  }

  // Remove node with given key from subtree rooted at given node;
  // Return changed subtree with given key missing.
  private Node<K, V> remove(Node<K, V> subtreeRoot, Node<K, V> toRemove) {
    int cmp = subtreeRoot.key.compareTo(toRemove.key);
    if (cmp == 0) {
      Node<K, V> n = remove(subtreeRoot);
      if (n != null) {
        return balance(n);
      } else {
        return null;
      }
    } else if (cmp > 0) {
      subtreeRoot.left = remove(subtreeRoot.left, toRemove);
    } else {
      subtreeRoot.right = remove(subtreeRoot.right, toRemove);
    }

    return balance(subtreeRoot);
  }

  // Remove given node and return the remaining tree (structural change).
  private Node<K, V> remove(Node<K, V> node) {
    // Easy if the node has 0 or 1 child.
    if (node.right == null) {
      return node.left;
    } else if (node.left == null) {
      return node.right;
    }

    // If it has two children, find the predecessor (max in left subtree),
    Node<K, V> toReplaceWith = max(node);
    // then copy its data to the given node (value change),
    node.key = toReplaceWith.key;
    node.value = toReplaceWith.value;
    // then remove the predecessor node (structural change).
    node.left = remove(node.left, toReplaceWith);

    return node;
  }

  // Return a node with maximum key in subtree rooted at given node.
  private Node<K, V> max(Node<K, V> node) {
    Node<K, V> curr = node.left;
    while (curr.right != null) {
      curr = curr.right;
    }
    return curr;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    Node<K, V> n = findForSure(k);
    n.value = v;
  }

  // Return node for given key,
  // throw an exception if the key is not in the tree.
  private Node<K, V> findForSure(K k) {
    Node<K, V> n = find(k);
    if (n == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    return n;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    Node<K, V> n = findForSure(k);
    return n.value;
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    return find(k) != null;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    return new AvlInorderIterator();
  }

  // Return node for given key.
  private Node<K, V> find(K k) {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> n = root;
    while (n != null) {
      int cmp = k.compareTo(n.key);
      if (cmp < 0) {
        n = n.left;
      } else if (cmp > 0) {
        n = n.right;
      } else {
        return n;
      }
    }
    return null;
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
  }

  private class AvlInorderIterator implements Iterator<K> {
    private final Stack<Node<K, V>> stack;

    AvlInorderIterator() {
      stack = new Stack<>();
      pushLeft(root);
    }

    private void pushLeft(Node<K, V> curr) {
      while (curr != null) {
        stack.push(curr);
        curr = curr.left;
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Node<K, V> top = stack.pop();
      pushLeft(top.right);
      return top.key;
    }
  }


  /**
   * Feel free to add whatever you want to the Node class (e.g. new fields).
   * Just avoid changing any existing names, deleting any existing variables,
   * or modifying the overriding methods.
   *
   * <p>Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers.</p>
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int height;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      height = 0;
    }

    @Override
    public String toString() {
      return key + ":" + value;
    }

    @Override
    public BinaryTreeNode getLeftChild() {
      return left;
    }

    @Override
    public BinaryTreeNode getRightChild() {
      return right;
    }
  }

}
