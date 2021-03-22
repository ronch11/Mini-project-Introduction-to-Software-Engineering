package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
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
        return point3d.subtract(this.center).normalize();
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        // TODO Auto-generated method stub
        return null;
    }

}
