/** MazeToGraph, the task of this class is to transform the 2D boolean array (maze) into a directed graph.
 *  The nodes are represented by the GraphNode class, and each node has a north, south, west and east neighbour.
 *  This allows to dramatically decrease the size of the maze.
 *
 * @author Robert Szafarczyk, July 2018
 *
 * */

import java.util.*;


public class MazeToGraph {
    private boolean[][] maze;
    private int height, width;
    private int[] startCo, endCo;
    private GraphNode startNode, endNode;
    private Set<GraphNode> graphNodes = new HashSet<>();

    public MazeToGraph (boolean[][] maze, int height, int width) {
        this.maze = maze;
        this.height = height;
        this.width  = width;

        startCo = getStartCoordinates();
        endCo = getEndCoordinates();
        startNode = new GraphNode(null, null, null, null, false, startCo[0], startCo[1]);
        endNode = new GraphNode(null, null, null, null, false, endCo[0], endCo[1]);

        getGraphMaze();
        connectStartEnd();

    }

    
    private void getGraphMaze() {
        int[] currCo = startCo;
        GraphNode lastNode = startNode;
        // available directions from start node
        Stack<Character> directions;
        // direction of the last lastNode. if 'x', then there was no previous direction
        char lastDirection = 'x';
        Stack<Hashtable<GraphNode, Stack<Character>>> lastJunctions = new Stack<>();

        while (lastNode != endNode || lastJunctions.size() > 0) {
            graphNodes.add(lastNode);
            directions = getDirection(currCo, lastDirection);

            // all possible nodes were added
            if (directions.size() == 0 && lastJunctions.size() == 0) {
                break;
            }
            // cannot continue on this path. Fallback to last available junction
            else if (directions.size() == 0) {
                // retrieve node from lastJunctions stack
                lastNode = (GraphNode) lastJunctions.peek().keySet().toArray()[0];
                lastDirection = lastJunctions.peek().get(lastNode).pop();
                lastNode = findNextNode(lastNode.coordinates, lastDirection, lastNode);

                // if the node from lastJunctions doesn't have anymore directions, pop it from the stack
                if (lastJunctions.peek().get(lastJunctions.peek().keySet().toArray()[0]).size() < 1) {
                    lastJunctions.pop();
                }
            }
            // continue on current path
            else {
                // check if current location is a junction
                if (directions.size() > 1) {
                    lastNode.isJunction = true;
                    Hashtable<GraphNode, Stack<Character>> thisJunction = new Hashtable<>();
                    thisJunction.put(lastNode, directions);
                    lastJunctions.push(thisJunction);
                    lastNode = findNextNode(currCo, directions.peek(), lastNode);
                }
                else {
                    lastNode = findNextNode(currCo, directions.peek(), lastNode);
                }

                lastDirection = directions.pop();
            }

            currCo = lastNode.coordinates;

            if (Arrays.equals(lastNode.coordinates, endCo)) {
                endNode = lastNode;
                graphNodes.add(endNode);
            }
        }
    }

    // returns the next node after lastNode in the specified direction
    private GraphNode findNextNode (int[] currCo, char direction, GraphNode lastNode) {
        int currHeight = currCo[0];
        int currWidth = currCo[1];
        GraphNode newNode;

        while (!(currHeight == endCo[0] && currWidth == endCo[1])) {
            switch (direction) {
                case 'n':
                    if (!maze[currHeight - 1][currWidth] ||
                            (maze[currHeight][currWidth + 1] && currHeight != currCo[0]) ||
                            (maze[currHeight][currWidth - 1] && currHeight != currCo[0])) {
                        newNode = new GraphNode(null, lastNode, null, null,
                                isJunction(currHeight, currWidth), currHeight, currWidth);
                        lastNode.n = newNode;
                        return newNode;
                    }
                    currHeight--;
                    break;
                case 's':
                    if (!maze[currHeight + 1][currWidth] ||
                            (maze[currHeight][currWidth + 1] && currHeight != currCo[0]) ||
                            (maze[currHeight][currWidth - 1] && currHeight != currCo[0])) {
                        newNode = new GraphNode(lastNode, null, null, null,
                                isJunction(currHeight, currWidth), currHeight, currWidth);
                        lastNode.s = newNode;
                        return newNode;
                    }
                    currHeight++;
                    break;
                case 'e':
                    if (!maze[currHeight][currWidth + 1] ||
                            (maze[currHeight + 1][currWidth] && currWidth != currCo[1]) ||
                            (maze[currHeight - 1][currWidth] && currWidth != currCo[1])) {
                        newNode = new GraphNode(null, null, lastNode, null,
                                isJunction(currHeight, currWidth), currHeight, currWidth);
                        lastNode.e = newNode;
                        return newNode;
                    }
                    currWidth++;
                    break;
                case 'w':
                    if (!maze[currHeight][currWidth - 1] ||
                            (maze[currHeight + 1][currWidth] && currWidth != currCo[1]) ||
                            (maze[currHeight - 1][currWidth] && currWidth != currCo[1])) {
                        newNode = new GraphNode(null, null, null, lastNode,
                                isJunction(currHeight, currWidth), currHeight, currWidth);
                        lastNode.w = newNode;
                        return newNode;
                    }
                    currWidth--;
                    break;
            }
        }

        return endNode;
    }

