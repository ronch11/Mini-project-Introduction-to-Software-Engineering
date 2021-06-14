package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import static primitives.Util.*;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * A class to represent a omnidirectional light source Point i.e. lightbulb.
 */
public class PointLight extends Light implements LightSource {
    private Point3D position;
    private double kC, kL, kQ;

    private double edge;

    private Random rand = new SecureRandom();

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

    /**
     * helper function to reduce repetition on calculating unit vector of 2 points.
     * 
     * @param sourcePoint      - point in range of the square with length of edge
     *                         and center point position.
     * @param destinationPoint - the point we want the light direction on.
     * @return - Light direction unit vector.
     */
    private Vector getL(Point3D sourcePoint, Point3D destinationPoint) {
        return destinationPoint.subtract(sourcePoint).normalize();
    }

    @Override
    public Vector getL(Point3D destinationPoint) {
        return getL(position, destinationPoint);
    }

    @Override
    public double getDistance(Point3D point) {
        return alignZero(point.distance(position));
    }

    @Override
    public Vector getDirection(Point3D sourcePoint, Point3D destinationPoint) {
        return getL(sourcePoint, destinationPoint);
    }

    @Override
    public double getSquareEdge() {
        return edge;
    }

    @Override
    public LightSource setSquareEdge(double squareEdge) {
        if (squareEdge < 0) {
            throw new IllegalArgumentException("Negative edge length is illegal");
        }
        edge = squareEdge;
        return this;
    }

    @Override
    public List<Point3D> calculatePoints(Vector n, int numOfPoints) {
        List<Point3D> points = new LinkedList<>();
        points.add(position);

        if (edge == 0 || numOfPoints == 1) {
            return points;
        }
        Vector vx = n.orthogonalVector();
        Vector vy = n.crossProduct(vx).normalize();
        double halfEdge = edge / 2;
        for (int i = 1; i < numOfPoints; i++) {
            Point3D pc = position;
            double x = rand.nextDouble() * edge - halfEdge;
            double y = rand.nextDouble() * edge - halfEdge;
            if (!isZero(x)) {
                pc = pc.add(vx.scale(x));
            }
            if (!isZero(y)) {
                pc = pc.add(vy.scale(y));
            }
            points.add(pc);
        }
        return points;
    }
}
