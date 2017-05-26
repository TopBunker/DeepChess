package boardRep;

import search.JDBC;

import java.awt.List;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.Map.Entry;


public class Board implements Cloneable {
	public static int nodes;
	public String FEN;
	public String color;
	public String castling;
	public String enpassant;
	public int halfmoves;
	public int fullmoves;
	public HashMap<String,String>[] board;

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
		//set board
		FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		setBoard(FEN);
		playertoMove(FEN);
		castlingRights(FEN);
		enpassantTarget(FEN);
		halfmoveclock(FEN);
		movesCount(FEN);
	}

	public Board(String fen){
		FEN=fen;
		readFen(fen);
	}
	
	public String getPieces(String c, String fn){
		String pces="";
		String fenboard = fn.split("\\s")[0]; //split fen at blank space and get first string
		String[] fenrow = fenboard.split("/");	//divide string into rows
		
		for(int j=0; j<fenrow.length; j++){
			String[] sqrs=fenrow[j].split("");
			for(String sq: sqrs){
				if(!Character.isDigit(sq.charAt(0))){
					if(c.equals("w")){
						if(sq.equals(sq.toUpperCase())){
							pces+=sq;
						}
					}else{
						if(sq.equals(sq.toLowerCase())){
							pces+=sq;
						}
					}
				}
			}

		}
		
		return pces;
	}

	public  void readFen(String fEN){
		setBoard(fEN);
		playertoMove(fEN);
		castlingRights(fEN);
		enpassantTarget(fEN);
		halfmoveclock(fEN);
		movesCount(fEN);
	}

	private void setBoard(String fEN2) {
		//initialise HashMap and unload FENboard
		board = new HashMap[8];
		for(int i=0;i<8;i++){
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
	}

	public void fentoBoard(String fEN3){
		String fenboard = fEN3.split("\\s")[0]; //split fen at blank space and get first string
		String[] fenrow = fenboard.split("/");	//divide string into rows

		for(int i=0; i<8; i++){
			HashMap<String, String> row = board[i];
			Set<String> keyset = row.keySet();
			Iterator<String> k = keyset.iterator();

			String rowi = fenrow[7-i];

			while(k.hasNext()){
				for(int j=0; j<rowi.length(); j++){
					String c = rowi.substring(j,j+1);

					if(Character.isDigit(c.charAt(0))){
						int count=1;
						while(count<((Integer.parseInt(c))+1)){
							row.put(k.next(),"");
							count+=1;
						}
					}
					else{
						row.put(k.next(),c);
					}
				}
			}
		}
	}

	private void playertoMove(String fEN4){
		color = fEN4.split("\\s")[1];
	}

	private void castlingRights(String fEN5){
		castling = fEN5.split("\\s")[2];
	}

	private void enpassantTarget(String fEN6){
		enpassant = fEN6.split("\\s")[3];
	}

	private void halfmoveclock(String fEN7){
		halfmoves = Integer.parseInt(fEN7.split("\\s")[4]);
	}

	private void movesCount(String fEN8){
		fullmoves = Integer.parseInt(fEN8.split("\\s")[5]);
	}

	public static boolean isPromotion(String p, String square){
		boolean promote =false;
		if(p.equals("P") && Board.getrank(square)==8){
			promote = true;
		}else{
			if(p.equals("p") && Board.getrank(square)==1){
				promote = true;
			}
		}	
		return promote;
	}

	public boolean isEmpty(String f, int r){
		boolean empty = false;
		String p = getPieceAt(f,r);
		if(p==null || p.isEmpty()){
			empty=true;
		}
		return empty;
	}

	public void makeMove(String cur_sqr, String move){
		String[] current = cur_sqr.split("");
		String[] next = move.split("");

		String f = current[0];
		int r = Integer.parseInt(current[1]);
		String p = current.length==3? current[2]:"";
		
		String nf = next[0];
		int nr = Integer.parseInt(next[1]);


		if(isPromotion(p,move)){
			p = p.equals("p")? "q":"Q";
		}

		//make move
		setPieceAt(nf,nr,p);
		setPieceAt(f,r,"");

		//set fullmove count
		if(p.equals(p.toLowerCase())){
			fullmoves+=1;
		}

		//set halfmove clock
		if(next.length==3 || p.equalsIgnoreCase("p")){
			halfmoves=0;
		}else{
			halfmoves+=1;
		}

		//set en passant target
		if(p.equals("p")){
			int sr=r-1;
			if((r-2)==nr && nf.equals(f)){
				enpassant=f+sr;
			}else{
				enpassant="-";
			}
		}else{
			if(p.equals("P")){
				int sr=r+1;
				if((r+2)==nr && nf.equals(f)){
					enpassant=f+sr;
				}
			}else{
				enpassant="-";
			}
		}

		//set castling
		if(p.equals("r") && f.equals("h") && r==8){
			String[] cstle = castling.split("k");
			if(cstle.length==1){
				castling=cstle[0];
			}else{
				int i=0;
				castling="";
				while(i<cstle.length){
					castling+=cstle[i];
				}
			}
		}else{
			if(p.equals("r") && f.equals("a") && r==8){
				String[] cstle = castling.split("q");
				if(cstle.length==1){
					castling=cstle[0];
				}else{
					int i=0;
					castling="";
					while(i<cstle.length){
						castling+=cstle[i];
					}
				}
			}else{
				if(p.equals("R") && f.equals("h") && r==1){
					String[] cstle = castling.split("K");
					if(cstle.length==1){
						castling=cstle[0];
					}else{
						int i=0;
						castling="";
						while(i<cstle.length){
							castling+=cstle[i];
						}
					}
				}else{
					if(p.equals("R") && f.equals("a") && r==1){
						String[] cstle = castling.split("Q");
						if(cstle.length==1){
							castling=cstle[0];
						}else{
							int i=0;
							castling="";
							while(i<cstle.length){
								castling+=cstle[i];
							}
						}
					}
					else{
						if(p.equals("k")){
							String[] cstle = castling.split("k");
							if(cstle.length==1){
								castling=cstle[0];
							}else{
								int i=0;
								castling="";
								while(i<cstle.length){
									castling+=cstle[i];
								}
							}
							cstle = castling.split("q");
							if(cstle.length==1){
								castling=cstle[0];
							}else{
								int i=0;
								castling="";
								while(i<cstle.length){
									castling+=cstle[i];
								}
							}
							
						}else{
							if(p.equals("K")){
								String[] cstle = castling.split("K");
								if(cstle.length==1){
									castling=cstle[0];
								}else{
									int i=0;
									castling="";
									while(i<cstle.length){
										castling+=cstle[i];
									}
								}
								cstle = castling.split("Q");
								if(cstle.length==1){
									castling=cstle[0];
								}else{
									int i=0;
									castling="";
									while(i<cstle.length){
										castling+=cstle[i];
									}
								}
							}
						}
					}
				}
			}
		}

		//set player to move
		color = p.equals(p.toLowerCase())? "w":"b";
	}
	
	public boolean canKSCastle(String king){
		if(castling.contains(king)){
			return true;
		}else{
			return false;
		}
	}

	public boolean canQSCastle(String king){
		String q = king.equals("K")? "Q":"q";
		if(castling.contains(q)){
			return true;
		}else{
			return false;
		}
	}

	public HashMap<String,String>[] getBoard(){
		return this.board;
	}

	private static String boardtoFen(HashMap<String,String>[] board){
		//convert hashmap to fen
		String rows = "";
		for(int i=7;i>-1;i--){
			HashMap<String,String> r = board[i];
			Set<String> keyset = r.keySet();
			Iterator<String> k = keyset.iterator();
			int count=0;
			while(k.hasNext()){
				String f=k.next();
				String p = r.get(f);
				if(p.isEmpty() && k.hasNext()==false){
					rows+=(count+1);
				}else{
					if(p.isEmpty()){
						count+=1;
					}
					else{
						if(count==0){
							rows+=p;
						}else{
							rows+=count;
							count=0;
							rows+=p;
						}
					}
				}
			}
			rows+="/";
		}
		return rows.substring(0,rows.length()-1);
	}

	public String getFen(){
		String rows=boardtoFen(board);
		rows+=" ";
		rows+=color;
		rows+=" ";
		rows+=castling;
		rows+=" ";
		rows+=enpassant;
		rows+=" ";
		rows+=halfmoves;
		rows+=" ";
		rows+=fullmoves;
		return rows;
	}

	public String getPieceAt(String file, int rank){
		if(rank>8 || rank <1){
			return "";
		}else{
			return board[rank-1].get(file);
		}
	}

	public void setPieceAt(String file, int rank, String piece){
		board[rank-1].put(file, piece);
	}


	public static void endgame(){
		//get winner
		//write fen stack to file
	}

	


	public static String getfile(String move){
		String[] m=move.split("");
		return m[0];
	}

	public static String getfile(int file){
		return FileInt.file(file).name();
	}

	public static int getfileval(String file){
		return FileInt.file(file).getval();
	}

	public static int getrank(String move){
		String[] m=move.split("");
		return Integer.parseInt(m[1]);
	}

	
	public static int pval(String p){
		int val =0;

		if("pP".contains(p)){
			val=1;
		}else{
			if("nNbB".contains(p)){
				val=3;
			}else{
				if("rR".contains(p)){
					val=5;
				}else{
					if("qQ".contains(p)){
						val=10;
					}else{
						if("kK".contains(p)){
							val=100;
						}
					}
				}
			}
		}

		return val;
	}

	public int symmetriceval(String player){
		int eval=0;
		//modified Shannon's formula: 10(Q-q)+5(R-r)+3(B-b + N-n)+1(P-p)
		for(int i=0;i<board.length;i++) {
			for(HashMap.Entry<String, String> entry : board[i].entrySet()) {
				String v = entry.getValue();
				if (v.equals("q")) {
					if(player.equals("w")){
						eval -= 10;
					}else{
						eval+=10;
					}
				}
				if (v.equals("r")) {
					if(player.equals("w")){
						eval -= 5;
					}else{
						eval+=5;
					}
				}
				if (v.equals("b") || v.equals("n")) {
					if(player.equals("w")){
						eval -= 3;
					}else{
						eval+=3;
					}
				}
				if (v.equals("p")) {
					if(player.equals("w")){
						eval -= 1;
					}else{
						eval+=1;
					}
				}
			}
		}

		for(int i=0;i<board.length;i++) {
			for(HashMap.Entry<String, String> entry : board[i].entrySet()) {
				String v = entry.getValue();
				if (v.equals("Q")) {
					if(player.equals("w")){
						eval += 10;
					}else{
						eval-=10;
					}
				}
				if (v.equals("R")) {
					if(player.equals("w")){
						eval += 5;
					}else{
						eval-=5;
					}
				}
				if (v.equals("B") || v.equals("N")) {
					if(player.equals("w")){
						eval += 3;
					}else{
						eval-=3;
					}
				}
				if (v.equals("P")) {
					if(player.equals("w")){
						eval += 1;
					}else{
						eval-=1;
					}
				}
			}
		}

		return eval;
	}
	
	public Board clone()throws CloneNotSupportedException{  
		return (Board) super.clone();  
		}  

	public void drawArray(){
		for(int i=0;i<8;i++){
			HashMap<String,String> row = board[i];
			ArrayList<String> squares = new ArrayList<>();
			for(HashMap.Entry<String,String> entry: row.entrySet()){
				squares.add(entry.getValue());
			}
			String[] printable = squares.toArray(new String[8]);
			System.out.println(Arrays.toString(printable));
		}
	}




}
