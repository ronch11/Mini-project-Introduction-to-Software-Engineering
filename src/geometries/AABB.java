package geometries;

import primitives.Point3D;
import primitives.Vector;

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
        return minLocation.add(xAxis.scale(xLength).add(yAxis.scale(yLength)) //
                .add(zAxis.scale(zLength)));
    }
}
