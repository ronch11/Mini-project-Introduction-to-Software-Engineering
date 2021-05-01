package unittests.primitives;

import static org.junit.Assert.*;
import org.junit.Test;

import static primitives.Util.*;

import java.util.LinkedList;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for primitives.Ray class
 */
public class RayTests {
    @Test
    public void testFindClosestPoint() {
        // Setup:
        Point3D p0 = new Point3D(0, 0, 1);
        Point3D p1 = new Point3D(1, 1, 2);
        Point3D p2 = new Point3D(2, 2, 3);
        Point3D p3 = new Point3D(3, 3, 4);
        Vector dir = new Vector(1, 1, 1);
        Ray ray = new Ray(p0, dir);
        // ============ Equivalence Partitions Tests ==============
        // TC01: The closest point is somewhere in the middle of the list.
        Point3D actualValue = ray.findClosestPoint(List.of(p2, p1, p3));
        assertEquals("TC01: Should get the (1,1,2) point", p1, actualValue);
        // =============== Boundary Values Tests ==================
        // TC02: The list is empty - returns null.
        actualValue = ray.findClosestPoint(new LinkedList<Point3D>());
        assertNull("TC02: should get back null on empty list.", actualValue);
        // TC03: The point is the head of list.
        actualValue = ray.findClosestPoint(List.of(p1, p2, p3));
        assertEquals("TC03: Should get the (1,1,2) point from the head of list.", p1, actualValue);
        // TC04: the point is the tail of list.
        actualValue = ray.findClosestPoint(List.of(p2, p3, p1));
        assertEquals("TC04: Should get the (1,1,2) point the tail of list.", p1, actualValue);
    }
}
