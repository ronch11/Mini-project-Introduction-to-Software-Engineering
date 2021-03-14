package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Plane implements Geometry {
    Point3D q0;
    Vector normal;

    /**
     * Constructor for Building Plane object
     * 
     * @param point  - A Point3D for indicating the plane using Vector and a point.
     * @param vector - A Vector for indicating the plane using Vector and a point.
     */
    Plane(Point3D point, Vector vector) {
        this.q0 = point;
        this.normal = vector.normalized();
    }

    /**
     * Constructor for Building Plane object
     * 
     * @param p1 - A Point3D for indicating the plane using Vector and a point(base
     *           point).
     * @param p2 - A Point3D for creating Vector between it and the base
     *           point @param p1.
     * @param p3 - A Point3D for creating Vector between it and the base
     *           point @param p1.
     */
    Plane(Point3D p1, Point3D p2, Point3D p3) {
        this.q0 = p1;
        this.normal = null;
    }

    /**
     * Calculating Normal to the Plane in a given point(need to be on the surface of
     * the Plane).
     * 
     * @param point3d - A Point3D on the surface.
     * @return Vector - unit Vector(length of 1) that is orthogonal to the point.
     */
    @Override
    public Vector getNormal(Point3D point3d) {
        return null;
    }

    /**
     * Calculating Normal to the Plane in a given point(need to be on the surface of
     * the Plane). (using the base point of the plane as point for the calculation)
     * 
     * @return Vector - unit Vector(length of 1) that is orthogonal to the point.
     */
    public Vector getNormal() {
        return getNormal(q0);
    }
}
