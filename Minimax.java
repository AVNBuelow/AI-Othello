import java.util.ArrayList;

/***
 * This is implemented via the slides Lecture 3 slide 10
 */

public class Minimax implements IOthelloAI{

    private static final int maxDepth = 5;
    public Position decideMove(GameState s){

        return minimax_search(s);
    }

    private Position minimax_search(GameState state){
        Move move = max_value(state, Float.MIN_VALUE, Float.MAX_VALUE, 0);
        return move.pos;
    }

    private Move max_value(GameState state, float alpha, float beta, int depth){
        if (state.legalMoves().isEmpty() || depth >= maxDepth){
            System.out.println("End of line: " + state.countTokens()[state.getPlayerInTurn()-1]);
            return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
        }

        System.out.println("Testing a move depth: " + depth);

        Move m = new Move(null, Float.MIN_VALUE);

        for (Position pos : state.legalMoves()){
            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null) { continue; }

            Move m2 = min_value(state2, alpha, beta, depth++);
            if (m2.utility > m.utility) {
                m.utility = m2.utility;
                m.pos = pos;
                alpha = Math.max(alpha, m.utility);
                System.out.println(m);
            }

            if (m.utility >= beta){
                return m;
            }
        }
        return m;
    }

    private Move min_value(GameState state, float alpha, float beta, int depth){
        if (state.legalMoves().isEmpty() || depth >= maxDepth){
            return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
        }

        Move m = new Move(null, Float.MAX_VALUE);

        for (Position pos : state.legalMoves()){
            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null){
                continue;
            }
            Move m2 = max_value(state2, alpha, beta, depth++);
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

    



}
