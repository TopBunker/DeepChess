package ChessEngine;

import java.util.ArrayList;

import ChessEngine.Board.FileInt;

//each piece subclass controls the move generation, attacking, etc. of that piece

public class Piece {
	String player;
	String piecesymbol;
	boolean inplay;
	boolean firstmove;
	ArrayList<String> moves=new ArrayList<String>();
	ArrayList<String> lmoves=new ArrayList<String>(); //legal moves
	ArrayList<String> amoves=new ArrayList<String>(); //attack moves
	ArrayList<String> bmoves=new ArrayList<String>(); //blocked moves
	ArrayList<String> dmoves=new ArrayList<String>(); //defensive moves
	String file;
	int rank;
	String square;
	
	Piece(){}
	
	Piece(String player, boolean inplay, String file, int rank){
		this.player=player;
		this.inplay=inplay;
		this.file=file;
		this.rank=rank;
		this.firstmove=true;
		setsquare();
	}
	
	public String getfile(){
		return this.file;
	}
	
	public int getrank(){
		return this.rank;
	}
	
	public void setsquare(){
		this.square=this.file+":"+this.rank+":"+this.piecesymbol;
	}
	
	public void findmove(){
		//search moves list and apply eval to find best move for this piece
	}
		
	public void makeMove(String file, int rank){
		if(Board.turn==this.player){
			Board.board[this.rank-1].put(this.file, "");
			this.file=file;
			this.rank=rank;
			setsquare();
			
		}
	}
	
	public String getfile(String move){
		String[] m=move.split(":");
		return m[0];
	}
	
	public int getfileval(String file){
		return FileInt.file(file).getval();
	}
	
	public int getrank(String move){
		String[] m=move.split(":");
		return Integer.parseInt(m[1]);
	}
	
	public String parssquare(String move){
		String[] m=move.split(":");
		if(m.length==3){
			return m[2];
		}
		else{
			return "";
		}
	}
	
	public void generatemoves(String file, int rank){
		this.moves=Board.movegen(file,rank);
	}
	
	public void generatemoves(){}
	
	public void parsMoves(){}

}
