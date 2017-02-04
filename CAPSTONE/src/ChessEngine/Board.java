package ChessEngine;

public class Board {
	String FEN;
	char turn;
	int game=0;
	int[] score; //tuple
	
	Board(){
		FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";	
		game=1;
	}
	
	public void endgame(){
		game=0;
		//wipe score;
	}
	
}
