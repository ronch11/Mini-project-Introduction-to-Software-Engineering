package geometries;

import java.util.ArrayList;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Class Sphere is the Geometry class representing a Sphere shape in space of
 * Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Sphere implements Geometry {

    private Point3D center;
    private double radius;

    /**
     * Constructor for building Sphere object.
     * 
     * @param point  - The Center of the sphere(Point3D).
     * @param radius - The radius of the sphere(double).
     */
    public Sphere(Point3D point, double radius) {
        this.center = point;
        this.radius = radius;
    }

    /**
     * Getter for internal field Center
     * 
     * @return Point3D - The Center Point of this Sphere.
     */
    public Point3D getCenter() {
        return center;
    }

    /**
     * Getter for internal field radius
     * 
     * @return double - the radius of this Sphere.
     */
    public double getRadius() {
        return radius;
    }

    @Override
    public Vector getNormal(Point3D point3d) {
        return point3d.subtract(center).normalize();
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        double tM, d;
        try {
            Vector u = center.subtract(ray.getP0());
            tM = alignZero(ray.getDir().dotProduct(u));
            d = alignZero(Math.sqrt(u.lengthSquared() - tM * tM));
        } catch (IllegalArgumentException e) {
            tM = 0;
            d = 0;
        }

        if (alignZero(d - radius) == 0) {
            return null;
        }

        double tH = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tM + tH);
        double t2 = alignZero(tM - tH);

        if (t1 > 0) {
            List<Point3D> tentativeIntersections = new ArrayList<>();
            tentativeIntersections.add(ray.getPoint(t1));
            if (t2 > 0) {
                tentativeIntersections.add(ray.getPoint(t2));
            }
            return tentativeIntersections;

        } else {
            if (t2 > 0) {
                List<Point3D> tentativeIntersections = new ArrayList<>();
                tentativeIntersections.add(ray.getPoint(t2));
                return tentativeIntersections;
            }
        }
        return null;
    }

}
