package fr.formiko.usual;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import fr.formiko.usual.Chrono;
import fr.formiko.usual.exceptions.MissingFolderException;
import fr.formiko.usual.media.audio.MusicPlayer;
import fr.formiko.usual.structures.listes.GString;
import fr.formiko.usual.types.str;

import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
*{@summary Class that have all link to all folder of formiko.}<br>
*You can acces to file by using getters.
*Ex : getFolderStable()+getFolderImages() will return the path to stable images.
*@author Hydrolien
*@lastEditedVersion 2.26
*/
public class Folder {
  // TODO #579 Move formiko data from Folder to FFolder.
  protected static String ROOT_PATH;
  protected static Folder folder;
  public static String DEFAULT_NULL_VERSION="0.0.0";
  private String folderMain=".formiko/";
  private String folderStable="stable/";
  private String folderTemporary="temporary/";
  private String folderResourcesPacks="resourcesPacks/";

  private String folderSaves="saves/";

  private String folderBin="bin/";
  private String folderImages="images/";
  private String folderSounds="sounds/";
  private String folderMusiques="musics/";
  private String folderMaps="maps/";
  private String folderLanguages="languages/";
  private String folderLevels="levels/";
  private String folderVideos="videos/";

  private int missingFolder;
  private boolean secondTime;
  private boolean launchDownload;

  // private static boolean newVersionAviableTestDone=false;
  private Progression progression;
  private static boolean firstGame;

  private static String version;
  /**
  *{@summary Main constructor. Create a Folder & initialize all needed thing.}<br>
  *After creating it, we can use any getter.
  *@param progression the progression interface where it can say how download or unzip is working.
  *@lastEditedVersion 2.26
  */
  public Folder(Progression progression){
    secondTime=false;
    this.progression=progression;
    iniFolderMain();
    // setFolderMain("");//always remove after test
    File folderM = new File(getFolderMain());
    if(!folderM.mkdirs() && !folderM.isDirectory()){
      erreur.alerte("can't create main folder : "+getFolderMain());
      setFolderMain();
      folderM = new File(getFolderMain());
      if(!folderM.mkdirs()){
        erreur.erreur("can't create main folder even in curent repository : "+getFolderMain());
      }
    }
  }
  /**
  *{@summary Create a Folder & initialize all needed thing.}<br>
  *After creating it, we can use any getter.
  *Progression will not be used.
  *@lastEditedVersion 2.26
  */
  public Folder(){
    this(new ProgressionNull());
  }
  // GET SET -------------------------------------------------------------------
  public static Folder getFolder(){if(folder==null){folder=new Folder();}return folder;}
  public static void setFolder(Folder f){folder=f;}

  public String getFolderRoot() {return folderMain;}
	public String getFolderMain() {return folderMain+"data/";}
  public String getFolderGameJar() {return folderMain+"game/";}
	public void setFolderMain(String folderMain) {this.folderMain = str.sToDirectoryName(folderMain);}
  public void setFolderMain() {setFolderMain("");}
	public String getFolderStable() {return getFolderMain()+folderStable;}
	public void setFolderStable(String folderStable) {this.folderStable = str.sToDirectoryName(folderStable);}
	public String getFolderTemporary() {return getFolderMain()+folderTemporary;}
	public void setFolderTemporary(String folderTemporary) {this.folderTemporary = str.sToDirectoryName(folderTemporary);}
	public String getFolderResourcesPacks() {return getFolderMain()+folderResourcesPacks;}
	public void setFolderResourcesPacks(String folderResourcesPacks) {this.folderResourcesPacks = str.sToDirectoryName(folderResourcesPacks);}

	public String getFolderSaves() {return getFolderMain()+folderSaves;}
	public void setFolderSaves(String folderSaves) {this.folderSaves = str.sToDirectoryName(folderSaves);}
  public String getFolderBin() {return folderBin;}
	public void setFolderBin(String folderBin) {this.folderBin = str.sToDirectoryName(folderBin);}

