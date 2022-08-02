package fr.formiko.usual;

import fr.formiko.usual.structures.listes.GString;
import fr.formiko.usual.images.Images;
import fr.formiko.usual.types.str;

/**
*{@summary Main class with all function that can be launch by args.}<br>
*@author Hydrolien
*@lastEditedVersion 2.29
*/
public class Usual {
  /**
  *{@summary Main function with all function that can be launch by args.}<br>
  *@lastEditedVersion 2.29
  */
  public static void main(String[] args) {
    color.iniColor();
    args = str.splitArgs(args);
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
      case "resizeAllUnder":{
        String fileName = args[1];
        int pixelMax = Integer.parseInt(args[2]);
        Images.resizeAll(fileName, pixelMax, false);
        break;
      }
      case "resizeAll":{
        String fileName = args[1];
        int pixelMax = Integer.parseInt(args[2]);
        Images.resizeAll(fileName, pixelMax, true);
        break;
      }
    }
  }
}
