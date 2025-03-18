public class Move {

    // Instance variable to store the position of the move
    public Position pos;
    // Instance variable to store the utility value of the move
    public float utility;


    // Constructor to initialize the Move object with a position and utility value
    public Move(Position pos, float utility){
        this.pos = pos;             // Assign the provided position to the instance variable 'pos'
        this.utility = utility;     // Assign the provided utility to the instance variable 'utility'
    }

    // Method to return a string representation of the Move object
    public String toString() { return "Position: " + this.pos + " with utility of " + this.utility; }

}
