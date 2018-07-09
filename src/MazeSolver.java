/** MazeSolver, this is the application class.
 *
 * @author Robert Szafarczyk, July 2018
 *
 * */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.awt.Color;


public class MazeSolver {
    private static String fileName;
    private static BufferedImage img;
    private static ProcessImage processImg;
    private static MazeToGraph mazeGraph;
    private static int height, width;
    private static boolean[][] mazeBoolean;
    private static Set<GraphNode> nodes;
    private static GraphNode startNode, endNode;
    private static AStar aStar;
    private static LinkedList<GraphNode> aStarPath;


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
        height = processImg.getHeight();
        width = processImg.getWidth();
        mazeBoolean = processImg.getMazeBoolean();

        mazeGraph = new MazeToGraph(mazeBoolean, height, width);
        nodes = mazeGraph.getGraphNodes();
        startNode = mazeGraph.getStartNode();
        endNode = mazeGraph.getEndNode();


        // A* algorithm
        System.out.println("Solving with A* ...");
        aStar = new AStar(startNode, endNode, nodes);
        aStarPath = aStar.getPath();

        BufferedImage imgSolved = drawPath(aStarPath, img);
        //write image
        try{
            File f = new File("../mazes/SOLUTION-" + new File(fileName).getName());
            ImageIO.write(imgSolved, "gif", f);
        }catch(IOException e){
            System.out.println(e);
        }


        System.out.println("\nFinished!\nAn image with the solution has created in the current directory.");
    }

    // draws a green line that represents the solution path
    private static BufferedImage drawPath (LinkedList<GraphNode> path, BufferedImage img) {
        BufferedImage solutionImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int w=0; w<width; w++) {
            for (int h=0; h<height; h++) {
                if (mazeBoolean[h][w]) {
                    solutionImage.setRGB(w, h, Color.WHITE.getRGB());
                }
                else {
                    solutionImage.setRGB(w, h, Color.BLACK.getRGB());
                }
            }
        }
        for (int i=0; i<path.size(); i++) {
            GraphNode curr = path.get(i);
            solutionImage.setRGB(curr.width, curr.height, Color.GREEN.getRGB());

            if (i == path.size()-1) {
                break;
            }

            GraphNode next = path.get(i+1);

            // draw pixels until next node
            char dir = getDrawDirection(curr, next);
            switch (dir) {
                case 'n':
                    for (int h=curr.height; h>next.height; h--) {
                        solutionImage.setRGB(curr.width, h, Color.GREEN.getRGB());
                    }
                    break;
                case 's':
                    for (int h=curr.height; h<next.height; h++) {
                        solutionImage.setRGB(curr.width, h, Color.GREEN.getRGB());
                    }
                    break;
                case 'w':
                    for (int w=curr.width; w>next.width; w--) {
                        solutionImage.setRGB(w, curr.height, Color.GREEN.getRGB());
                    }
                    break;
                case 'e':
                    for (int w=curr.width; w<next.width; w++) {
                        solutionImage.setRGB(w, curr.height, Color.GREEN.getRGB());
                    }
                    break;
            }
        }

        return solutionImage;
    }


    private static char getDrawDirection (GraphNode curr, GraphNode next) {
        int currHeight = curr.height;
        int currWidth = curr.width;
        int nextHeight = next.height;
        int nextWidth = next.width;
        char dir = ' ';

        if (nextHeight - currHeight < 0) {
            dir = 'n';
        }
        else if (nextHeight - currHeight > 0) {
            dir = 's';
        }
        else if (nextWidth - currWidth < 0) {
            dir = 'w';
        }
        else if (nextWidth - currWidth > 0) {
            dir = 'e';
        }

        return dir;
    }
}