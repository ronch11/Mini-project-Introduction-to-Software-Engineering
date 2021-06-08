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

    private double edge;

    /**
     * A basic constructor for Point light source.
     * 
     * @param intensity Color - The Light Color.
     * @param position  Point3D - Location of the light source.
     */
    public PointLight(Color intensity, Point3D position) {
        super(intensity);
        this.position = position;
        kL = kQ = edge = 0;
        kC = 1;
    }

    /**
     * A setter for kl field.
     * 
     * @param kL double - The kl scalar for phong equation.
     * @return PointLight - self return for builder patter.
     */
    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * A setter for kC field.
     * 
     * @param kC double - The kC scalar for phong equation.
     * @return PointLight - self return for builder patter.
     */
    public PointLight setKC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * A setter for kQ field.
     * 
     * @param kQ double - The kQ scalar for phong equation.
     * @return PointLight - self return for builder patter.
     */
    public PointLight setKQ(double kQ) {
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

    @Override
    public double getDistance(Point3D point) {
        return alignZero(point.distance(position));
    }

    @Override
    public Point3D getSourcePoint() {
        return position;
    }

    @Override
    public Vector getDirection(Point3D point) {
        return getL(point);
    }

    @Override
    public double getSquareEdge() {
        return edge;
    }

    @Override
    public LightSource setSquareEdge(double squareEdge) {
        edge = squareEdge;
        return this;
    }
}
