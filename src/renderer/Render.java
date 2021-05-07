package renderer;

import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;
import primitives.Ray;

/**
 * A Render Class to create pictures from scene's Camera point-of-view.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Render {
    private Camera camera;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    /**
     * A builder setter for chaining definitions.
     * 
     * @param camera - the camera we will use to interact with the Scene during
     *               rendering.
     * @return Render - the modifying object (self return).
     */
    public Render setCamera(Camera camera) {
        this.camera = camera;
        return this;
    }

    /**
     * A builder setter for chaining definitions.
     * 
     * @param rayTracer - the tracing rays object we will use during rendering.
     * @return Render - the modifying object (self return).
     */
    public Render setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return this;
    }

    /**
     * A builder setter for chaining definitions.
     * 
     * @param writer - the writer we will use to output the rendered image.
     * @return Render - the modifying object (self return).
     */
    public Render setImageWriter(ImageWriter writer) {
        this.imageWriter = writer;
        return this;
    }

    /**
     * render Camera point of view image from the scene using the camera view panel
     * and ray tracing from to the scene.
     */
    public void renderImage() {
        if (camera == null) {
            throw new MissingResourceException("Camera was not set can not render image.", "Render", "Camera");
        }
        if (imageWriter == null) {
            throw new MissingResourceException("Writer was not set can not render image.", "Render", "ImageWriter");
        }
        if (rayTracer == null) {
            throw new MissingResourceException("RayTracer was not set can not render image.", "Render",
                    "RayTracerBase");
        }

        int ny = imageWriter.getNy();
        int nx = imageWriter.getNx();

        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                Ray cameraRay = camera.constructRayThroughPixel(nx, ny, j, i);
                imageWriter.writePixel(j, i, rayTracer.traceRay(cameraRay));
            }
        }
    }

    /**
     * print grid overlay on the image *can be used for testing).
     * 
     * @param interval int - how often draw the lines (determine the squares size).
     * @param color    Color - the color of the grid lines.
     */
    public void printGrid(int interval, Color color) {
        if (imageWriter == null) {
            throw new MissingResourceException("Writer was not set can not render Grid.", "Render", "ImageWriter");
        }
        for (int i = 0; i < imageWriter.getNy(); i++) {
            for (int j = 0; j < imageWriter.getNx(); j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
    }

    /**
     * save rendered image to file.
     */
    public void writeToImage() {
        if (imageWriter == null) {
            throw new MissingResourceException("Writer was not set can not save to image.", "Render", "ImageWriter");
        }
        imageWriter.writeToImage();
    }
}
