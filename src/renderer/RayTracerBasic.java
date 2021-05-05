package renderer;

import java.util.List;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Material;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
        return calcColor(closest, ray);
    }

    /**
     * Calculate what color the point3D has according to the scene.
     * 
     * @param closest Point3D - A point in the scene or null.
     * @return Color - The Color of the point in the scene or the background color
     *         if param is null.
     */
    private Color calcColor(GeoPoint closest, Ray ray) {
        if (closest == null) {
            return scene.background;
        }
        return scene.ambientLight.getIntensity().add(closest.geometry.getEmission())
                // add calculated light contribution from all light sources)
                .add(calcLocalEffects(closest, ray));
    }

    /**
     * A helper function to reduce clutter in the calcColor function.
     * 
     * @param intersection GeoPoint - The intersection between point and shape
     *                     GeoPoint represent.
     * @param ray          Ray - The ray we want to test with the intersection.
     * @return Color - The Color of the point in the scene according to all lights
     *         effects in the scene.
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v));
        if (isZero(nv))
            return Color.BLACK;
        Material material = intersection.geometry.getMaterial();
        int nShininess = material.nShininess;
        double kd = material.kD;
        double ks = material.kS;
        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sign(nv)
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
            }
        }
        return color;
    }

    /**
     * A helper function to reduce clutter in calcLocalEffects function.
     * 
     * @param kd             double - The diffusion scalar of the material the shape
     *                       is made of.
     * @param l              Vector - The light Vector to the point.
     * @param n              Vector - Normal to the point.
     * @param lightIntensity Color - The intensity light of light source at the
     *                       point.
     * @return Color - The diffusion effect color at the point.
     */
    private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n));
        if (isZero(ln)) {
            return Color.BLACK;
        }
        double diffusionFactor = alignZero(kd * Math.abs(ln));
        if (isZero(diffusionFactor)) {
            return Color.BLACK;
        }
        return lightIntensity.scale((diffusionFactor));
    }

    /**
     * A helper function to reduce clutter in calcLocalEffects function.
     * 
     * @param ks             double - The specular scalar of the material the shape
     *                       is made of.
     * @param l              Vector - The light Vector to the point.
     * @param n              Vector - Normal to the point.
     * @param v              Vector - the direction of the camera ray that look to
     *                       the scene.
     * @param nShininess     int - The shininess factor of the material the shape is
     *                       made of.
     * @param lightIntensity Color - The intensity light of light source at the
     *                       point.
     * @return Color - The specular effect color at the point.
     */
    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n));
        if (isZero(ln)) {
            return Color.BLACK;
        }
        Vector r = l.subtract(n.scale(2 * ln)).normalize();
        double minusVR = alignZero(v.scale(-1).dotProduct(r));
        minusVR = minusVR < 0 ? 0 : minusVR;
        if (isZero(minusVR)) {
            return Color.BLACK;
        }

        double specularFactor = alignZero(ks * alignZero(Math.pow(minusVR, nShininess)));
        if (isZero(specularFactor)) {
            return Color.BLACK;
        }
        return lightIntensity.scale(specularFactor);
    }
}
