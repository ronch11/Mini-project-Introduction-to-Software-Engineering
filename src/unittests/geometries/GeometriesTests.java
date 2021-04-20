package unittests.geometries;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.Cylinder;
import geometries.Geometries;
import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import geometries.Tube;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Testing eometries.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class GeometriesTests {
    /**
     * Test method for {@link geometries.Geometries} Constructor.
     */
    @Test
    public void testFindIntersections() {
        // setup
        Ray axisRay = new Ray(new Point3D(0, 0, 1), new Vector(0, 0, 1));
        Vector unitVectorXAxis = new Vector(1, 0, 0);
        Tube tube = new Tube(new Ray(new Point3D(0, 0, 50), unitVectorXAxis), 5);
        Plane plane = new Plane(new Point3D(0, 0, 30), axisRay.getDir());
        Cylinder cylinder = new Cylinder(axisRay, 5, 25);
        Triangle triangle = new Triangle(new Point3D(0, 20, 6), new Point3D(0, -20, 6), new Point3D(10, 0, 10));
        Sphere sphere = new Sphere(new Point3D(0, 0, 10), 5);
        Polygon polygon = new Polygon(new Point3D(0, -5, 5), new Point3D(5, -5, 0), new Point3D(0, 5, 0),
                new Point3D(-5, 5, 5));
        Geometries geom = new Geometries(plane, polygon, sphere, triangle);
        Geometries empty = new Geometries();

        // ============ Equivalence Partitions Tests ==============
        // TC01: There is some intersections (not all shapes intersected).
        Ray intersectingRay = new Ray(new Point3D(-5, -5, 10), new Vector(15, 5, -10));
        List<Point3D> actualValue = geom.findIntersections(intersectingRay);
        assertEquals("There should be 3 intersections with our shapes.", 3, actualValue.size());

        // =============== Boundary Values Tests ==================
        // TC02: There is no intersections (no shapes).
        actualValue = empty.findIntersections(axisRay);
        assertNull("There should not be any intersections without shapes", actualValue);

        // TC03: There is no intersections (with shapes).
        actualValue = geom.findIntersections(new Ray(new Point3D(0, 0, -1), unitVectorXAxis));
        assertNull("There should not be any intersections with our shapes.", actualValue);

        // TC04: There is only one intersection.
        intersectingRay = new Ray(new Point3D(0, 0, 2), unitVectorXAxis);
        actualValue = geom.findIntersections(intersectingRay);
        assertEquals("There should not be any intersections with our shapes.", 1, actualValue.size());

        // TC05: There is intersections with all shapes.
        intersectingRay = new Ray(new Point3D(2, 0, 0), axisRay.getDir());
        actualValue = geom.findIntersections(intersectingRay);
        assertEquals("There should be 5 intersections with our shapes.", 5, actualValue.size());
    }

}
