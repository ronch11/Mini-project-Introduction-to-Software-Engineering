package unittests.elements;

import static org.junit.Assert.*;

import org.junit.Test;

import elements.Camera;
import primitives.*;

/**
 * Testing Camera Class
 * 
 * @author Dan
 *
 */
public class CameraTest {

	/**
	 * Test method for
	 * {@link elements.Camera#constructRayThroughPixel(int, int, int, int)}.
	 */
	@Test
	public void testConstructRayThroughPixel() {
		Camera camera = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0)).setViewPlaneDistance(10);

		// ============ Equivalence Partitions Tests ==============
		// TC01: 3X3 Corner (0,0)
		assertEquals("Bad ray", new Ray(Point3D.ZERO, new Vector(-2, -2, 10)),
				camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 0, 0));

		// TC02: 4X4 Corner (0,0)
		assertEquals("Bad ray", new Ray(Point3D.ZERO, new Vector(-3, -3, 10)),
				camera.setViewPlaneSize(8, 8).constructRayThroughPixel(4, 4, 0, 0));

		// TC03: 4X4 Side (0,1)
		assertEquals("Bad ray", new Ray(Point3D.ZERO, new Vector(-1, -3, 10)),
				camera.setViewPlaneSize(8, 8).constructRayThroughPixel(4, 4, 1, 0));

		// TC04: 4X4 Inside (1,1)
		assertEquals("Bad ray", new Ray(Point3D.ZERO, new Vector(-1, -1, 10)),
				camera.setViewPlaneSize(8, 8).constructRayThroughPixel(4, 4, 1, 1));

		// =============== Boundary Values Tests ==================
		// TC11: 3X3 Center (1,1)
		assertEquals("Bad ray", new Ray(Point3D.ZERO, new Vector(0, 0, 10)),
				camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 1, 1));

		// TC12: 3X3 Center of Upper Side (0,1)
		assertEquals("Bad ray", new Ray(Point3D.ZERO, new Vector(0, -2, 10)),
				camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 1, 0));

		// TC13: 3X3 Center of Left Side (1,0)
		assertEquals("Bad ray", new Ray(Point3D.ZERO, new Vector(-2, 0, 10)),
				camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 0, 1));

	}

	/**
	 * Test method for {@link elements.Camera#moveCamera(Point3D, Point3D)}.
	 */
	@Test
	public void testMoveCamera() {
		Camera camera = new Camera(new Point3D(1, 0, 0), new Vector(0, 0, -1), new Vector(0, 1, 0))
				.setViewPlaneDistance(10);
		camera.moveCamera(new Point3D(1, -10, -10), new Point3D(1, 0, -10));
		Camera expected = new Camera(new Point3D(1, -10, -10), new Vector(0, 1, 0), new Vector(0, 0, -1))
				.setViewPlaneDistance(10);
		assertEquals("Camera should still be looking at the same point.", expected, camera);
	}

	/**
	 * Test method for {@link elements.Camera#rotateCameraCounterClockWise()}.
	 */
	@Test
	public void testRotateCameraCounterClockWise() {
		Camera camera = new Camera(new Point3D(1, 0, 0), new Vector(0, 0, -1), new Vector(0, 1, 0))
				.setViewPlaneDistance(10);

		camera.rotateCameraCounterClockWise(90);

		Camera expected = new Camera(new Point3D(1, 0, 0), new Vector(0, 0, -1), new Vector(1, 0, 0))
				.setViewPlaneDistance(10);

		assertEquals("Camera should be rotated 90 degrees counter clockwise.", expected, camera);
	}

	/**
	 * Test method for {@link elements.Camera#rotateCameraClockWise()}.
	 */
	@Test
	public void testRotateCameraClockWise() {
		Camera camera = new Camera(new Point3D(1, 0, 0), new Vector(0, 0, -1), new Vector(0, 1, 0))
				.setViewPlaneDistance(10);

		camera.rotateCameraClockWise(90);

		Camera expected = new Camera(new Point3D(1, 0, 0), new Vector(0, 0, -1), new Vector(-1, 0, 0))
				.setViewPlaneDistance(10);

		assertEquals("Camera should be rotated 90 degrees clockwise.", expected, camera);
	}

}
