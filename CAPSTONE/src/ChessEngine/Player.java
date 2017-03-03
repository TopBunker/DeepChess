package ChessEngine;

import java.util.Dictionary;

public class Player {
	public String colour;
	public boolean turn;
	public int score;
	public Dictionary[] board = new Dictionary[8];
	
	public Player(String colour, boolean turn){
		this.colour = colour;
		this.turn = turn;
	}
	
	public void makeMove(String fen){
	
		
	}
	
	public void setBoard(String fen){
		String[] lst = fen.split("/");
		for(int i=0;i<(lst.length+1);i++){
			String[] row = lst[i].split("");
			for(int p=0;p<(row.length+1);p++){
				int file=p+1;
				switch(file){
					case 1: 
						board[i].put('a',row[p]);
					case 2:
						board[i].put('b',row[p]);
					case 3:
						board[i].put('c',row[p]);
					case 4:
						board[i].put('d',row[p]);
					case 5:
						board[i].put('e',row[p]);
					case 6:
						board[i].put('f',row[p]);
					case 7:
						board[i].put('g',row[p]);
					case 8:
						board[i].put('h',row[p]);
				}
			}
		}
	}

	
}
