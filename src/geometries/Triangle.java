package geometries;

import static primitives.Util.*;

import java.util.List;

import primitives.*;

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
    public List<GeoPoint> calculateGeoIntersection(Ray ray) {

        List<GeoPoint> tentativeIntersection = plane.findGeoIntersections(ray);
        // if we do not intersect with plane we can not possibly intersect the triangle.
        if (tentativeIntersection == null)
            return null;

        // algorithm to test if given point P we got from plane findIntersections is
        // inside the triangle.
        Point3D p0 = ray.getP0();
        Vector v = ray.getDir();
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        double s1 = v.dotProduct(v1.crossProduct(v2));
        double s2 = v.dotProduct(v2.crossProduct(v3));
        if (alignZero(s1 * s2) <= 0)
            return null;
        double s3 = v.dotProduct(v3.crossProduct(v1));
        if (alignZero(s1 * s3) <= 0)
            return null;

        tentativeIntersection.get(0).geometry = this;
        return tentativeIntersection;
    }
}
