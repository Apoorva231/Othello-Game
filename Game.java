package A2;

import java.util.Scanner;
import java.io.*;

public class Game {
    private Board board;
    private Scanner sc;
    private int menuInput=0;
    
    //sending instances of players from the game constructor to the board constructor
    public Game(Player p1, Player p2) {
        this.board = new Board(p1, p2, 1);
        this.sc = new Scanner(System.in);
    }
    
    //starting displays the menu
    public void start() {
        menu();
    }
    
    //menu options of loading, starting and quitting
    private void menu() {
    	while(menuInput!=1 &&menuInput!=2 && menuInput!=3) {
            System.out.println("1: Load Game");
            System.out.println("2: Start New Game");
            System.out.println("3: Quit");
            menuInput=sc.nextInt();
            sc.nextLine();

            switch (menuInput) {
                case 1:
                    System.out.print("Enter filename to load: ");
                    String filename = sc.nextLine();
                    try {
                        board = Board.load(filename);
                        playBoardGame();
                    } catch (IOException e) {
                        System.out.println("Error loading game: " + e.getMessage());
                        menu();
                    }
                    break;
                case 2:
                    System.out.println("Enter the name of Player 1");
                    String p1Name = sc.nextLine();
                    System.out.println("Enter the name of Player 2");
                    String p2Name = sc.nextLine();
                    int startFormat=0;
                    //selecting the starting positioning
                    while(true){
                        System.out.println("Select starting pieces placement:");
                        System.out.println("1-4: 2x2 offset starting position");
                        System.out.println("1- 1 row above and 1 column left of centered starting position.");
                        System.out.println("2- 1 row above and 1 column right of centered starting position.");
                        System.out.println("3- 1 row below and 1 column left of centered starting position.");
                        System.out.println("4- 1 row below and 1 column right of centered starting position.");
                        System.out.println("5: 4x4 centered starting position");
                        startFormat=sc.nextInt();
                        if(startFormat<1 || startFormat>5){
                            System.out.println("Not valid. Choose the correct number");
                        }
                        else{
                            break;
                        }
                    }
                    //updating the previous default board values with new user specifed values
                    board= new Board(new Player(p1Name,Position.BLACK),new Player(p2Name,Position.WHITE),startFormat);
                    playBoardGame();
                    break;
                case 3:
                    System.out.println("Game Ended");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid. Try again.");
            }

        }
    }    

    //checks if game has ended, displays the winner if true and then goes back to the start menu
    private void playBoardGame() {
        while (!board.hasGameEnded()) {
            board.play();
        }
        board.declareWinner();
        menuInput=0;
        menu();
    }
}