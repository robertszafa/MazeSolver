/** ProcessImage, the task of this class is to transform the maze image into a 2D boolean array where true represents a
 *  path and false represents a wall.
 *
 * @author Robert Szafarczyk, July 2018
 *
 * */

import java.awt.image.BufferedImage;

public class ProcessImage {
    private int height, width;
    private int[][] pixelValues;
    private boolean[][] mazeBoolean;

    public ProcessImage (BufferedImage img) {
        height = img.getHeight();
        width = img.getWidth();
        pixelValues = getPixelValues(img);
        mazeBoolean = transformPixelValuesToBoolean(pixelValues);
    }

    // gets the 32bit value of the pixel. Where alpha, red, green, blue take
    // each 8 bits. We are dealing with black-white pixel values only, so this
    // pixel values is sufficient for what we want to achieve.
    private int[][] getPixelValues (BufferedImage img) {
        int[][] result = new int [height][width];

        for (int h=0; h<height; h++) {
            for (int w=0; w<width; w++) {
                // get RGB(x, y) - x is width of maze, y is height
                result[h][w] = img.getRGB(w, h);
            }
        }

        return result;
    }

    // TRUE if pixel is white, FALSE if pixel is black
    private boolean[][] transformPixelValuesToBoolean (int[][] pixels) {
        boolean[][] result = new boolean[height][width];

        for (int h=0; h<height; h++) {
            for (int w=0; w <width; w++) {
                result[h][w] = (pixels[h][w] == -1);
            }
        }

        return result;
    }

    public int getWidth() {
    	return width;
    }

    public int getHeight() {
    	return height;
    }


    public boolean[][] getMazeBoolean() {
    	return mazeBoolean;
    }
}
