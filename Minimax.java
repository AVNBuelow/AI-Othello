import java.util.ArrayList;

/***
 * This is implemented via the slides Lecture 3 slide 10
 */

public class Minimax implements IOthelloAI{

    public Position decideMove(GameState s){

        return minimax_search(s);

        /*
        ArrayList<Position> moves = s.legalMoves();
        if ( !moves.isEmpty() )
            return moves.get(0);
        else
            return new Position(-1,-1);

         */
    }

    private Position minimax_search(GameState state){
        Move move = max_value(state);
        return move.pos;
    }

    private Move max_value(GameState state){
        if (state.legalMoves().isEmpty()){
            System.out.println("End of line: " + state.countTokens()[state.getPlayerInTurn()-1]);
            return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
        }

        System.out.println("Testing a move");

        Move m = new Move(null, Float.MIN_VALUE);

        for (Position pos : state.legalMoves()){
            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null) { continue; }

            Move m2 = min_value(state2);
            if (m2.utility > m.utility) {
                m.utility = m2.utility;
                m.pos = pos;
                System.out.println(m);
            }
        }
        return m;
    }

    private Move min_value(GameState state){
        if (state.legalMoves().isEmpty()){
            return new Move(null, state.countTokens()[state.getPlayerInTurn()-1]);
        }

        Move m = new Move(null, Float.MAX_VALUE);

        for (Position pos : state.legalMoves()){
            GameState state2 = Result(state, pos, state.getPlayerInTurn());
            if (state2 == null){
                continue;
            }
            Move m2 = max_value(state2);
            if (m2.utility < m.utility) {
                m = m2;
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
