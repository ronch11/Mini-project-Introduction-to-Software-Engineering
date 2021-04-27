package unittests.primitives;

import static org.junit.Assert.*;
import org.junit.Test;

import primitives.Point3D;
import static primitives.Util.*;

public class Point3DTests {

    @Test
    public void testPoint3DDistance() {

        // Setup:
        Point3D p0 = new Point3D(0, 0, 1);
        Point3D p1 = new Point3D(0, 0, 2);
        Point3D n0 = new Point3D(0, 0, -1);
        Point3D n1 = new Point3D(0, 0, -2);
        // ##################### Equivalence Partitions #####################
        // TC01: calculate distance between positive numbers points.
        double distance = alignZero(p0.distance(p1));
        assertTrue("TC01: Should have distance of 1.", isZero(1 - distance));
        // TC02: calculate distance between negative and positive point.
        distance = alignZero(p0.distance(n1));
        assertTrue("TC02: Should have distance of 3.", isZero(3 - distance));
        // TC03: calculate distance between negative numbers points.
        distance = alignZero(n0.distance(n1));
        assertTrue("TC03: Should have distance of 1.", isZero(1 - distance));
        // ##################### Boundary Values #####################
        // TC04: distance with ZERO.
        distance = alignZero(p0.distance(Point3D.ZERO));
        assertTrue("TC04: Should have distance of 1.", isZero(1 - distance));
        // TC05: distance with equal point.
        distance = alignZero(p0.distance(p0));
        assertTrue("TC05: Should have distance of 0.", isZero(distance));
    }
}
