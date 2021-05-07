package scene;

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

import primitives.*;
import geometries.*;
import elements.AmbientLight;
import elements.LightSource;

import static primitives.Util.getDoublesFromString;

/**
 * A Class to combine elements to one scene.
 */
public class Scene {
    public String name;
    public Color background;
    public Geometries geometries;
    public List<LightSource> lights;
    public AmbientLight ambientLight;

    private static final String FOLDER_PATH = System.getProperty("user.dir");

    /**
     * A Constructor for creating new Scene
     * 
     * @param name String - name of Scene
     */
    public Scene(String name) {
        this.name = name;
        background = Color.BLACK;
        lights = new LinkedList<>();
        geometries = new Geometries();
        ambientLight = new AmbientLight();
    }

    /**
     * Setter for scene name field
     * 
     * @param name String - name of scene.
     * @return Scene - self return for builder pattern.
     */
    public Scene setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Setter for scene background field
     * 
     * @param background Color - the scene background Color.
     * @return Scene - self return for builder pattern.
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Setter for scene ambientLight field
     * 
     * @param ambientLight AmbientLight - the ambient light of Scene
     * @return Scene - self return for builder pattern.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Setter for scene geometries field
     * 
     * @param geometries Geometries - the collection of intersectable shapes in the
     *                   scene.
     * @return Scene - self return for builder pattern.
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * a Setter for lights field using builder pattern.
     * 
     * @param lights List<LightSource> - a list of all light sources in the scene.
     * @return Scene - self return for builder patter.
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * An XML loader function - read data of scene from XML file and update scene.
     * 
     * @param xmlFileName String - Name of the xml file without extension.
     * @return Scene - Self return for builder pattern.
     */
    public Scene loadFromXml(String xmlFileName) {
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
     * A helper function for setting backgroundLight of Scene from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return Scene - Self return for builder pattern.
     */
    private Scene loadBackgroundColor(Element rootElement) {
        double[] rgbBackground = getDoublesFromString(rootElement.getAttribute("background-color"));
        background = new Color(rgbBackground[0], rgbBackground[1], rgbBackground[2]);
        return this;
    }

    /**
     * A helper function for setting AmbientLight of Scene from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return Scene - Self return for builder pattern.
     */
    private Scene loadAmbientLight(Element rootElement) {
        Element ambientElement = (Element) rootElement.getElementsByTagName("ambient-light").item(0);
        double[] rgbLight = getDoublesFromString(ambientElement.getAttribute("color"));
        ambientLight = new AmbientLight(new Color(rgbLight[0], rgbLight[1], rgbLight[2]), 1);
        return this;
    }

    /**
     * A helper function for setting Geometries of Scene from XML.
     * 
     * @param rootElement Element - the root Dom element.
     * @return Scene - Self return for builder pattern.
     */
    private Scene loadGeometries(Element rootElement) {
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
        return this;
    }
}
