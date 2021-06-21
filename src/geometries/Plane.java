package geometries;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class Plane is the Geometry class representing a Plane shape in space of
 * Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Plane extends Geometry {
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
        q0 = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        normal = v1.crossProduct(v2).normalize();
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
        return normal;
    }

    @Override
    public AABB getAABB() {
        // base of 2 that span the plane in 3D Cartesian 3-Dimensional coordinate
        // system.
        Vector base1 = normal.orthogonalVector();
        Vector base2 = normal.crossProduct(base1).normalize();

        Point3D head1 = base1.getHead();
        Point3D head2 = base2.getHead();

        double minX, maxX, minY, maxY, minZ, maxZ;

        if (head1.getX() + head2.getX() == 0) {
            minX = maxX = 0;
        } else {
            minX = Double.MIN_VALUE;
            maxX = Double.MAX_VALUE;
        }

        if (head1.getY() + head2.getY() == 0) {
            minY = maxY = 0;
        } else {
            minY = Double.MIN_VALUE;
            maxY = Double.MAX_VALUE;
        }

        if (head1.getZ() + head2.getZ() == 0) {
            minZ = maxZ = 0;
        } else {
            minZ = Double.MIN_VALUE;
            maxZ = Double.MAX_VALUE;
        }
        return new AABB(new Point3D(minX, minY, minZ), maxX - minX, maxY - minY, maxZ - minZ);
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        double nv = alignZero(normal.dotProduct(ray.getDir()));
        if (isZero(nv))
            return null;
        Vector vec;
        try {
            vec = q0.subtract(ray.getP0());
        } catch (IllegalArgumentException e) {
            return null;
        }

        double nQMinusP0 = alignZero(normal.dotProduct(vec));
        double t = alignZero(nQMinusP0 / nv);
        return t <= 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
    }
}
