package renderer;

import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;

public abstract class RenderBase {
    private static final String RESOURCE_ERROR = "Renderer resource not set";
    private static final String IMAGE_WRITER_COMPONENT = "Image writer";
    private static final String CAMERA_COMPONENT = "Camera";
    private static final String RAY_TRACER_COMPONENT = "Ray tracer";
    private String renderClass;

    /**
     * how much ray we will calculate in a grid. Level = number of Rows and Columns
     * in the grid. ex. level 1 is no super sampling 2 is using 2 by 2 grid (+
     * center).
     */
    protected int antiAliasingLevel = 1;

    protected Camera camera;
    protected ImageWriter imageWriter;
    protected RayTracerBase rayTracer;

    protected RenderBase(String renderClass) {
        this.renderClass = renderClass;
    }

    /**
     * A setter for antiAliasingLevel field
     * 
     * @param antiAliasingLevel - level of anti aliasing (num of rays will be
     *                          level^2(+1 if center was not included we add it back
     *                          in)).
     * @return - self return builder pattern.
     */
    public RenderBase setAntiAliasingLevel(int antiAliasingLevel) {
        this.antiAliasingLevel = antiAliasingLevel;
        return this;
    }

    /**
     * A builder setter for chaining definitions.
     * 
     * @param camera - the camera we will use to interact with the Scene during
     *               rendering.
     * @return RenderBase - the modifying object (self return).
     */
    public RenderBase setCamera(Camera camera) {
        this.camera = camera;
        return this;
    }

    /**
     * A builder setter for chaining definitions.
     * 
     * @param rayTracer - the tracing rays object we will use during rendering.
     * @return RenderBase - the modifying object (self return).
     */
    public RenderBase setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return this;
    }

    /**
     * A builder setter for chaining definitions.
     * 
     * @param writer - the writer we will use to output the rendered image.
     * @return RenderBase - the modifying object (self return).
     */
    public RenderBase setImageWriter(ImageWriter writer) {
        this.imageWriter = writer;
        return this;
    }

    /**
     * save rendered image to file.
     */
    public void writeToImage() {
        if (imageWriter == null)
            throw new MissingResourceException(RESOURCE_ERROR, renderClass, IMAGE_WRITER_COMPONENT);

        imageWriter.writeToImage();
    }

    /**
     * Create a grid [over the picture] in the pixel color map. given the grid's
     * step and color.
     * 
     * @param step  grid's step
     * @param color grid's color
     */
    public void printGrid(int step, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException(RESOURCE_ERROR, renderClass, IMAGE_WRITER_COMPONENT);

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                if (j % step == 0 || i % step == 0)
                    imageWriter.writePixel(j, i, color);
    }

    /**
     * This function renders image's pixel color map from the scene included with
     * the Renderer object
     */
    public void renderImage() {
        if (imageWriter == null)
            throw new MissingResourceException(RESOURCE_ERROR, renderClass, IMAGE_WRITER_COMPONENT);
        if (camera == null)
            throw new MissingResourceException(RESOURCE_ERROR, renderClass, CAMERA_COMPONENT);
        if (rayTracer == null)
            throw new MissingResourceException(RESOURCE_ERROR, renderClass, RAY_TRACER_COMPONENT);

        renderAlg();
    }

    protected abstract void renderAlg();
}
