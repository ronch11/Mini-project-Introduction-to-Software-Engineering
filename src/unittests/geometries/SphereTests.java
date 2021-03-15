package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

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
}
