package gridSolver;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Solver {

    ArrayList<Node> unexpanded = new ArrayList<Node>(); // Holds unexpanded node list
    ArrayList<Node> expanded = new ArrayList<Node>();   // Holds expanded node list
    Node rootNode;                                      // Node representing initial state

    public Solver(int[] startBoard){
        GameState firstState = new GameState(startBoard); // Starting state
        rootNode = new Node(firstState);
    }

    public void solve(PrintWriter output) {
        unexpanded.add(rootNode);

        long startTime = System.currentTimeMillis();
        long endTime;

        while (unexpanded.size() > 0) { // While there remains no solution found and unexpanded nodes
            Node n = getLowestCostNode(unexpanded);  // Find the node of the lowest cost in the
            expanded.add(n); // Add this node to the expanded list
            unexpanded.remove(n); // Remove from unexpanded


            if (n.state.isGoal()) { // Solution found, report solution & exit
                endTime = System.currentTimeMillis();
                reportSolution(n, output, startTime, endTime);
                System.out.println("Solution found with " +  n.getNoOfMoves() + " moves");
                System.out.println("Time elapsed : " + TimeUnit.MILLISECONDS.toSeconds(endTime-startTime) + " Seconds \t(" + (endTime-startTime) + " ms)");
                System.out.println(n.state);
                return;
            } else {
                ArrayList<GameState> moveList = n.state.getPossibleMoves(); // Find a list of possible moves from the current state
                for (GameState gs : moveList) {
                    if ((Node.findNodeWithState(unexpanded, gs) == null) && (Node.findNodeWithState(expanded, gs) == null)) { // If state doesn't exist in expanded or unexpanded
                       int newCost = (n.getCost()+1) + getHeuristicCost(gs.board); // Calculate cost based on number of previous moves and difference between current solution and goal (Heuristic)
                        Node newNode = new Node(gs, n, newCost, n.getNoOfMoves()+1); // Create a new node for new state, store no of moves and cost
                        unexpanded.add(newNode);
                    }
                }
            }
        }
        System.out.println("No solution!");
        output.println("No solution found");
    }

    public static void main(String[] args) throws Exception {
        Solver problem = new Solver(GameState.INITIAL_BOARD);  // Set up the problem to be solved.
        File outFile = new File("output.txt");                 // Create a file as the destination for output
        PrintWriter output = new PrintWriter(outFile);         // Create a PrintWriter for that file
        problem.solve(new PrintWriter(output));                                 // Search for and print the solution
        output.close();                                        // Close the PrintWriter (to ensure output is produced).
    }


    public void reportSolution(Node n, PrintWriter output, long startTime, long endTime) {
        output.println("Solution found! \n");
        printSolution(n, output);
        output.println("\n" + n.getNoOfMoves() + " Moves");
        output.println("Nodes expanded: " + this.expanded.size());
        output.println("Nodes unexpanded: " + this.unexpanded.size());
        output.println("Time elapsed : " + TimeUnit.MILLISECONDS.toSeconds(endTime-startTime) + " Seconds \t(" + (endTime-startTime) + " ms)");
    }

    public void printSolution(Node n, PrintWriter output) {
        if (n.parent != null) printSolution(n.parent, output);
        output.println(n.state);
    }

    // Find the lowest cost node in the unexpanded node list
    private Node getLowestCostNode(ArrayList<Node> unexpanded){
        int lowestCost = 10000;
        Node lowestCostNode = null;

        for(Node n : unexpanded){
            if(n.getCost()<lowestCost){
                lowestCost = n.getCost();
                lowestCostNode = n;
            }
        }
        return lowestCostNode;
    }

    private int indexOf(int[] array, int value){
        for(int i=0;i<array.length;i++){
            if(array[i]==value) return i;
        }
        return -1;
    }

    // Calculate the Heuristic cost of the current board using the sum of distances
    // between the expected value and the actual value of each board tile. (Manhattan Distance)

    private int getHeuristicCost(int[] board) {
        int cost = 0;
        for(int x=0; x<board.length; x++){
            int actual_value = board[x];
            int tcost = Math.abs(indexOf(board, actual_value)-indexOf(board, x));
            cost+=tcost;
        }
        return cost;
    }

    // Calculate the Heuristic cost of the current board based on it's difference to the
    // goal state, where a board close to the goal state is lower cost.

    private int getHeuristicCost_lazy(int[] board) {
        int cost = 0;
        for(int x=0; x<board.length; x++){
            if (board[x] != GameState.goal[x]) cost+=1;
        }
        return cost;
    }


}
