package fr.formiko.usual;

import fr.formiko.usual.structures.listes.GString;

public class Usual {
  public static void main(String[] args) {
    color.iniColor();
    args = splitArgs(args);
    switch(args[0]){
      case "tws", "translateWebSite":{
        Translation.translateWebSite(args[1]);
        break;
      }
      case "javadocHeader":{
        fichier.download("https://raw.githubusercontent.com/HydrolienF/HydrolienF.github.io/master/javadocHeader.html", "javadocHeader.html");
        fichier.download("https://raw.githubusercontent.com/HydrolienF/HydrolienF.github.io/master/styleJavadoc.css", "styleJavadoc.css");
        GString pom = ReadFile.readFileGs("pom.xml");
        GString pomOut = new GString();
        for (String lineP : pom) {
          if(lineP.endsWith("€{HEADER}")){
            GString header = ReadFile.readFileGs("javadocHeader.html");
            for (String lineH : header) {
              pomOut.add(lineH.replaceAll("<","&#60;").replaceAll(">","&#62;"));
            }
          }else{
            pomOut.add(lineP);
          }
        }
        ecrireUnFichier.ecrireUnFichier(pomOut,"pom.xml");
        break;
      }
    }
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
