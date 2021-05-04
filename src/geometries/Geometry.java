package geometries;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Geometry is the common abstract class for all Geometry classes to implement.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public abstract class Geometry implements Intersectable {

    protected Color emissions = Color.BLACK;

    /**
     * Getter for protected field emissions
     * 
     * @return
     */
    public Color getEmissions() {
        return emissions;
    }

    /**
     * A setter using builder pattern for protected field emissions
     * 
     * @param emissions
     * @return
     */
    public Geometry setEmissions(Color emissions) {
        this.emissions = emissions;
        return this;
    }

    /**
     * A function that return unit vector (length equal to 1) of perpendicular
     * vector to the Geometry shape and the given point in @param point3d
     * 
     * @param point3d - point on the shape surface
     * @return - A perpendicular unit vector to the shape in the point.
     */
    public abstract Vector getNormal(Point3D point3d);
}
