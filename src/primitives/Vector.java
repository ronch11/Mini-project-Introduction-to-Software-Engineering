package primitives;

/**
 * Class Vector is the basic class representing a vector starting in origin of
 * Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Vector {
    Point3D head;

    public Vector(Point3D head) {
        this.head = head;
    }

    public Vector(double x, double y, double z) {
        Point3D temp = new Point3D(x, y, z);
        if (temp.equals(Point3D.ZERO)) {
            throw new IllegalArgumentException("Vector can not be zero vector.");
        }
        this.head = temp;
    }

    public Vector(Coordinate x, Coordinate y, Coordinate z) {
        this(x.coord, y.coord, z.coord);
    }

    public Point3D getHead() {
        return head;
    }

    public Vector add(Vector vec) {
        return new Vector(head.add(vec));

    }

    public Vector substract(Vector vec) {
        return head.substract(vec.head);

    }

    public Vector scale(Double scalar) {
        return new Vector(scalar * head.x.coord, scalar * head.y.coord, scalar * head.z.coord);

    }

    public Vector crossProduct(Vector vec) {
        double productYZ = this.head.y.coord * vec.head.z.coord;
        double productZX = this.head.z.coord * vec.head.x.coord;
        double productXY = this.head.x.coord * vec.head.y.coord;

        double productZY = this.head.z.coord * vec.head.y.coord;
        double productXZ = this.head.x.coord * vec.head.z.coord;
        double productYX = this.head.y.coord * vec.head.x.coord;

        return new Vector(productYZ - productZY, productZX - productXZ, productXY - productYX);

    }

    public double dotProduct(Vector vec) {
        double productX = this.head.x.coord * vec.head.x.coord;
        double productY = this.head.y.coord * vec.head.y.coord;
        double productZ = this.head.z.coord * vec.head.z.coord;
        return productX + productY + productZ;
    }

    public double lengthSquared() {
        return head.distanceSquared(Point3D.ZERO);

    }

    public double length() {
        return head.distance(Point3D.ZERO);
    }

    public Vector normalize() {
        head = normalized().head;
        return this;

    }

    public Vector normalized() {
        double factor = 1 / this.length();
        return scale(factor);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Vector))
            return false;
        Vector other = (Vector) obj;
        return this.head.equals(other.head);
    }

    @Override
    public String toString() {
        return head.toString();
    }
}