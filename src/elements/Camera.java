package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.*;

import java.util.LinkedList;
import java.util.List;

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
        if (!isZero(camVTo.dotProduct(camVUp))) {
            throw new IllegalArgumentException("The Vectors supplied should be orthogonal to each other");
        }
        vTo = camVTo.normalized();
        vUp = camVUp.normalized();
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
    public Vector getVTo() {
        return vTo;
    }

    /**
     * Getter for private field Vector vUp.
     * 
     * @return {@link Vector}
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * Getter for private field Vector vRight.
     * 
     * @return {@link Vector}
     */
    public Vector getVRight() {
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

    private Vector rotateVRightByVTo(double angleInDeg) {
        // Using this formula from wikipedia in order to rotate vector *V* around other
        // vector *K* by *t*
        // Vrot = Vcost + (KxV)sint + K(KV)(1 - cost)

        double cosT = Math.cos(Math.toRadians(angleInDeg));
        double sinT = Math.sin(Math.toRadians(angleInDeg));
        double kvOneMinusCosT = vTo.dotProduct(vRight) * (1 - cosT);
        Point3D rotatedVectorHead = Point3D.ZERO;
        if (!isZero(cosT)) {
            rotatedVectorHead = rotatedVectorHead.add(vRight.scale(cosT));
        }
        if (!isZero(sinT)) {
            rotatedVectorHead = rotatedVectorHead.add(vTo.crossProduct(vRight).scale(sinT));
        }
        if (!isZero(kvOneMinusCosT)) {
            rotatedVectorHead = rotatedVectorHead.add(vTo.scale(kvOneMinusCosT));
        }
        return new Vector(rotatedVectorHead).normalize();
    }

    /**
     * Rotate camera in angle given by user (using degrees) counter clockwise.
     * 
     * @param angleInDeg - the angle to rotate (in degrees).
     * @return self return for more mutations.
     */
    public Camera rotateCameraCounterClockWise(double angleInDeg) {
        vRight = rotateVRightByVTo(-angleInDeg);
        vUp = vTo.crossProduct(vRight).normalize();
        vRight = vTo.crossProduct(vUp).normalize();
        return this;
    }

    /**
     * Rotate camera in angle given by user (using degrees) clockwise.
     * 
     * @param angleInDeg - the angle to rotate (in degrees).
     * @return self return for more mutations.
     */
    public Camera rotateCameraClockWise(double angleInDeg) {
        vRight = rotateVRightByVTo(angleInDeg);
        vUp = vTo.crossProduct(vRight).normalize();
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

    public List<Ray> createGridCameraRays(int nx, int ny, int j, int i, int gridSize) {
        // pixel size
        double rX = alignZero(width / nx);
        double rY = alignZero(height / ny);

        int halfGrid = Math.floorDiv(gridSize, 2);
        // interval between 2 points in the sub grid
        double xInterval = rX / gridSize;
        double yInterval = rY / gridSize;

        Ray centerRay = constructRayThroughPixel(nx, ny, j, i);
        Point3D center = position.add(centerRay.getDir().scale(distance));
        List<Ray> rays = new LinkedList<>();
        for (int row = -halfGrid; row < gridSize; row++) {
            for (int col = -halfGrid; col < gridSize; col++) {
                Point3D gridPij = isZero(col * xInterval) ? center : center.add(vRight.scale(col * xInterval));
                gridPij = isZero(row * yInterval) ? gridPij : gridPij.add(vUp.scale(row * yInterval));
                rays.add(new Ray(position, gridPij.subtract(position)));
            }
        }
        if (gridSize == 2) {
            rays.add(0, centerRay);
        }
        return rays;
    }

}
