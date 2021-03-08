package primitives;

/**
 * Class Ray is the basic class representing Ray of Euclidean geometry in
 * Cartesian 3-Dimensional coordinate system.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */

public class Ray {

    Point3D p0;
    Vector dir;

    public Ray(Point3D p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir;
    }

    /**
     * Test if 2 Rays are equals.
     * 
     * @param obj - An Object to test if it's equal to This Ray in algebric manner.
     * @return boolean - True if it's the same Ray or with same characterisics,
     *         False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Ray))
            return false;
        Ray other = (Ray) obj;
        return this.p0.equals(other.p0) && this.dir.equals(other.dir);
    }

    /**
     * Returning This Ray as String Using Point3D and Vector toString methods.
     * 
     * @return String - A 3-Dimensional coordinate Ray using point and Vector.
     *         ex:"p0= (x, y, z), dir= (a, b, c)".
     */
    @Override
    public String toString() {
        return "p0= " + p0.toString() + " " + ", dir= " + dir.toString() + " ";
    }

}