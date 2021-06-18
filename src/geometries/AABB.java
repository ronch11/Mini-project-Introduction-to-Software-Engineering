package geometries;

import primitives.Point3D;
import primitives.Vector;

import static primitives.Util.*;

/**
 * A Class that re a Axis aligned bounding box in a 3D-dimensional space.
 */
public class AABB {
    private static final Vector xAxis = new Vector(1, 0, 0);
    private static final Vector yAxis = new Vector(0, 1, 0);
    private static final Vector zAxis = new Vector(0, 0, 1);
    private Point3D minLocation;

    private double xLength, yLength, zLength;

    public AABB(Point3D minLocation, double xLength, double yLength, double zLength) {
        this.minLocation = minLocation;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
    }

    /**
     * @return the minLocation
     */
    public Point3D getMinLocation() {
        return minLocation;
    }

    /**
     * @return the MaxLocation
     */
    public Point3D getMaxLocation() {
        Point3D maxLocation = minLocation;
        if (!isZero(xLength)) {
            maxLocation = maxLocation.add(xAxis.scale(xLength));
        }

        if (!isZero(yLength)) {
            maxLocation = maxLocation.add(yAxis.scale(yLength));
        }

        if (!isZero(zLength)) {
            maxLocation = maxLocation.add(zAxis.scale(zLength));
        }
        return maxLocation;
    }

    public Point3D getCenterLocation() {
        Point3D center = minLocation;

        double midX = xLength / 2;
        if (!isZero(midX)) {
            center = center.add(xAxis.scale(midX));
        }

        double midY = yLength / 2;
        if (!isZero(midY)) {
            center = center.add(yAxis.scale(midY));
        }

        double midZ = zLength / 2;
        if (!isZero(midZ)) {
            center = center.add(zAxis.scale(midZ));
        }
        return center;
    }
}
