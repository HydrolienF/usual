package fr.formiko.usual;

public class Usual {
  public static void main(String[] args) {
    color.iniColor();
    args = splitArgs(args);
    Translation.translateWebSite(args[1]);
  }

  public static String [] splitArgs(String[] args){
    if(args.length!=0){
      if(args.length==1 && args[0] != null){
        args = args[0].split(" ");
      }
    }
    return args;
  }
}
