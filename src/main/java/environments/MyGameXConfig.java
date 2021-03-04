package environments;

import helperclasses.Util;
import nl.uu.cs.aplib.utils.Pair;
import game.Platform;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This defines some configuration parameters for the Lab-Recruits game, as far
 * as they can be configured. This configuration will be sent to the Lab Recruits
 * when we connect iv4xr Environment to it.
 */
public class MyGameXConfig {

    // will not be sent over to Unity
    public transient String host = "localhost";
    public transient int port = 8053;

    // the save-game-file to load
    public String gamesaveFileName = "";
    public String gamesavesFolder = "";
    
    // configurations
    public int seed = 1;

    public float agent_speed = 0.13f;
    public float npc_speed = 0.11f;
    public float fire_spread = 0.02f;
    public float jump_force = 0.18f;
    public float view_distance = 10f;
    public float light_intensity = 1f; 

    /**
     * Return a default configuration. The path to the level-file and the name of the level
     * are left blank though.
     */
    public MyGameXConfig(){}
    
    /**
     * Create a default configuration, with the given level-name. The level-file is assumed
     * to be stored in the standard location defined by Platform.LEVEL_PATH (which points to
     * projectdir/src/test/resources/levels".
     */
    public MyGameXConfig(String levelName){
    	useSaveFile(levelName, Platform.LEVEL_PATH);
    }

    /**
     * Create a default configuration, with the given level-name. The level-file is assumed
     * to be stored in the given folder-path.
     */ 
    public MyGameXConfig(String gamesaveFileName, String folder){
    	useSaveFile(gamesaveFileName, folder);
    }

    private MyGameXConfig useSaveFile(String gamesaveFileName, String folder){
    	String gameSaveExtension = ".hahaha" ; // fill in the correct extension
    	if(true)
    		throw new UnsupportedOperationException() ;
        String fullPath = Paths.get(folder, gamesaveFileName + gameSaveExtension).toAbsolutePath().toString();
        Util.verifyPath(fullPath);
        this.gamesaveFileName = folder;
        this.gamesavesFolder = gamesaveFileName;
        return this;
    }
   
}
