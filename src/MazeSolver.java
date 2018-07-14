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
    private static int nodeCount;
    private static GraphNode startNode, endNode;
    private static AStar aStar;
    private static LinkedList<GraphNode> aStarPath;
    private static BFS bfs;
    private static LinkedList<GraphNode> bfsPath;
    private static long startTime;


    public static void main(String[] args) {
        startTime = System.nanoTime();

        // READ FILE
        System.out.println("\nLoading image..");
        img = null;
        try {
            fileName = args[0];
            img = ImageIO.read(new File(fileName));
        }
        catch (IOException e) {
            System.out.println("Could not read file");
            System.exit(1);
        }

        System.out.println("Converting image to graph..");
        // Process image, convert maze to boolean, convert to nodes
        processImg = new ProcessImage(img);
        height = processImg.getHeight();
        width = processImg.getWidth();
        mazeBoolean = processImg.getMazeBoolean();
        mazeGraph = new MazeToGraph(mazeBoolean, height, width);
        nodes = mazeGraph.getGraphNodes();
        nodeCount = nodes.size();
        startNode = mazeGraph.getStartNode();
        endNode = mazeGraph.getEndNode();
        System.out.println("Total node count: " + nodeCount);
        System.out.println("Time elapsed: " + 1.0 * (System.nanoTime() - startTime) / 1000000000 + " s") ;

        processAStar();
        processBFS();
    }


    private static void processAStar () {
        System.out.println("\nSolving with A*..");
        aStar = new AStar(startNode, endNode, nodes);
        aStarPath = aStar.getPath();
        System.out.println("Path found");
        System.out.println("Nodes visited: " + aStar.nodesVisitedCount);
        System.out.println("Nodes in solution path: " + aStarPath.size());
        System.out.println("Time elapsed: " + 1.0 * (System.nanoTime() - startTime) / 1000000000 + " s");


        System.out.println("Saving A* solution image to current directory..");
        // Save image with solution
        BufferedImage imgSolved = drawPath(aStarPath, img);
        try{
            String extension = "";

            int i = fileName.lastIndexOf('.');
            int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

            if (i > p) {
                extension = fileName.substring(i+1);
            }

            File f = new File("../mazes/SOLUTION-ASTAR-" + new File(fileName).getName());
            ImageIO.write(imgSolved, extension, f);
        } catch(IOException e){
            System.out.println("Could not save solution image");
        }
    }

    private static void processBFS () {
        System.out.println("\nSolving with BFS..");
        bfs = new BFS(startNode, endNode, nodes);
        bfsPath = bfs.getPath();
        System.out.println("Path found");
        System.out.println("Nodes visited: " + bfs.nodesVisitedCount);
        System.out.println("Nodes in solution path: " + bfsPath.size());
        System.out.println("Time elapsed: " + 1.0 * (System.nanoTime() - startTime) / 1000000000 + " s");


        System.out.println("Saving BFS solution image to current directory..");
        // Save image with solution
        BufferedImage imgSolved = drawPath(aStarPath, img);
        try{
            String extension = "";

            int i = fileName.lastIndexOf('.');
            int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

            if (i > p) {
                extension = fileName.substring(i+1);
            }

            File f = new File("../mazes/SOLUTION-BFS-" + new File(fileName).getName());
            ImageIO.write(imgSolved, extension, f);
        } catch(IOException e){
            System.out.println("Could not save solution image");
        }
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