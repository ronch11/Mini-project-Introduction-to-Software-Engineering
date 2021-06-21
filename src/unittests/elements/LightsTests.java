package unittests.elements;

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
public class LightsTests {
	private Scene scene1 = new Scene("Test scene");
	private Scene scene2 = new Scene("Test scene") //
			.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
	private Camera camera1 = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setViewPlaneSize(150, 150) //
			.setViewPlaneDistance(1000);
	private Camera camera2 = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setViewPlaneSize(200, 200) //
			.setViewPlaneDistance(1000);

	private static Geometry triangle1 = new Triangle( //
			new Point3D(-150, -150, -150), new Point3D(150, -150, -150), new Point3D(75, 75, -150));
	private static Geometry triangle2 = new Triangle( //
			new Point3D(-150, -150, -150), new Point3D(-70, 70, -50), new Point3D(75, 75, -150));
	private static Geometry sphere = new Sphere(new Point3D(0, 0, -50), 50) //
			.setEmission(new Color(java.awt.Color.BLUE)) //
			.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(100));

	/**
	 * Produce a picture of a sphere lighted by a directional light
	 */
	@Test
	public void sphereDirectional() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new DirectionalLight(new Color(500, 300, 0), new Vector(1, 1, -1)));

		ImageWriter imageWriter = new ImageWriter("lightSphereDirectional", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera1) //
				.setRayTracer(new RayTracerBasic(scene1));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a point light
	 */
	@Test
	public void spherePoint() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new PointLight(new Color(500, 300, 0), new Point3D(-50, -50, 50))//
				.setKL(0.00001).setKQ(0.000001));

		ImageWriter imageWriter = new ImageWriter("lightSpherePoint", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera1) //
				.setRayTracer(new RayTracerBasic(scene1));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void sphereSpot() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new SpotLight(new Color(500, 300, 0), new Point3D(-50, -50, 50), new Vector(1, 1, -2)) //
				.setKL(0.00001).setKQ(0.00000001));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpot", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera1) //
				.setRayTracer(new RayTracerBasic(scene1));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a directional light
	 */
	@Test
	public void trianglesDirectional() {
		scene2.geometries.add(triangle1.setMaterial(new Material().setKD(0.8).setKS(0.2).setNShininess(300)), //
				triangle2.setMaterial(new Material().setKD(0.8).setKS(0.2).setNShininess(300)));
		scene2.lights.add(new DirectionalLight(new Color(300, 150, 150), new Vector(0, 0, -1)));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesDirectional", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera2) //
				.setRayTracer(new RayTracerBasic(scene2));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a point light
	 */
	@Test
	public void trianglesPoint() {
		scene2.geometries.add(triangle1.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)), //
				triangle2.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)));
		scene2.lights.add(new PointLight(new Color(500, 250, 250), new Point3D(10, -10, -130)) //
				.setKL(0.0005).setKQ(0.0005));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesPoint", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera2) //
				.setRayTracer(new RayTracerBasic(scene2));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light
	 */
	@Test
	public void trianglesSpot() {
		scene2.geometries.add(triangle1.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)),
				triangle2.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)));
		scene2.lights.add(new SpotLight(new Color(500, 250, 250), new Point3D(10, -10, -130), new Vector(-2, -2, -1)) //
				.setKL(0.0001).setKQ(0.000005));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpot", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera2) //
				.setRayTracer(new RayTracerBasic(scene2));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a narrow spot light
	 */
	@Test
	public void sphereSpotSharp() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new SpotLight(new Color(500, 300, 0), new Point3D(-50, -50, 50), new Vector(1, 1, -2)) //
				.setNarrowBeam(5).setKL(0.000005).setKQ(0.00000025));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpotSharp", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera1) //
				.setRayTracer(new RayTracerBasic(scene1));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a narrow spot light
	 */
	@Test
	public void trianglesSpotSharp() {
		scene2.geometries.add(triangle1.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)),
				triangle2.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)));
		scene2.lights.add(new SpotLight(new Color(800, 400, 400), new Point3D(10, -10, -130), new Vector(-2, -2, -1)) //
				.setNarrowBeam(5).setKL(0.00005).setKQ(0.0000025));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpotSharp", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera2) //
				.setRayTracer(new RayTracerBasic(scene2));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by all light sources
	 */
	@Test
	public void sphereOmniLights() {
		scene1.geometries.add(sphere);
		scene1.lights.add(new DirectionalLight(new Color(190, 115, 255).scale(2), new Vector(0.5, -0.5, -0.5)));
		scene1.lights.add(new PointLight(new Color(java.awt.Color.green).scale(2), new Point3D(60, 10, 10)) //
				.setKL(0.0005).setKQ(0.0005));
		scene1.lights.add(
				new SpotLight(new Color(java.awt.Color.red).scale(1.5), new Point3D(-30, -50, 40), new Vector(4, 2, -4)) //
						.setNarrowBeam(10).setKL(0.000005).setKQ(0.00000025));

		ImageWriter imageWriter = new ImageWriter("lightSphereOmniLights", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera1) //
				.setRayTracer(new RayTracerBasic(scene1));//
		render.renderImage();
		render.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by all light sources.
	 */
	@Test
	public void trianglesOmniLight() {
		scene2.geometries.add(triangle1.setMaterial(new Material().setKD(0.8).setKS(0.2).setNShininess(300)), //
				triangle2.setMaterial(new Material().setKD(0.8).setKS(0.2).setNShininess(300)));
		scene2.lights.add(new DirectionalLight(new Color(java.awt.Color.magenta).scale(1.2), new Vector(2, 2, -0.5)));
		scene2.lights.add(new PointLight(new Color(java.awt.Color.yellow), new Point3D(-20, 10, -90)) //
				.setKL(0.0005).setKQ(0.0005));
		scene2.lights.add(new SpotLight(new Color(java.awt.Color.blue).scale(1.5), new Point3D(10, -5, -120),
				new Vector(3, -4, -2)) //
						.setNarrowBeam(5).setKL(0.000005).setKQ(0.00000025));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesOmniLight", 500, 500);
		RenderBase render = new Render()//
				.setImageWriter(imageWriter) //
				.setCamera(camera2) //
				.setRayTracer(new RayTracerBasic(scene2));//
		render.renderImage();
		render.writeToImage();
	}

}
