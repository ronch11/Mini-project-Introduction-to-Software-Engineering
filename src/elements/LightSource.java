package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * An interface to share common functionality of light sources.
 */
public interface LightSource {
    /**
     * Get the light intensity of the light source at Point3D given.
     * 
     * @param p Point#d - the Point we want to get the light intensity.
     * @return Color - The light that ight source project on said point.
     */
    public Color getIntensity(Point3D p);

    /**
     * get the Vector that connect the given point and the base point of the Light
     * Source.
     * 
     * @param p Point3D - The point we want to get Vector to form the light source.
     * @return Vector - The Vector that represent the direction from light source to
     *         the given point.
     */
    public Vector getL(Point3D p);

    public double getDistance(Point3D point);
}
