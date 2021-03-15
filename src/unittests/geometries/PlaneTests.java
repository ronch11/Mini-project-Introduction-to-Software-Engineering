package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

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

}
