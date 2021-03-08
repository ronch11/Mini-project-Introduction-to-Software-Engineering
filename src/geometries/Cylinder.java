package geometries;

import primitives.Ray;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Cylinder extends Tube {
    double height;

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
