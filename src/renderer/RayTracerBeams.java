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
            if (nl * nv > 0) {
                double ktr = calcKtr(intersection, lightSource, k);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(kd, lightIntensity, nl),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity, nl));
                }
            }
        }
        return color;
    }

    private double calcKtr(GeoPoint intersection, LightSource lightSource, double k) {
        double sumOfKtr = 0;

        var points = getPoints(intersection.point, lightSource.getL(intersection.point), lightSource.getSquareEdge());
        for (var point : points) {
            Vector l = lightSource.getL(point);
            Vector n = intersection.geometry.getNormal(point);
            double ktr = transparency(lightSource, l, n, point);
            // if (ktr * k > MIN_CALC_COLOR_K) {
            sumOfKtr += ktr;
            // }
        }

        return alignZero(sumOfKtr / points.size());
    }

    public RayTracerBeams setNumOfRays(int numOfRays) {
        this.numOfRays = numOfRays;
        return this;
    }

    private List<Point3D> getPoints(Point3D center, Vector n, double edge) {
        List<Point3D> points = new LinkedList<>();
        points.add(center);
        if (edge == 0 || numOfRays == 1) {
            return points;
        }

        Point3D head = n.getHead();
        double nx = head.getX();
        double ny = head.getY();
        double nz = head.getZ();

        double cx = center.getX();
        double cy = center.getY();
        double cz = center.getZ();

        double d = nx * cx + ny * cy + nz * cz;

        // the "top left" of the square
        double baseX = cx - (edge / 2);
        double baseY = cy - (edge / 2);

        for (int i = 1; i < numOfRays; i++) {
            // create coordinates for the new point.
            double x = baseX + rand.nextDouble() * edge;
            double y = baseY + rand.nextDouble() * edge;
            double z = (d - (x * nx + y * ny)) / nz;
            points.add(new Point3D(x, y, z)); // added the new point to the list.
        }
        return points;
    }

}
