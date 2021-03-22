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
    protected Ray axisRay;
    protected double radius;

    /**
     * Constructor for building Tube Object.
     * 
     * @param ray    - The axis Ray of the Tube.
     * @param radius - The radius of the tube's circle(side cute pane).
     */
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
     * Calculating Normal to the Tube in a given point(need to be on the surface of
     * the Tube).
     * 
     * @param point3d - A Point3D on the surface.
     * @return Vector - unit Vector(length of 1) that is orthogonal to the point.
     */
    @Override
    public Vector getNormal(Point3D point3d) {
        double t = this.axisRay.getDir().dotProduct(point3d.subtract(this.axisRay.getP0()));
        Point3D projection = this.axisRay.getP0().add(this.axisRay.getDir().scale(t));
        return point3d.subtract(projection).normalize();
    }

}
