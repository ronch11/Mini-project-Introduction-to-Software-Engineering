package geometries;

import static primitives.Util.alignZero;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class Sphere is the Geometry class representing a Sphere shape in space of
 * Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Sphere extends BoundableGeometry {
    private Point3D center;
    private double radius;
    private double radiusSquared;

    /**
     * Constructor for building Sphere object.
     * 
     * @param point  - The Center of the sphere(Point3D).
     * @param radius - The radius of the sphere(double).
     */
    public Sphere(Point3D point, double radius) {
        this.center = point;
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    /**
     * Getter for internal field Center
     * 
     * @return Point3D - The Center Point of this Sphere.
     */
    public Point3D getCenter() {
        return center;
    }

    /**
     * Getter for internal field radius
     * 
     * @return double - the radius of this Sphere.
     */
    public double getRadius() {
        return radius;
    }

    @Override
    public Vector getNormal(Point3D point3d) {
        return point3d.subtract(center).normalize();
    }

    @Override
    public List<GeoPoint> calculateGeoIntersection(Ray ray) {
        double tM, dSquared;
        try {
            Vector u = center.subtract(ray.getP0());
            tM = alignZero(ray.getDir().dotProduct(u));
            dSquared = alignZero(u.lengthSquared() - tM * tM);
        } catch (IllegalArgumentException e) {
            return List.of(new GeoPoint(this, ray.getPoint(radius)));
        }

        double thSquared = alignZero(radiusSquared - dSquared);
        if (thSquared <= 0)
            return null;

        double tH = Math.sqrt(thSquared);
        double t1 = alignZero(tM + tH);

        if (t1 > 0) {
            GeoPoint gp1 = new GeoPoint(this, ray.getPoint(t1));
            double t2 = alignZero(tM - tH);
            return t2 > 0 ? //
                    List.of(new GeoPoint(this, ray.getPoint(t2)), gp1) : //
                    List.of(gp1);
        }
        return null;
    }

    @Override
    protected AABB getAABB() {
        return new AABB(center.add(new Vector(-radius, -radius, -radius))//
                , radius * 2, radius * 2, radius * 2);
    }
}
