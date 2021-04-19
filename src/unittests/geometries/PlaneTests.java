package unittests.geometries;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit Testing Cylinders
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class PlaneTests {
    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        double sqrt3 = Math.sqrt(1d / 3);
        Plane plane = new Plane(new Point3D(0, 0, 1), new Vector(sqrt3, sqrt3, sqrt3));

        assertEquals("Bad normal to Plane", new Vector(sqrt3, sqrt3, sqrt3), plane.getNormal(new Point3D(0, 0, 1)));
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersection() {
        // setup
        Point3D q0 = new Point3D(1, 0, 1);
        Vector dir = new Vector(q0);
        Plane plane = new Plane(q0, dir);
        Point3D p1 = new Point3D(1, 0, 0);
        Point3D p2 = new Point3D(1.5, 0, 0.5);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is before and do not cross the plane (0 points)
        assertNull("TC01: Plane should not intersected by Ray.", plane.findIntersections(new Ray(p1, dir.scale(-1d))));

        // TC02: Ray starts before and crosses the plane (1 point)
        Ray r1 = new Ray(p1, dir);

        List<Point3D> actualValue = plane.findIntersections(r1);
        assertEquals("TC02: The ray should intersected the Plane once.", 1, actualValue.size());

        assertEquals("TC02: Wrong values in the intersection point.", List.of(p2), actualValue);

        // TC03: Ray starts on the plane (0 points)
        assertNull("TC03: Plane should not intersected by Ray.", plane.findIntersections(new Ray(p2, dir)));

        // TC04: Ray starts after the plane (0 points)
        assertNull("TC04: Plane should not intersected by Ray.",
                plane.findIntersections(new Ray(new Point3D(2, 0, 1), dir)));

        // =============== Boundary Values Tests ==================

        // TC05: ray starts before Plane and go through the plane at the q0 point.
        Point3D p3 = new Point3D(-2, 0, -2);
        Ray r2 = new Ray(p3, dir);
        actualValue = plane.findIntersections(r2);
        assertEquals("TC05: The ray should intersected the Plane once.", 1, actualValue.size());

        assertEquals("TC05: Wrong values in the intersection point.", List.of(q0), actualValue);

        // TC06: ray starts at the Plane at the q0 point.
        assertNull("TC06: Plane should not intersected by Ray.", plane.findIntersections(new Ray(q0, dir)));

        // **** Group: Ray's line is orthogonal to the plane Normal
        Vector v1 = new Vector(0, 1, 0);
        // TC07: Ray start before the Plane (0 points)
        assertNull("TC07: Plane should not intersected by Ray.", plane.findIntersections(new Ray(p3, v1)));

        // TC08: Ray start on the Plane (0 points)
        assertNull("TC08: Plane should not intersected by Ray.", plane.findIntersections(new Ray(p2, v1)));

        // **** Group: Special cases
    }

}
