package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Triangle class
 */
public class TriangleTests {

        /**
         * Test method for {@link geometries.Triangle#getNormal(primitives.Point3D)}.
         */
        @Test
        public void testGetNormal() {
                // ============ Equivalence Partitions Tests ==============
                // TC01: There is a simple single test here
                Triangle triangle = new Triangle(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0));
                double sqrt3 = Math.sqrt(1d / 3);
                assertEquals("Bad normal to triangle", new Vector(sqrt3, sqrt3, sqrt3),
                                triangle.getNormal(new Point3D(0, 0, 1)));
        }

        /**
         * Test method for
         * {@link geometries.Triangle#findIntersections(primitives.Ray)}. This test does
         * not try to check for rays that does not cross the Plane of the triangle those
         * cases are covered by the plane tests.
         */
        @Test
        public void testFindIntersections() {
                // setup
                Point3D p0 = new Point3D(2, 0, 2);
                Point3D t1 = new Point3D(-5, 2, 1);
                Point3D t2 = new Point3D(5, 2, 1);
                Point3D t3 = new Point3D(0, 6, 6);
                Triangle triangle = new Triangle(t1, t2, t3);

                // ============ Equivalence Partitions Tests ==============
                // **** Group: Ray intersect the plane of the triangle:
                // TC01: Ray intersect the plane of the triangle inside the triangle
                Ray r1 = new Ray(p0, new Vector(0, 1, 0));

                List<Point3D> actualValue = triangle.findIntersections(r1);
                assertEquals("TC01: The ray should intersected the triangle once.", 1, actualValue.size());

                assertEquals("TC01: Wrong values in the intersection point.", List.of(new Point3D(2, 2.8, 2)),
                                actualValue);

                // TC02: Ray intersect the plane of the triangle outside the triangle(against
                // edge)
                assertNull("TC02: triangle should not intersected by Ray.",
                                triangle.findIntersections(new Ray(p0, new Vector(1, 1, 0))));

                // TC03: Ray intersect the plane of the triangle outside the triangle(against
                // vertex)
                assertNull("TC02: triangle should not intersected by Ray.",
                                triangle.findIntersections(new Ray(p0, new Vector(6, 1.6, 0.5))));
                // =============== Boundary Values Tests ==================
                // TC04: Ray intersect Triangle on edge
                Ray r2 = new Ray(p0, new Vector(0.5, 4, 1.5));                
                assertNull("TC04: The ray should intersected the triangle once.",triangle.findIntersections(r2));

                // TC05: Ray intersect Triangle on vertex
                assertNull("TC05: triangle should not intersected by Ray.",
                                triangle.findIntersections(new Ray(p0, new Vector(3, 2, -1))));

                // TC06: Ray intersect on plane on line continuation of the Triangle edge
                assertNull("TC06: triangle should not intersected by Ray.",
                                triangle.findIntersections(new Ray(p0, new Vector(4, 2, -1))));

        }
}
