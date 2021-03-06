package primitives;

public class Material {
    /**
     * A Diffusion factor of the material.
     */
    public double kD = 0;
    /**
     * A Specularity factor of the material.
     */
    public double kS = 0;
    /**
     * A Reflection factor of the material.
     */
    public double kR = 0;
    /**
     * A Translucency factor of the material.
     */
    public double kT = 0;
    /**
     * A Shininess factor of the material.
     */
    public int nShininess = 1;

    /**
     * A setter for field kD using builder pattern.
     * 
     * @param kD double - The diffusion scalar of the material.
     * @return Material - self return for builder pattern.
     */
    public Material setKD(double kD) {
        this.kD = kD;
        return this;
    }

    /**
     * A setter for field kS using builder pattern.
     * 
     * @param kS double - The specular scalar of the material.
     * @return Material - self return for builder pattern.
     */
    public Material setKS(double kS) {
        this.kS = kS;
        return this;
    }

    /**
     * A setter for field nShininess using builder pattern.
     * 
     * @param nShininess int - The shininess factor of the Material.
     * @return Material - self return for builder pattern.
     */
    public Material setNShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * A builder pattern setter
     * 
     * @param kR - The reflection factor of this material.
     * @return - self return for builder pattern.
     */
    public Material setKR(double kR) {
        this.kR = kR;
        return this;
    }

    /**
     * A builder patter setter
     * 
     * @param kT - The translucent factor of this material.
     * @return - self return for builder pattern.
     */
    public Material setKT(double kT) {
        this.kT = kT;
        return this;
    }
}
