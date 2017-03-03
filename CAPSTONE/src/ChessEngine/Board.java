package ChessEngine;

import java.awt.List;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import java.util.Set;
import java.util.Stack;


public class Board {
	public String FEN;
	public static String turn;
	public static String castling;
	public static String enpassant;
	public static int halfmoves;
	public static int fullmoves;
	public static int game=0;
	public static HashMap<String,Integer> score = new HashMap<String,Integer>(); 
	public static HashMap<String,String>[] board = new HashMap[8];
	public static ArrayList<String> weaksquares=new ArrayList<String>();
	public static ArrayList<String> vulnrsquares=new ArrayList<String>();

	public static Object[] pieceList = new Object[32];
	
	public enum FileInt {
		a(1),b(2),c(3),d(4),e(5),f(6),g(7),h(8);
		private int val;
		
		private static HashMap<Integer,FileInt> fileIterator= new HashMap<Integer,FileInt>();
		static {
			for (FileInt f : FileInt.values()){
				fileIterator.put(f.val,f);
			}
		}
		
		private FileInt(int val){
			this.val=val;
		}
		
		public int getval(){
			return this.val;
		}
		
		public static FileInt file(String col){
			FileInt file=null;
			for(FileInt f : FileInt.values()){
				if(col.equals(f.name())){
					file=f;
				}
			}
			return file;
		}
		
		public static FileInt file(int val){
			FileInt result = null;
			for(Integer f : fileIterator.keySet()){
				if(f==val){
					result = fileIterator.get(f);
				}
			}
			return result; 
		}
	}
	
	public Board(){
		FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		setBoard(FEN);
		playertoMove(FEN);
		castlingRights(FEN);
		enpassantTarget(FEN);
		halfmoveclock(FEN);
		movesCount(FEN);
		score.put("w", 0);
		score.put("b", 0);
		//setPieces();
	}
	
	public  void readFen(String fEN){
		fentoBoard(fEN);
		playertoMove(fEN);
		castlingRights(fEN);
		enpassantTarget(fEN);
		halfmoveclock(fEN);
		movesCount(fEN);
	}
	
	private void setBoard(String fEN2) {
		//initialise HashMap and unload FENboard
		for(int i=0;i<board.length;i++){
			HashMap<String,String> d= new HashMap<String, String>();
			d.put("a", "");
			d.put("b", "");
			d.put("c", "");
			d.put("d", "");
			d.put("e", "");
			d.put("f", "");
			d.put("g", "");
			d.put("h", "");
			board[i]=d;
		}
		fentoBoard(fEN2);
		//verify position of pieces in database
	}
	
	public void fentoBoard(String fEN3){
		String fenboard = fEN3.split("\\s")[0]; //split fen at blank space and get first string
		String[] fenrow = fenboard.split("/");	//divide string into rows
		
		for(int i=0; i<(board.length); i++){
			HashMap<String, String> row = board[i];
			Set<String> keyset = row.keySet();
			Iterator<String> k = keyset.iterator();
			
			String rowi = fenrow[7-i];
			
			while(k.hasNext()){	
				for(int j=0; j<rowi.length(); j++){
					char c = rowi.charAt(j);
					
					if(Character.isDigit(c)){
						int count=1;
						while(count<((Integer.parseInt(Character.toString(c)))+1)){
							row.put(k.next(),"");
							count++;
						}
					}
					else{
								row.put(k.next(),Character.toString(c));
					}
				}
			}
		}
	}
	
	private void playertoMove(String fEN4){
		this.turn = fEN4.split("\\s")[1];
	}
	
	private void castlingRights(String fEN5){
		this.castling = fEN5.split("\\s")[2];
	}
	
	private void enpassantTarget(String fEN6){
		this.enpassant = fEN6.split("\\s")[3];
	}
	
	private void halfmoveclock(String fEN7){
		this.halfmoves = Integer.parseInt(fEN7.split("\\s")[4]);
	}
	
	private void movesCount(String fEN8){
		this.fullmoves = Integer.parseInt(fEN8.split("\\s")[5]);
	}
	
	private String boardtoFen(HashMap<String,String>[] board){
		//convert hashmap to fen
		String rows = "";
		for(int i=7;i>-1;i--){
			HashMap<String,String> r = board[i];
			Set<String> keyset = r.keySet();
			Iterator<String> k = keyset.iterator();
			int count=0;
			while(k.hasNext()){	
				String p=k.next();
				if(r.get(p).isEmpty() && k.hasNext()==false){
					rows+=Integer.toString(count+1);
				}
				if(r.get(p).isEmpty()){
					count++;
				}
				else{
					if(count==0){
						rows+=r.get(p);
					}else{
						rows+=Integer.toString(count);
						count=0;
						rows+=r.get(p);
					}
				}
			}
			rows+="/";
		}
		return rows.substring(0,rows.length()-1);
	}
	
