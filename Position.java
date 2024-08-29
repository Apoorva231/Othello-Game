package A2;
public class Position {
    protected char piece;
    public static final char UNPLAYABLE = '*';
    public static final char EMPTY = ' ';
    public static final char BLACK = 'B';
    public static final char WHITE = 'W';

    //constructor
    public Position(char piece) {
        this.piece = piece;
    }

    //always returns false 
    public boolean canPlay() {
        return false;
    }

    //getter and setter
    public char getPiece() {
        return piece;
    }

    public void setPiece(char piece) {
        this.piece = piece;
    }
}