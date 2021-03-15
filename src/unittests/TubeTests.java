package unittests;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

public class TubeTests {

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test getNormal of Tube.
        // setup
        Point3D p = new Point3D(-5, 0, 4.32);
        Point3D p0 = new Point3D(0, 0, 1);
        Vector dirVector = new Vector(0, 0, 1);
        Ray axisRay = new Ray(p0, dirVector);
        Tube tube = new Tube(axisRay, 5);

        // calculate the o point.
        Vector vector = p.subtract(tube.getAxisRay().getP0());
        double t = tube.getAxisRay().getDir().dotProduct(vector);
        Point3D o = tube.getAxisRay().getP0().add(tube.getAxisRay().getDir().scale(t));

        // test for normal
        Vector normal = p.subtract(o).normalize();

        Vector expected = new Vector(-1, 0, 0);
        assertEquals("normal was not correct.", expected, normal);
    }
}
