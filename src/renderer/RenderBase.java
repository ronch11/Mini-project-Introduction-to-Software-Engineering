package renderer;

import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;

public abstract class RenderBase {
    private static final String RESOURCE_ERROR = "Renderer resource not set";
    private static final String IMAGE_WRITER_COMPONENT = "Image writer";
    private static final String CAMERA_COMPONENT = "Camera";
    private static final String RAY_TRACER_COMPONENT = "Ray tracer";
    private String renderClass;
    protected boolean adaptive = false;

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
     * @param adaptive the adaptive to set
     */
    public RenderBase setAdaptive(boolean adaptive) {
        this.adaptive = adaptive;
        return this;
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

    /**
     * Cast ray from camera in order to color a pixel
     * 
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     */
    protected void castRay(int nX, int nY, int col, int row) {
        if (antiAliasingLevel == 1) { // no AA
            Ray ray = camera.constructRayThroughPixel(nX, nY, col, row);
            Color color = rayTracer.traceRay(ray);
            imageWriter.writePixel(col, row, color);
        } else if (adaptive) { // adaptive AA
            Color averageColor = adaptiveSuperSampling(nX, nY, col, row);
            imageWriter.writePixel(col, row, averageColor);
        } else { // normal AA
            Color averageColor = superSampling(nX, nY, col, row, antiAliasingLevel);
            imageWriter.writePixel(col, row, averageColor);
        }
    }

    protected Color adaptiveSuperSampling(int nx, int ny, int j, int i) {
        HashMap<Point3D, Color> colorRepo = new HashMap<>();
        Ray centerRay = camera.constructRayThroughPixel(nx, ny, j, i);
        Color baseColor = getPointColor(colorRepo, camera.getPixel(centerRay));
        return calculateColorAdaptive(nx, ny, antiAliasingLevel, centerRay.getP0(), baseColor, colorRepo);
    }

    protected Color getPointColor(HashMap<Point3D, Color> colorRepo, Point3D point) {
        if (colorRepo.containsKey(point)) {
            return colorRepo.get(point);
        }
        Color color = rayTracer.traceRay(camera.constructRayThroughPixel(point));
        colorRepo.put(point, color);
        return color;
    }

    protected Color calculateColorAdaptive(int nx, int ny, int level, Point3D center, Color base,
            HashMap<Point3D, Color> colorRepo) {
        if (level == 1) { // end of recursion
            return getPointColor(colorRepo, center);
        }
        List<Point3D> corners = camera.pixelCorners(nx, ny, center);
        List<Point3D> centers = camera.getNewCenters(nx, ny, corners);

        Point3D tl = corners.get(0); // top left
        Point3D tr = corners.get(1); // top right
        Point3D bl = corners.get(2); // bottom left
        Point3D br = corners.get(3); // bottom right
        boolean difference = false;

        Color tlRayColor = getPointColor(colorRepo, tl);
        if (!tlRayColor.equals(base)) {
            tlRayColor = calculateColorAdaptive(nx / 2, ny / 2, level - 1, centers.get(0), base, colorRepo);
            difference = true;
        }

        Color trRayColor = getPointColor(colorRepo, tr);
        if (!trRayColor.equals(base)) {
            trRayColor = calculateColorAdaptive(nx / 2, ny / 2, level - 1, centers.get(1), base, colorRepo);
            difference = true;
        }

        Color blRayColor = getPointColor(colorRepo, bl);
        if (!blRayColor.equals(base)) {
            blRayColor = calculateColorAdaptive(nx / 2, ny / 2, level - 1, centers.get(2), base, colorRepo);
            difference = true;
        }

        Color brRayColor = getPointColor(colorRepo, br);
        if (!brRayColor.equals(base)) {
            brRayColor = calculateColorAdaptive(nx / 2, ny / 2, level - 1, centers.get(3), base, colorRepo);
            difference = true;
        }

        if (difference) {
            return base.add(tlRayColor, trRayColor, blRayColor, brRayColor).reduce(4);
        } else {
            return base;
        }
    }

    protected Color superSampling(int ny, int nx, int j, int i, int gridSize) {
        Color averageColor = Color.BLACK;
        List<Ray> rays = camera.createGridCameraRays(camera.calculatePoints(nx, ny, j, i, gridSize));
        for (Ray cameraRay : rays) {
            averageColor = averageColor.add(rayTracer.traceRay(cameraRay));
        }
        averageColor = averageColor.reduce(rays.size());
        return averageColor;
    }

    protected abstract void renderAlg();
}
