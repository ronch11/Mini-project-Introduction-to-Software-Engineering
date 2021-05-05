package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;

/**
 * Geometry is the common abstract class for all Geometry classes to implement.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public abstract class Geometry implements Intersectable {

    protected Color emission = Color.BLACK;
    private Material material = new Material();

    /**
     * Getter for protected field emissions
     * 
     * @return Color - The shape "emitted" Color.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * A getter for private field material.
     * 
     * @return Material - the material the shape is made from.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * A setter using builder pattern for protected field emissions.
     * 
     * @param emissions Color - The color that the geometry shape is "emitting".
     * @return Geometry - self return for builder patter.
     */
    public Geometry setEmission(Color emissions) {
        this.emission = emissions;
        return this;
    }

    /**
     * A setter using builder pattern for private field material.
     * 
     * @param material Material - the material of the Geometry shape.
     * @return Geometry - self return for builder patter.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * A function that return unit vector (length equal to 1) of perpendicular
     * vector to the Geometry shape and the given point in @param point3d
     * 
     * @param point3d - point on the shape surface
     * @return - A perpendicular unit vector to the shape in the point.
     */
    public abstract Vector getNormal(Point3D point3d);
}
