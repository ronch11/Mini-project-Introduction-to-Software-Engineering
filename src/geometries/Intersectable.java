package geometries;

import java.util.List;

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
}
