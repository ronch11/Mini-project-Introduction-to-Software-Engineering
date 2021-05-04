package renderer;

import static primitives.Util.getDoublesFromString;

import java.io.File;
import java.io.IOException;
import java.nio.file.ProviderMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import elements.AmbientLight;
import elements.Camera;
import geometries.Geometries;
import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

/**
 * A Render Class to create pictures from scene's Camera point-of-view.
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

    /**
     * An XML loader function - read data of scene from XML file and update scene.
     * 
     * @param xmlFilePath String - Name of the xml file without extension.
     * @return Render - Self return for builder pattern.
     */
    public Render readFromXml(String xmlFilePath) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
            dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(FOLDER_PATH + "/" + xmlFilePath + ".xml"));
            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();
            return getBackgroundLight(rootElement).getAmbientLight(rootElement).getGeometries(rootElement);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not parse XML");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalAccessError("Could not access XML file.");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new ProviderMismatchException("Could not configure the parser.");
        }
    }

    /**
     * A helper function for setting backgroundLight of Scene from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return Render - Self return for builder pattern.
     */
    private Render getBackgroundLight(Element rootElement) {
        double[] rgbBackground = getDoublesFromString(rootElement.getAttribute("background-color"));
        Color background = new Color(rgbBackground[0], rgbBackground[1], rgbBackground[2]);
        scene.setBackground(background);
        return this;
    }

    /**
     * A helper function for setting AmbientLight of Scene from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return Render - Self return for builder pattern.
     */
    private Render getAmbientLight(Element rootElement) {
        Element ambientElement = (Element) rootElement.getElementsByTagName("ambient-light").item(0);
        double[] rgbLight = getDoublesFromString(ambientElement.getAttribute("color"));
        Color light = new Color(rgbLight[0], rgbLight[1], rgbLight[2]);
        scene.setAmbientLight(new AmbientLight(light, 1));
        return this;
    }

    /**
     * A helper function for setting Geometries of Scene from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return Render - Self return for builder pattern.
     */
    private Render getGeometries(Element rootElement) {
        Geometries geometries = new Geometries();
        Element geometriesElement = (Element) rootElement.getElementsByTagName("geometries").item(0);
        NodeList geometriesList = geometriesElement.getChildNodes();
        for (int i = 0; i < geometriesList.getLength(); i++) {
            Node node = geometriesList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element geometry = (Element) node;
                String name = geometry.getTagName();
                switch (name) {
                    case "triangle":
                        double[] p0 = getDoublesFromString(geometry.getAttribute("p0"));
                        double[] p1 = getDoublesFromString(geometry.getAttribute("p1"));
                        double[] p2 = getDoublesFromString(geometry.getAttribute("p2"));
                        geometries.add(new Triangle(new Point3D(p1[0], p1[1], p1[2]), new Point3D(p2[0], p2[1], p2[2]),
                                new Point3D(p0[0], p0[1], p0[2])));
                        break;
                    case "sphere":
                        double[] center = getDoublesFromString(geometry.getAttribute("center"));
                        double radius = Double.parseDouble(geometry.getAttribute("radius"));
                        geometries.add(new Sphere(new Point3D(center[0], center[1], center[2]), radius));
                        break;
                    case "plane":
                        double[] q0 = getDoublesFromString(geometry.getAttribute("q0"));
                        double[] dir = getDoublesFromString(geometry.getAttribute("dir"));
                        geometries.add(new Plane(new Point3D(q0[0], q0[1], q0[2]), new Vector(dir[0], dir[1], dir[2])));
                        break;
                    case "polygon":
                        List<Point3D> points = new LinkedList<>();
                        NamedNodeMap attributes = geometry.getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            double[] coords = getDoublesFromString(attributes.item(j).getTextContent());
                            points.add(new Point3D(coords[0], coords[1], coords[2]));
                        }
                        geometries.add(new Polygon(points.toArray(new Point3D[] {})));
                        break;
                    default:
                        break;
                }
            }
        }
        scene.setGeometries(geometries);
        return this;
    }
}
