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
  @Test
  public void testCompareToNull(){
    Version v = new Version("1.2.3");
    assertFalse(v.equals(null));
    assertTrue(v.compareTo(null)>0);
  }
  @Test
  public void testToString(){
    Version v = new Version("3.41.4");
    assertEquals(v.toString(),"3.41.4");
  }
  @Test
  public void testBadArgument(){
    assertThrows(IllegalArgumentException.class, () -> new Version(null));
    assertThrows(IllegalArgumentException.class, () -> new Version("versionQuatre"));
    assertThrows(IllegalArgumentException.class, () -> new Version("v1.2.3"));
  }
  @Test
  public void testEqualsString(){
    Version v = new Version("3.41.4");
    assertTrue(v.equals("3.41.4"));
    assertFalse(v.equals("3.41.1"));
    assertFalse(v.equals("3.42.4"));
    assertFalse(v.equals("2.42.4"));
  }
  @Test
  public void testEqualsFalse(){
    Version v = new Version("3.41.4");
    assertFalse(v.equals(Integer.valueOf(3)));
    assertFalse(v.equals(new Os()));
  }
  @Test
  public void testCompareTo0(){
    Version v = new Version("3.41.4");
    assertTrue(v.equals(new Version("3.41.4.0")));
    v = new Version("3.41.0");
    assertTrue(v.equals(new Version("3.41")));
  }
}
