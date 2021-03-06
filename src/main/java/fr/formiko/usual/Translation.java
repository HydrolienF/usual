package fr.formiko.usual;

import fr.formiko.usual.ReadFile;
import fr.formiko.usual.chargerLesTraductions;
import fr.formiko.usual.debug;
import fr.formiko.usual.ecrireUnFichier;
import fr.formiko.usual.erreur;
import fr.formiko.usual.g;
import fr.formiko.usual.structures.listes.GString;
import fr.formiko.usual.types.str;

import java.awt.Font;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Translation {
  private static String sep = ":";
  // FUNCTIONS -----------------------------------------------------------------
  private static String getFolderTrad(){
    return chargerLesTraductions.getRep();
  }
  /**
  *{@summary Update files content as fr.txt while keeping the knowed translation.}<br>
  *@lastEditedVersion 2.27
  */
  @SuppressWarnings("unchecked")
  public static void copieTrads(){
    String tLangue[] = chargerLesTraductions.getTLangue();
    int lentl = tLangue.length;
    // on récupère les traductions déja effectuée.
    Map<String,String> trad [];
    try {
      trad = new Map[lentl];//un tableau de map
      trad[0] = chargerLesTraductions.chargerLesTraductions(0);
      for (int i=2;i<lentl ;i++ ) {
        trad[i-1]=chargerLesTraductions.chargerLesTraductions(i);
      }
      int k=0;
      String defaultLines [] = ReadFile.readFileArray(getFolderTrad()+"fr.txt");
      for (String s :tLangue ) {
        if(!s.equals("fr")){
          copieTradBase(s,trad[k], defaultLines);k++;
        }
      }
    }catch (Exception e) {
      erreur.erreur("La mise au format standard des traductions a échouée.");
    }
  }
  /**
  *{@summary Save the List of the line of an updated translation file.}<br>
  *@param se curent language
  *@param map translation map of the curent language
  *@param defaultLines List of the lines of the defaut file
  *@lastEditedVersion 2.27
  */
  private static void copieTradBase(String se, Map<String,String> map, String [] defaultLines){
    GString gs = new GString();
    for (String s : defaultLines) {
      if(chargerLesTraductions.estLigneDeTrad(s) && !str.contient(s,"[]",2)){//si c'est une ligne de trad qui ne correspond pas a un nom propre.
        if(str.contient(s,"test:",0)){gs.add("test:test"+str.sToSMaj(se));}
        else{gs.add(toTranslationLine(s,map));}//edited
      }else{
        gs.add(s);//not edited
      }
    }
    ecrireUnFichier.ecrireUnFichier(gs,getFolderTrad()+se+".txt");
  }
  /**
  *{@summary Save the List of the line of an updated translation file.}<br>
  *@param lineIn the translation line
  *@param map translation map of the curent language
  *@return a new translation line that may be juste key:
  *@lastEditedVersion 2.27
  */
  private static String toTranslationLine(String lineIn, Map<String,String> map){
    String key = keyInTranslationLine(lineIn);
    boolean updated=false;
    String lineOut="";
    for (String s4 : map.keySet()) { // s4 vaut les clés.
      if (s4.equals(key)){ //si on reconnait la clé dans la map.
        lineOut = key+sep+map.get(s4);
        updated=true; break;
      }
    }
    //dans ce cas on n'enregistre pas la valeur de la traduction :
    if(!updated){lineOut = key+sep;}
    //Si la ligne ce termine par [], on ne le modifie pas car c'est un nom propre.
    return lineOut;
  }
  /**
  *{@summary Save the List of the line of an updated translation file.}<br>
  *@param line the translation line
  *@param map translation map of the curent language
  *@lastEditedVersion 2.27
  */
  public static String keyInTranslationLine(String line){
    int lens = line.length();
    String sr="";int i=0;char c = ' ';
    if (lens !=0 ){ c = line.charAt(i);}
    if(lens > 1 && c=='\\' && line.charAt(1)=='\\'){return line;}
    for (i=1;i<lens && c!=':';i++ ) {
      sr = sr+c;
      c = line.charAt(i);
    }
    return sr;
  }
  /**
  *{@summary Translate the web site files.}<br>
  *@param pathToWebSiteFile path to acces to web site files.
  *@param pathToWebSiteTranslation path to acces to translation files.
  *@lastEditedVersion 2.27
  */
  public static void translateWebSite(String pathToWebSiteFile, String pathToWebSiteTranslation){
    Os.setOs(new Os());
    Folder.setFolder(new Folder(new ProgressionNull()));
    // Main.iniOp();
    chargerLesTraductions.setRep(pathToWebSiteTranslation);
    chargerLesTraductions.iniTLangue();
    // Chrono ch = new Chrono();
    // Main.startCh(ch);
    for (int i=0; i<3; i++) {
      HashMap<String,String> hm = chargerLesTraductions.chargerLesTraductions(i);
      g.setMap(hm);
      Translation.translateWebSiteFiles(pathToWebSiteFile, i);
    }
    // Main.endCh("translateWebSite",ch);
    // chargerLesTraductions.setRep(null);
  }
  /**
  *{@summary Translate the web site files.}<br>
  *@param pathToGit path to acces all files.
  *@lastEditedVersion 2.27
  */
  public static void translateWebSite(String pathToGit){
    translateWebSite(pathToGit+"docs/", pathToGit+"docs/data/language/");
  }
  /**
  *{@summary Translate all web site file for curent language.}<br>
  *It need to have the good path to web site file.
  *@param pathToWebSiteFile path to acces to web site files.
  *@lastEditedVersion 2.27
  */
  public static void translateWebSiteFiles(String pathToWebSiteFile, int languageId){
    String language = chargerLesTraductions.getLanguage(languageId);
    File directory = new File(pathToWebSiteFile+"ini/");

    for (File fileToTranslate : Arrays.stream(directory.listFiles()).sorted().toList()) {
      GString out = new GString();
      for (String s : ReadFile.readFileGs(fileToTranslate)) {
        out.add(replaceTranslation(s, pathToWebSiteFile+language+"/"));
      }
      ecrireUnFichier.ecrireUnFichier(out,pathToWebSiteFile+language+"/"+fileToTranslate.getName());
    }
  }
  /**
  *{@summary Translate a String by replacing €{key} by the translation of key.}<br>
  *@param s the String to translate
  *@param pathToWebSiteFile path to acces to web site files.
  *@lastEditedVersion 2.27
  */
  public static String replaceTranslation(String s, String pathToWebSiteFile){
    // replaceAll​("€[^€]", String replacement)
    String sr = "";
    boolean euro=false;
    boolean readingKey=false;
    String key="";
    //.toCharArray() is maybe not the best solution but I think that always using .charAt(i); for long String will cost more time.
    for (char c : s.toCharArray()) {
      if (readingKey) { //if were reading the key.
        if(c=='}'){ //if it just end.
          readingKey=false;
          if(key.startsWith("cmd:")){
            sr+=execCmd(key.substring(4), pathToWebSiteFile);
          }else{
            sr+=g.get(key);
          }
          key="";
        }else{ //if were still readinf the key.
          key+=c;
        }
      }else if(c=='€'){ //if next char may be key.
        euro=true;
      }else{
        if(euro){ //if last char where €.
          euro=false;
          if(c=='{'){ //if key start.
            readingKey=true;
          }else{ //if it wasn't a key.
            sr+='€';
            sr+=c;
          }
        }else{ //if no key start & not reading key.
          sr+=c;
        }
      }
    }
    return sr;
  }
  /**
  *{@summary Translate a String by replacing €{key} by the translation of key.}<br>
  *@param s the String to translate
  *@lastEditedVersion 2.27
  */
  public static String replaceTranslation(String s){
    return replaceTranslation(s, (String)null);
  }
  /**
  *{@summary Exec a command request in a file to translate.}<br>
  *@param s the String to exec
  *@param pathToWebSiteFile path to acces to web site files.
  *@lastEditedVersion 2.27
  */
  private static String execCmd(String s, String pathToWebSiteFile){
    String cmd="";
    String args="";
    boolean inArgs=false;
    for (char c : s.toCharArray()) {
      if(c==' '){
        inArgs=true;
      }else if(inArgs){
        args+=c;
      }else{
        cmd+=c;
      }
    }
    switch(cmd){
      case "include":
      return ReadFile.readFile(pathToWebSiteFile+args);
      default:
      return "";
    }
  }
  /**
  *{@summary Count how many time every char is used in a language map.}<br>
  *@param translationMap the Map to use to count char
  *@lastEditedVersion 2.12
  */
  public static Map<Character,Integer> countCharUsedInTranslation(Map<String,String> translationMap){
    Map<Character,Integer> map = new HashMap<Character,Integer>();
    // int total=0;
    for (var entry : translationMap.entrySet()) {
      for (char c : entry.getValue().toCharArray()) {
        int x=1;
        if(map.get(c)!=null){x+=map.get(c);}
        map.put(Character.valueOf(c), Integer.valueOf(x));
        // total++;
      }
    }
    // map.put(Character.valueOf('§'),total);
    return map;
  }
  /**
  *{@summary Count how many time every char is used in a language map.}<br>
  *@param id id of the language to count char
  *@lastEditedVersion 2.12
  */
  public static Map<Character,Integer> countCharUsedInTranslation(int id){
    return countCharUsedInTranslation(chargerLesTraductions.chargerLesTraductions(id));
  }
  /**
  *{@summary Count how many char can be draw in a language map.}<br>
  *@param id id of the language to count char
  *@param font font to test language printability
  *@param charWeigth more used char will have a higer weigth in the result
  *@lastEditedVersion 2.12
  */
  public static double partOfPrintableChar(int id, Font font, boolean charWeigth){
    Map<Character,Integer> map = countCharUsedInTranslation(id);
    int printable=0;
    int nonPrintable=0;
    for (var entry : map.entrySet()) {
      if(font.canDisplay(entry.getKey())){
        if(charWeigth){printable+=entry.getValue();}
        else{printable+=1;}
      }else{
        erreur.print(entry.getKey()+" ");
        if(charWeigth){nonPrintable+=entry.getValue();}
        else{nonPrintable+=1;}
      }
    }
    return (double)printable/(double)(printable+nonPrintable);
  }
  /**
  *{@summary Count how many char can be draw in a language map.}<br>
  *@param id id of the language to count char
  *@param fontName name of the font to test language printability
  *@param charWeigth more used char will have a higer weigth in the result
  *@lastEditedVersion 2.12
  */
  public static double partOfPrintableChar(int id, String fontName, boolean charWeigth){
    return partOfPrintableChar(id, new Font(fontName, Font.PLAIN, 1), charWeigth);
  }
  /**
  *{@summary True if all char can be draw in a language map.}<br>
  *@param id id of the language to count char
  *@param font font to test language printability
  *@lastEditedVersion 2.12
  */
  public static boolean canDisplayLanguage(int id, Font font){
    Map<Character,Integer> map = countCharUsedInTranslation(id);
    for (var entry : map.entrySet()) {
      if(!font.canDisplay(entry.getKey())){
        return false;
      }
    }
    return true;
  }
  /**
  *{@summary True if all char can be draw in a language map.}<br>
  *@param id id of the language to count char
  *@param fontName name of the font to test language printability
  *@lastEditedVersion 2.12
  */
  public static boolean canDisplayLanguage(int id, String fontName){
    return canDisplayLanguage(id, new Font(fontName, Font.PLAIN, 1));
  }
}
