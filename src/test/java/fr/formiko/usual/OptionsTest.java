package fr.formiko.usual;

import org.junit.jupiter.api.Test;

import fr.formiko.tests.TestCaseMuet;
import fr.formiko.usual.structures.listes.GString;
import fr.formiko.usual.ecrireUnFichier;

public class OptionsTest extends TestCaseMuet {
  @Test
  public void testGetEmpty(){
    Options op = new Options();
    assertEquals("",op.getString("clé"));
  }
  @Test
  public void testGetString(){
    Options op = new Options();
    op.set("clé","val1");
    assertEquals("val1",op.getString("clé"));
  }
  @Test
  public void testGetString2(){
    Options op = new Options();
    op.set("k","val1");
    op.set("k.maxlen",2);
    assertEquals("va",op.getString("k"));
  }
  @Test
  public void testGetString3(){
    Options op = new Options();
    op.set("k","val1");
    op.set("k.minlen",10);
    assertEquals("val1      ",op.getString("k"));
  }

  @Test
  public void testGetBoolean(){
    Options op = new Options();
    op.set("clé","true");
    assertEquals(true,op.getBoolean("clé"));
  }
  @Test
  public void testGetBoolean2(){
    Options op = new Options();
    // op.set("clé","true");
    assertEquals(false, op.getBoolean("clé"));
  }
  @Test
  public void testGetBoolean3(){
    Options op = new Options();
    op.set("clé","false");
    assertEquals(false, op.getBoolean("clé"));
  }
  @Test
  public void testGetBoolean4(){
    Options op = new Options();
    op.set("clé","qqchose");
    assertEquals(false, op.getBoolean("clé"));
  }
  @Test
  public void testGetBoolean5(){
    Options op = new Options();
    op.set("clé",true);
    assertEquals(true,op.getBoolean("clé"));
  }

  @Test
  public void testGetInt(){
    Options op = new Options();
    op.set("k1",0);
    assertEquals(0,op.getInt("k1"));
  }
  @Test
  public void testGetInt2(){
    Options op = new Options();
    op.set("k1",0);
    op.set("k1.max",10);
    op.set("k1.min",-2);
    assertEquals(0,op.getInt("k1"));
  }
  @Test
  public void testGetInt3(){
    Options op = new Options();
    op.set("k1",-9);
    op.set("k1.max",10);
    op.set("k1.min",-2);
    assertEquals(-2,op.getInt("k1"));
  }
  @Test
  public void testGetInt4(){
    Options op = new Options();
    op.set("k1",10);
    op.set("k1.max",10);
    op.set("k1.min",-2);
    assertEquals(10,op.getInt("k1"));
  }
  @Test
  public void testGetInt5(){
    Options op = new Options();
    op.set("k1",11);
    op.set("k1.max",10);
    op.set("k1.min",-2);
    assertEquals(10,op.getInt("k1"));
  }
  @Test
  public void testGetByte(){
    Options op = new Options();
    op.set("k1",(byte)11);
    op.set("k1.max",(byte)10);
    op.set("k1.min",(byte)-2);
    assertEquals((byte)10,op.getByte("k1"));
  }
  @Test
  public void testGetDouble(){
    Options op = new Options();
    op.set("k1",11.9764);
    op.set("k1.max",10.2);
    op.set("k1.min",-2);
    assertEquals(10.2,op.getDouble("k1"));
  }

  @Test
  public void testSetNext(){
    Options op = new Options();
    op.set("k",2);
    op.set("k.max",4);
    op.set("k.min",-2);
    assertEquals(2,op.getInt("k"));
    op.setNext("k");
    assertEquals(3,op.getInt("k"));
    op.setNext("k");
    assertEquals(4,op.getInt("k"));
    op.setNext("k");
    assertEquals(-2,op.getInt("k"));
    op.setNext("k");
    assertEquals(-1,op.getInt("k"));
    op.setNext("k");
    assertEquals(0,op.getInt("k"));
    op.setNext("k");
    assertEquals(1,op.getInt("k"));
    op.setNext("k");
    assertEquals(2,op.getInt("k"));
    op.setNext("k");
    assertEquals(3,op.getInt("k"));
    op.setNext("k");
    assertEquals(4,op.getInt("k"));
    op.setNext("k");
    assertEquals(-2,op.getInt("k"));
  }
  @Test
  public void testSet(){
    Options op = new Options();
    op.set("k",2,null,-2,4);
    assertEquals(2,op.getInt("k"));
    op.setNext("k");
    assertEquals(3,op.getInt("k"));
    op.setNext("k");
    assertEquals(4,op.getInt("k"));
    op.setNext("k");
    assertEquals(-2,op.getInt("k"));
    op.setNext("k");
    assertEquals(-1,op.getInt("k"));
    op.setNext("k");
    assertEquals(0,op.getInt("k"));
    op.setNext("k");
    assertEquals(1,op.getInt("k"));
    op.setNext("k");
    assertEquals(2,op.getInt("k"));
    op.setNext("k");
    assertEquals(3,op.getInt("k"));
    op.setNext("k");
    assertEquals(4,op.getInt("k"));
    op.setNext("k");
    assertEquals(-2,op.getInt("k"));
  }

  @Test
  public void loadFromFileTest(){
    String fileName="loadFromFileTest"+getId();
    GString gs = new GString();
    gs.add("language=fr");
    gs.add("op=true");
    ecrireUnFichier.ecrireUnFichier(gs, fileName);

    Options op = new Options();
    op.loadFromFile(fileName);
    assertEquals("fr",op.getString("language"));
    assertEquals(true,op.getBoolean("op"));

    fichier.deleteDirectory(fileName);
  }

  @Test
  public void loadFromFileTest2(){
    String fileName="loadFromFileTest"+getId();
    String fileName2="loadFromFileTest"+getId();
    GString gs = new GString();
    gs.add("language=fr");
    gs.add("op=true");
    ecrireUnFichier.ecrireUnFichier(gs, fileName);
    gs = new GString();
    gs.add("vol:100");
    ecrireUnFichier.ecrireUnFichier(gs, fileName2);

    Options op = new Options(fileName, fileName2);
    assertEquals("fr",op.getString("language"));
    assertEquals(true,op.getBoolean("op"));
    assertEquals(100,op.getInt("vol"));

    fichier.deleteDirectory(fileName);
    fichier.deleteDirectory(fileName2);
  }

  @Test
  public void loadFromFileTest3(){
    String fileName="loadFromFileTest"+getId();
    String fileName2="loadFromFileTest"+getId();
    GString gs = new GString();
    gs.add("language=fr");
    gs.add("op=true");
    ecrireUnFichier.ecrireUnFichier(gs, fileName);
    gs = new GString();
    gs.add("language=en");
    gs.add("vol:100");
    ecrireUnFichier.ecrireUnFichier(gs, fileName2);

    Options op = new Options(fileName, fileName2);
    assertEquals("en",op.getString("language"));
    assertEquals(true,op.getBoolean("op"));
    assertEquals(100,op.getInt("vol"));

    fichier.deleteDirectory(fileName);
    fichier.deleteDirectory(fileName2);
  }

  // @Test
  // public void testAll(){
  //
  // }
}
