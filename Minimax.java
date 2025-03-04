import java.util.ArrayList;

/***
 * This is implemented via the slides Lecture 3 slide 10
 */

public class Minimax implements IOthelloAI{

    private static final int maxDepth = 5;
    private float[][] penaltyMatrix;
    public Position decideMove(GameState s){

        if (penaltyMatrix == null){
            penaltyMatrix = GenerateMatrix(s.getBoard().length);
        }
        double timer = System.currentTimeMillis();
        Position p = minimax_search(s);
        System.out.println("Time taken: " + ((System.currentTimeMillis() - timer) / 1000));
        return p;

    }

    private Position minimax_search(GameState state){
        Move move = max_value(state, Float.MIN_VALUE, Float.MAX_VALUE, 0);
        return move.pos;
    }

    private Move max_value(GameState state, float alpha, float beta, int depth){
        if (state.legalMoves().isEmpty() || depth >= maxDepth){
            //return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
            return new Move(null, Evaluation(state.getBoard(), state.getPlayerInTurn()-1));
        }

        Move m = new Move(null, Float.MIN_VALUE);

        for (Position pos : state.legalMoves()){
            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null) { continue; }
            Move m2 = min_value(state2, alpha, beta, depth+1);
            if (m2.utility > m.utility) {
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

    private Move min_value(GameState state, float alpha, float beta, int depth){
        if (state.legalMoves().isEmpty() || depth >= maxDepth){

            //return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
            return new Move(null, Evaluation(state.getBoard(), state.getPlayerInTurn()-1));
        }

        Move m = new Move(null, Float.MAX_VALUE);

        for (Position pos : state.legalMoves()){
            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null){
                continue;
            }
            Move m2 = max_value(state2, alpha, beta, depth+1);
            if (m2.utility < m.utility) {
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

    private GameState Result (GameState state, Position pos, int player){
        GameState state2 = new GameState(state.getBoard(), player);
        if (!state2.insertToken(pos)){
            System.out.println("Illegal Move!");
            return null;
        }
        return state2;
    }

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
        //return penaltyMatrix[pos.col][pos.row];
    }

    private float[][] GenerateMatrix(int size){
        float[][] matrix = new float[size][size];

        for (int i = 0; i < size; i++){
            for (int y = 0; y < size; y++){
                matrix[i][y] = 1;
            }
        }

        // Corners
        matrix[0][0]            = 50f;
        matrix[0][size-1]       = 50f;
        matrix[size-1][0]       = 50f;
        matrix[size-1][size-1]  = 50f;

        // Around corners
        matrix[1][0]            = -20f;
        matrix[0][1]            = -20f;
        matrix[size-1][size-2]  = -20f;
        matrix[size-2][size-1]  = -20f;
        matrix[0][size-2]       = -20f;
        matrix[1][size-1]       = -20f;
        matrix[size-1][1]       = -20f;
        matrix[size-2][0]       = -20f;

        // Around corners diagonally
        matrix[1][1]            = -50f;
        matrix[size-2][size-2]  = -50f;
        matrix[size-2][1]       = -50f;
        matrix[1][size-2]       = -50f;
        return matrix;
    }



}
