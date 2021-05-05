package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * A position light source that light in a certain direction.
 */
public class SpotLight extends PointLight {
    private Vector direction;

    /**
     * A constructor for Spot light sources.
     * 
     * @param intensity Color - The Light Color.
     * @param position  Point3D - Location of the light source.
     * @param direction Vector - a vector that indicate the direction the light
     *                  comes in.
     */
    public SpotLight(Color intensity, Point3D position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalized();
    }

    @Override
    public Color getIntensity(Point3D p) {
        Color i = super.getIntensity(p);
        double numerator = alignZero(direction.dotProduct(getL(p)));
        return i.scale(numerator < 0 ? 0 : numerator);
    }
}
