/** GraphNode, a single pixel location in the maze. Has height and width attributes. Has north, south, west and east
 *  attributes that are the neighbour GraphNodes in the respective direction.
 *
 * @author Robert Szafarczyk, July, 2018
 *
 * */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GraphNode implements Comparable<GraphNode> {
    // north, south, west, east
    protected GraphNode n, s, w, e;
    protected int height, width;
    protected int[] coordinates;
    protected boolean isJunction;
    protected int cost;
    protected int heuristic;
    // fscore is the sum of the heuristic function and the cost. It's used in the A* algorithm
    protected int fScore;

    public GraphNode (GraphNode north, GraphNode south,
                        GraphNode west, GraphNode east, boolean isJunction, int height, int width) {
        n = north;
        s = south;
        w = west;
        e = east;
        this.isJunction = isJunction;
        this.height = height;
        this.width = width;
        coordinates = new int[] {height, width};
    }

    public Set<GraphNode> nextNode(Set<GraphNode> unvisited) {
        Set<GraphNode> nextNodes = new HashSet<>();

        if (isJunction) {
            if (n != null && unvisited.contains(n)) {
                nextNodes.add(n);
            }
            if (s != null && unvisited.contains(s)) {
                nextNodes.add(s);
            }
            if (w != null && unvisited.contains(w)) {
                nextNodes.add(w);
            }
            if (e != null && unvisited.contains(e)) {
                nextNodes.add(e);
            }
        }
        else {
            if (n != null && unvisited.contains(n)) {
                nextNodes.add(n);
            }
            else if (s != null && unvisited.contains(s)) {
                nextNodes.add(s);
            }
            else if (w != null && unvisited.contains(w)) {
                nextNodes.add(w);
            }
            else if (e != null && unvisited.contains(e)) {
                nextNodes.add(e);
            }
        }

        return nextNodes;
    }

    public Set<GraphNode> getNeighbors() {
        Set<GraphNode> neighbors = new HashSet<>();

        if (n != null) {
            neighbors.add(n);
        }
        if (s != null) {
            neighbors.add(s);
        }
        if (w != null) {
            neighbors.add(w);
        }
        if (e != null) {
            neighbors.add(e);
        }

        return neighbors;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null) {
            return false;
        }

        GraphNode node = (GraphNode) obj;

        if (Arrays.equals(coordinates, node.coordinates)) {
            return true;
        }

        return false;
    }

    // compare function for AStar algorithm.
    // Compares the sum of taken steps (cost) and the euclidean distance (our heuristic).
    @Override
    public int compareTo(GraphNode node) {
        return (this.cost + this.heuristic) - (node.cost + node.heuristic);
    }
}
