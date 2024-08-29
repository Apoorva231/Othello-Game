package A2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
    private static final int BOARDSIZE = 8;
    private Player firstPlayer;
    private Player secondPlayer;
    private Player currentPlayer;
    private Position[][] boardPieces;
    private Scanner sc;  
  
    //constructor sets the fields, creates array of positions and initializes board.
    public Board(Player p1, Player p2, int start) {
        this.firstPlayer = p1;
        this.secondPlayer = p2;
        this.currentPlayer = p1;
        this.boardPieces = new Position[BOARDSIZE][BOARDSIZE];
        initializeBoard(start);
        sc = new Scanner(System.in);
    }   

    //getters and setters
    public Player getFirstPlayer() {
		return firstPlayer;
	}
	public void setFirstPlayer(Player firstPlayer) {
		this.firstPlayer = firstPlayer;
	}
	public Player getSecondPlayer() {
		return secondPlayer;
	}
	public void setSecondPlayer(Player secondPlayer) {
		this.secondPlayer = secondPlayer;
	}



	// sets up all the initial playable and unplayable positions on the board and also sets up the starting format that is specified by the user.
	private void initializeBoard(int start) 
	{
        for (int i = 0; i < BOARDSIZE; i++) 
        {
            for (int j = 0; j < BOARDSIZE; j++) 
            {
              boardPieces[i][j] = new PlayablePosition();
            }
        }

        
        boardPieces[3][7] = new Position(Position.UNPLAYABLE);
        boardPieces[4][7] = new Position(Position.UNPLAYABLE);

        
        switch (start) {
            case 1: 
                boardPieces[2][2].setPiece(Position.WHITE);
                boardPieces[3][2].setPiece(Position.BLACK);
                boardPieces[2][3].setPiece(Position.BLACK);
                boardPieces[3][3].setPiece(Position.WHITE);
                break;
            case 2: 
                boardPieces[2][4].setPiece(Position.WHITE);
                boardPieces[2][5].setPiece(Position.BLACK);
                boardPieces[3][5].setPiece(Position.WHITE);
                boardPieces[3][4].setPiece(Position.BLACK);
                break;
            case 3: 
                boardPieces[4][2].setPiece(Position.WHITE);
                boardPieces[4][3].setPiece(Position.BLACK);
                boardPieces[5][3].setPiece(Position.WHITE);
                boardPieces[5][2].setPiece(Position.BLACK);
                break;
            case 4:
                boardPieces[4][4].setPiece(Position.WHITE);
                boardPieces[4][5].setPiece(Position.BLACK);
                boardPieces[5][4].setPiece(Position.BLACK);
                boardPieces[5][5].setPiece(Position.WHITE);
                break;
            case 5: 
                boardPieces[2][2].setPiece(Position.WHITE);
                boardPieces[2][3].setPiece(Position.WHITE);
                boardPieces[2][4].setPiece(Position.BLACK);
                boardPieces[2][5].setPiece(Position.BLACK);
                boardPieces[3][2].setPiece(Position.WHITE);
                boardPieces[3][3].setPiece(Position.WHITE);
                boardPieces[3][4].setPiece(Position.BLACK);
                boardPieces[3][5].setPiece(Position.BLACK);
                boardPieces[4][2].setPiece(Position.BLACK);
                boardPieces[4][3].setPiece(Position.BLACK);
                boardPieces[4][4].setPiece(Position.WHITE);
                boardPieces[4][5].setPiece(Position.WHITE);
                boardPieces[5][2].setPiece(Position.BLACK);
                boardPieces[5][3].setPiece(Position.BLACK);
                boardPieces[5][4].setPiece(Position.WHITE);
                boardPieces[5][5].setPiece(Position.WHITE);
                break;
        }
    }

	//carries out the gameplay
	public void play(){
        drawBoard();
        System.out.println("("+currentPlayer.getPiece()+")'s turn: "+currentPlayer.getName());
        if(!isMovesRemaining())
        {
            manageNoMovesRemaining();
            return;
        }

        takeTurn();
    }

    //draws out the board after it has been initialized 
    public void drawBoard() {
        System.out.println("  \033[0;4m"+"A B C D E F G H"+"\033[0m");
       
        for (int i = 0; i < BOARDSIZE; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < BOARDSIZE; j++) {
                System.out.print("\033[0;4m"+boardPieces[i][j].getPiece() + "|"+"\033[0m");
            }
            System.out.println();
        }
    }
    

    //loads game from a file 
    public static Board load(String filename) 
    	throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(filename));
        String p1Name = r.readLine();
        String p2Name = r.readLine();
        String cpName = r.readLine();
        Player p1 = new Player(p1Name, Position.BLACK);
        Player p2 = new Player(p2Name, Position.WHITE);
        Board board = new Board(p1, p2, 1);
        if(cpName.equals(p1Name)) {
        	board.currentPlayer=p1;
        }
        else {
        	board.currentPlayer=p2;
        }

        for (int i = 0; i < BOARDSIZE; i++) {
            String l = r.readLine();
            for (int j = 0; j < BOARDSIZE; j++) {
                char pieceColor = l.charAt(j);
                board.boardPieces[i][j].setPiece(pieceColor);
            }
        }
        r.close();
        return board;
    }

    
    //saves game to a file 
    private void save() {
        System.out.println("Enter filename to save:");
        String file = sc.nextLine();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
            w.write(firstPlayer.getName());
            w.newLine();
            w.write(secondPlayer.getName());
            w.newLine();
            w.write(currentPlayer.getName());
            w.newLine();
            for (int i = 0; i < BOARDSIZE; i++) {
                for (int j = 0; j < BOARDSIZE; j++) {
                    w.write(boardPieces[i][j].getPiece());
                }
                w.newLine();
            }
            System.out.println("Game has been saved.");
        } catch (IOException e) {
            System.out.println("Error. Could not save : " + e.getMessage());
        }
    }

    //manages options for a player during the game
    private void takeTurn() {
        System.out.println("Enter move, 'save', or 'concede':");
        String input = sc.nextLine().toLowerCase();

        if (input.equals("save")) {
            save();
        } else if (input.equals("concede")) {
            changeCurrentPlayer();
            System.out.println(currentPlayer.getName() + " wins by concession!");
            System.exit(0);
        }         
        else {
            if (input.length() != 2 || Character.isLetter(input.charAt(0))==false || Character.isDigit(input.charAt(1))==false) {
                System.out.println("Invalid input. Please enter a valid move (ex: D5), 'save', or 'concede'.");
                return;
            }

            int columnBoard = input.charAt(0) - 'a';
            int rowBoard = input.charAt(1) - '1';

            if (columnBoard < 0 || columnBoard >= BOARDSIZE || rowBoard < 0 || rowBoard >= BOARDSIZE) {
                System.out.println("Invalid. Enter a box within the board.");
                return;
            }

            if (isMoveAllowed(rowBoard, columnBoard)) {
                makeMove(rowBoard, columnBoard);
                changeCurrentPlayer();
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }
    

    //checks if there are moves remaining for the currentPlayer
    private boolean isMovesRemaining() {
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (isMoveAllowed(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    //manages game if there are no moves remaining for the user
    private void manageNoMovesRemaining() {
        System.out.println("No valid moves. Options: 'save', 'concede', or 'pass'");
        String input = sc.nextLine().toLowerCase();
        switch (input) {
            case "save":
                save();
                break;
            case "concede":
                changeCurrentPlayer();
                System.out.println(currentPlayer.getName() + " wins by concession!");
                break;
            case "forfeit turn":
                changeCurrentPlayer();
                drawBoard();
                break;
            default:
                System.out.println("Incorrect Option. Choose again");
                changeCurrentPlayer();
        }
    }

    //checks if a move is legal and returns true or false
    private boolean isMoveAllowed(int row, int col) {
        if (row < 0 || row >= BOARDSIZE || col < 0 || col >= BOARDSIZE || boardPieces[row][col].canPlay()==false ) {
            return false;
        }

        char opponentPiece = (currentPlayer.getPiece() == Position.BLACK) ? Position.WHITE : Position.BLACK;

        //checking all directions
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};

        for (int[] direction : directions) {
            int r = row + direction[0];
            int c = col + direction[1];
            
            if (r >= 0 && r < BOARDSIZE && c >= 0 && c < BOARDSIZE && boardPieces[r][c].getPiece() == opponentPiece) {
                while (r >= 0 && r < BOARDSIZE && c >= 0 && c < BOARDSIZE && boardPieces[r][c].getPiece() != Position.EMPTY) {
                    if (boardPieces[r][c].getPiece() == currentPlayer.getPiece()) {
                        return true;
                    }
                    r = r+direction[0];
                    c = c+direction[1];
                }
            }
        }

        return false;
    }

    
    //executes the move specified by the player if it is legal
    private void makeMove(int row, int col) {
        boardPieces[row][col].setPiece(currentPlayer.getPiece());

        char opponentPiece = (currentPlayer.getPiece() == Position.BLACK) ? Position.WHITE : Position.BLACK;

        //to check all directions from a specific tile
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};

        for (int[] direction : directions) {
            int r = row + direction[0];
            int c = col + direction[1];
            boolean canFlip = false;
            List<int[]> toFlip = new ArrayList<>();
            
            while (r >= 0 && r < BOARDSIZE && c >= 0 && c < BOARDSIZE) {
                if (boardPieces[r][c].getPiece() == Position.EMPTY || boardPieces[r][c].getPiece() == Position.UNPLAYABLE) {
                    break;
                }
                if (boardPieces[r][c].getPiece() == opponentPiece) {
                    toFlip.add(new int[]{r, c});
                }
                if (boardPieces[r][c].getPiece() == currentPlayer.getPiece()) {
                    canFlip = true;
                    break;
                }
                r += direction[0];
                c += direction[1];
            }
            
            if (canFlip) {
                for (int[] pos : toFlip) {
                    boardPieces[pos[0]][pos[1]].setPiece(currentPlayer.getPiece());
                }
            }
        }
    }

    //switches player
    private void changeCurrentPlayer() {
        currentPlayer = (currentPlayer == firstPlayer) ? secondPlayer : firstPlayer;
    }

    //checks if game has ended
    public boolean hasGameEnded() {
        return isMovesRemaining()==false && opponentValidMovesRemaining()==false;
    }

    //checks if opponent player has any valid moves remaining
    private boolean opponentValidMovesRemaining() {
        changeCurrentPlayer();
        boolean result = isMovesRemaining();
        changeCurrentPlayer();
        return result;
    }

    //displays winner
    public void declareWinner() {
        int blackCount = 0, whiteCount = 0;
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (boardPieces[i][j].getPiece() == Position.BLACK) blackCount++;
                else if (boardPieces[i][j].getPiece() == Position.WHITE) whiteCount++;
            }
        }
        System.out.println("Game Over!");
        System.out.println("Black: " + blackCount + " White: " + whiteCount);
        if (blackCount > whiteCount) {
            System.out.println(firstPlayer.getName() + " (Black) wins!");
        } else if (whiteCount > blackCount) {
            System.out.println(secondPlayer.getName() + " (White) wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }
}
