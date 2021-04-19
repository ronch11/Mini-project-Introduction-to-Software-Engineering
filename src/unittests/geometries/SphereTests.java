package unittests.geometries;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Sphere class
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class SphereTests {

        /**
         * Test method for {@link geometries.Sphere#getNormal(primitives.Point3D)}.
         */
        @Test
        public void testGetNormal() {
                // ============ Equivalence Partitions Tests ==============
                // TC01: There is a simple single test here
                Sphere sphere = new Sphere(new Point3D(0, 0, 1), 5);
                Point3D p = new Point3D(0, 0, 6);

                Vector expected = new Vector(0, 0, 1);
                Vector actualValue = p.subtract(sphere.getCenter()).normalize();

                assertEquals("Bad normal to Sphere", expected, actualValue);
        }

        /**
         * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
         */
        @Test
        public void testFindIntersections() {
                // setup
                Sphere sphere = new Sphere(new Point3D(1, 0, 0), 1d);
                Vector xAxisPositiveDir = new Vector(1, 0, 0); // unit vector in the positive x axis for creating Rays.
                Vector xAxisNegativeDir = xAxisPositiveDir.scale(-1d); // unit vector in the negative x axis for
                                                                       // creating Rays.
                Point3D p0 = new Point3D(-1, 0, 0); // used many time in code so creating it once for optimize test.
                // p1 and p2 for EP TC02
                Point3D p1 = new Point3D(0.0651530771650466, 0.355051025721682, 0);
                Point3D p2 = new Point3D(1.53484692283495, 0.844948974278318, 0);
                Point3D p3 = new Point3D(0.2, 0.2, 0.2); // for EP TC03
                Point3D p4 = new Point3D(1.9591663046625438, 0.2, 0.2); // for EP TC03
                Point3D p5 = new Point3D(0.5, 0.5, 1 / Math.sqrt(2)); // for BVA TC11 and TC12 and EP TC04
                Point3D p6 = new Point3D(1.5, 0.5, 1 / Math.sqrt(2)); // for BVA TC11
                Point3D p7 = new Point3D(2, 0, 0);
                Point3D p8 = new Point3D(0.5, 0, 0);
                Point3D p9 = new Point3D(3, 0, 0); // optimize code create the point only once for BVA TC12 and TC18
                Point3D p10 = new Point3D(1, 0, 1); // optimize code create the point only once for BVA TC19, TC20 and
                                                    // TC21
                // ============ Equivalence Partitions Tests ==============

                // TC01: Ray's line is outside the sphere (0 points)
                assertNull("TC01: Ray's line out of sphere",
                                sphere.findIntersections(new Ray(p0, new Vector(1, 1, 1))));

                // TC02: Ray starts before and crosses the sphere (2 points)
                List<Point3D> result = sphere.findIntersections(new Ray(p0, new Vector(3, 1, 0)));
                assertEquals("TC02: Wrong number of points", 2, result.size());
                if (result.get(0).getX() > result.get(1).getX())
                        result = List.of(result.get(1), result.get(0));
                assertEquals("TC02: Ray crosses sphere", List.of(p1, p2), result);

                // TC03: Ray starts inside the sphere (1 point)
                result = sphere.findIntersections(new Ray(p3, xAxisPositiveDir));
                assertEquals("Number of Intersection Points should be 1.", 1, result.size());
                assertEquals("Ray should cross the sphere only once", List.of(p4), result);

                // TC04: Ray starts after the sphere (0 points)
                assertNull("Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(new Point3D(1, 1, 1), new Vector(0, 1, 1))));
                // =============== Boundary Values Tests ==================

                // **** Group: Ray's line crosses the sphere (but not the center)
                // TC11: Ray starts at sphere and goes inside (1 points)
                result = sphere.findIntersections(new Ray(p5, xAxisPositiveDir));
                assertEquals("TC11: Number of Intersection Points should be 1.", 1, result.size());
                assertEquals("TC11: Ray should cross the sphere only once", List.of(p6), result);

                // TC12: Ray starts at sphere and goes outside (0 points)
                // scale with -1 to make ray go in the direction of the negative X axis.
                assertNull("TC12: Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(p5, xAxisNegativeDir)));
                // **** Group: Ray's line goes through the center

                // TC13: Ray starts before the sphere (2 points)
                result = sphere.findIntersections(new Ray(p0, xAxisPositiveDir));
                assertEquals("TC13: Number of intersection points should be 2.", 2, result.size());
                if (result.get(0).getX() > result.get(1).getX())
                        result = List.of(result.get(1), result.get(0));
                assertEquals("TC13: Ray crosses sphere", List.of(Point3D.ZERO, p7), result);

                // TC14: Ray starts at sphere and goes inside (1 points)
                result = sphere.findIntersections(new Ray(p7, xAxisNegativeDir));
                assertEquals("TC14: Number of Intersection Points should be 1.", 1, result.size());
                assertEquals("TC14: Ray should cross the sphere only once", List.of(Point3D.ZERO), result);

                // TC15: Ray starts inside (1 points)
                result = sphere.findIntersections(new Ray(p8, xAxisPositiveDir));
                assertEquals("TC15: Number of Intersection Points should be 1.", 1, result.size());
                assertEquals("TC15: Ray crosses sphere", List.of(p7), result);

                // TC16: Ray starts at the center (1 points)
                result = sphere.findIntersections(new Ray(new Point3D(1, 0, 0), xAxisPositiveDir));
                assertEquals("TC16: Number of Intersection Points should be 1.", 1, result.size());
                assertEquals("TC16: Ray crosses sphere", List.of(p7), result);

                // TC17: Ray starts at sphere and goes outside (0 points)
                assertNull("TC17: Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(p7, xAxisPositiveDir)));

                // TC18: Ray starts after sphere (0 points)
                assertNull("TC18: Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(p9, xAxisPositiveDir)));

                // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
                // TC19: Ray starts before the tangent point
                assertNull("TC19: Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(p10.add(xAxisNegativeDir), xAxisPositiveDir)));

                // TC20: Ray starts at the tangent point
                assertNull("TC20: Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(p10, xAxisPositiveDir)));

                // TC21: Ray starts after the tangent point
                assertNull("TC21: Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(p10.add(xAxisPositiveDir), xAxisPositiveDir)));

                // **** Group: Special cases
                // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's
                // center line
                assertNull("TC22: Number of Intersection Points should be null.",
                                sphere.findIntersections(new Ray(p10.add(new Vector(0, 0, 1)), xAxisPositiveDir)));
        }
}
