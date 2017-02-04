package ChessEngine;

//each piece object will control the move generation, attacking, etc. of that piece

public class Piece {
	int player;
	boolean inplay;
	String[] moves;
	String square;
	
	Piece(int player, boolean inplay){
		this.player=player;
		this.inplay=inplay;
	}

}
