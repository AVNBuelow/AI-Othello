
/**
 * OthelloAIStoak implements an AI player for the game Othello.
 * It utilizes the Minimax algorithm with alpha-beta pruning to determine the best move.
 * Additionally, it incorporates a penalty matrix to evaluate board positions.
 *
 * Most code is implemented based on the slides provided in the course.
 */
public class OthelloAIStoak implements IOthelloAI{

    // max depth that the algorithm traverses
    private static final int maxDepth = 9;

    // boolean that decides whether to use the penalty matrix in evaluations
    private static final boolean useMatrix = true;

    // this is a debug-mode that prints search delays and the evaluation matrix
    private static final boolean debugMode = false;

    // penalty matrix used for board evaluation
    private float[][] penaltyMatrix;

    /**
     * Decides the best move based on the current game state.
     * @param s The current game state
     * @return The best move position determined by the algorithm
     */
    public Position decideMove(GameState s){
        if (penaltyMatrix == null){
            penaltyMatrix = GenerateMatrix(s.getBoard().length);
        }
        double timer = System.currentTimeMillis();
        Position p = minimax_search(s);
        if (debugMode){
            System.out.println("Time taken: " + ((System.currentTimeMillis() - timer) / 1000) + " to achieve p: " + p);
        }
        return p;
    }

    /**
     * Initiates the Minimax search algorithm.
     * @param state The current game state
     * @return The best move found
     */
    private Position minimax_search(GameState state){
        Move move = max_value(state, Float.MIN_VALUE, Float.MAX_VALUE, 0);
        return move.pos;
    }

    /**
     * The maximizing function of the Minimax algorithm with alpha-beta pruning.
     * @param state The current game state
     * @param alpha Alpha value for pruning
     * @param beta Beta value for pruning
     * @param depth Current search depth
     * @return The best move found at this level
     */
    private Move max_value(GameState state, float alpha, float beta, int depth){
        if (state.legalMoves().isEmpty() || depth >= maxDepth){
            if (useMatrix){
                return new Move(null, Evaluation(state.getBoard(), state.getPlayerInTurn()-1));
            }else{
                return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
            }
        }

        Move m = new Move(null, Float.MIN_VALUE);

        for (Position pos : state.legalMoves()){
            if (pos == null) { continue; }

            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null) { continue; }
            Move m2 = min_value(state2, alpha, beta, depth+1);
            if (m2.utility > m.utility || m2.pos == null) {
                m.utility = m2.utility;
                m.pos = pos;
                alpha = Math.max(alpha, m.utility);
            }

            if (m.utility >= beta){
                return m;
            }
        }

        return m;
    }

    /**
     * The minimizing function of the Minimax algorithm with alpha-beta pruning.
     * @param state The current game state
     * @param alpha Alpha value for pruning
     * @param beta Beta value for pruning
     * @param depth Current search depth
     * @return The best move found at this level
     */
    private Move min_value(GameState state, float alpha, float beta, int depth){
        if (state.legalMoves().isEmpty() || depth >= maxDepth){
            if (useMatrix){
                return new Move(null, Evaluation(state.getBoard(),state.getPlayerInTurn()-1));
            }else{
                return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
            }
        }

        Move m = new Move(null, Float.MAX_VALUE);

        for (Position pos : state.legalMoves()){
            if (pos == null) {continue;}
            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null) { continue; }

            Move m2 = max_value(state2, alpha, beta, depth+1);
            if (m2.utility < m.utility || m2.pos == null) {
                m.utility = m2.utility;
                m.pos = pos;
                beta = Math.min(beta, m.utility);
            }

            if (m.utility <= alpha){
                return m;
            }
        }
        return m;
    }

    /**
     * Simulates the result of a move.
     * @param state The current game state
     * @param pos The position to place the token
     * @param player The player making the move
     * @return The new game state after the move
     */
    private GameState Result (GameState state, Position pos, int player){
        GameState state2 = new GameState(state.getBoard(), player);
        if (!state2.insertToken(pos)){
            System.out.println("Illegal Move!");
            return null;
        }
        return state2;
    }

    /**
     * Evaluates the board state based on the penalty matrix.
     * @param board The game board
     * @param player The player to evaluate for
     * @return The evaluation score
     */
    private float Evaluation(int[][] board, int player){
        float sum = 0;
        for (int i = 0; i < board.length; i++){
            for (int y = 0; y < board.length; y++){
                if (board[i][y] == player){
                    sum += penaltyMatrix[i][y];
                }
            }
        }

        return sum;
    }

    /**
     * Generates the penalty matrix used for board evaluation.
     * @param size The size of the board
     * @return A matrix assigning different weights to board positions
     */
    private float[][] GenerateMatrix(int size){
        float[][] matrix = new float[size][size];

        for (int i = 0; i < size; i++){
            for (int y = 0; y < size; y++){
                matrix[i][y] = 50;
            }
        }

        // Corners
        matrix[0][0]            = 100f;
        matrix[0][size-1]       = 100f;
        matrix[size-1][0]       = 100f;
        matrix[size-1][size-1]  = 100f;

        // Around corners
        matrix[1][0]            = 15f;
        matrix[0][1]            = 15f;
        matrix[size-1][size-2]  = 15f;
        matrix[size-2][size-1]  = 15f;
        matrix[0][size-2]       = 15f;
        matrix[1][size-1]       = 15f;
        matrix[size-1][1]       = 15f;
        matrix[size-2][0]       = 15f;

        // Around corners diagonally
        matrix[1][1]            = 1f;
        matrix[size-2][size-2]  = 1f;
        matrix[size-2][1]       = 1f;
        matrix[1][size-2]       = 1f;

        if (debugMode){
            // Prints Matrix in terminal if debug-mode is enabled
            PrintBoard(matrix);
        }

        return matrix;
    }

    /**
     * Prints the penalty matrix.
     * @param board The matrix to print
     */
    private void PrintBoard(float[][] board){
        int size = board.length;
        for (int i = 0; i < size; i++){
            for (int y = 0; y < size; y++){
                System.out.print(board[i][y]);
                for (int space = 0; space < (7 - String.valueOf(board[i][y]).length()); space++){
                    System.out.print(" ");
                }
            }
            System.out.println();
            for (int line = 0; line < (size * 7); line++){
                System.out.print("_");
            }
            System.out.println();
        }
    }
}
