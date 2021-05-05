package elements;

import primitives.Color;

public class AmbientLight extends Light {

    /**
     * the constructor for Ambient light get Color and a scalar that dictate the
     * ambient intensity of the light.
     * 
     * @param light     Color - Color of the light.
     * @param intensity double - Intensity of the light.
     */
    public AmbientLight(Color light, double intensity) {
        super(light.scale(intensity));
    }

    @Override
    public String toString() {
        return getIntensity().toString();
    }
}
