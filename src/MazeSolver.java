/** MazeSolver, this is the application class.
 *
 * @author Robert Szafarczyk, July 2018
 *
 * */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MazeSolver {
    private static String fileName;
    private static BufferedImage img;
    private static ProcessImage processImg;
    private static int height, width;
    private static boolean[][] mazeBoolean;
    private static MazeToGraph mazeGraph;

    public static void main(String[] args) {
        // READ FILE
        fileName = args[0];
        img = null;
        try {
            img = ImageIO.read(new File(fileName));
        }
        catch (IOException e) {
            System.out.println( e);
            System.exit(1);
        }

        processImg = new ProcessImage(img);
        height = processImg.getWidth();
        width = processImg.getHeight();
        mazeBoolean = processImg.getMazeBoolean();

        for (int h=0; h<height; h++) {
            for (int w=0; w<width; w++) {
                if (mazeBoolean[h][w]) {
                    System.out.print("T");
                }
                else {
                    System.out.print("F");
                }
                System.out.print(" ");
            }
            System.out.println();
        }


        mazeGraph = new MazeToGraph(mazeBoolean, height, width);
    }
}