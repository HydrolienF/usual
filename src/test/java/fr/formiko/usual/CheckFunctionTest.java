package fr.formiko.usual;

import org.junit.jupiter.api.Test;

import fr.formiko.tests.TestCaseMuet;

public class CheckFunctionTest extends TestCaseMuet {
  @Test
  public void testBuilder(){
    CheckFunction cf = CheckFunction.newCheckFunction();
    assertEquals("",cf.getText());
    assertFalse(cf.isChecked());
    cf.setChecked(false);
    assertFalse(cf.isChecked());
    cf.setChecked(true);
    assertTrue(cf.isChecked());
  }
  @Test
  public void testBuilder2(){
    CheckFunction cf = CheckFunction.newCheckFunction()
        .setChecked(true)
        .setText("some text.");
    assertTrue(cf.isChecked());
    assertEquals("some text.",cf.getText());
  }
  //for test coverage
  @Test
  public void testExecDefault(){
    CheckFunction cf = new CheckFunction();
    cf.setChecked(true);
    cf.run();
  }
  @Test
  public void testExecFalse(){
    Point p = new Point(0,0);
    CheckFunction cf = new CheckFunction(){
      @Override
      public void exec(){
        p.setX(1);
      }
    };
    cf.setChecked(false);
    cf.run();
    assertEquals(0,(int)p.getX());
  }
  @Test
  public void testExecTrue(){
    Point p = new Point(0,0);
    CheckFunction cf = new CheckFunction(){
      @Override
      public void exec(){
        p.setX(1);
      }
    };
    cf.setChecked(true);
    cf.run();
    assertEquals(1,(int)p.getX());
  }
}
