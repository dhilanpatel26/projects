package hw6;

import hw6.bst.TreapMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to Treap.
 */
@SuppressWarnings("All")
public class TreapMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new TreapMap<>();
  }

  @Test
  public void testInsertNoRotation() {
    map = new TreapMap<>(new Random(2));
    map.insert("B", "-");
    map.insert("A", "-");
    map.insert("C", "-");

    String[] expected = new String[]{
        "B:-:-1154715079",
        "A:-:1260042744 C:-:-423279216"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void testInsertLeftRotation() {
    map = new TreapMap<>(new Random(2));
    map.insert("A", "-");
    map.insert("B", "-");
    map.insert("C", "-");

    String[] expected = new String[]{
        "A:-:-1154715079",
        "null C:-:-423279216",
        "null null B:-:1260042744 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void testInsertRightRotation() {
    map = new TreapMap<>(new Random(2));
    map.insert("C", "-");
    map.insert("B", "-");
    map.insert("A", "-");

    String[] expected = new String[]{
        "C:-:-1154715079",
        "A:-:-423279216 null",
        "null B:-:1260042744 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void testRemoveSubtreeRootRightRotation() {
    map = new TreapMap<>(new Random(0));
    map.insert("A", "-");
    map.insert("C", "-");
    map.insert("B", "-");
    map.remove("C");

    String[] expected = new String[]{
        "A:-:-1155484576",
        "null B:-:1033096058"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void testRemoveSubtreeRootLeftRotation() {
    map = new TreapMap<>(new Random(0));
    map.insert("C", "-");
    map.insert("A", "-");
    map.insert("B", "-");
    map.remove("A");

    String[] expected = new String[]{
        "C:-:-1155484576",
        "B:-:1033096058 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void testRemoveLeafNoRotation() {
    map = new TreapMap<>(new Random(0));
    map.insert("A", "-");
    map.insert("C", "-");
    map.insert("B", "-");
    map.remove("B");

    String[] expected = new String[]{
        "A:-:-1155484576",
        "null C:-:-723955400"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

}