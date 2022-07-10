package fr.formiko.usual;

/**
*{@summary .}<br>
*@author Hydrolien
*@lastEditedVersion 2.28
*/
public class CheckFunction {
  private boolean checked;
  private String text;
  // CONSTRUCTORS --------------------------------------------------------------
  /***
  *{@summary Empty main constructor.}<br>
  *@lastEditedVersion 2.28
  */
  public CheckFunction(){
    text="";
  }
  /**
  *{@summary Empty main builder.}<br>
  *@lastEditedVersion 2.28
  */
  public static CheckFunction newCheckFunction(){
    return new CheckFunction();
  }
  // GET SET -------------------------------------------------------------------
  public boolean isChecked() {return checked;}
  /**
  *{@summary Setter that return this.}<br>
  *@lastEditedVersion 2.28
  */
  public CheckFunction setChecked(boolean checked) {
    this.checked=checked;
    return this;
  }
  public String getText() {return text;}
  /**
  *{@summary Setter that return this.}<br>
  *@lastEditedVersion 2.28
  */
  public CheckFunction setText(String text) {
    this.text=text;
    return this;
  }
  // FUNCTIONS -----------------------------------------------------------------
  /***
  *{@summary Fuction to override &#38; to launch at the end of use of this.}<br>
  *@lastEditedVersion 2.28
  */
  protected void exec(){}

  public void run(){
    if(isChecked()){
      exec();
    }
  }
}