	public String getFen(){
		String rows=boardtoFen(this.board);
		rows+=" ";
		rows+=this.turn;
		rows+=" ";
		rows+=this.castling;
		rows+=" ";
		rows+=this.enpassant;
		rows+=" ";
		rows+=this.halfmoves;
		rows+=" ";
		rows+=this.fullmoves;
		return rows;
	}
	

	/**public static void setPieces() {
		// TODO Set pieces from board
		int i=0;
		while(i<32){ ///a wah a gwaan heresuh???
			for(int j=0;j<board.length;j++){
				for(String f: board[j].keySet()){
					String piece = board[j].get(f);
					switch(piece){
						case "r": pieceList[i]=new Rook("b",f,j); i++;break;
						case "n": pieceList[i]=new Knight("b",f,j); i++;break;
						case "b": pieceList[i]=new Bishop("b",f,j); i++;break;
						case "q": pieceList[i]=new Queen("b",f,j); i++;break;
						case "k": pieceList[i]=new King("b",f,j); i++;break;
						case "p": pieceList[i]=new Pawn("b",f,j); i++;break;
						case "R": pieceList[i]=new Rook("w",f,j); i++;break;
						case "N": pieceList[i]=new Knight("w",f,j); i++;break;
						case "B": pieceList[i]=new Bishop("w",f,j); i++;break;
						case "Q": pieceList[i]=new Queen("w",f,j); i++;break;
						case "K": pieceList[i]=new King("w",f,j); i++;break;
						case "P": pieceList[i]=new Pawn("w",f,j); i++;break;
						default: break;
						
					}
					
				}
			}
		}
		
	}**/
	
	public static String getPieceAt(String file, int rank){
		return board[rank-1].get(file);
	}
	
	public static void setPieceAt(String file, int rank, String piece){
		board[rank-1].put(file, piece);
	}

	public void endgame(){
		//wipe score;
		//reset board
		//get winner
		//write data to file
	}
	
	public static ArrayList<String> movegen(String col, int row) {
		// TODO Auto-generated method stub
		ArrayList<String> moves=new ArrayList<String>();
		FileInt c = FileInt.file(col);
		int cval = c.val;
		int nextrank=row+1;
		int prevrank=row-1;
	
		String nextFile= FileInt.file(cval+1)==null ? null : FileInt.file(cval+1).name();
		String prevFile= FileInt.file(cval-1)==null ? null : FileInt.file(cval-1).name();
		
		
		while(nextrank<=8 || prevrank>=1 || nextFile!=null || prevFile!=null){
			if(nextrank<=8){
				String move=col+":"+nextrank+":"+Board.getPieceAt(col,nextrank);
				if(!moves.contains(move)){
					moves.add(move);
				}
			}
			if(prevrank>=1){
				String move=col+":"+prevrank+":"+Board.getPieceAt(col,prevrank);
				if(!moves.contains(move)){
					moves.add(move);
				}
			}
			if(nextFile!=null){
				String move=nextFile+":"+row+":"+Board.getPieceAt(nextFile,row);
				if(!moves.contains(move)){
					moves.add(move);
				}
			}
			if(prevFile!=null){
				String move=prevFile+":"+row+":"+Board.getPieceAt(prevFile,row);
				if(!moves.contains(move)){
					moves.add(move);
				}
			}
			if(nextrank<=8 && nextFile!=null){
				String move=nextFile+":"+nextrank+":"+Board.getPieceAt(nextFile, nextrank);
				if(!moves.contains(move)){
					moves.add(move);
				}
				
			}
			if(nextrank<=8 && prevFile!=null){
				String move=prevFile+":"+nextrank+":"+Board.getPieceAt(prevFile, nextrank);
				if(!moves.contains(move)){
					moves.add(move);
				}
			}
			if(prevrank>=1 && nextFile!=null){
				String move=nextFile+":"+prevrank+":"+Board.getPieceAt(nextFile, prevrank);
				if(!moves.contains(move)){
					moves.add(move);
				}
			}
			if(prevrank>=1 && prevFile!=null){
				String move=prevFile+":"+prevrank+":"+Board.getPieceAt(prevFile, prevrank);
				if(!moves.contains(move)){
					moves.add(move);
				}
			}
			
			nextrank+=1;
			prevrank-=1;
			
			int nfval = FileInt.file(nextFile!=null ? nextFile : "h").val;
			int pfval = FileInt.file(prevFile!=null ? prevFile : "a").val;
			nextFile= FileInt.file(nfval+1)==null ? null : FileInt.file(FileInt.file(nextFile).val+1).name();
			prevFile= FileInt.file(pfval-1)==null ? null : FileInt.file(FileInt.file(prevFile).val-1).name();
		}
		return moves;

		
	}		
	
	
	
}
