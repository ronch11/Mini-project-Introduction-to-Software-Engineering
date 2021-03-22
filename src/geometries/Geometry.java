package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public interface Geometry {

    /**
     * A function that return unit vector (length equal to 1) of perpendicular
     * vector to the Geometry shape and the given point in @param point3d
     * 
     * @param point3d - point on the shape surface
     * @return - A perpendicular unit vector to the shape in the point.
     */
    public Vector getNormal(Point3D point3d);
}
