package elements;

import java.util.List;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * A directional light class (light comes from certain direction) that
 * relatively completely engulf our scene i.e. the sun.
 */
public class DirectionalLight extends Light implements LightSource {
    private Vector direction;

    /**
     * Basic constructor for directional lights.
     * 
     * @param direction Vector - a vector that indicate the direction the light
     *                  comes in.
     * @param intensity Color - the light color and intensity.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalized();
    }

    @Override
    public Color getIntensity(Point3D p) {
        return intensity;
    }

    @Override
    public Vector getL(Point3D p) {
        return direction;
    }

    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getSquareEdge() {
        return 0;
    }

    @Override
    public LightSource setSquareEdge(double squareEdge) {
        throw new UnsupportedOperationException("Directional lights have no radius limitations");
    }

    @Override
    public Point3D getSourcePoint() {
        throw new UnsupportedOperationException("Directional lights have no source point");
    }

    @Override
    public Vector getDirection(Point3D source, Point3D point) {
        return direction;
    }

    @Override
    public List<Point3D> calculatePoints(Point3D intersectionPoint, int numOfPoints) {
        return List.of(intersectionPoint);
    }
}
