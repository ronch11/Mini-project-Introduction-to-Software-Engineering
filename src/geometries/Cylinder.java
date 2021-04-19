package geometries;

import primitives.Ray;

/**
 * Class Cylinder is the Geometry class representing a Cylinder shape in space
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Cylinder extends Tube {
    private double height;

    /**
     * Constructor for building Cylinder object
     * 
     * @param ray    - The axis Ray of the Tube.
     * @param radius - The radius of the tube's circle(side cute pane).
     * @param height - The height of the Cylinder from base to base.
     */
    public Cylinder(Ray ray, double radius, double height) {
        super(ray, radius);
        this.height = height;
    }

    /**
     * Getter for the internal field height.
     * 
     * @return double - this Cylinder height.
     */
    public double getHeight() {
        return height;
    }

}
