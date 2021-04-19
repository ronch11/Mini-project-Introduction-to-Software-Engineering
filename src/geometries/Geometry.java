package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Interface Geometry is the common interface for all Geomtry classes to
 * implement.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public interface Geometry extends Intersectable {

    /**
     * A function that return unit vector (length equal to 1) of perpendicular
     * vector to the Geometry shape and the given point in @param point3d
     * 
     * @param point3d - point on the shape surface
     * @return - A perpendicular unit vector to the shape in the point.
     */
    public Vector getNormal(Point3D point3d);
}
