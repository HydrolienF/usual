package fr.formiko.usual.structures;

import org.junit.jupiter.api.Test;

import fr.formiko.tests.TestCaseMuet;
import fr.formiko.usual.fichier;
import fr.formiko.usual.structures.ImageTree;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageTreeTest extends TestCaseMuet {

  // FUNCTIONS -----------------------------------------------------------------
  @Test
  public void testFolderToTree(){
    int x = getId();
    File dir = new File("testDir"+x+"/testDir2/testDir3/");
    dir.mkdirs();
    dir = new File("testDir"+x+"/testDir2/testDir3bis/");
    dir.mkdirs();
    File dirTested = new File("testDir"+x);
    ImageTree tree = new ImageTree();
    tree.folderToTree(dirTested);
    assertEquals("0(0(0,1))",tree.toString());
    dir = new File("testDir"+x+"/testDir2/testDir3ter/");
    dir.mkdirs();
    tree = new ImageTree();
    tree.folderToTree(dirTested);
    assertEquals("0(0(0,1,2))",tree.toString());
    dir = new File("testDir"+x+"/testDir2bis/");
    dir.mkdirs();
    tree = new ImageTree();
    tree.folderToTree(dirTested);
    assertEquals("0(0(0,1,2),1)",tree.toString());
    assertTrue(fichier.deleteDirectory(dirTested));
  }
  @Test
  public void testFolderToTree2(){
    int x = getId();
    File dir = new File("testDir"+x+"/testDir2/testDir3/");
    dir.mkdirs();
    dir = new File("testDir"+x+"/testDir2/testDir3bis/");
    dir.mkdirs();
    File dirTested = new File("testDir"+x);
    ImageTree tree = new ImageTree();
    tree.folderToTree("testDir"+x);
    assertEquals("0(0(0,1))",tree.toString());
    assertTrue(fichier.deleteDirectory(dirTested));
  }
  @Test
  public void testFolderToTree3(){
    int x = getId();
    File dirTested = new File("testDir"+x);
    fichier.deleteDirectory(dirTested);
    File dir = new File("testDir"+x+"/testDir2/testDir3/");
    dir.mkdirs();
    dir = new File("testDir"+x+"/testDir2/testDir3bis/");
    dir.mkdirs();
    try {
      File file = new File("testDir"+x+"/testDir2/testDir3bis/bka.txt");
      file.createNewFile();
    }catch (Exception e) {
      assertTrue(false);
    }
    ImageTree tree = new ImageTree();
    tree.folderToTree("testDir"+x);
    assertEquals("0(0(0,1))",tree.toString());
    assertTrue(fichier.deleteDirectory(dirTested));
  }
  @Test
  public void testFolderToTree4(){
    int x = getId();
    File dirTested = new File("testDir"+x);
    fichier.deleteDirectory(dirTested);
    File dir = new File("testDir"+x+"/testDir2/0testDir/");
    dir.mkdirs();
    dir = new File("testDir"+x+"/testDir2/1testDir/");
    dir.mkdirs();
    try {
      File file = new File("testDir"+x+"/testDir2/1testDir/bka.txt");
      file.createNewFile();
      file = new File("testDir"+x+"/testDir2/1testDir/imag.png");
      BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
      ImageIO.write(bi, "png", file);
    }catch (Exception e) {
      assertTrue(false);
    }
    ImageTree tree = new ImageTree();
    tree.folderToTree("testDir"+x);
    assertEquals("0(0(0,1I))",tree.toString());
    assertTrue(fichier.deleteDirectory(dirTested));
  }
  @Test
  public void testFolderToTree5(){
    int x = getId();
    File dirTested = new File("testDir"+x);
    fichier.deleteDirectory(dirTested);
    File dir = new File("testDir"+x+"/testDir2/1testDir/");
    dir.mkdirs();
    dir = new File("testDir"+x+"/testDir2/0testDir/");
    dir.mkdirs();
    dir = new File("testDir"+x+"/testDir2/2testDir/");
    dir.mkdirs();
    try {
      File file = new File("testDir"+x+"/testDir2/1testDir/imag.png");
      BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
      ImageIO.write(bi, "png", file);
    }catch (Exception e) {
      assertTrue(false);
    }
    ImageTree tree = new ImageTree();
    tree.folderToTree("testDir"+x);
    assertEquals("0(0(0,1I,2))",tree.toString());
    assertTrue(fichier.deleteDirectory(dirTested));
  }
  @Test
  public void testBuilder(){
    ImageTree it = ImageTree.newImageTree();
  }
  @Test
  public void testCopyStructure(){
    ImageTree it = new ImageTree();
    ImageTree itcp = it.copyStructure();
    // need tree to implements equals function to work
    // assertEquals(it, itcp);
    it.getRoot().setContent(new BufferedImage(10,9,BufferedImage.TYPE_INT_ARGB));
    assertNotEquals(it, itcp);
  }
  @Test
  public void testFolderToTreeNull(){
    ImageTree tree = new ImageTree();
    assertEquals(0,tree.getRoot().getChildrenSize());
    tree.folderToTree(new File("dontExist135295"));
    assertEquals(0,tree.getRoot().getChildrenSize());
    File f = new File("Exist135295");
    try {
      f.createNewFile();
    }catch (IOException e) {
      assertTrue(false);
    }
    f.deleteOnExit();
    tree.folderToTree(f);
    assertEquals(0,tree.getRoot().getChildrenSize());
  }
}
