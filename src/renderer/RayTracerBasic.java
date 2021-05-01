package renderer;

import java.util.List;

import elements.AmbientLight;
import primitives.Color;
import primitives.Point3D;
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
        // TODO Auto-generated constructor stub
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point3D> points = scene.geometries.findIntersections(ray);
        Point3D closest = ray.findClosestPoint(points);
        Color color = calcColor(closest);
        return color;
    }

    /**
     * Calculate what color the point3D has according to the scene.
     * 
     * @param closest Point3D - A point in the scene or null.
     * @return Color - The Color of the point in the scene or the background color
     *         if param is null.
     */
    private Color calcColor(Point3D closest) {
        if (closest == null) {
            return scene.background;
        }
        return scene.ambientLight.getIntensity();
    }

}
