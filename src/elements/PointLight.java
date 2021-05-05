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
        this.kL = 0;
        this.kQ = 0;
        this.kC = 1;
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
        double d = alignZero(Math.sqrt(dSquared)); // no reason to calculate the distance^2 again only to sqrt it.
        double denominator = alignZero(kC + d * kL + dSquared * kQ);
        return getIntensity().scale(1 / denominator);
    }

    @Override
    public Vector getL(Point3D p) {
        return p.subtract(position).normalize();
    }

}
