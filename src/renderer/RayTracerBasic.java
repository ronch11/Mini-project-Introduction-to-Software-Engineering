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

    protected static final double INITIAL_K = 1.0;
    protected static final int MAX_CALC_COLOR_LEVEL = 10;
    protected static final double MIN_CALC_COLOR_K = 0.001;

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
        return closest == null ? scene.background : calcColor(closest, ray);
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
    protected Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
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
                double ktr = transparency(lightSource, l, n, intersection);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(kd, lightIntensity, nl),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity, nl));
                }
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
    protected Color calcDiffusive(double kd, Color lightIntensity, double ln) {
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
    protected Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity,
            double ln) {
        Vector r = l.subtract(n.scale(2 * ln)).normalize();
        double minusVR = -alignZero(v.dotProduct(r));
        if (minusVR <= 0)
            return Color.BLACK;

        double specularFactor = ks * Math.pow(minusVR, nShininess);
        return isZero(specularFactor) ? Color.BLACK : lightIntensity.scale(specularFactor);
    }

    /**
     * A helper function to find all intersection to a ray in the scene.
     *
     * @param ray
     * @return the closest GP or null.
     */
    public GeoPoint findClosestIntersection(Ray ray) {
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
        Color color = closest.geometry.getEmission()
                // add calculated light contribution from all light sources)
                .add(calcLocalEffects(closest, ray, k));
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
            if (reflectedPoint != null) {
                color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
            }
        }
        double kt = material.kT, kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(n, closest.point, ray);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null) {
                color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
            }
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
        double factor = 2 * alignZero(v.dotProduct(n));
        return new Ray(point, v.subtract(n.scale(factor)), n);
    }

    /**
     * A function to get how much transparent a shape in scene is.
     * 
     * @param ls       - Light source that we using to define if it's light being
     *                 casted on the shape at the point given.
     * @param l        - light direction to the point on the shape.
     * @param n        - Normal vector to the given point at the shape.
     * @param geoPoint - The given shape and point at it.
     * @return - The transparent factor between 0.0(translucent) and 1.0(opaque)
     */
    protected double transparency(LightSource ls, Vector l, Vector n, GeoPoint geoPoint) {
        Vector lightDirection = l.scale(-1);

        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);

        var intersections = scene.geometries.findGeoIntersections(lightRay);

        double ktr = 1.0;
        if (intersections == null)
            return ktr;

        var lightDistance = ls.getDistance(geoPoint.point);

        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0) {
                ktr *= gp.geometry.getMaterial().kT;
                if (ktr < MIN_CALC_COLOR_K)
                    return 0.0;
            }
        }
        return ktr;
    }
}
