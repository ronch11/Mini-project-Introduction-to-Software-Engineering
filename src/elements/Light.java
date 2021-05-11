package elements;

import primitives.Color;

/**
 * An abstract class that give the common field for all light classes.
 */
abstract class Light {
    protected final Color intensity;

    /**
     * default constructor for all light based classes.
     * 
     * @param intensity
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * A Getter for the private field intensity
     * 
     * @return Color - The Color of light.
     */
    public Color getIntensity() {
        return intensity;
    }
}
