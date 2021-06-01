package renderer;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import elements.LightSource;

import static primitives.Util.*;

import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

public class RayTracerBeams extends RayTracerBasic {
    private int numOfRays = 1;
    private Random rand = new SecureRandom();

    public RayTracerBeams(Scene scene) {
        super(scene);
    }

    @Override
    protected Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
        if (numOfRays == 1) { // if we do not have a number of rays aka no advance ray tracing so call
                              // rayTracerBasic function.
            return super.calcLocalEffects(intersection, ray, k);
        }
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
            if (nl * nv > 0) {
                double ktr = calcKtr(intersection, lightSource);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(kd, lightIntensity, nl),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity, nl));
                }
            }
        }

        return color;
    }

    private double calcKtr(GeoPoint intersection, LightSource lightSource) {
        double sumOfKtr = 0;

        var points = getPoints(intersection.point, lightSource.getL(intersection.point), lightSource.getRadius());
        for (var point : points) {
            Vector l = lightSource.getL(point);
            Vector normal = intersection.geometry.getNormal(point);
            sumOfKtr += transparency(lightSource, l, normal, intersection);
        }
        return sumOfKtr / points.size();
    }

    // private Color calcKtr(GeoPoint intersection, double k, Vector v, Vector n,
    // int nShininess, double kd, double ks,
    // Color color, LightSource lightSource, Vector l, double nl) {
    // double ktr = transparency(lightSource, l, n, intersection);
    // if (ktr * k > MIN_CALC_COLOR_K) {
    // Color lightIntensity =
    // lightSource.getIntensity(intersection.point).scale(ktr);
    // color = color.add(calcDiffusive(kd, lightIntensity, nl),
    // calcSpecular(ks, l, n, v, nShininess, lightIntensity, nl));
    // }
    // return color;
    // }

    public RayTracerBeams setNumOfRays(int numOfRays) {
        this.numOfRays = numOfRays;
        return this;
    }

    private List<Point3D> getPoints(Point3D p, Vector n, double radius) {
        List<Point3D> points = new LinkedList<>();
        points.add(p);
        if (radius == 0) {
            return points;
        }
        Point3D center = n.getHead();
        // u and v are unit vectors that span the plane of the circle that orthogonal to
        // the normal n.
        Vector v = new Vector(-center.getY(), center.getX(), 0).normalize();
        Vector u = n.crossProduct(v).normalize();

        for (int i = 1; i < numOfRays; i++) {
            // x and y inside the circle with the given radius.
            double x = rand.nextDouble() * radius;
            double y = Math.sqrt(radius * radius - x * x);
            points.add(v.scale(x).add(u.scale(y)).getHead()); // added the new point to the list.
        }
        return points;
    }
}