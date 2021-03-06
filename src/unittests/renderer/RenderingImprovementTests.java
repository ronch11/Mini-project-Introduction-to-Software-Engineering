package unittests.renderer;

import org.junit.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

public class RenderingImprovementTests {

        private Scene scene = new Scene("Test scene");

        Camera camera = new Camera(new Point3D(1000, 0, 250), new Vector(-1000, 0, -250), new Vector(-250, 0, 1000)) //
                        .setViewPlaneSize(200, 200).setViewPlaneDistance(1000);

        /**
         * setting scene for AA tests.
         */
        private void setScene() {
                scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
                scene.geometries.add( //
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

                for (int i = 0; i < 10; i++) {
                        scene.geometries.add(new Sphere(new Point3D(70, 70, 15), 15) //
                                        .setEmission(new Color(java.awt.Color.BLUE)) //
                                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setNShininess(30)));
                }

                scene.lights.add(new SpotLight(new Color(700, 700, 700), new Point3D(200, -200, 200),
                                new Vector(-1, 1, -1)) //
                                                .setKL(4E-4).setKQ(2E-5).setSquareEdge(30));
                scene.lights.add(new DirectionalLight(new Color(100, 100, 100), new Vector(-1, 1, -1)));
                scene.lights.add(new PointLight(new Color(500, 250, 250), new Point3D(150, 0, 100)) //
                                .setKL(0.0005).setKQ(0.0005));
        }

        @Test
        public void simpleSoftShadows() {
                setScene();
                RenderBase render = new MultiThreadsRender() //
                                .setDebugPrint().setMultithreading(3)
                                .setImageWriter(new ImageWriter("soft shadows simple", 600, 600)) //
                                .setCamera(camera) //
                                .setRayTracer(new RayTracerBeams(scene).setNumOfRays(500));
                render.renderImage();
                render.writeToImage();
        }

        @Test
        public void softShadowsBVH() {
                setScene();
                scene.geometries.buildBVHTree();
                RenderBase render = new MultiThreadsRender() //
                                .setDebugPrint().setMultithreading(3)
                                .setImageWriter(new ImageWriter("soft shadows BVH", 600, 600)) //
                                .setCamera(camera) //
                                .setRayTracer(new RayTracerBeams(scene).setNumOfRays(500));
                render.renderImage();
                render.writeToImage();
        }
}
