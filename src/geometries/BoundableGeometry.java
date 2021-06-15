package geometries;

import java.util.List;

import primitives.Point3D;
import primitives.Ray;

import static primitives.Util.*;

/**
 * An class that give smart AABB intersection check capabilities to a geometry.
 */
public abstract class BoundableGeometry extends Geometry {

    /**
     * Get a AABB that bounding the geometry.
     * 
     * @return
     */
    protected abstract AABB getAABB();

    /**
     * check if a ray is intersecting with the AAB of the geometry shape.
     * 
     * @param ray
     * @return
     */
    private boolean isRayIntersecting(Ray ray) {
        AABB box = getAABB();
        Point3D min = box.getMinLocation();
        Point3D max = box.getMaxLocation();
        Point3D p0 = ray.getP0();
        Point3D dirHead = ray.getDir().getHead();

        // in order to optimize and avoid div by 0 error we will use the inverted
        // values.
        double dirInvertedX = 1d / dirHead.getX();
        double dirInvertedY = 1d / dirHead.getY();
        double dirInvertedZ = 1d / dirHead.getZ();

        // calculate distance between min/max and p0 in 3D dimensions separately:
        double xMin = alignZero((min.getX() - p0.getX()) * dirInvertedX);
        double xMax = alignZero((max.getX() - p0.getX()) * dirInvertedX);
        double yMin = alignZero((min.getY() - p0.getY()) * dirInvertedY);
        double yMax = alignZero((max.getY() - p0.getY()) * dirInvertedY);
        double zMin = alignZero((min.getZ() - p0.getZ()) * dirInvertedZ);
        double zMax = alignZero((max.getZ() - p0.getZ()) * dirInvertedZ);

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
        return isRayIntersecting(ray) ? calculateGeoIntersection(ray) : null;
    }

}
