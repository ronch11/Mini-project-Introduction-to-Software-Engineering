package primitives;

public class Material {
    public double kD = 0, kS = 0;
    public int nShininess = 1;

    /**
     * A setter for field kD using builder pattern.
     * 
     * @param kD double - The diffusion scalar of the material.
     * @return Material - self return for builder pattern.
     */
    public Material setkD(double kD) {
        this.kD = kD;
        return this;
    }

    /**
     * A setter for field kS using builder pattern.
     * 
     * @param kS double - The specular scalar of the material.
     * @return Material - self return for builder pattern.
     */
    public Material setkS(double kS) {
        this.kS = kS;
        return this;
    }

    /**
     * A setter for field nShininess using builder pattern.
     * 
     * @param nShininess int - The shininess factor of the Material.
     * @return Material - self return for builder pattern.
     */
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
