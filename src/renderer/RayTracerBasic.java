package renderer;

import java.util.List;

import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * A Basic Ray tracing implementation.
 */
public class RayTracerBasic extends RayTracerBase {

    /**
     * A Constructor for the basic Ray Tracing object.
     * 
     * @param scene Scene - The scene we should work with.
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Calculate what color the intersection of Ray with scene should be.
     * 
     * @param ray Ray - A Ray that need to be checked against the scene.
     * @return Color - The Color of the point in the scene or the background color
     *         if Ray does not intersect any shape in the scene.
     */
    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> points = scene.geometries.findGeoIntersections(ray);
        GeoPoint closest = ray.findClosestGeoPoint(points);
        return calcColor(closest);
    }

    /**
     * Calculate what color the point3D has according to the scene.
     * 
     * @param closest Point3D - A point in the scene or null.
     * @return Color - The Color of the point in the scene or the background color
     *         if param is null.
     */
    private Color calcColor(GeoPoint closest) {
        if (closest == null) {
            return scene.background;
        }
        return scene.ambientLight.getIntensity().add(closest.geometry.getEmission());
    }

}
