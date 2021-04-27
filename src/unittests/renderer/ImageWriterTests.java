package unittests.renderer;

import org.junit.Test;

import primitives.Color;
import renderer.ImageWriter;

public class ImageWriterTests {

    @Test
    public void writeImageTest() {
        int nX = 800;
        int nY = 500;
        Color color = new Color(0, 210, 255);

        ImageWriter writer = new ImageWriter("TestCase01", nX, nY);

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % 50 == 0 || j % 50 == 0) {
                    writer.writePixel(j, i, Color.BLACK);
                } else {
                    writer.writePixel(j, i, color);
                }
            }

        }
        writer.writeToImage();
    }
}
