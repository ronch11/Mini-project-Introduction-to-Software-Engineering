package geometries;

import primitives.Point3D;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Triangle extends Polygon {
    /**
     * Constructor for building Triangle object.
     * 
     * @param p1 - First Point3D in triangle.
     * @param p2 - Second Point3D in triangle.
     * @param p3 - Triangle Point3D in triangle.
     */
    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        super(p1, p2, p3);
    }
}
