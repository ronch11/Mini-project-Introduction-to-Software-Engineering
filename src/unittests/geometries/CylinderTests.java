package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

public class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test getNormal of Cylinder (not in bases).
        // setup
        Point3D p = new Point3D(-5, 0, 4.32);
        Point3D p0 = new Point3D(0, 0, 1);
        Vector dirVector = new Vector(0, 0, 1);
        Ray axisRay = new Ray(p0, dirVector);
        Cylinder cylinder = new Cylinder(axisRay, 5, 25);

        // calculate the o point.
        Vector vector = p.subtract(cylinder.getAxisRay().getP0());
        double t = cylinder.getAxisRay().getDir().dotProduct(vector);
        Point3D o = cylinder.getAxisRay().getP0().add(cylinder.getAxisRay().getDir().scale(t));

        // test for normal
        Vector normal = p.subtract(o).normalize(); // new Vector (x,y,z)

        Vector expected = new Vector(-1, 0, 0);
        assertEquals("normal was not correct.", expected, normal);

        // TC02: Test getNormal of Cylinder in first base.
        // two point that are on the same plane of the "circle" base in on.
        Point3D firstPoint = new Point3D(p0.x.coord, p0.y.coord + cylinder.getRadius(), p0.z.coord);
        Point3D secondPoint = new Point3D(p0.x.coord + cylinder.getRadius(), p0.y.coord, p0.z.coord);

        Vector v1 = firstPoint.subtract(p0);
        Vector v2 = secondPoint.subtract(p0);

        Vector crossVector = v1.crossProduct(v2).normalize();

        boolean normalIsOk = crossVector.equals(cylinder.getAxisRay().getDir())
                || crossVector.equals(cylinder.getAxisRay().getDir().scale(-1d));

        assertTrue("bad normal to first base of Cylinder", normalIsOk);

        // TC03: Test getNormal of Cylinder in second base.
        // two point that are on the same plane of the "circle" base in on.
        firstPoint = new Point3D(p0.x.coord, p0.y.coord + cylinder.getRadius(), p0.z.coord + cylinder.getHeight());
        secondPoint = new Point3D(p0.x.coord + cylinder.getRadius(), p0.y.coord, p0.z.coord + cylinder.getHeight());

        // p1 is the center of the second circular "base".
        Point3D p1 = p0.add(cylinder.getAxisRay().getDir().scale(cylinder.getHeight()));
        v1 = firstPoint.subtract(p1);
        v2 = secondPoint.subtract(p1);

        crossVector = v1.crossProduct(v2).normalize();

        normalIsOk = crossVector.equals(cylinder.getAxisRay().getDir())
                || crossVector.equals(cylinder.getAxisRay().getDir().scale(-1d));

        assertTrue("bad normal to second base of Cylinder", normalIsOk);

        // =============== Boundary Values Tests ==================
        // TC04: the "corner" of the first base and the side of the cylinder

        // TC05: the "corner" of the second base and the side of the cylinder

        // TC04: the center of the first base and the side of the cylinder

        // TC05: the center of the second base and the side of the cylinder
    }
}
