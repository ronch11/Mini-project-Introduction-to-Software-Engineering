package geometries;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import primitives.Point3D;
import primitives.Ray;

import static primitives.Util.*;

/**
 * Geometries class represents composition of Geometries in 3D Cartesian
 * coordinate system
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Geometries implements Intersectable {
    private List<Intersectable> geometriesList;
    // in order to save time in calculation we save the AABB of Geometries.
    private AABB boundingBox;

    /**
     * A Constructor the sets the object to be with empty list of intersectable
     * shapes.
     */
    public Geometries() {
        geometriesList = new LinkedList<>();
    }

    /**
     * A Constructor the gets an Array of intersectable Geometries shapes. and
     * create a composite list of them.
     * 
     * @param geometries
     */
    public Geometries(Intersectable... geometries) {
        geometriesList = List.of(geometries);
    }

    /**
     * Add a new Shape/s to the List.
     * 
     * @param geometries - array of Intersectable Geometries.
     */
    public void add(Intersectable... geometries) {
        geometriesList.addAll(List.of(geometries));
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        if (geometriesList.isEmpty()) {
            return null;
        } else {
            if (boundingBox != null && !isRayIntersectingAABB(ray)) {
                return null;
            }
            List<GeoPoint> intersections = null;
            for (Intersectable intersectable : geometriesList) {
                List<GeoPoint> intersectableIntersections = intersectable.findGeoIntersections(ray);
                if (intersectableIntersections != null) {
                    if (intersections != null) {
                        intersections.addAll(intersectableIntersections);
                    } else {
                        intersections = new LinkedList<>(intersectableIntersections);
                    }
                }
            }
            return intersections;
        }
    }

    public void buildBVHTree() {
        boundGeometries();
        double minimumDistance;
        while (geometriesList.size() > 1) {
            Geometries leftSon = null, rightSon = null;
            minimumDistance = Double.MAX_VALUE;
            int loopsCount = geometriesList.size();
            for (int i = 0; i < loopsCount; i++) {
                Geometries geo1 = (Geometries) geometriesList.get(i);
                for (int j = i + 1; j < loopsCount; j++) {
                    Geometries geo2 = (Geometries) geometriesList.get(j);
                    if (!(geo1.equals(geo2))) {
                        double distance = geo1.distance(geo2);
                        if (distance < minimumDistance) {
                            minimumDistance = distance;
                            leftSon = geo1;
                            rightSon = geo2;
                        }
                    }
                } // end of for j
            } // end of for i
            Geometries newComposite = new Geometries(leftSon, rightSon);
            geometriesList.remove(leftSon);
            geometriesList.remove(rightSon);
            newComposite.setBox();
            geometriesList.add(newComposite);
            setBox();
        } // end of while
    }

    /**
     * get the distance between two geometries boundingBox center points.
     * 
     * @param geo2 - other Geometries
     * @return - the distance between the center of the two boundingBox.
     */
    private double distance(Geometries geo2) {
        Point3D c1 = this.boundingBox.getCenterLocation();
        Point3D c2 = geo2.boundingBox.getCenterLocation();
        return c1.distance(c2);
    }

    /**
     * return new Geometries that has all the old Geometry objects inside
     * geometriesList encapsulated in separated Geometries object, creating a base
     * for building a BVH tree(as every shape is inside a "Node")
     * 
     * @return - Geometries tree (for BVH)
     */
    private void boundGeometries() {
        List<Intersectable> list = new LinkedList<>();
        for (Intersectable boundedIntersectable : geometriesList) {
            Geometries g = new Geometries(boundedIntersectable);
            g.setBox();
            list.add(g);
        }
        geometriesList = list;
    }

    @Override
    public AABB getAABB() {
        return boundingBox;
    }

    /**
     * helper function to update the AABB of this collection of Geometries shapes.
     */
    private void setBox() {
        if (geometriesList.isEmpty()) {
            boundingBox = new AABB(Point3D.ZERO, 0, 0, 0);
        } else {
            List<AABB> boxes = geometriesList.stream().parallel().map(geo -> geo.getAABB())
                    .collect(Collectors.toList());
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE, maxX = -Double.MAX_VALUE,
                    maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
            for (AABB aabb : boxes) {
                Point3D minPoint = aabb.getMinLocation();
                Point3D maxPoint = aabb.getMaxLocation();

                if (minPoint.getX() < minX) {
                    minX = minPoint.getX();
                }

                if (minPoint.getY() < minY) {
                    minY = minPoint.getY();
                }

                if (minPoint.getZ() < minZ) {
                    minZ = minPoint.getZ();
                }

                if (maxPoint.getX() < maxX) {
                    maxX = maxPoint.getX();
                }

                if (maxPoint.getY() < maxY) {
                    maxY = maxPoint.getY();
                }

                if (maxPoint.getZ() < maxZ) {
                    maxZ = maxPoint.getZ();
                }
            }

            boundingBox = new AABB(new Point3D(minX, minY, minZ), maxX - minX, maxY - minY, maxZ - minZ);
        }
    }

    private boolean isOverlapping(double minCoord, double maxCoord, double pointCoord, double dirInverted) {
        double min, max;
        if (dirInverted > 0) {
            min = alignZero((minCoord - pointCoord) * dirInverted);
            max = alignZero((maxCoord - pointCoord) * dirInverted);
        } else {
            min = alignZero((maxCoord - pointCoord) * dirInverted);
            max = alignZero((minCoord - pointCoord) * dirInverted);
        }
        return !(min > max);
    }

    /**
     * check if a ray is intersecting with the AABB of the geometry shape.
     *
     * @param ray
     * @return
     */
    private boolean isRayIntersectingAABB(Ray ray) {
        Point3D p0 = ray.getP0();
        Point3D dirHead = ray.getDir().getHead();
        Point3D min = boundingBox.getMinLocation();
        Point3D max = boundingBox.getMaxLocation();

        // in order to optimize and avoid div by 0 error we will use the inverted
        // values.
        double dirInvertedX = alignZero(1d / dirHead.getX());
        double dirInvertedY = alignZero(1d / dirHead.getY());
        double dirInvertedZ = alignZero(1d / dirHead.getZ());

        if (!isOverlapping(min.getX(), max.getX(), p0.getX(), dirInvertedX)) {
            return false;
        }

        if (!isOverlapping(min.getY(), max.getY(), p0.getY(), dirInvertedY)) {
            return false;
        }

        if (!isOverlapping(min.getZ(), max.getZ(), p0.getZ(), dirInvertedZ)) {
            return false;
        }
        return true;
        // calculate distance between min/max and p0 in 3D dimensions separately:
        // double xMin = alignZero((min.getX() - p0.getX()) * dirInvertedX);
        // double xMax = alignZero((max.getX() - p0.getX()) * dirInvertedX);
        // double yMin = alignZero((min.getY() - p0.getY()) * dirInvertedY);
        // double yMax = alignZero((max.getY() - p0.getY()) * dirInvertedY);
        // double zMin = alignZero((min.getZ() - p0.getZ()) * dirInvertedZ);
        // double zMax = alignZero((max.getZ() - p0.getZ()) * dirInvertedZ);

        // pick the maximum between minimum values in the 3D dimensions.
        // double tMin = Math.max(Math.max(Math.min(xMin, xMax), Math.min(yMin, yMax)),
        // Math.min(zMin, zMax));
        // pick the minimum between the maximum values in the 3D dimensions.
        // double tMax = Math.min(Math.min(Math.max(xMin, xMax), Math.max(yMin, yMax)),
        // Math.max(zMin, zMax));

        // if tMax < 0, ray (line) is intersecting AABB, but the whole AABB is behind
        // us
        // if tMin > tMax, ray doesn't intersect AABB
        // which in both cases we return false.
        // else, ray intersecting with the Axis Aligned Bounding Box and we return
        // true.
        // return !(tMax < 0 || tMin > tMax);
    }
}
