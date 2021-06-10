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
                } else {
                    Color averageColor = Color.BLACK;
                    List<Ray> rays = camera.createGridCameraRays(nx, ny, j, i, antiAliasingLevel);
                    for (Ray cameraRay : rays) {
                        averageColor = averageColor.add(rayTracer.traceRay(cameraRay));
                    }
                    averageColor = averageColor.reduce(rays.size());
                    imageWriter.writePixel(j, i, averageColor);
                }
            }
        }
    }
}
