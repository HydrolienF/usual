package fr.formiko.usual;

import fr.formiko.usual.maths.math;
import fr.formiko.usual.erreur;

import java.io.Serializable;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/**
*{@summary Options class.}<br>
*It contain all globals options and can save it.<br>
*@author Hydrolien
*@lastEditedVersion 2.30
*/
public class Options implements Serializable {
  protected SortedProperties properties;
  // CONSTRUCTORS --------------------------------------------------------------
  /**
  *{@summary Main constructor.}<br>
  *It can be launch with 0, 1 or severals files names in args.<br>
  *@param fileNames name of the files to load data from
  *@lastEditedVersion 2.30
  */
  public Options(String ... fileNames){
    properties=new SortedProperties();
    for(String fileName : fileNames){
      loadFromFile(fileName);
    }
  }
  // GET SET -------------------------------------------------------------------
  protected SortedProperties getProperties(){return properties;}
  /**
  *{@summary Return the String value of this option.}<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  private String get(String key){
    if(key==null){return "";}
    String value=properties.getProperty(key, "");
    return value;
  }
  /**
  *{@summary Return the String value of this option.}<br>
  *String can be parameter by setting key.maxlen or key.minlen.<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public String getString(String key){
    String value=get(key);
    int maxlen=getInt(key+".maxlen");
    int minlen=getInt(key+".minlen");
    int len=value.length();
    if(maxlen>0 && len>maxlen){return value.substring(0,maxlen);}
    if(minlen>0 && len<minlen){return value+" ".repeat(minlen-len);}
    return value;
  }
  /**
  *{@summary Return the boolean value of this option.}<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public boolean getBoolean(String key){
    String value=get(key);
    if(value.equalsIgnoreCase("true")){return true;}
    else{return false;}
  }
  /**
  *{@summary Return true if it is a boolean.}<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public boolean isBoolean(String key){
    String value=get(key);
    if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){return true;}
    else{return false;}
  }
  /**
  *{@summary Return the long value of this option.}<br>
  *Long can be parameter by setting key.max or key.min.<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public long getLong(String key){
    long value;
    try {
      value=Long.parseLong(get(key));
    }catch (NumberFormatException e) {
      value=-1;
    }
    long max;
    long min;
    try {
      max=Long.parseLong(get(key+".max"));
    }catch (NumberFormatException e) {
      max=Long.MAX_VALUE;
    }
    try {
      min=Long.parseLong(get(key+".min"));
    }catch (NumberFormatException e) {
      min=Long.MIN_VALUE;
    }
    return math.between(min, max, value);
  }
  /**
  *{@summary Return the int value of this option.}<br>
  *Int can be parameter by setting key.max or key.min.<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public int getInt(String key){
    int value;
    try {
      value=Integer.parseInt(get(key));
    }catch (NumberFormatException e) {
      value=-1;
    }
    int max;
    int min;
    try {
      max=Integer.parseInt(get(key+".max"));
    }catch (NumberFormatException e) {
      max=Integer.MAX_VALUE;
    }
    try {
      min=Integer.parseInt(get(key+".min"));
    }catch (NumberFormatException e) {
      min=Integer.MIN_VALUE;
    }
    return math.between(min, max, value);
  }

  /**
  *{@summary Return the byte value of this option.}<br>
  *Byte can be parameter by setting key.max or key.min.<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public byte getByte(String key){
    byte value;
    try {
      value=Byte.parseByte(get(key));
    }catch (NumberFormatException e) {
      value=-1;
    }
    byte max;
    byte min;
    try {
      max=Byte.parseByte(get(key+".max"));
    }catch (NumberFormatException e) {
      max=Byte.MAX_VALUE;
    }
    try {
      min=Byte.parseByte(get(key+".min"));
    }catch (NumberFormatException e) {
      min=Byte.MIN_VALUE;
    }
    return math.between(min, max, value);
  }

  /**
  *{@summary Return the double value of this option.}<br>
  *Double can be parameter by setting key.max or key.min.<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public double getDouble(String key){
    double value;
    try {
      value=Double.parseDouble(get(key));
    }catch (NumberFormatException e) {
      value=-1;
    }
    double max;
    double min;
    try {
      max=Double.parseDouble(get(key+".max"));
    }catch (NumberFormatException e) {
      max=Double.MAX_VALUE;
    }
    try {
      min=Double.parseDouble(get(key+".min"));
    }catch (NumberFormatException e) {
      min=Double.MIN_VALUE;
    }
    return math.between(min, max, value);
  }

  /**
  *{@summary Set an option.}<br>
  *It can be a new option or the edition of an existing option.<br>
  *@param key name of the option
  *@param value value of the option
  *@lastEditedVersion 2.30
  */
  public void set(String key, Object value){
    properties.setProperty(key, value.toString());
  }
  /**
  *{@summary Set an option.}<br>
  *It can be a new option or the edition of an existing option.<br>
  *@param key name of the option
  *@param value value of the option
  *@param cat the category of the option
  *@lastEditedVersion 2.30
  */
  public void set(String key, Object value, Object cat){
    set(key, value);
    if(cat!=null){
      set(key+".cat", cat);
    }
  }
  /**
  *{@summary Set an option.}<br>
  *It can be a new option or the edition of an existing option.<br>
  *@param key name of the option
  *@param value value of the option
  *@param cat the category of the option
  *@param hide if true, this option need to be hide in UI
  *@lastEditedVersion 2.30
  */
  public void set(String key, Object value, Object cat, boolean hide){
    set(key, value, cat);
    setHide(hide);
  }
  /**
  *{@summary Set an option.}<br>
  *It can be a new option or the edition of an existing option.<br>
  *@param key name of the option
  *@param value value of the option
  *@param cat the category of the option
  *@param min the min value of the option (minlen if value is a String)
  *@param max the max value of the option (maxlen if value is a String)
  *@lastEditedVersion 2.30
  */
  public void set(String key, Object value, Object cat, Object min, Object max){
    set(key, value, cat);
    if(value instanceof String){
      if(min!=null){set(key+".minlen", min);}
      if(max!=null){set(key+".maxlen", max);}
    }else{
      if(min!=null){set(key+".min", min);}
      if(max!=null){set(key+".max", max);}
    }
  }
  /**
  *{@summary Set an option.}<br>
  *It can be a new option or the edition of an existing option.<br>
  *@param key name of the option
  *@param value value of the option
  *@param cat the category of the option
  *@param min the min value of the option (minlen if value is a String)
  *@param max the max value of the option (maxlen if value is a String)
  *@param hide if true, this option need to be hide in UI
  *@lastEditedVersion 2.30
  */
  public void set(String key, Object value, Object cat, Object min, Object max, boolean hide){
    set(key, value, cat, min, max);
    setHide(hide);
  }
  /**
  *{@summary Set value to the next possible option.}<br>
  *For boolean it swap to the other value.<br>
  *For long, int or byte it do (value+1)%max.<br>
  *@param key name of the option
  *@lastEditedVersion 2.30
  */
  public void setNext(String key){
    if(isBoolean(key)){
      set(key, !getBoolean(key));
    }else{
      long value=getLong(key);
      if(value==getLong(key+".max")){set(key, getLong(key+".min"));}
      else{set(key, value+1);}
    }
  }
  /**
  *{@summary Set the hide value.}<br>
  *@param hide if true, this option need to be hide in UI
  *@lastEditedVersion 2.30
  */
  public void setHide(boolean hide){
    if(hide){
      set(key+".hide", true);
    }
  }

  // FUNCTIONS -----------------------------------------------------------------
  /**
  *{@summary Load more options from a file.}<br>
  *@param fileName name of the file
  *@lastEditedVersion 2.30
  */
  public void loadFromFile(String fileName){
    if(fileName==null){return;}
    try {
      InputStream is = Files.newInputStream(Path.of(fileName));
      properties.load(is);
    }catch (IOException e) {
      erreur.erreur("Fail to load properties from file "+fileName+" "+e);
    }
  }
  /**
  *{@summary Return true if key is a parameter of an other key.}<br>
  *@lastEditedVersion 2.30
  */
  protected boolean isParameter(String key){
    return (key.endsWith(".max")
        || key.endsWith(".min")
        || key.endsWith(".maxlen")
        || key.endsWith(".minlen")
        || key.endsWith(".cat"));
        || key.endsWith(".hide"));
  }
}
