package geometries;

import java.util.LinkedList;
import java.util.List;
import primitives.Point3D;
import primitives.Ray;

/**
 * Geometries class represents composition of Geometries in 3D Cartesian
 * coordinate system
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Geometries implements Intersectable {
    private List<Intersectable> geometriesList;

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
    public List<Point3D> findIntersections(Ray ray) {
        if (geometriesList.isEmpty()) {
            return null;
        } else {
            List<Point3D> intersections = null;
            for (Intersectable intersectable : geometriesList) {
                List<Point3D> intersectableIntersections = intersectable.findIntersections(ray);
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

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
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

}
