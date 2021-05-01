package elements;

import primitives.Color;

public class AmbientLight {
    private Color intensity;

    public AmbientLight(Color light, double intensity) {
        this.intensity = light.scale(intensity);
    }

    public Color getIntensity() {
        return intensity;
    }
}
