package unittests;

import org.junit.Test;

import elements.AmbientLight;
import elements.Camera;
import elements.SpotLight;
import geometries.Sphere;
import geometries.Triangle;
import primitives.*;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import renderer.Render;
import renderer.RenderBase;
import scene.Scene;

/**
 * An integration Test to validate that camera moves / rotates correctly in
 * scene.
 */
public class CameraRotationAndMovementTestsInScene {
        private Camera camera1 = new Camera(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                        .setViewPlaneDistance(100) //
                        .setViewPlaneSize(500, 500);

        private Scene scene1 = new Scene("Test scene")//
                        .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.2)); //

        private Scene scene2 = new Scene("Test scene");
        private Camera camera2 = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                        .setViewPlaneSize(200, 200).setViewPlaneDistance(1000);

        /**
         * Test method for {@link elements.Camera#rotateCameraClockWise()}. with *
         * picture coming from the scene.
         */
        @Test
        public void testCameraRotationClockWiseWithScene() {
                scene1.geometries.add(new Sphere(new Point3D(0, 0, -100), 50), //
                                new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100),
                                                new Point3D(-100, 100, -100)) // up
                                                                              // left
                                                                .setEmission(new Color(java.awt.Color.GREEN)),
                                new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100),
                                                new Point3D(100, 100, -100)), // up
                                                                              // right
                                new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100),
                                                new Point3D(-100, -100, -100)) // down
                                                                               // left
                                                                .setEmission(new Color(java.awt.Color.RED)),
                                new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100),
                                                new Point3D(100, -100, -100)) // down
                                                                              // right
                                                                .setEmission(new Color(java.awt.Color.BLUE)));
                ImageWriter imageWriter = new ImageWriter("clockwise rotated color render test", 1000, 1000);
                RenderBase render =  new Render() //
                                .setImageWriter(imageWriter) //
                                .setCamera(camera1) //
                                .setRayTracer(new RayTracerBasic(scene1));

                camera1.rotateCameraClockWise();

                render.renderImage();
                render.printGrid(100, new Color(java.awt.Color.WHITE));
                render.writeToImage();
        }

        /**
         * Test method for {@link elements.Camera#rotateCameraCounterClockWise()}. with
         * picture coming from the scene.
         */
        @Test
        public void testCameraRotationCounterClockWiseWithScene() {
                scene1.geometries.add(new Sphere(new Point3D(0, 0, -100), 50), //
                                new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100),
                                                new Point3D(-100, 100, -100)) // up
                                                                              // left
                                                                .setEmission(new Color(java.awt.Color.GREEN)),
                                new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100),
                                                new Point3D(100, 100, -100)), // up
                                                                              // right
                                new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100),
                                                new Point3D(-100, -100, -100)) // down
                                                                               // left
                                                                .setEmission(new Color(java.awt.Color.RED)),
                                new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100),
                                                new Point3D(100, -100, -100)) // down
                                                                              // right
                                                                .setEmission(new Color(java.awt.Color.BLUE)));
                ImageWriter imageWriter = new ImageWriter("counter clockwise rotated color render test", 1000, 1000);
                RenderBase render =  new Render() //
                                .setImageWriter(imageWriter) //
                                .setCamera(camera1) //
                                .setRayTracer(new RayTracerBasic(scene1));

                camera1.rotateCameraCounterClockWise();

                render.renderImage();
                render.printGrid(100, new Color(java.awt.Color.WHITE));
                render.writeToImage();
        }

        /**
         * Test method for {@link elements.Camera#moveCamera(Point3D, Point3D)}. with
         * picture coming from the scene.
         */
        @Test
        public void testCameraMovementWithScene() {
                scene2.geometries.add( //
                                new Sphere(new Point3D(0, 0, -200), 60) //
                                                .setEmission(new Color(java.awt.Color.BLUE)) //
                                                .setMaterial(new Material().setKD(0.5).setKS(0.5) //
                                                                .setNShininess(30)), //
                                new Triangle(new Point3D(-55, -30, 0), new Point3D(-30, -55, 0),
                                                new Point3D(-53, -53, -6)) //
                                                                .setEmission(new Color(java.awt.Color.BLUE)) //
                                                                .setMaterial(new Material().setKD(0.5) //
                                                                                .setKS(0.5).setNShininess(30)) //
                );
                scene2.lights.add( //
                                new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200),
                                                new Vector(1, 1, -3)) //
                                                                .setKL(1E-5).setKQ(1.5E-7));

                RenderBase render =  new Render(). //
                                setImageWriter(new ImageWriter("camera movement in scene", 400, 400)) //
                                .setCamera(camera2) //
                                .setRayTracer(new RayTracerBasic(scene2));

                camera2.moveCamera(new Point3D(-300, 0, 1000), new Point3D(0, 0, -200));

                render.renderImage();
                render.writeToImage();
        }
}
