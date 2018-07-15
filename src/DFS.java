/** DFS, implements the Depth First Search search algorithms to find a path in a maze
 *
 * @author Robert Szafarczyk, July, 2018
 *
 * */

import java.util.*;

public class DFS {
    private GraphNode start, end;
    private Set<GraphNode> nodes;
    private LinkedList<GraphNode> path;
    protected int nodesVisitedCount = 0;

    public DFS (GraphNode start, GraphNode end, Set<GraphNode> nodes) {
        this.start = start;
        this.end = end;
        this.nodes = nodes;

        path = findPath();
    }


    private LinkedList<GraphNode> findPath() {
        Set<GraphNode> finished = new HashSet<>();
        Stack<GraphNode> stack = new Stack<>();
        LinkedHashMap<GraphNode, GraphNode> cameFrom = new LinkedHashMap<>();

        cameFrom.put(start, null);
        finished.add(start);
        stack.push(start);

        while (!stack.isEmpty()) {
            GraphNode current = stack.pop();
            finished.add(current);
            nodesVisitedCount++;

            if (current == end) {
                return GraphNode.reconstructPath(cameFrom, end, start);
            }

            Set<GraphNode> currNeighbors = current.getNeighbors();
            for (GraphNode neighbor : currNeighbors) {
                if (!finished.contains(neighbor)) {
                    stack.push(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        return GraphNode.reconstructPath(cameFrom, end, start);
    }

    public LinkedList<GraphNode> getPath() {
        return path;
    }
}
