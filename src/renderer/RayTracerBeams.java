package renderer;

import static primitives.Util.*;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Material;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

/**
 * A Ray tracing class with enhancement capabilities of soft shadows.
 */
public class RayTracerBeams extends RayTracerBasic {
    private int numOfShadowRays = 1;

    /**
     * Constructor for our advanced RayTracer with beams.
     * 
     * @param scene - Scene we need to trace rays in.
     */
    public RayTracerBeams(Scene scene) {
        super(scene);
    }

    /**
     * calculate the local effect on point's color, if needed then soft shadows
     * improvement is used.
     * 
     * @param intersection - the geopoint we want to check color effects.
     * @param ray          - the camera ray toward the scene.
     * @param k            - the current k (color scalar) we check
     * @return - A Color after all local effects are included.
     */
    @Override
    protected Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
        if (numOfShadowRays == 1) { // if we do not have a number of rays aka no advance ray tracing so call
            // rayTracerBasic function.
            return super.calcLocalEffects(intersection, ray, k);
        }
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        Material material = intersection.geometry.getMaterial();
        int nShininess = material.nShininess;
        double kd = material.kD;
        double ks = material.kS;
        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double ktr = calcKtr(intersection, lightSource, nv, l, n);
            if (ktr * k > MIN_CALC_COLOR_K) {
                double nl = alignZero(n.dotProduct(l));
                Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                color = color.add(calcDiffusive(kd, lightIntensity, nl),
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity, nl));
            }
        }
        return color;
    }

    /**
     * A function to calc the average ktr for point on shape.
     * 
     * @param intersection - the geopoint we want to check.
     * @param lightSource  - the light source that effecting the geopoint
     * @return - average ktr
     */
    private double calcKtr(GeoPoint intersection, LightSource lightSource, double nv, Vector baseL, Vector n) {
        // if no soft shadows.
        // if (numOfShadowRays == 1) {
        //     return transparency(lightSource, baseL, n, intersection.point);
        // } - un needd anymore it's being handle in calcLocalEffect
        // else soft shadows.
        double sumOfKtr = 0;

        var points = lightSource.calculatePoints(baseL, numOfShadowRays);
        for (var point : points) {
            Vector l = lightSource.getDirection(point, intersection.point);
            if (alignZero(n.dotProduct(l)) * nv > 0) {
                sumOfKtr += transparency(lightSource, l, n, intersection.point);
            }
        }
        return alignZero(sumOfKtr / points.size());
    }

    /**
     * A setter for numOfRays field.
     * 
     * @param numOfRays - the number of rays we should use in order to calc soft
     *                  shadows effect.
     * @return - self return for builder pattern.
     */
    public RayTracerBeams setNumOfRays(int numOfRays) {
        this.numOfShadowRays = numOfRays;
        return this;
    }
}
