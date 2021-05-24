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

        double yI = alignZero((((nY - 1) / 2d) - i) * rY);
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);

        Point3D pIJ = pc;
        if (xJ != 0)
            pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0)
            pIJ = pIJ.add(vUp.scale(yI));

        return new Ray(position, pIJ.subtract(position));
    }

    /**
     * A function that moves the camera around using new point parameter while
     * keeping the camera direction at the point given in the lookAtPoint parameter.
     * 
     * @param newPos      - A point to move to.
     * @param lookAtPoint - A point to fix camera Point of view at it.
     * @return self return for more mutations.
     */
    public Camera moveCamera(Point3D newPos, Point3D lookAtPoint) {
        position = newPos;
        vTo = lookAtPoint.subtract(position).normalize();
        vUp = vTo.crossProduct(vRight).normalize();
        vRight = vTo.crossProduct(vUp).normalize();

        return this;
    }

    /**
     * A function to move camera around the scene while keeping focus on point.
     * 
     * @return self return for more mutations.
     */
    public Camera rotateCameraCounterClockWise() {
        vUp = vRight;
        vRight = vTo.crossProduct(vUp).normalize();
        return this;
    }

    /**
     * Rotate camera 90 degrees clockwise.
     * 
     * @return self return for more mutations.
     */
    public Camera rotateCameraClockWise() {
        vUp = vRight.scale(-1);
        vRight = vTo.crossProduct(vUp).normalize();
        return this;
    }

    /**
     * An override to check if two cameras positioned in the same location with the
     * same directions in vectors.
     * 
     * @param obj - An object to compare to this camera.
     * @return True if: same camera or same fields. else, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Camera)) {
            return false;
        }
        Camera camera = (Camera) obj;
        return position.equals(camera.position) && vTo.equals(camera.vTo) && vUp.equals(camera.vUp)
                && vRight.equals(camera.vRight) && width == camera.width && height == camera.height
                && distance == camera.distance;
    }
}
