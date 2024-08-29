package A2;
public class Player {
    private String name;
    private char piece;
    
//player constructor
    public Player(String name, char piece) {
        this.name = name;
        this.piece = piece;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public char getPiece() {
        return piece;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setPiece(char piece) {
		this.piece = piece;
	}
    
    
}