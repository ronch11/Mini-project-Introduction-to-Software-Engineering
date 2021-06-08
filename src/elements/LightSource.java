package elements;

import java.util.List;

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
     * @return Color - The light that light source project on said point.
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

    /**
     * gets the distance of the light source to a point.
     * 
     * @param point - point we want to get the distance to light source is.
     * @return - the distance between the point and the light source.
     */
    public double getDistance(Point3D point);

    /**
     * gets the edge length for determine the area of effect of the light.
     * 
     * @return square edge length or 0 if: square edge was not set or it's not a
     *         point based light.
     */
    public double getSquareEdge();

    /**
     * setter for the square edge length. will throw exception for non point based
     * light sources.
     * 
     * @param squareEdge - the length of the squareEdge.
     * @return - self return for builder pattern.
     */
    public LightSource setSquareEdge(double squareEdge);

    /**
     * getter for get the central point of this light.
     * 
     * @return central point.
     */
    public Point3D getSourcePoint();

    /**
     * the direction which the light shine on the given point.
     * 
     * @param source - A point in Light source area.
     * @param point  - A point we want to get lights direction.
     * @return - the light direction to the given point.
     */
    public Vector getDirection(Point3D source, Point3D point);

    public List<Point3D> calculatePoints(Point3D intersectionPoint, int numOfPoints);
}
