package primitives;

import static primitives.Util.*;
import java.util.List;
import java.util.Optional;

import geometries.Intersectable.GeoPoint;

/**
 * Class Ray is the basic class representing Ray of Euclidean geometry in
 * Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Ray {

    private static final double DELTA = 0.1;

    private final Point3D p0;
    private final Vector dir;

    /**
     * Constructor for Vector Class.
     * 
     * @param p0  - A Point3D to indicate the starting point of the ray in the
     *            Cartesian 3-Dimensional coordinate system.
     * 
     * @param dir - A normalized Vector (unit vector) to indicate the direction of
     *            the ray in the Cartesian 3-Dimensional coordinate system.
     * 
     */
    public Ray(Point3D p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalized();
    }

    /**
     * A constructor to define a new Ray that is close to another ray but with some
     * change to p0(Ray head).
     * 
     * @param head      - The head of new Ray should be (approximately)
     * @param direction - The direction of the ray.
     * @param normal    - A normal vector to the head(used to define how the minor
     *                  change will be applied)
     */
    public Ray(Point3D head, Vector direction, Vector normal) {
        double sign = alignZero(direction.dotProduct(normal));
        this.p0 = head.add(normal.scale(sign > 0 ? DELTA : -DELTA));
        this.dir = direction.normalized();
    }

    /**
     * Getter for private field dir
     * 
     * @return - A Vector (unit vector that indicate the direction of the Ray).
     */
    public Vector getDir() {
        return dir;
    }

    /**
     * Getter for private field p0
     * 
     * @return - A Point3D (the starting point of the Ray).
     */
    public Point3D getP0() {
        return p0;
    }

    /**
     * get the Point3D on the Ray that is in distance of t from Ray starting Point3D
     * (p0).
     * 
     * @param t
     * @return Point3D - point on Ray axis with distance of t from Ray origin
     *         point(p0).
     */
    public Point3D getPoint(double t) {
        return p0.add(dir.scale(t));
    }

    /**
     * Find the closest point to the ray (distance is minimal between point and the
     * base of the ray).
     * 
     * @param points - List of Point3D to find the closest between them.
     * @return Point3D - the Point3D with the minimal distance from it to the ray
     *         P0(starting point).
     */
    public Point3D findClosestPoint(List<Point3D> points) {
        if (points == null)
            return null;

        var closest = points.stream().parallel()
                .reduce((p1, p2) -> alignZero(p2.distance(p0) - p1.distance(p0)) < 0 ? p2 : p1);
        return closest.isPresent() ? closest.get() : null;
    }

    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPoints) {
        if (geoPoints == null || geoPoints.isEmpty()) {
            return null;
        }
        // reduce the list to the one GeoPoint with minimal distance to ray.
        Optional<GeoPoint> closest = geoPoints.stream().parallel()
                .reduce((gp1, gp2) -> alignZero(gp2.point.distance(p0) - gp1.point.distance(p0)) < 0 ? gp2 : gp1);
        // return it if it exists or null.
        return closest.isPresent() ? closest.get() : null;
    }

    /**
     * Test if two Rays are equals.
     * 
     * @param obj - An Object to test if it's equal to This Ray in algebraic manner.
     * @return boolean - True if it's the same Ray or with same characteristics,
     *         False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Ray))
            return false;
        Ray other = (Ray) obj;
        return this.p0.equals(other.p0) && this.dir.equals(other.dir);
    }

    /**
     * Returning This Ray as String Using Point3D and Vector toString methods.
     * 
     * @return String - A 3-Dimensional coordinate Ray using point and Vector.
     *         ex:"p0= (x, y, z), dir= (a, b, c)".
     */
    @Override
    public String toString() {
        return "p0= " + p0.toString() + " " + ", dir= " + dir.toString() + " ";
    }

}