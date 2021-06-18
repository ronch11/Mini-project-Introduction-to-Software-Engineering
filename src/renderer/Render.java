package renderer;

import java.util.List;

import primitives.Color;
import primitives.Ray;

/**
 * A Render Class to create pictures from scene's Camera point-of-view.
 * 
 * @author SHAI FALACH and RON HAIM HODADEDI
 */
public class Render extends RenderBase {
    public Render() {
        super("Render");
    }

    /**
     * render Camera point of view image from the scene using the camera view panel
     * and ray tracing from to the scene.
     */
    @Override
    protected void renderAlg() {
        int ny = imageWriter.getNy();
        int nx = imageWriter.getNx();
        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                if (antiAliasingLevel == 1) {
                    Ray cameraRay = camera.constructRayThroughPixel(nx, ny, j, i);
                    imageWriter.writePixel(j, i, rayTracer.traceRay(cameraRay));
                }
                // else if (adaptive) {
                // Color averageColor = adaptiveSuperSampling(ny, nx, i, j);
                // imageWriter.writePixel(j, i, averageColor);
                // }
                else {
                    Color averageColor = superSampling(ny, nx, i, j, antiAliasingLevel);
                    imageWriter.writePixel(j, i, averageColor);
                }
            }
        }
    }

    // private Color adaptiveSuperSampling(int ny, int nx, int i, int j) {
    // Color finalColor = Color.BLACK;

    // List<Point3D> points = camera.calculatePoints(nx, ny, j, i,
    // antiAliasingLevel);

    // Color centerRayColor = rayTracer.traceRay(camera.constructRayThroughPixel(nx,
    // ny, j, i));

    // return finalColor;
    // }

    // private Color calculateColorAdaptive(int ny, int nx, int i, int j) {
    // Color centerRayColor = rayTracer.traceRay(camera.constructRayThroughPixel(nx,
    // ny, j, i));
    // Color averageColor = superSampling(ny, nx, i, j, 2);

    // if (averageColor.equals(centerRayColor)) {
    // return centerRayColor;
    // }
    // else {
    // // divide to 4 quarters and run again for each quarter
    // Color q1 = calculateColorAdaptive();
    // Color q2 = calculateColorAdaptive();
    // Color q3 = calculateColorAdaptive();
    // Color q4 = calculateColorAdaptive();

    // return averageColor.add(q1,q2,q3,q4).reduce(4);
    // }
    // }

    private Color superSampling(int ny, int nx, int i, int j, int gridSize) {
        Color averageColor = Color.BLACK;
        List<Ray> rays = camera.createGridCameraRays(camera.calculatePoints(nx, ny, j, i, gridSize));
        for (Ray cameraRay : rays) {
            averageColor = averageColor.add(rayTracer.traceRay(cameraRay));
        }
        averageColor = averageColor.reduce(rays.size());
        return averageColor;
    }

}
