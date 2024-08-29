package A2;

public class Main {
    public static void main(String[] args) {
    	//creating an instance of the game class with default board and players.
        Game game = new Game(new Player("Player 1", Position.BLACK), new Player("Player 2", Position.WHITE));
        //starting the game we initialized
        game.start();
    }
}

