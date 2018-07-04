/** GraphNode, a single pixel location in the maze. Has height and width attributes. Has north, south, west and east
 *  attributes that are the neighbour GraphNodes in the respective direction.
 *
 * @author Robert Szafarczyk, July, 2018
 *
 * */

import java.util.Arrays;

public class GraphNode {
    // north, south, west, east
    protected GraphNode n, s, w, e;
    protected int height, width;
    protected int[] coordinates;
    protected boolean isJunction;

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

}
