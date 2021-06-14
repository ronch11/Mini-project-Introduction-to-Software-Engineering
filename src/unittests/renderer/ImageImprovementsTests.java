package unittests.renderer;

import org.junit.Test;

import elements.*;
import primitives.*;
import geometries.*;
import renderer.ImageWriter;
import renderer.MultiThreadsRender;
import renderer.RayTracerBasic;
import renderer.RayTracerBeams;
import renderer.RenderBase;
import scene.Scene;

/**
 * class for creating images with soft shadows.
 */
public class ImageImprovementsTests {

        private Scene scene1 = new Scene("Test scene") //
                        .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        private Camera camera1 = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                        .setViewPlaneSize(200, 200) //
                        .setViewPlaneDistance(1000);

        private Scene scene2 = new Scene("Test scene");

        Camera camera2 = new Camera(new Point3D(1000, 0, 250), new Vector(-1000, 0, -250), new Vector(-250, 0, 1000)) //
                        .setViewPlaneSize(200, 200).setViewPlaneDistance(1000);

        private static Geometry triangle1 = new Triangle( //
                        new Point3D(-150, -150, -150), new Point3D(150, -150, -150), new Point3D(75, 75, -150));
        private static Geometry triangle2 = new Triangle( //
                        new Point3D(-150, -150, -150), new Point3D(-70, 70, -50), new Point3D(75, 75, -150));

        /**
         * Produce a picture of a two triangles lighted by a narrow spot light
         */
        @Test
        public void testsSS() {
                scene1.geometries.add(triangle1.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)),
                                triangle2.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)));
                scene1.geometries.add(
                                new Sphere(new Point3D(-60, -80, -150), 20).setEmission(new Color(java.awt.Color.BLUE)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(100)));

                scene1.lights.add(new SpotLight(new Color(800, 400, 400), new Point3D(10, -10, -130),
                                new Vector(-2, -2, -1)).setNarrowBeam(3).setKC(1).setKL(0.000005).setKQ(0.00000025)
                                                .setSquareEdge(30));

                ImageWriter imageWriter = new ImageWriter("trianglesSpotSharpSoftShadow", 500, 500);
                RenderBase render = new MultiThreadsRender() //
                                .setDebugPrint().setMultithreading(3) //
                                .setImageWriter(imageWriter) //
                                .setCamera(camera1).setRayTracer(new RayTracerBeams(scene1).setNumOfRays(10));
                render.renderImage();
                render.writeToImage();
        }

        /**
         * setting scene 2 for AA test and SS test.
         */
        private void setScene2() {
                scene2.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
                scene2.geometries.add( //
                                new Polygon(new Point3D(200, 200, 0), new Point3D(200, -200, 0),
                                                new Point3D(-200, -200, 0), new Point3D(-200, 200, 0)) //
                                                                .setEmission(new Color(java.awt.Color.BLACK)) //
                                                                .setMaterial(new Material().setKD(0.5).setKS(0.5)
                                                                                .setNShininess(30)),
                                new Sphere(new Point3D(0, 0, 5), 5) //
                                                .setEmission(new Color(java.awt.Color.BLUE)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(20, 30, 10), 10) //
                                                .setEmission(new Color(java.awt.Color.PINK)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(-40, -60, 30), 30) //
                                                .setEmission(new Color(java.awt.Color.YELLOW)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(40, -20, 12), 12) //
                                                .setEmission(new Color(java.awt.Color.GRAY)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(-30, 50, 20), 20) //
                                                .setEmission(new Color(java.awt.Color.DARK_GRAY)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(70, 70, 15), 15) //
                                                .setEmission(new Color(java.awt.Color.BLUE)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(-60, 0, 20), 20) //
                                                .setEmission(new Color(java.awt.Color.PINK)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(160, 0, 20), 20) //
                                                .setEmission(new Color(java.awt.Color.CYAN)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(120, -60, 20), 20) //
                                                .setEmission(new Color(java.awt.Color.RED)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)),
                                new Sphere(new Point3D(-30, 50, 20), 20) //
                                                .setEmission(new Color(java.awt.Color.DARK_GRAY)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)));
        }

        /**
         * soft shadows improvement picture
         */
        @Test
        public void softTest() {
                setScene2();

                scene2.lights.add(new SpotLight(new Color(700, 700, 700), new Point3D(200, -200, 200),
                                new Vector(-1, 1, -1)) //
                                                .setKL(4E-4).setKQ(2E-5).setSquareEdge(30));
                scene2.lights.add(new DirectionalLight(new Color(100, 100, 100), new Vector(-1, 1, -1)));
                scene2.lights.add(new PointLight(new Color(500, 250, 250), new Point3D(150, 0, 100)) //
                                .setKL(0.0005).setKQ(0.0005));

                RenderBase render = new MultiThreadsRender() //
                                .setDebugPrint().setMultithreading(3)
                                .setImageWriter(new ImageWriter("softShadowsBeforeImprovement", 600, 600)) //
                                .setCamera(camera2) //
                                .setRayTracer(new RayTracerBasic(scene2));
                render.renderImage();
                render.writeToImage();
                render = new MultiThreadsRender() //
                                .setDebugPrint().setMultithreading(3)
                                .setImageWriter(new ImageWriter("softShadowsWithImprovement", 600, 600)) //
                                .setCamera(camera2) //
                                .setRayTracer(new RayTracerBeams(scene2).setNumOfRays(500));
                render.renderImage();
                render.writeToImage();
        }

        /**
         * A function to generate picture to check the difference in anti aliasing
         * implementation.
         */
        @Test
        public void antiAliasingTest() {
                setScene2();
                RenderBase render = new MultiThreadsRender().setDebugPrint().setMultithreading(3).setCamera(camera2)
                                .setImageWriter(new ImageWriter("antialiasingBeforeImprovement", 600, 600))
                                .setRayTracer(new RayTracerBasic(scene2));

                render.renderImage();
                render.writeToImage();

                render = new MultiThreadsRender() //
                                .setDebugPrint().setMultithreading(3).setAntiAliasingLevel(3)
                                .setImageWriter(new ImageWriter("antialiasingWithImprovement", 600, 600)) //
                                .setCamera(camera2) //
                                .setRayTracer(new RayTracerBeams(scene2));
                render.renderImage();
                render.writeToImage();
        }

}
