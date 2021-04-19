package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Class Plane is the Geometry class representing a Plane shape in space of
 * Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Plane implements Geometry {
    private Point3D q0;
    private Vector normal;

    /**
     * Constructor for Building Plane object
     * 
     * @param point  - A Point3D for indicating the plane using Vector and a point.
     * @param vector - A Vector for indicating the plane using Vector and a point.
     */
    public Plane(Point3D point, Vector vector) {
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
     * @throws IllegalArgumentException - in case of parallel vectors created in the
     *                                  equation v1 = p2-p1 and v2 = p3-p1.
     */
    Plane(Point3D p1, Point3D p2, Point3D p3) {
        this.q0 = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Getter for the private field q0.
     * 
     * @return Point3D - q0 (the base point used to describe the plane).
     */
    public Point3D getQ0() {
        return q0;
    }

    /**
     * Getter for the private field normal
     * 
     * @return Vector - unit Vector(length of 1) that is orthogonal to the plane.
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public Vector getNormal(Point3D point3d) {
        return this.normal;
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        double nv = this.normal.dotProduct(ray.getDir());
        if (isZero(nv) || ray.getP0() == this.q0) {
            return null;
        }
        Vector vec = q0.subtract(ray.getP0());

        double nQMinusP0 = normal.dotProduct(vec);
        double t = alignZero(nQMinusP0 / nv);
        if (t > 0) {
            return List.of(ray.getPoint(t));
        }
        return null;

    }
}
