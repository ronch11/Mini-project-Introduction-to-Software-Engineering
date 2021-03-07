package primitives;

/**
 * Class point3D is the basic class representing a point in space of Euclidean
 * geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Point3D {
    /**
     * Zero Element - represnting the Origin Point of the 3-Dimensional coordinate
     * system.
     */
    public static Point3D ZERO = new Point3D(0, 0, 0);

    Coordinate x, y, z;

    public Point3D(Coordinate x, Coordinate y, Coordinate z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(double x, double y, double z) {
        this.x = new Coordinate(x);
        this.y = new Coordinate(y);
        this.z = new Coordinate(z);
    }

    public Point3D add(Vector vec) {
        return new Point3D(this.x.coord + vec.head.x.coord, this.y.coord + vec.head.y.coord,
                this.z.coord + vec.head.z.coord);
    }

    public Vector substract(Point3D point) {
        return new Vector(this.x.coord - point.x.coord, this.y.coord - point.y.coord, this.z.coord - point.z.coord);

    }

    public double distance(Point3D point) {
        return Math.sqrt(distanceSquared(point));

    }

    public double distanceSquared(Point3D point) {
        double deltaX = (this.x.coord - point.x.coord);
        double deltaY = (this.y.coord - point.y.coord);
        double deltaZ = (this.z.coord - point.z.coord);
        return (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Point3D))
            return false;
        Point3D other = (Point3D) obj;
        return this.x.equals(other.x) && this.y.equals(other.y) && this.z.equals(other.z);
    }

    @Override
    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ", " + z + ")";
    }

}