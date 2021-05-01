package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * A basic requirement for all ray tracing object to follow. represented by
 * abstract class.
 */
public abstract class RayTracerBase {
    protected Scene scene;

    /**
     * The constructor for creating a RayTracer object should get Scene as
     * parameter.
     * 
     * @param scene - Scene to work on.
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * A function to get the color of pixel in the scene.
     * 
     * @param ray - The ray to test the scene with.
     * @return Color - The appropriate color for the pixel that the ray intersected
     *         the scene with.
     */
    public abstract Color traceRay(Ray ray);
}
