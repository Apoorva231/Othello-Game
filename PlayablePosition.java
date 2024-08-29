package A2; 
//Playable position is inheriting from Position
public class PlayablePosition extends Position {
    public PlayablePosition() {
        super(EMPTY);
    }

    @Override
    public boolean canPlay() {
    	return this.piece==EMPTY;
        //true if it is blank. Polymorphism is being used to override the function
    }
}