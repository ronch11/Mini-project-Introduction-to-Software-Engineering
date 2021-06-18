package scene;

import primitives.*;
import geometries.*;
import elements.AmbientLight;
import elements.LightSource;

import java.util.LinkedList;
import java.util.List;

/**
 * A Class to combine elements to one scene.
 */
public class Scene {
    public String name;
    public Color background;
    public Geometries geometries;
    public List<LightSource> lights;
    public AmbientLight ambientLight;

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

}