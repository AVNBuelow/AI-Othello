public class Move {

    public Position pos;
    public float utility;

    public Move(Position pos, float utility){
        this.pos = pos;
        this.utility = utility;
    }

    public String toString() { return "Position: " + this.pos + " with utility of " + this.utility; }

}
