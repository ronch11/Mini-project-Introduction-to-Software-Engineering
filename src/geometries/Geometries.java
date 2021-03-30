package geometries;

import java.util.ArrayList;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;

public class Geometries implements Intersectable {
    private List<Intersectable> geometriesList;

    public Geometries() {
        geometriesList = new ArrayList<>();
    }

    public Geometries(Intersectable... geometries) {
        geometriesList = List.of(geometries);
    }

    public void add(Intersectable... geometries) {
        // TODO Auto-generated method stub
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
                        intersections = new ArrayList<>(intersectableIntersections);
                    }
                }
            }
            return intersections;
        }
    }

}
