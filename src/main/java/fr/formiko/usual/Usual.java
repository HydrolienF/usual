package fr.formiko.usual;

import fr.formiko.usual.structures.listes.GString;
import fr.formiko.usual.images.Images;
import fr.formiko.usual.types.str;
import fr.formiko.usual.stats;

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
      case "stats":{
        stats(args);
        break;
      }
      case "statsG":{
        stats.setOnlyLastLine(true);
        stats.setSpliter(';');
        stats2(args);
        break;
      }
    }
  }


  /**
  *{@summary update stats in stats.txt.}<br>
  *It can also print a value if it have a 2a args that is an integer.
  *It always print a global value for every file:
  *<ul>
  *<li>1: number of lines.
  *<li>2: number of classes.
  *<li>3: number of long functions.
  *<li>4: number of short functions.
  *</ul>
  *@lastEditedVersion 2.20
  */
  private static void stats(String args[]){
    int valueToPrint = 0;
    if(args.length>2){
      valueToPrint=str.sToI(args[2]);
    }
    if(args.length>1){
      stats.statsJavadoc(args[1], false);
    }else{
      stats.statsJavadoc("src/main/java/", true);
    }
    if(valueToPrint>0){
      String s = switch (valueToPrint) {
        case 1:
        yield stats.sommeNbrDeLigneG+"";
        case 2:
        yield stats.sommeDesClassG+"";
        case 3:
        yield stats.sommeDesFctLG+"";
        case 4:
        yield stats.sommeDesFctCG+"";
        default:
        yield "";
      };
      erreur.println(s);
    }
  }
  /**
  *{@summary Print global stats about the current state of the projet.}<br>
  *@lastEditedVersion 2.20
  */
  private static void stats2(String args[]){
    String result="";
    if(args.length>1){
      result+=stats.getStats(args[1], false).toString();
    }else{
      result+=stats.getStats("src/main/java/", true).toString();
    }
    erreur.println(result);
  }
}