	public String getFolderImages() {return folderImages;}
	public void setFolderImages(String folderImages) {this.folderImages = str.sToDirectoryName(folderImages);}
	public String getFolderSounds() {return folderSounds;}
	public void setFolderSounds(String folderSounds) {this.folderSounds = str.sToDirectoryName(folderSounds);}
	public String getFolderMusiques() {return folderMusiques;}
	public void setFolderMusiques(String folderMusiques) {this.folderMusiques = str.sToDirectoryName(folderMusiques);}
	public String getFolderMaps() {return folderMaps;}
	public void setFolderMaps(String folderMaps) {this.folderMaps = str.sToDirectoryName(folderMaps);}
	public String getFolderLanguages() {return folderLanguages;}
	public void setFolderLanguages(String folderLanguages) {this.folderLanguages = str.sToDirectoryName(folderLanguages);}
	public String getFolderLevels() {return folderLevels;}
	public void setFolderLevels(String folderLevels) {this.folderLevels = str.sToDirectoryName(folderLevels);}
  public String getFolderVideos() {return folderVideos;}
	public void setFolderVideos(String folderVideos) {this.folderVideos = str.sToDirectoryName(folderVideos);}

  public void setLaunchDownload(boolean b){launchDownload=b;}
  public Progression getProgression(){return progression;}

  public static boolean getFirstGame(){return firstGame;}
  public static void setFirstGame(boolean b){firstGame=b;}
  /**
  *{@summary Return version &#38; initialize it to last downloaded version if needed.}<br>
  *@lastEditedVersion 2.28
  */
  public static String getVersion(){
    if(version==null){
      version=getLastDownloadedGameVersion();
      if(version==null){
        return DEFAULT_NULL_VERSION;
      }
    }
    return version;
  }
  public static void setVersion(String version){Folder.version=version;}

