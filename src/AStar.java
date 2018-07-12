import java.util.*;

public class AStar {
    private GraphNode start, end;
    private Set<GraphNode> nodes;
    private LinkedList<GraphNode> path;
    // set of discovered nodes, not yet evaluated
    private Set<GraphNode> openSet = new HashSet<>();
    // set of already evaluated nodes
    private Set<GraphNode> closedSet = new HashSet<>();
    // for each node, which node it can most efficiently be reached from.
    private LinkedHashMap<GraphNode, GraphNode> cameFrom = new LinkedHashMap<>();
    protected int nodesVisitedCount = 0;

    public AStar (GraphNode start, GraphNode end, Set<GraphNode> nodes) {
        this.start = start;
        this.end = end;
        this.nodes = nodes;

        getHeuristic();
        setInitialFScore();


        path = findPath();
    }

    private LinkedList<GraphNode> findPath() {
        openSet.add(start);

        while (!openSet.isEmpty()) {
            GraphNode curr = lowestFScore(openSet);
            nodesVisitedCount++;

            if (curr == end) {
                // we reached our goal
                return reconstructPath(cameFrom, end);
            }

            openSet.remove(curr);
            closedSet.add(curr);

            Set<GraphNode> currNeighbors = curr.getNeighbors();
            for (GraphNode n : currNeighbors) {
                if (closedSet.contains(n)) {
                    // neighbour already evaluated
                    continue;
                }
                if (!openSet.contains(n)) {
                    openSet.add(n);
                }

                int tentativeCost = curr.cost + 1;
                // check if the neighbour can we reached more efficiently from the curr node
                if (tentativeCost >= n.cost) {
                    // if not, ignore
                    continue;
                }

                // if yes, change the cost for neighbour and update it in the cameFrom map
                n.cost = tentativeCost;
                n.fScore = n.cost + n.heuristic;
                cameFrom.put(n, curr);
            }
        }

        return reconstructPath(cameFrom, end);
    }

    private LinkedList<GraphNode> reconstructPath(LinkedHashMap<GraphNode, GraphNode> cameFrom, GraphNode end) {
        LinkedList<GraphNode> path = new LinkedList<>();

        GraphNode curr = end;
        path.add(curr);
        while (curr != start) {
            curr = cameFrom.get(curr);
            path.add(curr);
        }

        return path;
    }

    private GraphNode lowestFScore(Set<GraphNode> nodes) {
        if (nodes.size() == 0) {
            return null;
        }

        GraphNode winner = (GraphNode) nodes.toArray()[0];
        for (GraphNode n : nodes) {
            if (n.fScore < winner.fScore) {
                winner = n;
            }
        }

        return winner;
    }

    // fscore is the function of adding the heuristic value and the cost
    private void setInitialFScore() {
        for (GraphNode n : nodes) {
            n.fScore = (int) Double.POSITIVE_INFINITY;
            n.cost = (int) Double.POSITIVE_INFINITY;
        }
        start.cost = 0;
        start.fScore = 0;
    }

    // use the Manhattan distance as our heuristic
    private void getHeuristic () {
        for (GraphNode n : nodes) {
            n.heuristic = Math.abs(n.height - end.height) + Math.abs(n.width - end.width);
        }
    }


    public LinkedList<GraphNode> getPath() {
        return path;
    }
}
