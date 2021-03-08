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

    Plane(Point3D point, Vector vector) {
        this.q0 = point;
        this.normal = vector.normalized();
    }

    Plane(Point3D p1, Point3D p2, Point3D p3) {
        this.q0 = p1;
        this.normal = null;
    }

    /**
     * @param point3d
     * @return Vector
     */
    @Override
    public Vector getNormal(Point3D point3d) {
        return null;
    }

    /**
     * @return Vector
     */
    public Vector getNormal() {
        return getNormal(q0);
    }
}
