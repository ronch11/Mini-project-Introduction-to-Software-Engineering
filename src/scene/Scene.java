package scene;

import elements.AmbientLight;
import geometries.Geometries;
import primitives.Color;

/**
 * A Class to combine elements to one scene.
 */
public class Scene {
    public String name;
    public Color background;
    public AmbientLight ambientLight;
    public Geometries geometries;

    /**
     * A Constructor for creating new Scene
     * 
     * @param name String - name of Scene
     */
    public Scene(String name) {
        this.name = name;
        geometries = new Geometries();
        background = Color.BLACK;
        ambientLight = new AmbientLight(background, 1);
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

}
