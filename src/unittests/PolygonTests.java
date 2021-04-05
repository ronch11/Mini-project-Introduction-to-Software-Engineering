package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Testing Polygons
 * 
 * @author Dan
 *
 */
public class PolygonTests {

    /**
     * Test method for
     * {@link geometries.Polygon#Polygon(primitives.Point3D, primitives.Point3D, primitives.Point3D, primitives.Point3D)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(0, 1, 0), new Point3D(1, 0, 0), new Point3D(-1, 1, 1));
            fail("Constructed a polygon with wrong order of vertices");
        } catch (IllegalArgumentException e) {
        }

        // TC03: Not in the same plane
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(0, 2, 2));
            fail("Constructed a polygon with vertices that are not in the same plane");
        } catch (IllegalArgumentException e) {
        }

        // TC04: Concave quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(0.5, 0.25, 0.5));
            fail("Constructed a concave polygon");
        } catch (IllegalArgumentException e) {
        }

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(0, 0.5, 0.5));
            fail("Constructed a polygon with vertix on a side");
        } catch (IllegalArgumentException e) {
        }

        // TC11: Last point = first point
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(0, 0, 1));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {
        }

        // TC12: Colocated points
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0), new Point3D(0, 1, 0));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {
        }

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0),
                new Point3D(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals("Bad normal to polygon", new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)));
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     * This test does not try to check for rays that does not cross the Plane of the
     * polygon those cases are covered by the plane tests.
     */
    @Test
    public void testFindIntersections() {
        // setup
        Polygon pl = new Polygon(new Point3D(0, 0, 4), new Point3D(4, 0, 0), new Point3D(0, 4, 0),
                new Point3D(-4, 4, 4));
        Point3D p0 = new Point3D(-1, 0, 1);

        // ============ Equivalence Partitions Tests ==============
        // **** Group: Ray intersect the plane of the polygon:
        // TC01: Ray intersect the plane of the polygon inside the polygon
        Ray r1 = new Ray(p0, new Vector(1, 1, 0));

        List<Point3D> actualValue = pl.findIntersections(r1);
        assertEquals("TC01: The ray should intersected the polygon once.", 1, actualValue.size());

        assertEquals("TC01: Wrong values in the intersection point.", List.of(new Point3D(1, 2, 1)), actualValue);

        // TC02: Ray intersect the plane of the polygon outside the polygon(against
        // edge)
        assertNull("TC02: polygon should not intersected by Ray.",
                pl.findIntersections(new Ray(p0, new Vector(1, 1, 10))));

        // TC03: Ray intersect the plane of the polygon outside the polygon(against
        // vertex)
        assertNull("TC02: polygon should not intersected by Ray.",
                pl.findIntersections(new Ray(p0, new Vector(1, -1, 4))));
        // =============== Boundary Values Tests ==================
        // TC04: Ray intersect Polygon on edge
        Ray r2 = new Ray(p0, new Vector(-0.5, 1.5, 3));
        assertNull("TC04: The ray should intersected the polygon once.", pl.findIntersections(r2));

        // TC05: Ray intersect Polygon on vertex
        assertNull("TC05: polygon should not intersected by Ray.",
                pl.findIntersections(new Ray(p0, new Vector(1, 0, 3))));

        // TC06: Ray intersect on plane on line continuation of the Polygon edge
        assertNull("TC06: polygon should not intersected by Ray.",
                pl.findIntersections(new Ray(p0, new Vector(3, -2, 3))));

    }
}
