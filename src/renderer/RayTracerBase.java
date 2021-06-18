package renderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.ProviderMismatchException;
import java.util.LinkedList;
import java.util.List;

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
import geometries.*;
import primitives.*;
import scene.Scene;

import static primitives.Util.getDoublesFromString;

/**
 * A basic requirement for all ray tracing object to follow. represented by
 * abstract class.
 */
public abstract class RayTracerBase {
    private static final String FOLDER_PATH = System.getProperty("user.dir");

    protected Scene scene;

    /**
     * The constructor for creating a RayTracer object should get Scene as
     * parameter.
     * 
     * @param scene - Scene to work on.
     */
    protected RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * A function to get the color of pixel in the scene.
     * 
     * @param ray - The ray to test the scene with.
     * @return Color - The appropriate color for the pixel that the ray intersected
     *         the scene with.
     */
    public abstract Color traceRay(Ray ray);

    /**
     * An XML loader function - read data of scene from XML file and update scene.
     * 
     * @param xmlFileName String - Name of the xml file without extension.
     * @return RayTracerBase - Self return for builder pattern.
     */
    public RayTracerBase loadFromXml(String xmlFileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
            dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(FOLDER_PATH + "/" + xmlFileName + ".xml"));
            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();
            return loadBackgroundColor(rootElement).loadAmbientLight(rootElement).loadGeometries(rootElement);
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
     * A helper function for setting backgroundLight of RayTracerBase from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return RayTracerBase - Self return for builder pattern.
     */
    private RayTracerBase loadBackgroundColor(Element rootElement) {
        double[] rgbBackground = getDoublesFromString(rootElement.getAttribute("background-color"));
        scene.background = new Color(rgbBackground[0], rgbBackground[1], rgbBackground[2]);
        return this;
    }

    /**
     * A helper function for setting AmbientLight of RayTracerBase from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return RayTracerBase - Self return for builder pattern.
     */
    private RayTracerBase loadAmbientLight(Element rootElement) {
        Element ambientElement = (Element) rootElement.getElementsByTagName("ambient-light").item(0);
        double[] rgbLight = getDoublesFromString(ambientElement.getAttribute("color"));
        scene.ambientLight = new AmbientLight(new Color(rgbLight[0], rgbLight[1], rgbLight[2]), 1);
        return this;
    }

    /**
     * A helper function for setting Geometries of RayTracerBase from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return RayTracerBase - Self return for builder pattern.
     */
    private RayTracerBase loadGeometries(Element rootElement) {
        Element geometriesElement = (Element) rootElement.getElementsByTagName("geometries").item(0);
        NodeList geometriesList = geometriesElement.getChildNodes();
        for (int i = 0; i < geometriesList.getLength(); i++) {
            Node node = geometriesList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element geometry = (Element) node;
                String geometryName = geometry.getTagName();
                switch (geometryName) {
                    case "triangle":
                        double[] p0 = getDoublesFromString(geometry.getAttribute("p0"));
                        double[] p1 = getDoublesFromString(geometry.getAttribute("p1"));
                        double[] p2 = getDoublesFromString(geometry.getAttribute("p2"));
                        scene.geometries.add(new Triangle(new Point3D(p1[0], p1[1], p1[2]), new Point3D(p2[0], p2[1], p2[2]),
                                new Point3D(p0[0], p0[1], p0[2])));
                        break;
                    case "sphere":
                        double[] center = getDoublesFromString(geometry.getAttribute("center"));
                        double radius = Double.parseDouble(geometry.getAttribute("radius"));
                        scene.geometries.add(new Sphere(new Point3D(center[0], center[1], center[2]), radius));
                        break;
                    case "plane":
                        double[] q0 = getDoublesFromString(geometry.getAttribute("q0"));
                        double[] dir = getDoublesFromString(geometry.getAttribute("dir"));
                        scene.geometries.add(new Plane(new Point3D(q0[0], q0[1], q0[2]), new Vector(dir[0], dir[1], dir[2])));
                        break;
                    case "polygon":
                        List<Point3D> points = new LinkedList<>();
                        NamedNodeMap attributes = geometry.getAttributes();
                        for (int j = 0; j < attributes.getLength(); j++) {
                            double[] coords = getDoublesFromString(attributes.item(j).getTextContent());
                            points.add(new Point3D(coords[0], coords[1], coords[2]));
                        }
                        scene.geometries.add(new Polygon(points.toArray(new Point3D[] {})));
                        break;
                    default:
                        break;
                }
            }
        }
        return this;
    }

}
