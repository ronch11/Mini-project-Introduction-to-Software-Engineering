package unittests.renderer;

import org.junit.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Test rendering a basic image
 * 
 * @author Dan
 */
public class RenderTests {
	private Camera camera = new Camera(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setViewPlaneDistance(100) //
			.setViewPlaneSize(500, 500);

	/**
	 * Produce a scene with basic 3D model and render it into a jpeg image with a
	 * grid
	 */
	@Test
	public void basicRenderTwoColorTest() {

		Scene scene = new Scene("Test scene")//
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191), 1)) //
				.setBackground(new Color(75, 127, 90));

		scene.geometries.add((BoundableGeometry) new Sphere(new Point3D(0, 0, -100), 50),
				(BoundableGeometry) new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100),
						new Point3D(-100, 100, -100)), // up
				// left
				(BoundableGeometry) new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100),
						new Point3D(100, 100, -100)), // up
				// right
				(BoundableGeometry) new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100),
						new Point3D(-100, -100, -100)), // down
				// left
				(BoundableGeometry) new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100),
						new Point3D(100, -100, -100))); // down
		// right

		ImageWriter imageWriter = new ImageWriter("base render test", 1000, 1000);
		RenderBase render = new Render() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene));

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.YELLOW));
		render.writeToImage();
	}

	/**
	 * Test for XML based scene - for bonus
	 * 
	 * @throws Exception
	 */
	@Test
	public void basicRenderXml() {
		Scene scene = new Scene("XML Test scene");
		// enter XML file name and parse from XML file into scene object
		String xmlFileName = "basicRenderTestTwoColors";
		ImageWriter imageWriter = new ImageWriter("xml render test", 1000, 1000);

		RenderBase render = new Render().setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene).loadFromXml(xmlFileName));

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.YELLOW));
		render.writeToImage();
	}

	@Test
	public void basicRenderMultiColorTest() {
		Scene scene = new Scene("Test scene")//
				.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.2)); //

		scene.geometries.add((BoundableGeometry) new Sphere(new Point3D(0, 0, -100), 50), //
				(BoundableGeometry) new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100),
						new Point3D(-100, 100, -100)) // up
								// left
								.setEmission(new Color(java.awt.Color.GREEN)),
				(BoundableGeometry) new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100),
						new Point3D(100, 100, -100)), // up
				// right
				(BoundableGeometry) new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100),
						new Point3D(-100, -100, -100)) // down
								// left
								.setEmission(new Color(java.awt.Color.RED)),
				(BoundableGeometry) new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100),
						new Point3D(100, -100, -100)) // down
								// right
								.setEmission(new Color(java.awt.Color.BLUE)));

		ImageWriter imageWriter = new ImageWriter("color render test", 1000, 1000);
		RenderBase render = new Render() //
				.setImageWriter(imageWriter) //
				.setCamera(camera) //
				.setRayTracer(new RayTracerBasic(scene));

		render.renderImage();
		render.printGrid(100, new Color(java.awt.Color.WHITE));
		render.writeToImage();
	}
}
