package unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import elements.Camera;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Integration Test of the Camera Ray constructs function with intersection
 * finding capabilities of intersectable geometries.
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * test Sphere Plane and Triangle intersections with Camera Rays.
     */
    @Test
    public void testCameraFindIntersection() {
        // setup constants
        int pixelNumberOfCols = 3;
        int pixelNumberOfRows = 3;
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

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = sphere.findIntersections(cameraRay);
                if (i == 1 && j == 1) {
                    assertEquals("TC01: The Center Ray should intersect the sphere 2 times.", 2, actualValue.size());
                } else {
                    assertNull("TC01: Only the center ray should intersect the sphere.", actualValue);
                }
            }
        }

        // TC02: all ray from camera through View Panel intersect twice.
        sphere = new Sphere(new Point3D(0, 0, -2.5), 2.5);
        camera = new Camera(new Point3D(0, 0, 0.5), camVTo, camVUp);
        camera.setViewPlaneDistance(distance);
        camera.setViewPlaneSize(vpHeight, vpWidth);

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = sphere.findIntersections(cameraRay);
                assertEquals("TC02: All Rays should intersect the sphere 2 times.", 2, actualValue.size());
            }
        }

        // TC03: all Ray but Corner Rays intersect the Sphere.
        sphere = new Sphere(new Point3D(0, 0, -2), 2);

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = sphere.findIntersections(cameraRay);
                if (i == 1 || j == 1) { // if it's not a corner ray
                    assertEquals("TC03: The Center Ray should intersect the sphere 2 times.", 2, actualValue.size());
                } else {
                    assertNull("TC03: Corner rays should not intersect the sphere.", actualValue);
                }
            }
        }

        // TC04: camera inside Sphere so only 9 intersection (1 for each pixel).
        sphere = new Sphere(new Point3D(0, 0, -2), 4);

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = sphere.findIntersections(cameraRay);
                assertEquals("TC04: All Rays should intersect the sphere one time only.", 1, actualValue.size());
            }
        }

        // TC05: no intersections because camera is after the Sphere.
        sphere = new Sphere(new Point3D(0, 0, 1), 0.5);

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = sphere.findIntersections(cameraRay);
                assertNull("TC05: All rays should not intersect the sphere.", actualValue);
            }
        }

        // TC06: plane is perpendicular to the Camera Vto Ray.
        Plane plane = new Plane(new Point3D(0, 0, -1), camVTo);

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = plane.findIntersections(cameraRay);
                assertEquals("TC06: All Rays should intersect the Plane one time only.", 1, actualValue.size());
            }
        }

        // TC07: Plane is diagonally aligned with camera but still all 9 pixels
        // intersects it.
        plane = new Plane(new Point3D(0, 0, -1), new Vector(0, 1, 5));

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = plane.findIntersections(cameraRay);
                assertEquals("TC07: All Rays should intersect the Plane one time only.", 1, actualValue.size());
            }
        }

        // TC08: Plane is intersected by Rays in the first 2 rows (last row does not
        // intersect).
        plane = new Plane(new Point3D(0, 0, -2), new Vector(0, -1, 1));

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = plane.findIntersections(cameraRay);
                if (i <= 1) {
                    assertEquals("TC08: Rays in first and second row should intersect the plane one time only.", 1,
                            actualValue.size());
                } else {
                    assertNull("TC08: Last row rays should not intersect the plane.", actualValue);
                }
            }
        }

        // TC09: Triangle should be intersected only by the center pixel.
        Triangle triangle = new Triangle(new Point3D(0, 1, -2), new Point3D(1, -1, -2), new Point3D(-1, -1, -2));

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = triangle.findIntersections(cameraRay);
                if (i == 1 && j == 1) {
                    assertEquals("TC09: The Center Ray should intersect the triangle once.", 1, actualValue.size());
                } else {
                    assertNull("TC09: Only the center ray should intersect the triangle.", actualValue);
                }
            }
        }

        // TC10: long triangle but narrow base so only center and upper rays intersect
        // with it.
        triangle = new Triangle(new Point3D(0, 20, -2), new Point3D(1, -1, -2), new Point3D(-1, -1, -2));

        for (int i = 0; i < vpHeight; i++) {
            for (int j = 0; j < vpWidth; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(pixelNumberOfCols, pixelNumberOfRows, j, i);
                List<Point3D> actualValue = triangle.findIntersections(cameraRay);
                if ((i == 1 || i == 0) && j == 1) {
                    assertEquals("TC10: The center/upper Ray should intersect the triangle once.", 1,
                            actualValue.size());
                } else {
                    assertNull("TC10: Only the center/upper ray should intersect the triangle.", actualValue);
                }
            }
        }

    }
}
