package geometries;

import java.util.ArrayList;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;

public class Geometries implements Intersectable {
    private List<Intersectable> geometries_list;

    public Geometries() {
        geometries_list = new ArrayList<>();
    }

    public Geometries(Intersectable... geometries) {
        geometries_list = List.of(geometries);
    }

    public void add(Intersectable... geometries) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        // TODO Auto-generated method stub
        return null;
    }

}
