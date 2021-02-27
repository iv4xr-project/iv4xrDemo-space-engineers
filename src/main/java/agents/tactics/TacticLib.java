/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package agents.tactics;

import static nl.uu.cs.aplib.AplibEDSL.*;
import nl.uu.cs.aplib.mainConcepts.Action;
import nl.uu.cs.aplib.mainConcepts.Tactic;
import nl.uu.cs.aplib.multiAgentSupport.Acknowledgement;
import nl.uu.cs.aplib.multiAgentSupport.Message;
import nl.uu.cs.aplib.agents.MiniMemory;
import nl.uu.cs.aplib.utils.Pair;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import eu.iv4xr.framework.spatial.meshes.Face;

import world.*;

import java.util.*;
import java.util.stream.Collectors;

public class TacticLib {


	/**
	 * When the agent comes to this distance to the current exploration target,
	 * the target is considered as achieved (and the agent may then move to
	 * the next exploration target).
	 */
	public static final float EXPLORATION_TARGET_DIST_THRESHOLD = 0.5f ;


	/**
	 * Threshold on the distance between a point to a surface to determine when
	 * the point is on the surface. This is used by the unstuck tactic to check
	 * that an unstucking proposal is still on the navigable surface,
	 */
	public static final float DIST_SURFACE_THRESHOLD_STUCK = 0.045f;


	/**
	 * The same as {@link rawNavigateTo}, but the constructed tactic will also try
	 * to get the agent unstucked if it thinks it has become stuck. So, the method
	 * constructs a tactic T that will drive the agent to move towards a given
	 * position using a path planning algorithm. The tactic T is enabled (more
	 * precisely: the action that underlies T) if the agent BELIEVES that there is a
	 * path (through the navigation-graph maintained by the agent) to the entity.
	 * Note that agents' view are limited, so what they believe may not be what
	 * reality is. In other words, the position might actually be unreachable. As
	 * the agent tries to reach it, eventually it will discover that it is
	 * unreachable though, after which the T will no longer be enabled.
	 *
	 * Similarly, the position might actually be reachable, but the agent believes
	 * it is not, and therefore T will not be enabled. In this case you will first
	 * need to make the agent explore so that it can update itself with a more
	 * recent information from which the agent can infer that the position is
	 * reachable.
	 *
	 * This tactic will try to detect if the agent has become stuck and try to
	 * unstuck it.
	 */
	public static Tactic navigateTo(Vec3 position) {
		return FIRSTof(
				 //forceReplanPath(),
				 //tryToUnstuck(),
				 rawNavigateTo(position)
			   )  ;
	}


	/**
	 * To navigate to the location of an in-game element. Be mindful that the destination
	 * location is literally the location of the given game element. E.g. if it is a
	 * closed door, trying to literally get to its position won't work, since that position
	 * is blocked.
	 */
	public static Tactic navigateTo(String id) {
		return FIRSTof(
				 //forceReplanPath(),
				 //tryToUnstuck(),
				 rawNavigateTo(id)
			   )  ;
	}

	/**
     * A tactic to navigate to the given entity's location. The tactic is enabled if
     * the agent believes the entity exists and is reachable. Else the tactic is NOT
     * enabled.
     */
    public static Tactic rawNavigateTo(String id) {

    	// let's just reuse rawNavigateTo_(..), and then we replace its guard:

    	Action move = unguardedNavigateTo("Navigate to " + id)
    			      // replacing its guard with this new one:
		              . on((MyAgentBeliefState belief) -> {
		                	var e = (MyGameXEntity) belief.worldmodel.getElement(id) ;
		    			    if (e==null) return null ;
		    			    var p = e.getFloorPosition() ;
		    			    // find path to p, but don't force re-calculation
		    			    return belief.findPathTo(p,false) ;
		                }) ;
    	
    	return move.lift() ;
    }
     

	/**
	 * Construct a tactic T that will drive the agent to move towards a given
	 * position using a path planning algorithm. The tactic T is enabled (more
	 * precisely: the action that underlies T) if the agent BELIEVES that there is a
	 * path (through the navigation-graph maintained by the agent) to the entity;
	 * otherwise the tactic is NOT enabled.
	 *
	 * Note that agents' view are limited, so what they believe may not be what
	 * reality is. In other words, the position might actually be unreachable. As
	 * the agent tries to reach it, eventually it will discover that it is
	 * unreachable though, after which the T will no longer be enabled.
	 *
	 * Similarly, the position might actually be reachable, but the agent believes
	 * it is not, and therefore T will not be enabled. In this case you will first
	 * need to make the agent explore so that it can update itself with a more
	 * recent information from which the agent can infer that the position is
	 * reachable.
	 *
	 * This tactic will not try to detect if the agent has become stuck.
	 */
    public static Tactic rawNavigateTo(Vec3 position) {
    	return unguardedNavigateTo("Navigate to " + position)
    		   . on((MyAgentBeliefState belief) -> {
    			    // Check if a path to the position can be found; use the flag "false"
    			    // so as not to force repeated recalculation of the reachability.
    			    // The fragment below will check if the given position is already
    			    // memorized as a goal-location; is so, no path will be calculated,
    			    // we will instead just use the path already memorized.
    			    // Ortherwise, a path is calculated, and the effect part above will
    			    // memorized it.

    			    // If no path can be found, this guard returns null... hence disabled.
    			    return belief.findPathTo(position, false) ;
    			 })
    		   . lift() ;
    }

