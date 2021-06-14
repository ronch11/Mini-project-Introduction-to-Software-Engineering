package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.*;

import java.util.List;

/**
 * Geometry is the common abstract class for all Geometry classes to implement.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public abstract class Geometry implements Intersectable {
    private Point3D min;
    private Point3D max;

    protected Color emission = Color.BLACK;
    private Material material = new Material();

    public Point3D getMax() {
        return max;
    }

    public Point3D getMin() {
        return min;
    }

    /**
     * Getter for protected field emissions
     * 
     * @return Color - The shape "emitted" Color.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * A getter for private field material.
     * 
     * @return Material - the material the shape is made from.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param min the min to set
     */
    public Geometry setMin(Point3D min) {
        this.min = min;
        return this;
    }

    /**
     * @param max the max to set
     */
    public Geometry setMax(Point3D max) {
        this.max = max;
        return this;
    }

    /**
     * A setter using builder pattern for protected field emissions.
     * 
     * @param emissions Color - The color that the geometry shape is "emitting".
     * @return Geometry - self return for builder patter.
     */
    public Geometry setEmission(Color emissions) {
        this.emission = emissions;
        return this;
    }

    /**
     * A setter using builder pattern for private field material.
     * 
     * @param material Material - the material of the Geometry shape.
     * @return Geometry - self return for builder patter.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    private boolean isIntersectingWithAABB(Ray ray) {
        Point3D p0 = ray.getP0();
        Point3D dirHead = ray.getDir().getHead();

        // in order to optimize and avoid div by 0 error we will use the inverted
        // values.
        double dirInvertedX = 1d / dirHead.getX();
        double dirInvertedY = 1d / dirHead.getY();
        double dirInvertedZ = 1d / dirHead.getZ();

        // calculate distance between min/max and p0 in 3D dimensions separately:
        double xMin = alignZero((this.getMin().getX() - p0.getX()) * dirInvertedX);
        double xMax = alignZero((this.getMax().getX() - p0.getX()) * dirInvertedX);
        double yMin = alignZero((this.getMin().getY() - p0.getY()) * dirInvertedY);
        double yMax = alignZero((this.getMax().getY() - p0.getY()) * dirInvertedY);
        double zMin = alignZero((this.getMin().getZ() - p0.getZ()) * dirInvertedZ);
        double zMax = alignZero((this.getMax().getZ() - p0.getZ()) * dirInvertedZ);

        // pick the maximum between minimum values in the 3D dimensions.
        double tMin = Math.max(Math.max(Math.min(xMin, xMax), Math.min(yMin, yMax)), Math.min(zMin, zMax));
        // pick the minimum between the maximum values in the 3D dimensions.
        double tMax = Math.min(Math.min(Math.max(xMin, xMax), Math.max(yMin, yMax)), Math.max(zMin, zMax));

        // if tMax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        // if tMin > tMax, ray doesn't intersect AABB
        // which in both cases we return false.
        // else, ray intersecting with the Axis Aligned Bounding Box and we return true.
        return !(tMax < 0 || tMin > tMax);
    }

    protected abstract List<GeoPoint> calculateGeoIntersection(Ray ray);

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return isIntersectingWithAABB(ray) ? calculateGeoIntersection(ray) : null;
    }

    /**
     * A function that return unit vector (length equal to 1) of perpendicular
     * vector to the Geometry shape and the given point in @param point3d
     * 
     * @param point3d - point on the shape surface
     * @return - A perpendicular unit vector to the shape in the point.
     */
    public abstract Vector getNormal(Point3D point3d);
}
