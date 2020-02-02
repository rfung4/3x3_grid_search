package gridSolver;

import java.util.ArrayList;

/*
The class Node represents nodes.
Its essential features are a GameState and a reference to the node's parent node. The
latter is used to assemble and output the solution path once the goal sate has been reached.
 */

public class Node {
    GameState state;    // The state associated with the node
    Node parent;        // The node from which this node was reached.
    private int cost;   // The cost, in terms of heuristic & number of previous moves
    private int moves; // Number of moves taken to get to this state

    /*
      Constructor used to create new nodes.
     */
    public Node(GameState state, Node parent, int cost, int moveCount) {
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        this.moves = moveCount;
    }

    /*
      Constructor used to create initial node.
     */
    public Node(GameState state) {
        this(state,null,0,0);
    }

    public int getCost() {
        return cost;
    }
    public int getNoOfMoves() {return moves;}

    public String toString() {
        return "Node:" + state + " ";
    }

    /*
      Given a list of nodes as first argument, findNodeWithState searches the list for a node
       whose state is that specified as the second argument.
       If such a node is in the list, the first one encountered is returned.
       Otherwise null is returned.
     */

    public static Node findNodeWithState(ArrayList<Node> nodeList, GameState gs) {
        for (Node n : nodeList) {
            if (gs.sameBoard(n.state)){
                return n;
            }
        }
        return null;
    }

}
