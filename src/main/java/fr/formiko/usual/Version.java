package fr.formiko.usual;

/**
*{@summary A simple version with comparator.}<br>
*@author Alex (https://stackoverflow.com/users/1445568/alex)
*@lastEditedVersion 2.26
*/
public class Version implements Comparable<Version> {
  private String version;

  // CONSTRUCTORS --------------------------------------------------------------
  /**
  *{@summary Main constructor.}<br>
  *@lastEditedVersion 2.26
  */
  public Version(String version) {
    if(version == null)
      throw new IllegalArgumentException("Version can not be null");
    if(!version.matches("[0-9]+(\\.[0-9]+)*"))
      throw new IllegalArgumentException("Invalid version format");
    this.version = version;
  }

  // GET SET -------------------------------------------------------------------
  public final String get() {return this.version;}
  // FUNCTIONS -----------------------------------------------------------------
  /**
  *{@summary Standard to string.}<br>
  *@lastEditedVersion 2.26
  */
  @Override
  public String toString(){
    return get();
  }
  /**
  *{@summary Compare to an other version.}<br>
  *@param v2 the other version to compare with
  *@lastEditedVersion 2.26
  */
  @Override
  public int compareTo(Version v2) {
    if(v2==null){
      return 1;
    }
    String[] thisParts = this.get().split("\\.");
    String[] v2Parts = v2.get().split("\\.");
    int length = Math.max(thisParts.length, v2Parts.length);
    for(int i = 0; i < length; i++) {
      int thisPart = i < thisParts.length ?
        Integer.parseInt(thisParts[i]) : 0;
      int v2Part = i < v2Parts.length ?
        Integer.parseInt(v2Parts[i]) : 0;
      if(thisPart < v2Part)
        return -1;
      if(thisPart > v2Part)
        return 1;
    }
    return 0;
  }
  /**
  *{@summary Standard equals function.}<br>
  *@param v2 the other version to compare with
  *@lastEditedVersion 2.26
  */
  @Override
  public boolean equals(Object v2) {
    if(this == v2)
      return true;
    if(v2 == null)
      return false;
    if(v2 instanceof String)
      return equals(new Version((String)v2));
    if(this.getClass() != v2.getClass())
      return false;
    return this.compareTo((Version) v2) == 0;
  }

}
