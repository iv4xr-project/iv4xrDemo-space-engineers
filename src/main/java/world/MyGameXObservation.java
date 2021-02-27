package world;

import java.io.Serializable;
import java.util.function.Function;

import eu.iv4xr.framework.spatial.Vec3;


/**
 * The observation JSON generated in Observation.cs in the game.
 */
public class MyGameXObservation {

    public static MyGameXWorldModel toWorldModel(MyGameXObservation obs) {
    	throw new UnsupportedOperationException() ;
    }
    
    // if IDs need to be generated....
    static String constructId(MyGameXEntity obj) {
    	throw new UnsupportedOperationException() ;
    }

    public static MyGameXEntity toWorldEntity(MyGameXEntity obj) {
    	throw new UnsupportedOperationException() ;
    }
}