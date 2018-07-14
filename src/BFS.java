import java.util.*;

public class BFS {
    private GraphNode start, end;
    private Set<GraphNode> nodes;
    private LinkedList<GraphNode> path;
    protected int nodesVisitedCount = 0;

    public BFS (GraphNode start, GraphNode end, Set<GraphNode> nodes) {
        this.start = start;
        this.end = end;
        this.nodes = nodes;

        path = findPath();
    }


    private LinkedList<GraphNode> findPath() {
        Set<GraphNode> finished = new HashSet<>();
        Queue<GraphNode> queue = new PriorityQueue<>();
        LinkedHashMap<GraphNode, GraphNode> cameFrom = new LinkedHashMap<>();

        cameFrom.put(start, null);
        finished.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            GraphNode current = queue.remove();
            nodesVisitedCount++;

            if (current == end) {
                return GraphNode.reconstructPath(cameFrom, end, start);
            }

            Set<GraphNode> currNeighbors = current.getNeighbors();
            for (GraphNode neighbor : currNeighbors) {
                if (!finished.contains(neighbor)) {
                    finished.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return GraphNode.reconstructPath(cameFrom, end, start);
    }

    public LinkedList<GraphNode> getPath() {
        return path;
    }
}
