package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.*;

/**
 * Class Triangle is the Geometry class representing a Triangle shape in space
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
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

    @Override
    public List<Point3D> findIntersections(Ray ray) {

        List<Point3D> tentativeIntersection = plane.findIntersections(ray);
        // if we do not intersect with plane we can not possibly intersect the triangle.
        if (tentativeIntersection == null) {
            return null;
        }

        // algorithm to test if given point P we got from plane findIntersections is
        // inside the triangle.
        Point3D p0 = ray.getP0();
        Vector v = ray.getDir();
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        Vector[] crossVectors = { v1.crossProduct(v2).normalize(), v2.crossProduct(v3).normalize(),
                v3.crossProduct(v1).normalize() };

        int numOfPositiveNumbers = 0;
        for (Vector vector : crossVectors) {
            double vn = v.dotProduct(vector);
            if (isZero(vn)) {
                return null;
            }
            if (vn > 0) {
                numOfPositiveNumbers++;
            }
        }
        // if numOfPositiveNumbers is not 0 or 3 it's mean there is at least 1 number
        // with odd sign.
        if (numOfPositiveNumbers != 0 && numOfPositiveNumbers != 3) {
            return null;
        }

        return tentativeIntersection;
    }
}