    /**
     * This action will in principle drive the agent towards a previously memorized 
     * goal-location (a position in the world), along a previously memorized path. 
     * The exact behavior is controlled
     * by what its guard passes/propagates to it. The given guard below is a dummy guard which is
     * always enabled and simply passes the pair (null,null),which will lead to the above
     * behavior. This guard should be replaced when using this action.
     *
     * If the guard propagates (d,path) where d is a destination and p is a non-null path,
     * this pair will be memorized as the new goal-location/path pair for the agent
     * to follow.
     *
     * If the guard propagates (*,null) the action will stick to the currently memorized
     * goal-location/path.
     *
     * IMPORTANT: this action will still try to move the agent to the goal-location, even
     * when it is already there. A higher level reasoning of the agent should decide
     * that whether it wants to stop this stuttering, and how (e.g. by imposing a guard
     * that prevents the stuttering, or by clearing the memorized goal-location.
     */
    private static Action unguardedNavigateTo(String actionName) {
    	Action move = action(actionName)
                .do2((MyAgentBeliefState belief) -> (Pair<Vec3,List<Vec3>> q)  -> {
                	// q is a pair of (distination,path). Passing the destination is not necessary
                	// for this tactic, but it will allows us to reuse the effect
                	// part for other similar navigation-like tactics
                	var destination = q.fst ;
                	var path = q.snd ;

                	//if a new path is received, memorize it as the current path to follow:
                	if (path!= null) {
                		belief.applyPath(belief.worldmodel.timestamp, destination, path) ;
                	}
                	if (belief.getMemorizedPath() != null) {
                		belief.worldmodel.moveToward(belief.env(),belief.getCurrentWayPoint());
                		return belief ;
                	}
                	else return null ;
                    })
                // a dummy guard; override this when using this action:
                .on((MyAgentBeliefState belief) -> new Pair(null,null)) ;

    	return move ;
    }


    /**
     * Send an interact command if the agent is close enough.
     * @param objectID The id of the in-game entity to interact with
     * @return A tactic in which the agent will interact with the object
     */
    public static Tactic interact(String objectID) {
        Tactic interact = action("Interact")
               . do2((MyAgentBeliefState belief) -> (WorldEntity e) -> {
                	  var obs = belief.worldmodel.interact(belief.env(), MyGameXWorldModel.INTERACT, e)  ;
                	  // force update to worldmodel:
                	  //System.out.println("## interacted with " + objectID) ;
                	  belief.mergeNewObservationIntoWOM(obs);
                      return belief;
                    })
               . on((MyAgentBeliefState belief) -> {
                	var e = belief.worldmodel.getElement(objectID) ;
                	//System.out.println(">>>> " + objectID + ": " + e) ;
                	if (e==null) return null ;
                	// System.out.println(">>>>    dist: " + Vec3.dist(belief.worldmodel.getFloorPosition(),e.getFloorPosition())) ;

                	if (belief.worldmodel.canInteract(MyGameXWorldModel.INTERACT, e)) {
                		return e ;
                	}
                	//System.out.println(">>> cannot interact with " + e.id) ;
            		//System.out.println("    Agent pos: " + belief.worldmodel.getFloorPosition()) ;
            		//System.out.println("    Entity pos:" + e.getFloorPosition()) ;
            		//System.out.println("    Entity extent:" + e.extent) ;
            		
                	return null ;
                    })
               . lift();
        return interact;
    }

    /*
     * This method will return an observe tactic which will do nothing but receive an
     * observation and update the agent
     * @return A do nothing action
     */
    public static Tactic observe() {
        //this is a wait action which will allow the agent to retrieve an observation
        Tactic observe = action("Observe")
                .do1((MyAgentBeliefState belief) -> {
                	// var obs = belief.worldmodel.observe(belief.env());
                	// force wom update:
                	// belief.mergeNewObservationIntoWOM(obs) ;

                	// agent-runtime already performs update at the start of a cyle, so we just return
                	// the resulting current belief:
                    return belief;
                }).lift();
        return observe;
    }

/*
    public static Tactic observeOnce() {
        //this is a wait action which will allow the agent to retrieve an observation
        Tactic observe = action("Observe once")
                .do1((BeliefState belief) -> {
                	LabWorldModel o = belief.env().observe(belief.id);
                    belief.updateBelief(o);
                    return belief;
                }).on((BeliefState b) -> !b.worldmodel.didNothingPreviousGameTurn).lift();
        return observe;
    }
    */

}
