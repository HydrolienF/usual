package fr.formiko.usual.structures;

import org.junit.jupiter.api.Test;

import fr.formiko.tests.TestCaseMuet;

public class NodeTest extends TestCaseMuet {
  @Test
  public void testNode(){
    Node<String> node = new Node<String>();
    assertThrows(UnsupportedOperationException.class, () -> node.getChildren(0));
  }
}
