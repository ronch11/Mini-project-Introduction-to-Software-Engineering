package geometries;

import java.util.List;
import java.util.Objects;

import primitives.Point3D;
import primitives.Ray;

/**
 * Interface for all shapes that can be intersected with Rays.
 */
public interface Intersectable {
    /**
     * find all intersection (if they exists) between a Ray and A Geometry Shape.
     * 
     * @param ray - a Ray that try to find intersection with the shape.
     * @return List Point3D - intersections points if they exists or Null (0
     *         intersection Points).
     */
    public List<Point3D> findIntersections(Ray ray);


    public List<GeoPoint> findGeoIntersections(Ray ray);

    /**
     * A helper class for intersectable interface. giving back a pair of geometry
     * shape and point on it.
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point3D point;

        /**
         * A constructor for GeoPoint.
         * 
         * @param geometry Geometry - A geometric shape that implement abstract class
         *                 Geometry.
         * @param point    Point3D - A point that we want to relate/ check relations
         *                 with the geometry.
         */
        public GeoPoint(Geometry geometry, Point3D point) {
            this.geometry = geometry;
            this.point = point;
        }

        /**
         * a equal override the two object will be equal if: 1. it's same object. 2.
         * they have the same geometry object and the two points are equal(not
         * necessarily the same object).
         */
        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof GeoPoint)) {
                return false;
            }
            GeoPoint geoPoint = (GeoPoint) o;
            return geometry == geoPoint.geometry && Objects.equals(point, geoPoint.point);
        }
    }
}
