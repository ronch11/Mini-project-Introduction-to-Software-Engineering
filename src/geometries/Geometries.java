package geometries;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import primitives.Point3D;
import primitives.Ray;

/**
 * Geometries class represents composition of Geometries in 3D Cartesian
 * coordinate system
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Geometries implements BoundedIntersectable {
    private List<BoundedIntersectable> geometriesList;
    // in order to save time in calculation we save the AABB of Geometries and
    // update it with each add.
    private AABB boundingBox;

    /**
     * A Constructor the sets the object to be with empty list of intersectable
     * shapes.
     */
    public Geometries() {
        geometriesList = new LinkedList<>();
        setBox();
    }

    /**
     * A Constructor the gets an Array of intersectable Geometries shapes. and
     * create a composite list of them.
     * 
     * @param geometries
     */
    public Geometries(BoundedIntersectable... geometries) {
        geometriesList = List.of(geometries);
        setBox();
    }

    /**
     * Add a new Shape/s to the List.
     * 
     * @param geometries - array of Intersectable Geometries.
     */
    public void add(BoundedIntersectable... geometries) {
        geometriesList.addAll(List.of(geometries));
        setBox();
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        // no need to add more to this function because checking AABB intersection
        // happens automatically in each geometry inside geometriesList.
        return calculateGeoIntersection(ray);
    }

    public void buildBVHTree() {
        boundGeometries();
        Geometries root = this;
        double minimumDistance;
        while (root.geometriesList.size() > 1) {
            Geometries leftSon = null, rightSon = null;
            minimumDistance = Double.MAX_VALUE;
            int loopsCount = root.geometriesList.size();
            for (int i = 0; i < loopsCount; i++) {
                Geometries geo1 = (Geometries) root.geometriesList.get(i);
                for (int j = i + 1; j < loopsCount; j++) {
                    Geometries geo2 = (Geometries) root.geometriesList.get(j);
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
            root.geometriesList.remove(leftSon);
            root.geometriesList.remove(rightSon);
            root.geometriesList.add(newComposite);
        } // end of while
        geometriesList = List.of(root.geometriesList.get(0));
    }

    /**
     * get the distance between two geometries boundingBox center points.
     * 
     * @param geo2 - other Geometries
     * @return - the distance between the center of the two boundingBox.
     */
    private double distance(Geometries geo2) {
        Point3D c1 = this.getAABB().getCenterLocation();
        Point3D c2 = geo2.getAABB().getCenterLocation();
        return c1.distance(c2);
    }

    /**
     * return new Geometries that has all the old Geometry object inside
     * geometriesList encapsulated in separated Geometries object, creating a base
     * for building a BVH tree(as every shape is inside a "Node")
     * 
     * @return - Geometries tree (for BVH)
     */
    private void boundGeometries() {
        List<BoundedIntersectable> list = new LinkedList<>();
        for (BoundedIntersectable boundedIntersectable : geometriesList) {
            Geometries g = new Geometries(boundedIntersectable);
            list.add(g);
        }
        geometriesList = list;
    }

    @Override
    public AABB getAABB() {
        return boundingBox;
    }

    @Override
    public List<GeoPoint> calculateGeoIntersection(Ray ray) {
        if (geometriesList.isEmpty()) {
            return null;
        } else {
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

    /**
     * helper function to update the AABB of this collection of Geometries shapes.
     */
    private void setBox() {
        if (geometriesList.isEmpty()) {
            boundingBox = new AABB(Point3D.ZERO, 0, 0, 0);
        } else {
            List<AABB> boxes = geometriesList.stream().parallel().map(geo -> geo.getAABB())
                    .collect(Collectors.toList());

            double minX = boxes.stream().parallel().map(box -> box.getMinLocation()).mapToDouble(point -> point.getX())
                    .min().getAsDouble();
            double minY = boxes.stream().parallel().map(box -> box.getMinLocation()).mapToDouble(point -> point.getY())
                    .min().getAsDouble();
            double minZ = boxes.stream().parallel().map(box -> box.getMinLocation()).mapToDouble(point -> point.getZ())
                    .min().getAsDouble();

            double maxX = boxes.stream().parallel().map(box -> box.getMaxLocation()).mapToDouble(point -> point.getX())
                    .max().getAsDouble();
            double maxY = boxes.stream().parallel().map(box -> box.getMaxLocation()).mapToDouble(point -> point.getY())
                    .max().getAsDouble();
            double maxZ = boxes.stream().parallel().map(box -> box.getMaxLocation()).mapToDouble(point -> point.getZ())
                    .max().getAsDouble();

            boundingBox = new AABB(new Point3D(minX, minY, minZ), maxX - minX, maxY - minY, maxZ - minZ);
        }
    }
}
