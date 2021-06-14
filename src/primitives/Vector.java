package primitives;

import static primitives.Util.*;

/**
 * Class Vector is the basic class representing a vector starting in origin of
 * Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Vector {
    Point3D head;

    /**
     * Constructor for Vector Class.
     * 
     * @param head - A Point3D to indicate the head of the Vector "arrow" in
     *             Cartesian 3-Dimensional coordinate system.
     * @throws IllegalArgumentException - In case of passing Zero Point3D (head is
     *                                  on the origin (0,0,0) of the) Cartesian
     *                                  3-Dimensional coordinate system.
     */
    public Vector(Point3D head) {
        if (head.equals(Point3D.ZERO)) {
            throw new IllegalArgumentException("Vector can not be zero vector.");
        }
        this.head = head;
    }

    /**
     * Constructor for Vector Class.
     * 
     * @param x - A Coordinate for the x axis in Cartesian 3-Dimensional coordinate
     *          system.
     * @param y - A Coordinate for the y axis in Cartesian 3-Dimensional coordinate
     *          system.
     * @param z - A Coordinate for the z axis in Cartesian 3-Dimensional coordinate
     *          system.
     * 
     * @throws IllegalArgumentException - In case of passing Zero in all parameters
     *                                  (head is on the origin (0,0,0) of the)
     *                                  Cartesian 3-Dimensional coordinate system.
     */
    public Vector(double x, double y, double z) {
        head = new Point3D(x, y, z);
        if (head.equals(Point3D.ZERO)) {
            throw new IllegalArgumentException("Vector can not be zero vector.");
        }
    }

    /**
     * Getter for getting internal field - head.
     * 
     * @return head - type Point3D
     */
    public Point3D getHead() {
        return head;
    }

    /**
     * @param vec - a Vector type
     * @return Vector - a new Vector that it's the summary of the 2 vectors.
     */
    public Vector add(Vector vec) {
        return new Vector(head.add(vec));
    }

    /**
     * @param vec - a Vector Type
     * @return Vector - a new Vector that is the result of subtracting the vec from
     *         this vector.
     */
    public Vector subtract(Vector vec) {
        return head.subtract(vec.head);
    }

    /**
     * @param scalar - a Real number (type double) used by this function to grow or
     *               shrink the vector, or even change the direction of the Vector
     *               using a negative number.
     * @return Vector - a new vector based on this vector and the scalar.
     */
    public Vector scale(double scalar) {
        return new Vector(scalar * head.x.coord, scalar * head.y.coord, scalar * head.z.coord);
    }

    /**
     * Calculating Cross Product between This Vector and vec using algebraic
     * equation.
     * 
     * @param vec - a Vector Type
     * @return Vector - new Vector that is a orthogonally to the 2 Vectors(this
     *         Vector and the param vec)
     */
    public Vector crossProduct(Vector vec) {
        double productYZ = this.head.y.coord * vec.head.z.coord;
        double productZX = this.head.z.coord * vec.head.x.coord;
        double productXY = this.head.x.coord * vec.head.y.coord;

        double productZY = this.head.z.coord * vec.head.y.coord;
        double productXZ = this.head.x.coord * vec.head.z.coord;
        double productYX = this.head.y.coord * vec.head.x.coord;

        return new Vector(productYZ - productZY, productZX - productXZ, productXY - productYX);
    }

    /**
     * Calculating Dot Product between This Vector and vec using algebraic equation.
     * 
     * @param vec - a Vector Type
     * @return double - the value of doing dot product between vec and this Vector.
     */
    public double dotProduct(Vector vec) {
        double productX = this.head.x.coord * vec.head.x.coord;
        double productY = this.head.y.coord * vec.head.y.coord;
        double productZ = this.head.z.coord * vec.head.z.coord;
        return productX + productY + productZ;
    }

    /**
     * Calculating the length of this Vector by the power of 2 using algebraic
     * equation.
     * 
     * @return double - a Real number (type double) representing the length^2 of the
     *         vector.
     */
    public double lengthSquared() {
        return head.x.coord * head.x.coord + head.y.coord * head.y.coord + head.z.coord * head.z.coord;
    }

    /**
     * Calculating the length of this Vector using algebraic equation.
     * 
     * @return double - a Real number (type double) representing the length of the
     *         vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Reducing this Vector to be unit Vector (length == 1).
     * 
     * @return Vector - This Vector (for chaining capabilities).
     */
    public Vector normalize() {
        double factor = 1 / this.length();
        head = new Point3D(factor * head.x.coord, factor * head.y.coord, factor * head.z.coord);
        return this;
    }

    /**
     * Creating copy of this Vector and reducing it to be unit Vector (length == 1).
     * 
     * @return Vector - a new unit Vector in the same direction of this Vector.
     */
    public Vector normalized() {
        double factor = 1 / this.length();
        return scale(factor);
    }

    /**
     * returns an orthogonal Vector to this vector.
     * 
     * @return orthogonal vector normalized
     */
    public Vector orthogonalVector() {
        var x = head.getX();
        var y = head.getY();
        var z = head.getZ();
        x = x < 0 ? -x : x;
        y = y < 0 ? -y : y;
        z = z < 0 ? -z : z;
        Vector orthVector;

        if (x < y) {
            if (x < z) {
                orthVector = new Vector(0, -z, y);
            } else {
                // z is smallest
                orthVector = new Vector(-y, x, 0);
            }
        } else {
            if (y < z) {
                // y is smallest
                orthVector = new Vector(-z, 0, x);
            } else {
                // z is smallest
                orthVector = new Vector(-y, x, 0);
            }
        }
        return orthVector.normalize();
    }

    /**
     * Test if two Vectors are equals.
     * 
     * @param obj - An Object to test if it's equal to This Vector in algebraic
     *            manner.
     * @return boolean - True if it's the same Vector or with same characteristics,
     *         False otherwise.
     */
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

    /**
     * Print the Vector as String using it's head to represent it.
     * 
     * @return String - 3-Dimensional coordinate Point ex:"(x, y, z)".
     */
    @Override
    public String toString() {
        return head.toString();
    }

    public Vector rotate(Vector k, double theta) {
        // Using this formula from wikipedia in order to rotate vector *V* around other
        // vector *K* by *t*
        // Vrot = Vcos(t) + (KxV)sin(t) + K(KV)(1 - cos(t))
        double cost = alignZero(Math.cos(theta)); // both sin() and cos() are resets in pi*(0/(pi/2)/pi...)
        double sint = alignZero(Math.sin(theta));
        Vector vcost = isZero(cost) ? null : scale(cost);
        Vector kvsint = isZero(sint) ? null : k.crossProduct(this).scale(sint);
        Vector kkv;
        try {
            kkv = k.scale(alignZero(k.dotProduct(this) * (1 - cost)));
        } catch (IllegalArgumentException e) {
            kkv = null;
        }
        Point3D endrot = Point3D.ZERO;
        endrot = vcost == null ? endrot : endrot.add(vcost);
        endrot = kvsint == null ? endrot : endrot.add(kvsint);
        endrot = kkv == null ? endrot : endrot.add(kkv);
        return new Vector(endrot);
    }
}