    // returns a stack with the available directions to go from the current location
    private Stack<Character> getDirection (int[] currCo, char lastDirection) {
        int currHeight = currCo[0];
        int currWidth = currCo[1];
        Stack<Character> directions = new Stack<>();

        if (currHeight + 1 < height - 1 && maze[currHeight + 1][currWidth] && lastDirection != 'n') {
            directions.push('s');
        }
        if (currHeight - 1 > 0 && maze[currHeight - 1][currWidth] && lastDirection != 's') {
            directions.push('n');
        }
        if (currWidth + 1 < width - 1 && maze[currHeight][currWidth + 1] && lastDirection != 'w') {
            directions.push('e');
        }
        if (currWidth - 1 > 0 && maze[currHeight][currWidth - 1] && lastDirection != 'e') {
            directions.push('w');
        }

        return directions;
    }

    // returns true if the current location has more than two directions to go
    private boolean isJunction (int height, int width) {
        ArrayList<Character> directions = new ArrayList<>();

        if (height + 1 < height - 1 && maze[height + 1][width]) {
            directions.add('s');
        }
        else if (height - 1 > 0 && maze[height - 1][width]) {
            directions.add('n');
        }
        else if (width + 1 < width - 1 && maze[height][width + 1]) {
            directions.add('e');
        }
        else if (width - 1 > 0 && maze[height][width - 1]) {
            directions.add('w');
        }

        if (directions.size() > 2) {
            return true;
        }

        return false;
    }

    // reverses the cost of the nodes to be decreasing when approaching the endNode
//    private void reverseNodeCost() {
//        int endCost = endNode.cost;
//        for (GraphNode n : graphNodes) {
//            n.cost = endCost - n.cost;
//        }
//    }

    // add north, south, west, east GraphNodes to endNode and startNode
    private void connectStartEnd() {
        // START is at north
        if (startCo[0] == 1) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[0] == 2 && n.coordinates[1] == startCo[1]) {
                    startNode.s = n;
                    n.n = startNode;
                    break;
                }
            }
        }
        // START is at south
        else if (startCo[0] == height - 2) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[0] == height - 3 && n.coordinates[1] == startCo[1]) {
                    startNode.n = n;
                    n.s = startNode;
                    break;
                }
            }
        }
        // START is at west
        else if (startCo[1] == 1) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[1] == 2 && n.coordinates[0] == startCo[0]) {
                    startNode.e = n;
                    n.w = startNode;
                    break;
                }
            }
        }
        // START is at east
        else if (startCo[1] == width - 2) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[1] == width - 3 && n.coordinates[0] == startCo[0]) {
                    startNode.w = n;
                    n.e = startNode;
                    break;
                }
            }
        }


        // END is at north
        if (endCo[0] == 1) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[0] == 2 && n.coordinates[1] == endCo[1]) {
                    endNode.s = n;
                    n.n = endNode;
                    break;
                }
            }
        }
        // END is at south
        else if (endCo[0] == height - 2) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[0] == height - 3 && n.coordinates[1] == endCo[1]) {
                    endNode.n = n;
                    n.s = endNode;
                    break;
                }
            }
        }
        // END is at west
        else if (endCo[1] == 1) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[1] == 2 && n.coordinates[0] == endCo[0]) {
                    endNode.e = n;
                    n.w = endNode;
                    break;
                }
            }
        }
        // END is at east
        else if (endCo[1] == width - 2) {
            for (GraphNode n : graphNodes) {
                if (n.coordinates[1] == width - 3 && n.coordinates[0] == endCo[0]) {
                    endNode.w = n;
                    n.e = endNode;
                    break;
                }
            }
        }
    }


    // returns height, width coordinates of START
    private int[] getStartCoordinates() {
        for (int w=1; w<width-2; w++) {
            if (maze[1][w]) {
                return new int[] {1, w};
            }
        }

        for (int w=1; w<width-2; w++) {
            if (maze[height-2][w]) {
                return new int[] {height-2, w};
            }
        }

        for (int h=1; h<height-2; h++) {
            if (maze[h][1]) {
                return new int[] {h, 1};
            }
        }

        return null;
    }

    // returns height, width coordinates of END
    private int[] getEndCoordinates() {
        for (int w=1; w<width-2; w++) {
            if (maze[1][w] && !(Arrays.equals(new int[] {1, w}, startCo))) {
                return new int[] {1, w};
            }
        }

        for (int w=1; w<width-2; w++) {
            if (maze[height-2][w] && !(Arrays.equals(new int[] {height-2, w}, startCo))) {
                return new int[] {height-2, w};
            }
        }

        for (int h=1; h<height-2; h++) {
            if (maze[h][1] && !(Arrays.equals(new int[] {h, 1}, startCo))) {
                return new int[] {h, 1};
            }
        }

        for (int h=1; h<height-2; h++) {
            if (maze[h][width-2] && !(Arrays.equals(new int[] {h, width-2}, startCo))) {
                return new int[] {h, width-2};
            }
        }

        return null;
    }


    public GraphNode getStartNode() {
        return startNode;
    }

    public GraphNode getEndNode() {
        return endNode;
    }

    public Set<GraphNode> getGraphNodes() {
        return graphNodes;
    }
}
