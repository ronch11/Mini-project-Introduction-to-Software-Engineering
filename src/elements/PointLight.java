package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * A class to represent a omnidirectional light source Point i.e. lightbulb.
 */
public class PointLight extends Light implements LightSource {
    private Point3D position;
    private double kC, kL, kQ;

    /**
     * A basic constructor for Point light source.
     * 
     * @param intensity Color - The Light Color.
     * @param position  Point3D - Location of the light source.
     */
    public PointLight(Color intensity, Point3D position) {
        super(intensity);
        this.position = position;
        kL = kQ = 0;
        kC = 1;
    }

    /**
     * A setter for kl field.
     * 
     * @param kL double - The kl scalar for phong equation.
     * @return PointLight - self return for builder patter.
     */
    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * A setter for kC field.
     * 
     * @param kC double - The kC scalar for phong equation.
     * @return PointLight - self return for builder patter.
     */
    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * A setter for kQ field.
     * 
     * @param kQ double - The kQ scalar for phong equation.
     * @return PointLight - self return for builder patter.
     */
    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point3D p) {
        double dSquared = alignZero(p.distanceSquared(position));
        return intensity.reduce(alignZero(kC + Math.sqrt(dSquared) * kL + dSquared * kQ));
    }

    @Override
    public Vector getL(Point3D p) {
        return p.subtract(position).normalize();
    }
}
