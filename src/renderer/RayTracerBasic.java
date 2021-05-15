package renderer;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A Basic Ray tracing implementation.
 */
public class RayTracerBasic extends RayTracerBase {

    private static final double INITIAL_K = 1.0;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;

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
        GeoPoint closest = findClosestIntersection(ray);
        return closest == null ? Color.BLACK : calcColor(closest, ray);
    }

    /**
     * Calculate what color the point3D has according to the scene.
     * 
     * @param closest Point3D - A point in the scene or null.
     * @return Color - The Color of the point in the scene or the background color
     *         if param is null.
     */
    private Color calcColor(GeoPoint closest, Ray ray) {
        return calcColor(closest, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K) //
                .add(scene.ambientLight.getIntensity());
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
            if (nl * nv > 0 && unshaded(lightSource, l, n, intersection)) {
                // sign(nl) == sign(nv) && not shaded by other shape
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
        return isZero(diffusionFactor) ? Color.BLACK : lightIntensity.scale((diffusionFactor));
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
        double minusVR = -alignZero(v.dotProduct(r));
        if (minusVR <= 0)
            return Color.BLACK;

        double specularFactor = ks * Math.pow(minusVR, nShininess);
        return isZero(specularFactor) ? Color.BLACK : lightIntensity.scale(specularFactor);
    }

    /**
     * A function to test if a given intersection point is the first point to be hit
     * by the light source. return true if it's the first (aka the point should be
     * lighted by the light source), false otherwise.
     * 
     * @param light    - The light source.
     * @param l        - The light source direction vector to the point we want to
     *                 inspect.
     * @param n        - The normal to the Geometry shape at the given point we
     *                 inspect.
     * @param geoPoint - The GeoPoint representing the intersection we inspecting in
     *                 scene.
     * @return - True if the point is not shaded by other shape, False otherwise.
     */
    private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint geoPoint) {
        Vector lightDirection = l.scale(-1);

        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);

        var intersections = scene.geometries.findGeoIntersections(lightRay);

        var lightDistance = light.getDistance(geoPoint.point);

        return intersections == null || intersections.stream() //
                .parallel().noneMatch(intersection -> //
                (alignZero(intersection.point.distance(geoPoint.point) - lightDistance) <= 0
                        && intersection.geometry.getMaterial().kT == 0));
    }

    /**
     * A helper function to find all intersection to a ray in the scene.
     * 
     * @param ray
     * @return the closest GP or null.
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        return ray.findClosestGeoPoint(scene.geometries.findGeoIntersections(ray));
    }

    /**
     * find all light effects on the ray in teh scene in order to calculate the
     * proper color we should give back.
     * 
     * @param closest - The GeoPoint we want to check in for the color.
     * @param ray     - The ray we using to look at the GeoPoint given.
     * @param level   - ...
     * @param k       - ...
     * @return Color - The Color the point should be in the scene.
     */
    private Color calcColor(GeoPoint closest, Ray ray, int level, double k) {
        if (closest == null) {
            return Color.BLACK;
        }
        Color color = closest.geometry.getEmission()
                // add calculated light contribution from all light sources)
                .add(calcLocalEffects(closest, ray));
        return 1 == level ? color : color.add(calcGlobalEffects(closest, ray, level, k));
    }

    /**
     * 
     * @param closest - The GeoPoint we want to check in for the color.
     * @param ray     - The ray we using to look at the GeoPoint given.
     * @param level   - ...
     * @param k       - ...
     * @return Color - The Color of light the Scene affect our GeoPoint.
     */
    private Color calcGlobalEffects(GeoPoint closest, Ray ray, int level, double k) {
        Color color = Color.BLACK;
        Material material = closest.geometry.getMaterial();
        double kr = material.kR, kkr = k * kr;
        var n = closest.geometry.getNormal(closest.point);
        if (kkr > MIN_CALC_COLOR_K) {
            Ray reflectedRay = constructReflectedRay(n, closest.point, ray);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
        }
        double kt = material.kT, kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(n, closest.point, ray);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
        }
        return color;
    }

    /**
     * A function to calculate refractions in a scene in order to find if the object
     * is opaque or not
     * 
     * @param n     - The normal to the shape in the scene at the given point.
     * @param point - The point we want to calculate refraction on.
     * @param ray   - The Ray we that "looking" at the point.
     * @return Ray - refraction ray.
     */
    private Ray constructRefractedRay(Vector n, Point3D point, Ray ray) {
        return new Ray(point, ray.getDir(), n);
    }

    /**
     * A function to calculate the reflected ray of a given intersection point.
     * 
     * @param n     - The normal to the shape in the scene at the given point.
     * @param point - The point we want to calculate reflection on.
     * @param ray   - The Ray we that "looking" at the point.
     * @return Ray - reflection ray.
     */
    private Ray constructReflectedRay(Vector n, Point3D point, Ray ray) {
        Vector v = ray.getDir();
        double factor = -2 * alignZero(v.dotProduct(n));
        return new Ray(point, v.subtract(n.scale(factor)));
    }
}
