package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import elements.Camera;
import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Integration Test of the Camera Ray constructs function
 * {@link elements.Camera#constructRayThroughPixel(int, int, int, int)} with
 * intersection finding capabilities of intersectable geometries
 * {@link geometries.Intersectable#findIntersections(Ray)}.
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * integration test of
     * {@link elements.Camera#constructRayThroughPixel(int, int, int, int)} and
     * {@link geometries.Sphere#findIntersections(Ray)}
     */
    @Test
    public void testCameraAndSphere() {
        // setup constants
        double distance = 1d;
        double vpWidth = 3d;
        double vpHeight = 3d;

        // TC01: only center ray (pixel #5 intersect the sphere).
        Sphere sphere = new Sphere(new Point3D(0, 0, -3), 1);
        Vector camVTo = new Vector(0, 0, -1);
        Vector camVUp = new Vector(0, 1, 0);
        Camera camera = new Camera(Point3D.ZERO, camVTo, camVUp);
        camera.setViewPlaneDistance(distance);
        camera.setViewPlaneSize(vpHeight, vpWidth);

        int actualValue = findAllIntersections(camera, sphere);
        assertEquals("TC01: The Center Ray should intersect the sphere 2 times.", 2, actualValue);

        // TC02: all ray from camera through View Panel intersect twice.
        sphere = new Sphere(new Point3D(0, 0, -2.5), 2.5);
        camera = new Camera(new Point3D(0, 0, 0.5), camVTo, camVUp);
        camera.setViewPlaneDistance(distance);
        camera.setViewPlaneSize(vpHeight, vpWidth);

        actualValue = findAllIntersections(camera, sphere);
        assertEquals("TC02: All Rays should intersect the sphere 2 times.", 18, actualValue);

        // TC03: all Ray but Corner Rays intersect the Sphere.
        sphere = new Sphere(new Point3D(0, 0, -2), 2);

        actualValue = findAllIntersections(camera, sphere);
        assertEquals("TC03: The non corner Rays should intersect the sphere 2 times.", 10, actualValue);

        // TC04: camera inside Sphere so only 9 intersection (1 for each pixel).
        sphere = new Sphere(new Point3D(0, 0, -2), 4);

        actualValue = findAllIntersections(camera, sphere);
        assertEquals("TC04: All Rays should intersect the sphere one time only.", 9, actualValue);

        // TC05: no intersections because camera is after the Sphere.
        sphere = new Sphere(new Point3D(0, 0, 1), 0.5);

        actualValue = findAllIntersections(camera, sphere);
        assertEquals("TC05: All rays should not intersect the sphere.", 0, actualValue);

    }

    /**
     * integration test of
     * {@link elements.Camera#constructRayThroughPixel(int, int, int, int)} and
     * {@link geometries.Plane#findIntersections(Ray)}
     */
    @Test
    public void testCameraAndPlane() {
        // setup constants
        double distance = 1d;
        double vpWidth = 3d;
        double vpHeight = 3d;
        Vector camVTo = new Vector(0, 0, -1);
        Vector camVUp = new Vector(0, 1, 0);
        Camera camera = new Camera(Point3D.ZERO, camVTo, camVUp);
        camera.setViewPlaneDistance(distance);
        camera.setViewPlaneSize(vpHeight, vpWidth);

        // TC01: plane is perpendicular to the Camera Vto Ray.
        Plane plane = new Plane(new Point3D(0, 0, -1), camVTo);
        int actualValue = findAllIntersections(camera, plane);
        assertEquals("TC01: All Rays should intersect the Plane one time only.", 9, actualValue);

        // TC02: Plane is diagonally aligned with camera but still all 9 pixels
        // intersects it.
        plane = new Plane(new Point3D(0, 0, -1), new Vector(0, 1, 5));

        actualValue = findAllIntersections(camera, plane);
        assertEquals("TC02: All Rays should intersect the Plane one time only.", 9, actualValue);

        // TC03: Plane is intersected by Rays in the first 2 rows (last row does not
        // intersect).
        plane = new Plane(new Point3D(0, 0, -2), new Vector(0, -1, 1));

        actualValue = findAllIntersections(camera, plane);
        assertEquals("TC03: Rays in first and second row should intersect the plane one time only.", 6, actualValue);
    }

    /**
     * integration test of
     * {@link elements.Camera#constructRayThroughPixel(int, int, int, int)} and
     * {@link geometries.Triangle#findIntersections(Ray)}
     */
    @Test
    public void testCameraAndTriangle() {
        // setup constants
        double distance = 1d;
        double vpWidth = 3d;
        double vpHeight = 3d;
        Vector camVTo = new Vector(0, 0, -1);
        Vector camVUp = new Vector(0, 1, 0);
        Camera camera = new Camera(Point3D.ZERO, camVTo, camVUp);
        camera.setViewPlaneDistance(distance);
        camera.setViewPlaneSize(vpHeight, vpWidth);

        // TC01: Triangle should be intersected only by the center pixel.
        Triangle triangle = new Triangle(new Point3D(0, 1, -2), new Point3D(1, -1, -2), new Point3D(-1, -1, -2));
        int actualValue = findAllIntersections(camera, triangle);
        assertEquals("TC01: Only the Center Ray should intersect the triangle once.", 1, actualValue);

        // TC02: long triangle but narrow base so only center and upper rays intersect
        // with it.
        triangle = new Triangle(new Point3D(0, 20, -2), new Point3D(1, -1, -2), new Point3D(-1, -1, -2));

        actualValue = findAllIntersections(camera, triangle);
        assertEquals("TC02: The center/upper Ray should intersect the triangle once.", 2, actualValue);
    }

    /**
     * A helper function to find all intersection point of camera view panel rays
     * and an intersectable shape
     * 
     * @param camera Camera - a camera we want to find intersection with.
     * @param shape  Intersectable - a Geometry that has implement the Intersectable
     *               interface.
     * @return Int - total number of intersection points.
     */
    private int findAllIntersections(Camera camera, Intersectable shape) {
        int pixelNumberOfCols = 3;
        int pixelNumberOfRows = 3;
        double vpWidth = 3d;
        double vpHeight = 3d;

        int sumOfIntersection = 0;
        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = shape.findIntersections(cameraRay);
                sumOfIntersection += actualValue == null ? 0 : actualValue.size();
            }
        }
        return sumOfIntersection;
    }
}
