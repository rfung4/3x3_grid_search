package gridSolver;

import java.util.ArrayList;
import java.util.Arrays;

/*
      Instances of the class GameState represent states that can arise in a 3x3 sliding
      puzzle, the board is represented using an array of integers where the empty tile is
      represented using 0.

      The goal and current states are represented using 1D integer array, and the spaceIndex
      field stores the index of the the 0 value, calculated on construction for efficiency.
 */

public class GameState {

    public static final int[] goal = {1,2,3,4,5,6,7,8,0}; // Board is represented by 9 length integer array, where the 'empty' tile is represented as 0
    public static final int[] INITIAL_BOARD = new int[] {8,7,6,5,4,3,2,1,0}; // Initial board state

    final int[] board; // The current board
    private int spaceIndex; // Index of the space (0) value

    /*
        GameState is a constructor that takes an integer array representing the current board
     */
    public GameState(int[] board) {
        this.board = board;
        spaceIndex = calculateSpaceIndex(board);
    }

    /*
        Returns the index of the empty space, represented on the board by the 0 value
     */

    private static int calculateSpaceIndex(int[] board){
        for(int i=0;i <board.length;i++){
            if(board[i]==0){
                return i;
            }
        }
        return -1;
    }

    private void recalculateSpaceIndex(){
        spaceIndex = calculateSpaceIndex(board);
    }

    /*
        clone returns a new GameState with the same board configuration as the current GameState.
     */

    public GameState clone() {
        return new GameState(Arrays.copyOf(board, board.length));
    }

    /*
        toString returns the board configuration of the current GameState as a printable string.
    */
    public String toString() {
        return Arrays.toString(board);
    }

    /*
        isGoal returns true if and only if the board configuration of the current GameState is the goal
        configuration, checked by comparing all of the board & goal elements.
    */

    public boolean isGoal() {
        for(int x=0; x<board.length; x++){
            if(board[x]!=goal[x]) return false;
        }
        return true;
    }

    /*
         sameBoard returns true if and only if the GameState supplied as argument has the same board
         configuration as the current GameState.
     */

    public boolean sameBoard (GameState gs) {
        for(int x=0; x<board.length; x++){
            if(gs.board[x]!=board[x]){
                return false;
            }
        }
        return true;
    }


    // Takes the row and column of the new empty tile,
    // calculates the index relative to the current states board
    // switching the two values and returning the new GameState

    private GameState moveEmptyTile(int nextRow, int nextCol){
        GameState nextState = this.clone();
        int nextSpaceIndex = (nextRow*3) + nextCol;
        int tmp = nextState.board[nextSpaceIndex];
        nextState.board[nextSpaceIndex] = 0;
        nextState.board[nextState.spaceIndex] = tmp;
        nextState.recalculateSpaceIndex();
        return nextState;
    }

    // Returns a list of possible moves from the current game state,
    // A maximum of 4 exist for any current game state (move tile above down, ect..)

    public ArrayList<GameState> getPossibleMoves(){
        ArrayList<GameState> moves = new ArrayList<>();
        // The row and column are calculated here for the space index (0) value,
        // this was done as opposed to storing each row as a separate array for efficiency.

        int spaceRow = (int) Math.floor(spaceIndex/3.0); // Calculate the row from the space index
        int spaceCol = spaceIndex%3; // Calculate space column from space index

        if(spaceRow!=0){ // Can move tile above down if not at top row
            int nextRow = spaceRow-1;
            int nextCol = spaceCol;
            GameState next = moveEmptyTile(nextRow,nextCol);
            moves.add(next);
        }

        if(spaceRow!=2){ // Can move tile below up if not at bottom row
            int nextRow = spaceRow+1;
            int nextCol = spaceCol;
            GameState next = moveEmptyTile(nextRow,nextCol);
            moves.add(next);
        }

        if(spaceCol!=0){ // Can move tile left if not at left most row
            int nextRow = spaceRow;
            int nextCol = spaceCol-1;
            GameState next = moveEmptyTile(nextRow,nextCol);
            moves.add(next);
        }

        if(spaceCol!=2){ // Can move tile right if not at right most row
            int nextRow = spaceRow;
            int nextCol = spaceCol+1;
            GameState next = moveEmptyTile(nextRow,nextCol);
            moves.add(next);
        }

        return moves;
    }


}

