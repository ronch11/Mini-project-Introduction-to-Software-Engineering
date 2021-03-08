package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Tube implements Geometry {
    Ray axisRay;
    double radius;

    public Tube(Ray ray, double radius) {
        this.axisRay = ray;
        this.radius = radius;
    }

    /**
     * Getter for internal field axisRay.
     * 
     * @return Ray - This Tube axisRay.
     */
    public Ray getAxisRay() {
        return axisRay;
    }

    /**
     * Getter for internal field radius.
     * 
     * @return double - This Tube radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * 
     * 
     * @param point3d
     * @return Vector
     */
    @Override
    public Vector getNormal(Point3D point3d) {
        return null;
    }

}
