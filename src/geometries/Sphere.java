package geometries;

import primitives.Point3D;
import primitives.Vector;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Sphere implements Geometry {

    Point3D center;
    double radius;

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

    /**
     * Calculating Normal to the Sphere in a given point(need to be on the surface
     * of the Sphere).
     * 
     * @param point3d - A Point3D on the surface.
     * @return Vector - unit Vector(length of 1) that is orthogonal to the point.
     */
    @Override
    public Vector getNormal(Point3D point3d) {
        return null;
    }

}
