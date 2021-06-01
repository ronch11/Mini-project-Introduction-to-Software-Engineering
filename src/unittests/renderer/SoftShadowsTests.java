package unittests.renderer;

import org.junit.Test;

import elements.*;
import primitives.*;
import geometries.*;
import renderer.ImageWriter;
import renderer.MultiThreadsRender;
import renderer.RayTracerBasic;
import renderer.RayTracerBeams;
import renderer.Render;
import scene.Scene;

public class SoftShadowsTests {
        private Scene scene = new Scene("Test scene");

        private Scene scene2 = new Scene("Test scene") //
                        .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
        private Camera camera2 = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                        .setViewPlaneSize(200, 200) //
                        .setViewPlaneDistance(1000);

        private static Geometry triangle1 = new Triangle( //
                        new Point3D(-150, -150, -150), new Point3D(150, -150, -150), new Point3D(75, 75, -150));
        private static Geometry triangle2 = new Triangle( //
                        new Point3D(-150, -150, -150), new Point3D(-70, 70, -50), new Point3D(75, 75, -150));

        /**
         * Produce a picture of a two triangles lighted by a narrow spot light
         */
        @Test
        public void testsSS() {

                scene2.geometries.add(triangle1.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)),
                                triangle2.setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(300)));
                scene2.geometries.add(
                                new Sphere(new Point3D(-60, -80, -150), 20).setEmission(new Color(java.awt.Color.BLUE)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(100)));
                scene2.lights.add(new SpotLight(new Color(800, 400, 400), new Point3D(10, -10, -130),
                                new Vector(-2, -2, -1)).setNarrowBeam(5).setKC(1).setKL(0.000005).setKQ(0.00000025)
                                                .setRadius(50));

                ImageWriter imageWriter = new ImageWriter("trianglesSpotSharpTry3", 500, 500);
                MultiThreadsRender render = new MultiThreadsRender().setDebugPrint().setMultithreading(3) //
                                .setImageWriter(imageWriter) //
                                .setCamera(camera2) //
                                // .setRayTracer(new RayTracerBasic(scene2));
                                .setRayTracer(new RayTracerBeams(scene2).setNumOfRays(100));
                render.renderImage();
                // render.printGrid(50,new Color(300,300,300));
                render.writeToImage();

        }

        /**
         * soft shadows improvement picture
         */
        @Test
        public void softTest() {
                int a = 250;
                Vector vto = new Vector(-1000, 0, -a);
                Vector vup = new Vector(-a, 0, 1000);
                Camera camera = new Camera(new Point3D(1000, 0, a), vto, vup) //
                                .setViewPlaneSize(200, 200).setViewPlaneDistance(1000);
                scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
                scene.geometries.add(
                                new Polygon(new Point3D(200, 200, 0), new Point3D(200, -200, 0),
                                                new Point3D(-200, -200, 0), new Point3D(-200, 200, 0)) //
                                                                .setEmission(new Color(java.awt.Color.BLACK)) //
                                                                .setMaterial(new Material().setKD(0.5).setKS(0.5)
                                                                                .setNShininess(30)),
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
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)));
                scene.lights.add(new SpotLight(new Color(700, 700, 700), new Point3D(200, -200, 200),
                                new Vector(-1, 1, -1)) //
                                                .setKL(4E-4).setKQ(2E-5).setRadius(30));
                scene.lights.add(new DirectionalLight(new Color(100, 100, 100), new Vector(-1, 1, -1)));
                scene.lights.add(new PointLight(new Color(500, 250, 250), new Point3D(150, 0, 100)) //
                                .setKL(0.0005).setKQ(0.0005));

                MultiThreadsRender render = new MultiThreadsRender() //
                                .setImageWriter(new ImageWriter("softShadowsBeforeImprovement", 600, 600)) //
                                .setCamera(camera) //
                                .setRayTracer(new RayTracerBasic(scene)).setDebugPrint().setMultithreading(3);
                render.renderImage();
                render.writeToImage();
                render = new MultiThreadsRender() //
                                .setImageWriter(new ImageWriter("softShadowsWithImprovement", 600, 600)) //
                                .setCamera(camera) //
                                .setRayTracer(new RayTracerBeams(scene).setNumOfRays(500)).setDebugPrint()
                                .setMultithreading(3);
                render.renderImage();
                render.writeToImage();
        }

}
