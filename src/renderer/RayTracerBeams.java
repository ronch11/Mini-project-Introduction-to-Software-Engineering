package renderer;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static primitives.Util.*;

import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;
import elements.LightSource;

/**
 * A Ray tracing class with enhancement capabilities of soft shadows.
 */
public class RayTracerBeams extends RayTracerBasic {
    private int numOfRays = 1;
    private Random rand = new SecureRandom();

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
        if (numOfRays == 1) { // if we do not have a number of rays aka no advance ray tracing so call
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
            double nl = alignZero(n.dotProduct(l));
            double ktr = calcKtr(intersection, lightSource, nv, n, k);
            if (ktr * k > MIN_CALC_COLOR_K) {
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
    private double calcKtr(GeoPoint intersection, LightSource lightSource, double nv, Vector n, double k) {
        double sumOfKtr = 0;

        var points = lightSource.calculatePoints(intersection.point, numOfRays);
        for (var point : points) {
            Vector l = lightSource.getDirection(point);
            if (alignZero(n.dotProduct(l)) * nv > 0) {
                double ktr = transparency(lightSource, l, n, point);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    sumOfKtr += ktr;
                }
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
        this.numOfRays = numOfRays;
        return this;
    }

    // /**
    // * Get points around teh center point given. limited to a square with edge
    // * length in param edge.
    // *
    // * @param center - the center point of the square.
    // * @param n - normal to the shape at the given point center.
    // * @param edge - length of the square edge.
    // * @return - List of point3D randomly around inside the square with length of
    // * edge and center as it's center point(first point will always be the
    // * center point) in case edge is 0 or numOfRays is 1 we get only the
    // * center point back.
    // */
    // private List<Point3D> getPoints(Point3D center, Vector n, double edge) {
    // List<Point3D> points = new LinkedList<>();
    // points.add(center);
    // if (edge == 0 || numOfRays == 1) {
    // return points;
    // }

    // Vector vx = n.orthogonalVector();
    // Vector vy = n.crossProduct(vx).normalize();

    // for (int i = 1; i < numOfRays; i++) {
    // Point3D pc = center;
    // double x = rand.nextDouble() * edge;
    // double y = rand.nextDouble() * edge;
    // if (!isZero(x)) {
    // pc = pc.add(vx.scale(x));
    // }
    // if (!isZero(y)) {
    // pc = pc.add(vy.scale(y));
    // }
    // points.add(pc);
    // }

    // return points;
    // }
}
