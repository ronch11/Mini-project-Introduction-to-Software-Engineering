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
    private int narrowBeam;

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
        narrowBeam = 1;
    }

    @Override
    public Color getIntensity(Point3D p) {
        double numerator = alignZero(direction.dotProduct(getL(p)));
        if (narrowBeam != 1)
            numerator = alignZero(Math.pow(numerator, narrowBeam));
        return numerator <= 0 ? Color.BLACK : super.getIntensity(p).scale(numerator);
    }

    @Override
    public Vector getDirection(Point3D point) {
        return direction;
    }

    public PointLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
}
