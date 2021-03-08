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

    /**
     * Calculate new Point3D using Vector to indicate the direction and length to
     * go, using algebric equation.
     * 
     * @param vec - A Vector used by this function to create new Point3D.
     * @return Point3D - A new Point3D object that is the Summary of the 2
     *         Points(this point and the Vector Head.)
     */
    public Point3D add(Vector vec) {
        return new Point3D(this.x.coord + vec.head.x.coord, this.y.coord + vec.head.y.coord,
                this.z.coord + vec.head.z.coord);
    }

    /**
     * Calculate new Vector using this Point3D another Point3D, using algebric
     * equation.
     * 
     * @param point - second Point3D passed as parameter to calculate the new
     *              Vector.
     * @return Vector - A new Vector representing the distance and direction between
     *         the 2 Points.
     */
    public Vector substract(Point3D point) {
        return new Vector(this.x.coord - point.x.coord, this.y.coord - point.y.coord, this.z.coord - point.z.coord);

    }

    /**
     * Caculating the Distance between two Points.
     * 
     * @param point - Another Point3D passed to the function.
     * @return double - A Real Number (double type) representing the distance
     *         between the two Points.
     */
    public double distance(Point3D point) {
        return Math.sqrt(distanceSquared(point));

    }

    /**
     * Caculating the Distance between two Points powered by 2 (Distance^2).
     * 
     * @param point - Another Point3D passed to the function.
     * @return double - A Real Number (double type) representing the distance
     *         between the two Points powered by 2.
     */
    public double distanceSquared(Point3D point) {
        double deltaX = (this.x.coord - point.x.coord);
        double deltaY = (this.y.coord - point.y.coord);
        double deltaZ = (this.z.coord - point.z.coord);
        return (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);

    }

    /**
     * Test if 2 Points are equals.
     * 
     * @param obj - An Object to test if it's equal to This Point3D in algebric
     *            manner.
     * @return boolean - True if it's the same Point3D or with same characterisics,
     *         False otherwise.
     */
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

    /**
     * Returning this Point3D as a String.
     * 
     * @return String - 3-Dimensional coordinate Point ex:"(x, y, z)".
     */
    @Override
    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ", " + z + ")";
    }
}