package hw6;

import hw6.bst.AvlTreeMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// added
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Collections;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to AVL Tree.
 */
@SuppressWarnings("All")
public class AvlTreeMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new AvlTreeMap<>();
  }

  @Test
  public void insertLeftRotation() {
    map.insert("1", "a");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a
     */

    map.insert("2", "b");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a,
        null 2:b
     */

    map.insert("3", "c"); // it must do a left rotation here!
    // System.out.println(avl.toString());
    // must print
    /*
        2:b,
        1:a 3:c
     */

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafLeftRotation() {
    map.insert("1", "a");
    map.insert("0", "0");
    map.insert("2", "b");
    map.insert("3", "c");
    map.remove("0");

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertRightRotation() {
    map.insert("3", "c");
    map.insert("2", "b");
    map.insert("1", "a");

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafRightRotation() {
    map.insert("3", "c");
    map.insert("4", "d");
    map.insert("2", "b");
    map.insert("1", "a");
    map.remove("4");

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertRightLeftRotation() {
    map.insert("3", "a");
    map.insert("7", "c");
    map.insert("5", "b");

    String[] expected = new String[]{
        "5:b",
        "3:a 7:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertLeftRightRotation() {
    map.insert("7", "c");
    map.insert("3", "a");
    map.insert("5", "b");

    String[] expected = new String[]{
        "5:b",
        "3:a 7:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafRightLeftRotation() {
    map.insert("2", "-");
    map.insert("1", "-");
    map.insert("4", "-");
    map.insert("3", "-");
    map.remove("1");

    String[] expected = new String[]{
        "3:-",
        "2:- 4:-"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafLeftRightRotation() {
    map.insert("3", "-");
    map.insert("1", "-");
    map.insert("2", "-");
    map.insert("4", "-");
    map.remove("4");

    String[] expected = new String[]{
        "2:-",
        "1:- 3:-"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeRootLeftRotation() {
    map.insert("2", "-");
    map.insert("1", "-");
    map.insert("3", "-");
    map.insert("4", "-");
    map.remove("2");

    String[] expected = new String[]{
        "3:-",
        "1:- 4:-"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeRootRightLeftRotation() {
    map.insert("2", "-");
    map.insert("1", "-");
    map.insert("4", "-");
    map.insert("3", "-");
    map.remove("2");

    String[] expected = new String[]{
        "3:-",
        "1:- 4:-"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertDoesNotCauseRotation() {
    map.insert("2", "-");
    map.insert("1", "-");
    map.insert("3", "-");

    String[] expected = new String[]{
        "2:-",
        "1:- 3:-",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeDoesNotCauseRotation() {
    map.insert("2", "-");
    map.insert("1", "-");
    map.insert("3", "-");
    map.insert("4", "-");
    map.remove("4");

    String[] expected = new String[]{
        "2:-",
        "1:- 3:-",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void testIteratorException() {
    AvlTreeMap<String, String> avlmap = (AvlTreeMap<String, String>) map;
    Iterator<String> it = avlmap.iterator();
    try {
      it.next();
      fail("Iterator did not throw NoSuchElementException");
    } catch (NoSuchElementException ex) {
      return;
    }
  }

  @Test
  public void testLinkedListOrder() {
    List<String> keys = new ArrayList<String>(Arrays.asList(
        "A", "B", "C", "D", "E", "F", "G"));

    for (String key : keys) {
      map.insert(key, "-");
    }

    String[] expected = new String[]{
        "D:-",
        "B:- F:-",
        "A:- C:- E:- G:-"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void testLinkedListReverseOrder() {
    List<String> keys = new ArrayList<String>(Arrays.asList(
        "G", "F", "E", "D", "C", "B", "A"));

    for (String key : keys) {
      map.insert(key, "-");
    }

    String[] expected = new String[]{
        "D:-",
        "B:- F:-",
        "A:- C:- E:- G:-"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

}
