package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Camera {
    private Point3D position;
    private Vector vTo, vUp, vRight;
    private double width, height, distance;

    /**
     * Only Constructor for Camera. must given position and 2 directions(forward and
     * up).
     * 
     * @param camPos - Point3D A position for the camera.
     * @param camVTo - Vector The forward vector direction.
     * @param camVUp - Vector The upward vector direction.
     */
    public Camera(Point3D camPos, Vector camVTo, Vector camVUp) {
        position = camPos;
        vTo = camVTo.normalized();
        vUp = camVUp.normalized();
        if (vTo.dotProduct(vUp) != 0) {
            throw new IllegalArgumentException("The Vectors supplied should be orthogonal to each other");
        }
        vRight = vTo.crossProduct(vUp).normalize();
    }

    /**
     * Getter for private field Position
     * 
     * @return {@link Point3D}
     */
    public Point3D getPosition() {
        return position;
    }

    /**
     * Getter for private field Vector vTo (forward).
     * 
     * @return {@link Vector}
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * Getter for private field Vector vUp.
     * 
     * @return {@link Vector}
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * Getter for private field Vector vRight.
     * 
     * @return {@link Vector}
     */
    public Vector getvRight() {
        return vRight;
    }

    public double getWidth() {
        return width;
    }

    /**
     * A Setter for View panel size. need to provide width and height.
     * 
     * @param width  - double the view panel's width.
     * @param height - double the view panel's height.
     * @return - Camera (self)
     */
    public Camera setViewPlaneSize(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * A setter for distance between view panel and the camera.
     * 
     * @param distance - double the distance between the camera and the view panel.
     * @return - Camera (self)
     */
    public Camera setViewPlaneDistance(double distance) {
        this.distance = distance;
        return this;
    }

    /**
     * gets Ray that align with 2 points (camera position and a viewPanel pixel).
     * 
     * @param nX int - number of columns in the View panel.
     * @param nY int - number of rows in the View panel.
     * @param j  int - column index of the pixel in the View panel.
     * @param i  int - row index of the pixel in the View panel.
     * @return Ray - that start at the camera and goes through the pixel[i,j].
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {

        double rX = alignZero(width / nX);
        double rY = alignZero(height / nY);
        Point3D pc = position.add(vTo.scale(distance));

        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);

        Point3D pIJ = pc;
        if (xJ != 0)
            pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0)
            pIJ = pIJ.add(vUp.scale(yI));

        return new Ray(position, pIJ.subtract(position));
    }
}