  /**
  *{@summary Initialize the main folder name depending of OS.}<br>
  *@param os Os to use for name initialisation.
  *@lastEditedVersion 2.7
  */
  public void iniFolderMain(Os os){
    if(os.isWindows()){
      setFolderMain(System.getenv("APPDATA")+"/.formiko/");
    }else if(os.isLinux()){
      setFolderMain(System.getProperty("user.home")+"/.formiko/");
    }else if(os.isMac()){
      setFolderMain(System.getProperty("user.home")+"/.formiko/");
    }else{
      setFolderMain("");
    }
  }
  public void iniFolderMain(){iniFolderMain(Os.getOs());}
  /**
  *{@summary Initialize missing folder if some folder are missing.}<br>
  *It will call download if main folder is missing.<br>
  *It will send an info if some were missing and an error if some unfixable folder were missing.
  *@param allowedDownolad true if we can download files.
  *@lastEditedVersion 2.27
  */
  public int ini(boolean allowedDownolad){
    missingFolder=0;
    try{
      File fgj = new File(getFolderGameJar());
      fgj.mkdirs();
      File f = new File(getFolderMain());
      // if(newVersionAviable()){
      //   erreur.info("A new version, "+getLastStableVersion()+" is aviable at https://formiko.fr/download");
      // }
      if(!f.exists() || f.listFiles().length==0){
        setFirstGame(true);
        f.mkdirs();
        missingFolder++;
        if(allowedDownolad){throw new MissingFolderException("main");}
      }else if(needToUpdateDataVersion()){
        erreur.alerte("A compatible data version ("+getWantedDataVersion()+") is downloaded");
        if(allowedDownolad){
          downloadData();
          allowedDownolad=false;//no more try
        }
      }

      f = new File(getFolderMain()+"Options.md");
      if(!f.exists() || f.isDirectory()){
        setFirstGame(true);
      }

      f = new File(getFolderSaves());
      if(f.mkdir()){missingFolder++;}
      iniStable(allowedDownolad);
      iniTemporary();
      iniRessourcesPacks();
    }catch (MissingFolderException e) {
      if(!getFirstGame()){
        erreur.erreur("an error occured when fixing file : "+e,"Download file from main repository");
      }else{
        erreur.info("Download file from main repository");
      }
      if(allowedDownolad){downloadData();}
      if(!secondTime){
        secondTime=true;
        return ini();
      }
    }catch (Exception e) {
      erreur.erreur("an error occured when fixing file : "+e);
    }
    if(missingFolder>0){
      erreur.info(missingFolder+" folders were missing & were add.");
    }
    return missingFolder;
  }
  public int ini(){return ini(true);}
  /**
  *{@summary Delete all unnecesary folders and files.}<br>
  *@lastEditedVersion 1.46
  */
  public void cleanFolder(){
    File folder = new File(getFolderMain());
    for (File file : folder.listFiles() ) {
      if(!file.getName().equals("stable") && !file.getName().equals("README.md") && !file.getName().equals("Keys.txt")){
        fichier.deleteDirectory(file);
      }
    }
  }
  /**
  *{@summary Initialize stable missing folder.}<br>
  *@param allowedDownolad true if we can download files.
  *@lastEditedVersion 1.46
  */
  private void iniStable(boolean allowedDownolad){
    File f = new File(getFolderStable());
    if(f.mkdir() || f.listFiles().length==0){
      missingFolder++;
      if(allowedDownolad){throw new MissingFolderException("stable");}
    }
    f = new File(getFolderStable()+getFolderBin());
    if(f.mkdir() || f.listFiles().length==0){
      missingFolder++;
      if(allowedDownolad){throw new MissingFolderException("stable bin");}
    }
    f = new File(getFolderStable()+getFolderImages());
    if(f.mkdir() || f.listFiles().length==0){
      missingFolder++;
      if(allowedDownolad){throw new MissingFolderException("stable images");}
    }
    f = new File(getFolderStable()+getFolderLanguages());
    if(f.mkdir() || f.listFiles().length==0){
      missingFolder++;
      if(allowedDownolad){throw new MissingFolderException("stable language");}
    }
    f = new File(getFolderStable()+getFolderLevels());
    if(f.mkdir() || f.listFiles().length==0){
      missingFolder++;
      if(allowedDownolad){throw new MissingFolderException("stable level");}
    }
    f = new File(getFolderStable()+getFolderMaps());
    if(f.mkdir() || f.listFiles().length==0){
      missingFolder++;
      if(allowedDownolad){throw new MissingFolderException("stable maps");}
    }
    f = new File(getFolderStable()+getFolderMusiques());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderStable()+getFolderSounds());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderStable()+getFolderVideos());
    if(f.mkdir()){missingFolder++;}
  }
  /**
  *{@summary Initialize temporary missing folder.}<br>
  *@lastEditedVersion 1.37
  */
  private void iniTemporary(){
    File f = new File(getFolderTemporary());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderBin());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderImages());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderLanguages());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderLevels());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderMaps());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderMusiques());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderSounds());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderTemporary()+getFolderVideos());
    if(f.mkdir()){missingFolder++;}
  }
  /**
  *{@summary Initialize resourcesPacks missing folder.}<br>
  *@lastEditedVersion 1.37
  */
  private void iniRessourcesPacks(){
    File f = new File(getFolderResourcesPacks());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderBin());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderImages());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderLanguages());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderLevels());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderMaps());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderMusiques());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderSounds());
    if(f.mkdir()){missingFolder++;}
    f = new File(getFolderResourcesPacks()+getFolderVideos());
    if(f.mkdir()){missingFolder++;}
  }
  /**
  *{@summary Download main data from github release.}<br>
  *It need Main.version to be correct to work.<br>
  *@lastEditedVersion 2.28
  */
  public void downloadData(){
    getProgression().iniLauncher();
    launchDownload=true;
    boolean needToRetry = true;
    while(needToRetry){
      // while(!launchDownload){ // while not first time or player haven't clic on retry, wait.
      //   getProgression().setDownloadingMessage("downloading will retry in 1s");
      //   erreur.alerte("wait for retry in 1s");
      //   System.out.println("wait for retry in 1s");//@a
      //   try {
      //     synchronized (this) {
      //       wait(1000);
      //     }
      //   }catch (Exception e) {
      //     System.out.println("Fail to wait");//@a
      //     e.printStackTrace();
      //   }
      //   launchDownload=true;
      // }
      while(!launchDownload){ // while not first time or player haven't clic on retry, wait.
        try {
          synchronized (this) {
            wait();
          }
        }catch (InterruptedException e) {}
      }
      prepareDownloadData();
      Chrono.startCh();
      getProgression().setDownloadingMessage("downloading game data");
      boolean downloadWork = download("https://github.com/HydrolienF/Formiko/releases/download/"+getWantedDataVersion()+"/data.zip", getFolderRoot()+"data.zip", true, getProgression());
      Chrono.endCh("downloadData");
      if(downloadWork){
        needToRetry = !unzipAndCleanDownloadData();
      }
      launchDownload=false;
      //if everything has work as intented needToRetry=false here.
    }
    getProgression().closeLauncher();
  }
  /**
  *{@summary Do pre download action.}<br>
  *@lastEditedVersion 2.7
  */
  private void prepareDownloadData(){
    Chrono.startCh();
    getProgression().setDownloadingMessage("deleting old file");
    fichier.deleteDirectory(getFolderMain());
    getProgression().setDownloadingValue(-5);
    getProgression().setDownloadingMessage("creating main Folder");
    File f = new File(getFolderMain());
    f.mkdirs();
    Chrono.endCh("removeOldData");
    getProgression().setDownloadingValue(0);
  }
  /**
  *{@summary Do post download action.}<br>
  *@lastEditedVersion 2.7
  */
  private boolean unzipAndCleanDownloadData(){
    Chrono.startCh();
    getProgression().setDownloadingMessage("unziping game data");
    fichier.unzip(getFolderRoot()+"data.zip",getFolderRoot());
    Chrono.endCh("unzipData");
    getProgression().setDownloadingValue(105);
    System.gc();
    getProgression().setDownloadingMessage("cleaning folders");
    if(!fichier.deleteDirectory(getFolderRoot()+"data.zip")){
      erreur.alerte("unable to delete "+getFolderRoot()+"data.zip");
      return false;
    }else{
      return true;
    }
    // getProgression().setDownloadingValue(110);
  }
  /**
  *{@summary Return true if data version is outdated or overdated.}<br>
  *Each game version have 1 linked data version, so we need to know witch one.
  *Data don't need to be update as many times as game is,
  *so when a new version of data is needed it is publish in github action.
  *A new data version always take curent game version as version number.
  *@lastEditedVersion 2.7
  */
  public boolean needToUpdateDataVersion(){
    String wantedDataVersion = getWantedDataVersion();
    String curentDataVersion = getCurentDataVersion();
    if(wantedDataVersion.equals("null") || curentDataVersion.equals("null")){return false;}
    if(!wantedDataVersion.equals(curentDataVersion)){return true;}
    return false;
  }
  /**
  *{@summary Return the version that game have.}<br>
  *@lastEditedVersion 2.28
  */
  public String getCurentVersion(){
    if(getVersion()!=null && !getVersion().equals(DEFAULT_NULL_VERSION)){return getVersion();}
    GString gs = ReadFile.readFileGs(getVersionMdPath());
    if(gs.isEmpty()){
      erreur.alerte("can't read curent game version");
      return DEFAULT_NULL_VERSION;
    }
    return gs.getFirst();
  }
  /**
  *{@summary Return the curent data version that game have.}<br>
  *@lastEditedVersion 2.7
  */
  public String getCurentDataVersion(){
    return getXVersion(Paths.get(getFolderMain()+"version.json"), "data");
  }
  /**
  *{@summary Return the curent music version that game have.}<br>
  *@lastEditedVersion 2.7
  */
  public String getCurentMusicVersion(){
    return getXVersion(Paths.get(getFolderMain()+"version.json"), "music");
  }
  /**
  *{@summary Return the data version that game want to have.}<br>
  *@lastEditedVersion 2.7
  */
  public String getWantedDataVersion(){
    return getXVersion(getVersionJsonPath(), "data");
  }
  /**
  *{@summary Return the music version that game want to have.}<br>
  *@lastEditedVersion 2.7
  */
  public String getWantedMusicVersion(){
    return getXVersion(getVersionJsonPath(), "music");
  }
  /**
  *{@summary Return the last stable version downloadable on the web site.}<br>
  *@lastEditedVersion 2.7
  */
  public String getLastStableVersion(){
    String fileName = getFolderMain()+"vTemp.json";
    try {
      download("https://gist.githubusercontent.com/HydrolienF/c7dbc5d2d61b749ff6878e93afdaf53e/raw/version.json", fileName, getProgression());
      return getXVersion(Paths.get(fileName), "lastStableVersion");
    }catch (Exception e) {
      erreur.alerte("Can't read last stable version");
      return DEFAULT_NULL_VERSION;
    }finally {
      File f = new File(fileName);
      if(!fichier.deleteDirectory(f)){}
    }
  }
  /**
  *{@summary Return the version from path &#38; name of the wanted version.}<br>
  *If it fail, it will return a defaut version.
  *@param pathToJson path to the .json file taht containt version
  *@param nameOfTheVersion name of the version
  *@return a version String as 1.49.12
  *@lastEditedVersion 2.7
  */
  public String getXVersion(Path pathToJson, String nameOfTheVersion){
    try {
      // create a reader
      Reader reader = Files.newBufferedReader(pathToJson);
      // create parser
      JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
      // read customer details
      String version = (String) parser.get(nameOfTheVersion);
      if(version==null){
        erreur.alerte("can't read "+nameOfTheVersion+" version");
      }
      return version;
    }catch (Exception e) {
      erreur.alerte("can't read "+nameOfTheVersion+" version");
      return "0.0.0";
    }
  }
  /**
  *{@summary return the path to version.x.}<br>
  *@lastEditedVersion 2.26
  */
  public Path getVersionPath(String fileName){
    //if in curent folder because running with maven
    File f = new File(fileName);
    if(f.exists()){
      return Paths.get(f.getPath());
    }
    // else it is in getFolderGameJar
    f = new File(getFolderGameJar()+getVersion()+"/"+fileName);
    erreur.info("Searching for "+f);
    if(f.exists()){
      return Paths.get(f.getPath());
    }
    // //else search depending of OS
    if(Os.getOs().isWindows()){
      f = new File(System.getenv("ProgramFiles")+"/Formiko/app/"+fileName);
      if(f.exists()){
        return Paths.get(f.getPath());
      }
    }else if(Os.getOs().isMac()){
      f = new File("/Applications/Formiko.app/Contents/app/"+fileName);
      if(f.exists()){
        return Paths.get(f.getPath());
      }
    }else{
      f = new File("/opt/formiko/lib/app/"+fileName);
      if(f.exists()){
        return Paths.get(f.getPath());
      }
    }
    // //last try just in case
    // f = new File("app/"+fileName);
    // if(f.exists()){
    //   return Paths.get(f.getPath());
    // }
    erreur.alerte("Can't fined "+fileName+" path");
    return Paths.get("");
  }
  /**
  *{@summary return the path to version.md.}<br>
  *Curent version is in version.md.
  *@lastEditedVersion 2.10
  */
  public Path getVersionMdPath(){
    return getVersionPath("version.md");
  }
  /**
  *{@summary return the path to version.json.}<br>
  *@lastEditedVersion 2.10
  */
  public Path getVersionJsonPath(){
    return getVersionPath("version.json");
  }
  /**
  *{@summary Download music data from github release.}<br>
  *It need Main.version to be correct to work.<br>
  *@lastEditedVersion 1.53
  */
  public void downloadMusicData(MusicPlayer mp){
    Thread th = new ThDownloadMusicData(this, mp, getProgression());
    th.start();
  }
  // /**
  // *{@summary Return true if we need to download the musics files.}<br>
  // *We need to download the music files if 1 or more is missing.<br>
  // *@lastEditedVersion 2.29
  // */
  // public boolean needToDownloadMusic(){
  //   int cpt = fichier.countEntryOfZipFile("https://github.com/HydrolienF/Formiko/releases/download/"+getWantedMusicVersion()+"/music.zip");
  //   File f = new File(getFolderStable()+getFolderMusiques());
  //   if(!f.exists()){return true;}
  //   erreur.info("needToDownloadMusic: "+cpt+" > "+f.list().length+" ?");//@a
  //   return cpt > f.list().length;
  // }
  /**
  *{@summary Download a file from the web.}<br>
  * It also update progression.
  *@param urlPath the url as a String
  *@param fileName the name of the file were to save data from the web
  *@param withInfo if true launch a thread to have info during download
  *@param progression the primitive view to update progression
  *@lastEditedVersion 2.25
  */
  public static boolean download(String urlPath, String fileName, boolean withInfo, Progression progression){
    boolean worked=false;
    if(progression!=null){
      progression.setButtonRetryVisible(false);
    }
    try {
      fichier.setProgression(progression);
      worked=fichier.download2(urlPath,fileName,withInfo);
    }catch (Exception e) {
      String err = "Download fail: "+e;
      try {
        progression.setDownloadingMessage(err);
        progression.setButtonRetryVisible(true);
      }catch (Exception e2) {
        erreur.erreur(err);
      }
      return worked;
    }
    return worked;
  }
  /***
  *{@summary Download a file from the web.}<br>
  * It also update progression.
  *@param urlPath the url as a String
  *@param fileName the name of the file were to save data from the web
  *@param progression the primitive view to update progression
  *@lastEditedVersion 2.25
  */
  public static boolean download(String urlPath, String fileName, Progression progression){return download(urlPath, fileName, false, progression);}
  /**
  *{@summary Download &#38; unzip a .zip from the web.}<br>
  * It also update progression.
  *@param urlPath the url as a String
  *@param folderName the name of the folder were to save data from the web
  *@param withInfo if true launch a thread to have info during download
  *@lastEditedVersion 2.26
  */
  public boolean downloadAndUnzip(String urlPath, String folderName, boolean withInfo){
    // TODO use fichier.downloadAndUnzip
    boolean downloadWork=download(urlPath, getFolderRoot()+"temp.zip", withInfo, getProgression());
    if(!downloadWork){return downloadWork;}
    getProgression().setDownloadingMessage("unziping");
    fichier.unzip(getFolderRoot()+"temp.zip", folderName);
    getProgression().setDownloadingValue(105);
    // System.gc();
    getProgression().setDownloadingMessage("cleaning folders");
    if(!fichier.deleteDirectory(getFolderRoot()+"temp.zip")){
      erreur.alerte("unable to delete zip file "+getFolderRoot()+"temp.zip");
      return false;
    }else{
      return true;
    }
  }
  //tools
  /**
  *{@summary Test if arrays have version.}<br>
  *@param t the arrays where to find version
  *@param version the version tofind
  *@lastEditedVersion 2.26
  */
  public static boolean containsVersion(String t[], String version){
    for (String s : t) {
      if(s.contains(version)){return true;}
    }
    return false;
  }
  /**
  *{@summary Return the last version currently on the computer.}<br>
  *@lastEditedVersion 2.26
  */
  public static String getLastDownloadedGameVersion(){
    File gameJarFolder = new File(getFolder().getFolderGameJar());
    if(gameJarFolder.list().length>0){
      List<Version> versions = new ArrayList<Version>();
      for (String folderV : gameJarFolder.list()) {
        if(folderV.equals("JRE")){continue;}
        try{
          versions.add(new Version(folderV));
        }catch (IllegalArgumentException e) {
          erreur.alerte("Folder "+folderV+" can't be used as a version folder");
        }
      }
      return Collections.max(versions).get();
    }else{
      return null;
    }
  }
  /**
  *{@summary Return true if user have last version.}<br>
  *@lastEditedVersion 2.27
  */
  public boolean haveLastVersion(){
    String ldv = getLastDownloadedGameVersion();
    if(ldv==null){return false;}
    return ldv.equals(getLastStableVersion());
  }
}
/**
*{@summary Download music data from github release in a Thread.}<br>
*It need Main.version to be correct to work.<br>
*@lastEditedVersion 1.53
*@author Hydrolien
*/
class ThDownloadMusicData extends Thread {
  private Folder folder;
  private MusicPlayer mp;
  private Progression progression;
  /**
  *{@summary Main constructor.}<br>
  *@lastEditedVersion 1.53
  */
  public ThDownloadMusicData(Folder f, MusicPlayer mp, Progression progression){
    this.mp=mp;
    this.folder=f;
    this.progression=progression;
  }
  /**
  *{@summary Download music data from github release.}<br>
  *@lastEditedVersion 2.29
  */
  @Override
  public void run(){
    erreur.info("downloadMusicData");
    Chrono.startCh();
    fichier.downloadAndUnzip("https://github.com/HydrolienF/Formiko/releases/download/"+folder.getWantedMusicVersion()+"/music.zip", folder.getFolderStable()+folder.getFolderMusiques(), "musics/");
    Chrono.endCh("downloadMusicData");
    mp.iniAvailableMusics();
  }
}
