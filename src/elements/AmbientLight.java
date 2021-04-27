package elements;

import primitives.Color;

public class AmbientLight {
    private Color intensity;

    public AmbientLight(Color light) {
        intensity = light;
    }

    public Color getIntensity() {
        return intensity;
    }
}
