package scene;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import elements.AmbientLight;
import geometries.Geometries;
import primitives.Color;

@XmlRootElement
public class Scene {
    @XmlAttribute
    public String name;
    @XmlElement
    public Color background;
    @XmlElement
    public AmbientLight ambientLight;
    @XmlElement
    public Geometries geometries;

    public Scene(String name) {
        this.name = name;
        geometries = new Geometries();
        background = new Color(Color.BLACK);
        ambientLight = new AmbientLight(background, 1);
    }

    public Scene setName(String name) {
        this.name = name;
        return this;
    }

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
