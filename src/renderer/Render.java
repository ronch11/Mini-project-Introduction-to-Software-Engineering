package renderer;

import elements.Camera;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.io.File;
import java.util.*;

/**
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Render {
    private Camera camera;
    private ImageWriter writer;
    private RayTracerBase rayTracer;
    private Scene scene;
    private static final String FOLDER_PATH = System.getProperty("user.dir");

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
     * @param scene - the scene we will render using camera.
     * @return Render - the modifying object (self return).
     */
    public Render setScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    /**
     * A builder setter for chaining definitions.
     * 
     * @param writer - the writer we will use to output the rendered image.
     * @return Render - the modifying object (self return).
     */
    public Render setWriter(ImageWriter writer) {
        this.writer = writer;
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
        if (writer == null) {
            throw new MissingResourceException("Writer was not set can not render image.", "Render", "ImageWriter");
        }
        if (rayTracer == null) {
            throw new MissingResourceException("RayTracer was not set can not render image.", "Render",
                    "RayTracerBase");
        }
        if (scene == null) {
            throw new MissingResourceException("Scene was not set can not render image.", "Render", "Scene");
        }

        for (int i = 0; i < writer.getNy(); i++) {
            for (int j = 0; j < writer.getNx(); j++) {
                Ray cameraRay = camera.constructRayThroughPixel(writer.getNx(), writer.getNy(), j, i);
                writer.writePixel(j, i, rayTracer.traceRay(cameraRay));
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
        if (writer == null) {
            throw new MissingResourceException("Writer was not set can not render Grid.", "Render", "ImageWriter");
        }
        for (int i = 0; i < writer.getNy(); i++) {
            for (int j = 0; j < writer.getNx(); j++) {
                if (i % interval == 0 || j % interval == 0) {
                    writer.writePixel(j, i, color);
                }
            }
        }
    }

    /**
     * save rendered image to file.
     */
    public void writeToImage() {
        if (writer == null) {
            throw new MissingResourceException("Writer was not set can not save to image.", "Render", "ImageWriter");
        }
        writer.writeToImage();
    }

    public Scene buildSceneFromXml(String xmlFileName) throws JAXBException {
        File file = new File(FOLDER_PATH + '/' + xmlFileName + ".xml");
        JAXBContext jci = JAXBContext.newInstance(Scene.class);

        Unmarshaller unmarshaller = jci.createUnmarshaller();
        return (Scene) unmarshaller.unmarshal(file);
    }
}
