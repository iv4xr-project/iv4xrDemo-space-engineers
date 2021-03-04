package world;

import java.util.Collection;
import java.util.HashSet;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Box;
import eu.iv4xr.framework.spatial.Line;
import eu.iv4xr.framework.spatial.LineIntersectable;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.mainConcepts.Environment;

public class MyGameXEntity extends WorldEntity implements LineIntersectable{
	
	// entity types
	public static final String DOOR = "Door" ;
	public static final String VOXEL = "VOXEL" ; // generic unpassable voxel
	public static final String SWITCH = "Switch" ;
	
	public MyGameXEntity(String id, String type, boolean dynamic) {
		super(id,type,dynamic) ;
	}
	
	/** 
	 * Return the center position of the entity, with the y-position shifted to the floor level.
	 */
	public Vec3 getFloorPosition() {
		return new Vec3(position.x,position.y -  extent.y, position.z) ;
	}
	
	@Override
	public Collection<Vec3> intersect(Line l) {
		// only these types can block movements:
		if (type.equals(VOXEL)) {
			// use a box to calculate the intersection with this door :D .. stretch the extent a bit larger
			Box box = new Box(this.getFloorPosition(),Vec3.add(Vec3.mul(this.extent,2f), new Vec3(0.1f,0,0.1f))) ;
			var intersections = box.intersect(l) ;
			//System.out.println(">>> " + intersections) ;
			return intersections ;
		}
		else 
			return new HashSet<>() ;
	}
	
}
