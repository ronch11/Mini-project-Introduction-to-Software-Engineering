package unittests;

import static org.junit.Assert.*;
import org.junit.Test;

import static primitives.Util.*;
import primitives.Point3D;
import primitives.Vector;

/**
 * Unit tests for primitives.Vector class
 */
public class VectorTests {

    /**
     * This test check that the Constructor can not create Zero length Vector.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: no Zero length Vector was created.
        assertThrows("can not create ZERO Vector.", IllegalArgumentException.class, () -> new Vector(0, 0, 0));
    }

    /**
     * This test is for checking that the getter for head field working properly.
     */
    @Test
    public void testGetHead() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct head point3D of Vector is returned via Getter.
        Point3D expected = new Point3D(1.0, 1.0, 1.0);
        Vector vector = new Vector(1.0, 1.0, 1.0);
        Point3D actualValue = vector.getHead();

        assertEquals("Should give back head: (1,1,1)", expected, actualValue);
    }

    /**
     * Test method for {@link Vector#add(Vector)}.
     */
    @Test
    public void testAdd() {
        // setup
        Vector positiveVector = new Vector(1.0, 1.0, 1.0);
        Vector otherPositiveVector = new Vector(2.0, 2.0, 2.0);

        Vector negativeVector = new Vector(-1.0, -1.0, -1.0);
        Vector otherNegativeVector = new Vector(-2.0, -2.0, -2.0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: adding two positive vectors.

        Vector expected = new Vector(3.0, 3.0, 3.0);
        Vector actualValue = positiveVector.add(otherPositiveVector);

        assertEquals("Should Get back Vector: (3,3,3)", expected, actualValue);

        // TC02: adding two negative vectors
        expected = new Vector(-3.0, -3.0, -3.0);
        actualValue = negativeVector.add(otherNegativeVector);

        assertEquals("Should Get back Vector: (-3,-3,-3)", expected, actualValue);

        // TC03: adding one negative and one positive Vectors together.
        expected = new Vector(-1.0, -1.0, -1.0);
        actualValue = positiveVector.add(otherNegativeVector);

        assertEquals("Should Get back Vector: (-1,-1,-1)", expected, actualValue);

        // =============== Boundary Values Tests ==================
        // TC04: adding 2 numbers equal in size but different signs (sum equal to ZERO).
        assertThrows("Vector ZERO should not be created by adding 2 equal size vectors.",
                IllegalArgumentException.class, () -> positiveVector.add(negativeVector));
    }

    /**
     * Test method for {@link Vector#subtract(Vector)}.
     */
    @Test
    public void testSubtract() {
        // setup
        Vector positiveVector = new Vector(1.0, 1.0, 1.0);
        Vector otherPositiveVector = new Vector(2.0, 2.0, 2.0);

        Vector negativeVector = new Vector(-1.0, -1.0, -1.0);
        Vector otherNegativeVector = new Vector(-2.0, -2.0, -2.0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: subtracting two positive vectors.
        Vector expected = new Vector(-1.0, -1.0, -1.0);
        Vector actualValue = positiveVector.subtract(otherPositiveVector);

        assertEquals("Should Get back Vector: (-1,-1,-1)", expected, actualValue);

        // TC02: subtracting two negative vectors
        expected = new Vector(1.0, 1.0, 1.0);
        actualValue = negativeVector.subtract(otherNegativeVector);

        assertEquals("Should Get back Vector: (1,1,1)", expected, actualValue);

        // TC03: subtracting one negative from one positive Vector.
        expected = new Vector(3.0, 3.0, 3.0);
        actualValue = positiveVector.subtract(otherNegativeVector);

        assertEquals("Should Get back Vector: (3,3,3)", expected, actualValue);
        // =============== Boundary Values Tests ==================
        // TC04: subtracting vector from himself or on that equal to him (result in
        // Vector ZERO).
        assertThrows("Vector ZERO should not be created.", IllegalArgumentException.class,
                () -> positiveVector.subtract(positiveVector));
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     */
    @Test
    public void testScale() {
        // setup
        Vector positiveVector = new Vector(1.0, 1.0, 1.0);
        Vector negativeVector = new Vector(-1.0, -1.0, -1.0);
        double positiveScalar = 5.0;
        double negativeScalar = -5.0;
        // ============ Equivalence Partitions Tests ==============
        // TC01: positive scalar with positive Vector.
        Vector expected = new Vector(5.0, 5.0, 5.0);
        Vector actualValue = positiveVector.scale(positiveScalar);

        assertEquals("Should Get back Vector: (5,5,5)", expected, actualValue);

        // TC02: negative scalar with negative Vector.
        actualValue = negativeVector.scale(negativeScalar);

        assertEquals("Should Get back Vector: (5,5,5)", expected, actualValue);

        // TC03: negative scalar with positive Vector.
        expected = new Vector(-5.0, -5.0, -5.0);
        actualValue = positiveVector.scale(negativeScalar);

        assertEquals("Should Get back Vector: (-5,-5,-5)", expected, actualValue);

        // TC04: positive scalar with negative Vector.
        actualValue = negativeVector.scale(positiveScalar);

        assertEquals("Should Get back Vector: (-5,-5,-5)", expected, actualValue);

        // =============== Boundary Values Tests ==================
        // TC05: Scaling with zero should not return Vector ZERO.
        assertThrows("Vector should not be scaled to ZERO.", IllegalArgumentException.class,
                () -> positiveVector.scale(0.0));

        // TC06: Scaling with 1 return same Vector
        expected = new Vector(1.0, 1.0, 1.0);
        actualValue = positiveVector.scale(1.0);

        assertTrue("Vectors should Be equals after scaling with 1", expected.equals(actualValue));

        // TC07: Scaling with -1 return same Vector in opposite direction.
        expected = new Vector(-1.0, -1.0, -1.0);
        actualValue = positiveVector.scale(-1.0);

        assertTrue("Vectors should Be equals after scaling with 1", expected.equals(actualValue));
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     */
    @Test
    public void testCrossProduct() {
        Vector vector = new Vector(1, 2, 3);
        Vector otherVector = new Vector(-2, -4, -6);
        Vector differentVector = new Vector(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: simple Cross product test

        Vector crossVector = vector.crossProduct(differentVector);

        boolean lengthTestOk = isZero(crossVector.length() - vector.length() * differentVector.length());
        assertTrue("crossProduct() wrong result length", lengthTestOk);
        boolean vectorOrthogonalToCrossVectorOK = (isZero(crossVector.dotProduct(vector))
                && isZero(crossVector.dotProduct(differentVector)));

        assertTrue("crossProduct() result is not orthogonal to its operands", vectorOrthogonalToCrossVectorOK);

        // =============== Boundary Values Tests ==================
        // TC02: Test parallel vectors.

        assertThrows("crossProduct() for parallel vectors does not throw an zero Vector exception.",
                IllegalArgumentException.class, () -> vector.crossProduct(otherVector));
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}.
     */
    @Test
    public void testDotProduct() {
        // setup
        Vector vector = new Vector(1.0, 1.0, 1.0);
        Vector otherVector = new Vector(1.0, 2.0, 4.0);
        Vector orthogonalVector = new Vector(1.0, 1.0, -2.0);
        Vector scaledVectorByTwo = new Vector(2.0, 2.0, 2.0);
        Vector scaledVectorByMinusTwo = new Vector(-2.0, -2.0, -2.0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: testing two linearly independent Vectors.
        double expected = 7.0;
        double actualValue = vector.dotProduct(otherVector);

        assertEquals("Should Get back Real Number(double): 7.0", expected, actualValue, 0.00000001);

        // =============== Boundary Values Tests ==================
        // TC02: two orthogonal vectors should return 0.0
        expected = 0.0;
        actualValue = vector.dotProduct(orthogonalVector);

        assertEquals("Should Get back Real Number(double): 0.0", expected, actualValue, 0.00000001);
        // TC03: two Vector on the same line and same direction.
        expected = 6.0;
        actualValue = vector.dotProduct(scaledVectorByTwo);

        assertEquals("Should Get back Real Number(double): 6.0", expected, actualValue, 0.00000001);

        // TC04: two Vector on the same line and opposite direction.
        expected = -6.0;
        actualValue = vector.dotProduct(scaledVectorByMinusTwo);

        assertEquals("Should Get back Real Number(double): 6.0", expected, actualValue, 0.00000001);
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    public void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Testing distanceSquared()
        Vector vector = new Vector(1, 2, 3);
        boolean lengthTestOk = isZero(vector.lengthSquared() - 14);
        assertTrue("lengthSquared() wrong value", lengthTestOk);

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    public void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Testing length
        boolean lengthTestOk = isZero(new Vector(0, 3, 4).length() - 5);
        assertTrue("length() wrong value", lengthTestOk);

        // =============== Boundary Values Tests ==================

    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    public void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Testing Normalize
        Vector vector = new Vector(1, 2, 3);
        Vector vectorCopy = new Vector(vector.getHead());
        Vector normalizeVectorCopy = vectorCopy.normalize();

        assertTrue("normalize() function should not create a new vector", vectorCopy.equals(normalizeVectorCopy));

        boolean unitVectorLengthOK = isZero(normalizeVectorCopy.length() - 1);
        assertTrue("normalize() result is not a unit vector", unitVectorLengthOK);

    }

    /**
     * Test method for {@link Vector#normalized()}.
     */
    @Test
    public void testNormalized() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Testing for normalized()
        Vector vector = new Vector(1, 2, 3);
        Vector normalizedVector = vector.normalized();

        assertFalse("normalized() function does not create a new vector", normalizedVector.equals(vector));
    }

    /**
     * Test method for {@link Vector#equals(Object)}.
     */
    @Test
    public void testEquals() {
        // setup
        Vector vector = new Vector(1.0, 1.0, 1.0);
        Vector sameInValuesVector = new Vector(1.0, 1.0, 1.0);
        Vector differentInValuesVector = new Vector(2.0, 2.0, 2.0);
        Vector sameRefVector = vector;
        Point3D differentClassObj = new Point3D(1.0, 1.0, 1.0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: two equal in values Vectors
        assertTrue("Vectors should Be equals", vector.equals(sameInValuesVector));

        // TC02: two different in values Vectors
        assertFalse("Vectors should not Be equals", vector.equals(differentInValuesVector));

        // =============== Boundary Values Tests ==================
        // TC03: same object different references
        assertTrue("Both references to same object should Be equals", vector.equals(sameRefVector));
        // TC04: different class object
        assertFalse("Vectors should not Be equals", vector.equals(differentClassObj));
    }

}
