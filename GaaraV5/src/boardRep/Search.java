package boardRep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.Callable;

import movegen.Piece;
import search.JDBC;

public class Search implements Cloneable {
	public String color;
	public int ply;
	public Board board;
	public Piece generic = new Piece(color,"generic","file",1,board);
	public Piece king;

	public Search(Board b,String colour, int ply) throws CloneNotSupportedException{
		this.color = colour;
		board = b;
		this.ply =ply;
		generic.setPlayer(color);
		generic.setBoard(b);
		king=generic.clone();
		king.setPlayer(color);
		king.setBoard(b);
	}


	public boolean isCheck() throws CloneNotSupportedException{	
		String k = this.color.equals("w")? "K":"k";
		HashMap<String,String>[] b = board.getBoard();
		ArrayList<String> squares = new ArrayList<>();
		boolean check = false;
		for(int i=0;i<b.length;i++){
			HashMap<String,String> row = b[i];
			for(HashMap.Entry<String,String> entry: row.entrySet()){
				if(entry.getValue().equals(k)){
					king.setSym(k);
					king.setFile(entry.getKey());
					king.setRank(i+1);
					king.setSquare();
					if(king.attacking(king.getSquare(), color).isEmpty()){
						check=false;
					}else{
						check=true;
					}
				}
			}
		}
		return check;
	}

	public boolean isCheckmate() throws CloneNotSupportedException{
		king.genMove();
		if(isCheck() && king.getLMoves().isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public Search clone()throws CloneNotSupportedException{  
		Search clone= (Search) super.clone(); 
		clone.generic=generic.clone();
		clone.king=king.clone();
		return clone;
	}  

	public String findMove() throws CloneNotSupportedException{
		board.nodes=0;
		String move ="";
		String sqr="";
		int rank = 0;
		HashMap<String,String>[] b = board.getBoard();
		Board brd = board.clone();
		Piece p;
		Stack<String> bestmove = new Stack<>();
		for(int i=0; i<b.length;i++){
			for(HashMap.Entry<String,String> entry: b[i].entrySet()){
				String piece=entry.getValue();
				if(!piece.isEmpty()){
					String pcolor = piece.equals(piece.toLowerCase())? "b":"w";
					if(pcolor.equals(this.color)){
						brd.readFen(board.getFen());
						p=generic.clone();
						p.setPlayer(pcolor);
						p.setBoard(brd);
						p.setSym(piece);
						p.setFile(entry.getKey());
						p.setRank(i+1);
						p.setSquare();
						bestmove=p.bestMove();
						while(!bestmove.isEmpty()){
							brd.readFen(board.getFen());
							String tst=p.getSquare();
							String tm=bestmove.pop();
							int tr = search(brd,tst,tm,ply);
							if(tr>rank){
								move=tm;
								sqr=tst;
								rank=tr;
							}
						}
						
					}
				}
			}
		}
		return sqr+"-"+move;
	}


	public int search(Board brd, String sqr, String move, int ply) throws CloneNotSupportedException{
		Search s = this.clone();
		s.color=this.color;
		s.board=brd.clone();
		s.board.readFen(brd.getFen());
		s.board.makeMove(sqr, move);
		board.nodes+=1;
		int eval = s.board.symmetriceval(color)+generic.mobility(s.board.getBoard());
		ply-=1;
		if(s.isCheck()){
			eval-=1000;
			return eval;
		}
		if(ply==0){
			return eval;
		}
		else{
			Search s1 = s.clone();
			int rank = 0;
			HashMap<String,String>[] b = s.board.getBoard();
			Piece p;
			Stack<String> bestmove = new Stack<>();
			for(int i=0; i<b.length;i++){
				for(HashMap.Entry<String,String> entry: b[i].entrySet()){
					String piece=entry.getValue();
					if(!piece.isEmpty()){
						String pcolor = piece.equals(piece.toLowerCase())? "b":"w";
						if(!pcolor.equals(this.color)){
							s1.board.readFen(s1.board.getFen());
							p=generic.clone();
							p.setPlayer(pcolor);
							p.setBoard(s1.board);
							p.setSym(piece);
							p.setFile(entry.getKey());
							p.setRank(i+1);
							p.setSquare();
							bestmove=p.bestMove();
							while(!bestmove.isEmpty()){
								s1.board.readFen(s1.board.getFen());
								String tst=p.getSquare();
								String tm=bestmove.pop();
								int tr = betaSearch(s1.board,tst,tm,ply);
								if(tr>rank){
									rank=tr;
								}
							}
							
						}
					}
				}
			}
			return eval;
		}

	}

	public int betaSearch(Board brd,String sqr,String move, int ply) throws CloneNotSupportedException{
		String player = sqr.substring(2).equals(sqr.substring(2).toUpperCase())? "w":"b";
		Search s = this.clone();
		s.board=brd.clone();
		s.board.color=player;
		s.board.readFen(brd.getFen());
		s.board.makeMove(sqr, move);
		s.board.nodes+=1;
		ply-=1;
		int eval=0;
		if(ply==0){
			return eval;
		}
		else{
			Search s2 = s.clone();
			int rank = 0;
			HashMap<String,String>[] b = s.board.getBoard();
			Piece p;
			Stack<String> bestmove = new Stack<>();
			for(int i=0; i<b.length;i++){
				for(HashMap.Entry<String,String> entry: b[i].entrySet()){
					s2.board.readFen(s.board.getFen());
					String piece=entry.getValue();
					if(!piece.isEmpty()){
						String pcolor = piece.equals(piece.toLowerCase())? "b":"w";
						if(pcolor.equals(this.color)){
							p=generic.clone();
							p.setPlayer(pcolor);
							p.setBoard(s2.board);
							p.setSym(piece);
							p.setFile(entry.getKey());
							p.setRank(i+1);
							p.setSquare();
							bestmove=p.bestMove();
							p.setSquare();
							while(!bestmove.isEmpty()){
								s2.board.readFen(s.board.getFen());
								String tst=p.getSquare();
								String tm=bestmove.pop();
								int tr = search(s2.board,tst,tm,ply);
								if(tr>rank){
									rank=tr;
								}
							}
							
						}
					}
				}
			}
			return eval;
		}
	}

}
