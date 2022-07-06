package fr.formiko.usual;

import org.junit.jupiter.api.Test;

import fr.formiko.tests.TestCaseMuet;

public class VersionTest extends TestCaseMuet {
  @Test
  public void testEquals(){
    Version v = new Version("1.2.3");
    assertTrue(v.equals(new Version("1.2.3")));
    assertEquals(v,new Version("1.2.3"));
    assertEquals(v,v);
  }
  @Test
  public void testCompareTo(){
    Version v = new Version("1.2.3");
    Version v2 = new Version("1.2.2");
    Version v3 = new Version("10.0.0");
    Version v4 = new Version("1.2.3");
    Version v5 = new Version("1.2");
    assertTrue(v.compareTo(v2)>0);
    assertTrue(v.compareTo(v4)==0);
    assertTrue(v.compareTo(v3)<0);
    assertTrue(v.compareTo(v5)>0);
  }
}
