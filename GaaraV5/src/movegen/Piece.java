package movegen;

import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import boardRep.Board;
import boardRep.Board.FileInt;
import search.JDBC;

public class Piece implements Cloneable {
	private Board board;
	private String color;
	private String piecesymbol;
	private String file;
	private int rank;
	private String square;
	private Vector<String> moves=new Vector<>();
	private Vector<String> lmoves=new Vector<>(); //legal moves
	private Vector<String> amoves=new Vector<>(); //attack moves
	private Vector<String> bmoves=new Vector<>(); //blocked moves
	private Vector<String> bamoves=new Vector<>(); //blockedattack moves



	public Piece(String player, String sym, String file, int rank, Board b){
		setPlayer(player);
		setFile(file);
		setRank(rank);
		this.piecesymbol=sym;
		this.board=b;
	}

	public String getPlayer(){return this.color;}

	public void setPlayer(String p){this.color = p;}

	public String getFile(){return this.file;}

	public void setFile(String f){this.file=f;}

	public int getRank(){return this.rank;}

	public void setRank(int r){this.rank=r;}

	public void setBoard(Board b){this.board=b;}

	public String getSym(){return this.piecesymbol;}

	public void setSym(String sym) {this.piecesymbol=sym;}

	public String getSquare(){return this.square;}

	public void setSquare(String sqr){
		this.square=sqr;
	}
	public void setSquare(){
		this.square=this.file+this.rank+getSym();
	}


	public Vector<String> getMoves() {return moves;}

	public void setMoves(Vector<String> moves) {
		this.moves = moves;
	}

	public Vector<String> getBMoves() {
		return bmoves;
	}

	public void setBMoves(Vector<String> bmoves) {
		this.bmoves = bmoves;
	}

	public Vector<String> getAMoves() {
		return amoves;
	}

	public void setAMoves(Vector<String> amoves) {
		this.amoves = amoves;
	}

	public Vector<String> getLMoves() {
		return lmoves;
	}

	public void setLMoves(Vector<String> lmoves) {
		this.lmoves = lmoves;
	}

	public boolean isAttack(String occupiedBy){
		boolean k = false;
		if(occupiedBy.equals(occupiedBy.toLowerCase()) && this.color.equals("w")){
			k=true;
		}
		else{
			if(occupiedBy.equals(occupiedBy.toUpperCase()) && this.color.equals("b")){
				k=true;
			}
		}
		return k;
	}

	public boolean isBlocked(String occupiedBy){
		boolean k = false;
		if(occupiedBy.equals(occupiedBy.toLowerCase()) && this.color.equals("b")){
			k=true;
		}
		else{
			if(occupiedBy.equals(occupiedBy.toUpperCase()) && this.color.equals("w")){
				k=true;
			}
		}
		return k;
	}


	public Vector<String> parsAMoves(){
		Vector<String> amoves = new Vector<>();
		for(int i=0; i<moves.size(); i++){
			String sqr = moves.get(i);
			if(sqr.length()==3){
				String p = sqr.substring(2);
				if(isAttack(p)){
					amoves.add(sqr);
				}
			}

		}
		return amoves;
	}

	public Vector<String> parsBMoves(){
		Vector<String> bmoves = new Vector<>();
		for(int i=0; i<moves.size(); i++){
			String sqr = moves.get(i);
			if(sqr.length()==3){
				String p = sqr.substring(2);
				if(isBlocked(p)){
					bmoves.add(sqr);
				}
			}

		}
		return bmoves;
	}

	public void genMove(){
		setMoves(JDBC.searchMoves(board.getBoard(),getSquare()));
		parsMoves();
	}

	public void parsMoves() {
		if(this.piecesymbol.equalsIgnoreCase("b")){
			setAMoves(parsAMoves());
			setBMoves(parsBMoves());
			setLMoves(parsBLMoves());
		}else{
			if(this.piecesymbol.equalsIgnoreCase("n")){
				setAMoves(parsAMoves());
				setBMoves(parsBMoves());
				Vector<String> legal = new Vector<>();
				Vector<String> blocked = getBMoves();
				for(int i=0; i<moves.size(); i++){
					String move = moves.get(i);
					if(!blocked.contains(move)){
						legal.add(move);
					}
				}
				setLMoves(legal);
			}else{
				if(this.piecesymbol.equalsIgnoreCase("p")){
					parsPMoves();
				}else{
					if(this.piecesymbol.equalsIgnoreCase("r")){
						setAMoves(parsAMoves());
						setBMoves(parsBMoves());
						setLMoves(parsRLMoves());

					}else{
						if(this.piecesymbol.equalsIgnoreCase("q")){
							Vector<String> moves = getMoves();
							setAMoves(parsAMoves());
							setBMoves(parsBMoves());
							Vector<String> legal = parsBLMoves();
							legal.addAll(parsRLMoves());
							setLMoves(legal);

						}else{
							parsKMoves();

						}
					}
				}
			}
		}
	}

	public void parsKMoves(){
		Vector<String> legal = new Vector<>();
		Vector<String> blocked = getBMoves();
		for(int i=0; i<moves.size(); i++){
			String move = moves.get(i);
			String plyr = getPlayer();
			if(!blocked.contains(move)){
				if(getPlayer().equals("w") && !safeSqr(move,"b")){
					legal.add(move);
				}else{
					if(getPlayer().equals("b") && !safeSqr(move,"w")){
						legal.add(move);
					}
				}
			}
			if(board.canKSCastle(getSym())){
				if(board.isEmpty("f", getRank()) && board.isEmpty("g", getRank())){
					legal.add("h"+getRank());
				}
			}else{
				if(board.canQSCastle(getSym())){
					if(board.isEmpty("d", getRank()) && board.isEmpty("c", getRank()) && board.isEmpty("b", getRank())){
						legal.add("c"+getRank());
					}
				}

			}
		}

		setLMoves(legal);
	}

	public void parsKMoves(String f, int r){
		Vector<String> legal = new Vector<>();
		Vector<String> blocked = getBMoves();
		for(int i=0; i<moves.size(); i++){
			String move = moves.get(i);
			String plyr = getPlayer();
			if(!blocked.contains(move)){
				if(getPlayer().equals("w") && !safeSqr(move,"b")){
					legal.add(move);
				}else{
					if(getPlayer().equals("b") && !safeSqr(move,"w")){
						legal.add(move);
					}
				}
			}
			if(board.canKSCastle(getSym())){
				if(board.isEmpty("f", r) && board.isEmpty("g", r)){
					legal.add("h"+getRank());
				}
			}else{
				if(board.canQSCastle(getSym())){
					if(board.isEmpty("d", r) && board.isEmpty("c", r) && board.isEmpty("b", r)){
						legal.add("c"+r);
					}
				}

			}
		}

		setLMoves(legal);
	}

	public void parsPMoves(){
		setAMoves(parsAMoves());
		setBMoves(parsBMoves());
		Vector<String> legal = new Vector<>();

		if(getPlayer().equals("b") && rank==7){
			String p =board.getPieceAt(getFile(), getRank()-1);
			String q=board.getPieceAt(getFile(), getRank()-2);
			String fm =getFile()+(getRank()-2);
			if(p.isEmpty() && q.isEmpty()){
				legal.add(fm);
			}

		}
		else {
			if(getPlayer().equals("w") && rank==2){
				String p =board.getPieceAt(getFile(), getRank()+1);
				String q=board.getPieceAt(getFile(), getRank()+2);
				String fm =getFile()+(getRank()+2);
				if(p.isEmpty() && q.isEmpty()){
					legal.add(fm);
				}

			}

		}

		for(int i=0; i<moves.size(); i++){
			String move = moves.get(i);
			if(!bmoves.contains(move)) {
				if (amoves.contains(move) && !Board.getfile(move).equals(getFile())) {
					legal.add(move);
				}
				else{
					if(amoves.contains(move) && Board.getfile(move).equals(getFile())){
						amoves.remove(move);
						bmoves.add(move);
					}
				}
				if(Board.getfile(move).equals(getFile()) && move.length()==2){
					legal.add(move);
				}
			}
		}

		if(!board.enpassant.equals("-")){
			if(moves.contains(board.enpassant)){
				legal.add(board.enpassant);
			}
		}

		setLMoves(legal);
	}

	public void parsPMoves(String f, int r){
		setAMoves(parsAMoves());
		setBMoves(parsBMoves());
		Vector<String> legal = new Vector<>();

		if(getPlayer().equals("b") && r==7){
			String p =board.getPieceAt(f, r-1);
			String q=board.getPieceAt(f, r-2);
			String fm =f+(r-2);
			if(p.isEmpty() && q.isEmpty()){
				legal.add(fm);
			}

		}
		else {
			if(getPlayer().equals("w") && r==7){
				String p =board.getPieceAt(f, r+1);
				String q=board.getPieceAt(f, r+2);
				String fm =f+(r+2);
				if(p.isEmpty() && q.isEmpty()){
					legal.add(fm);
				}

			}

		}

		for(int i=0; i<moves.size(); i++){
			String move = moves.get(i);
			if(!bmoves.contains(move)) {
				if (amoves.contains(move) && !Board.getfile(move).equals(f)) {
					legal.add(move);
				}
				else{
					if(amoves.contains(move) && Board.getfile(move).equals(f)){
						amoves.remove(move);
						bmoves.add(move);
					}
				}
				if(Board.getfile(move).equals(f) && move.length()==2){
					legal.add(move);
				}
			}
		}

		if(!board.enpassant.equals("-")){
			if(moves.contains(board.enpassant)){
				legal.add(board.enpassant);
			}
		}

		setLMoves(legal);
	}

	public int dominance(String sqr, String colr){
		int counter=0;
		Vector<String> attacking = attacking(sqr,colr);
		counter= attacking.size();
		return counter;
	}

	public String sqrDomination(String sqr){
		String dom = "";

		int wdominance = dominance(sqr,"w");
		int bdominance = dominance(sqr,"b");

		if(wdominance>bdominance){
			dom="w";
		}
		else{
			if(bdominance>wdominance){
				dom="b";
			}
			else{
				if(bdominance!=0 && bdominance==wdominance){
					dom="wb";
				}
				else{
					if(bdominance==0 && wdominance==0){
						dom="";
					}
				}
			}
		}

		return dom;
	}

	public boolean safeSqr(String sqr, String plyr){
		boolean safe = false;
		String dom=sqrDomination(sqr);
		if(dom.contains(plyr)){
			safe = true;
		}
		return safe;
	}

	public int mobility(HashMap<String,String>[] b){
		int mcount=0;
		String m;
		for(int i=0; i<b.length;i++){
			for(HashMap.Entry<String,String> entry: b[i].entrySet()){
				String piece=entry.getValue();
				if(!piece.isEmpty()){
					if(isBlocked(piece)){
						m=entry.getKey()+(i+1)+piece;
						moves = JDBC.searchMoves(b, m);
						setFile(entry.getKey());
						setRank(i+1);
						setSym(piece);
						setSquare();
						parsMoves();
						mcount+=lmoves.size();
					}
				}
			}
		}
		return mcount;
	}

	public Stack<String> bestMove() throws CloneNotSupportedException{		
		Stack<String> best = new Stack<>();
		genMove();
		Vector<String> moves = getLMoves();
		String mySquare = getSquare();
		int rank = 0;
		Board start = board.clone();
		start.readFen(board.getFen());
		for(String m : moves){
			board.makeMove(mySquare, m);
			board.nodes+=1;
			
			//evaluate board after applying move
			int eval = board.symmetriceval(getPlayer())+mobility(board.getBoard());
			if(safeSqr(m,color)){
				eval+=2;
			}

			String op = color.equals("w")? "b":"w";
			if(this.piecesymbol.equalsIgnoreCase("k") && safeSqr(m,op)){
				break; //if square is safe for opponent (opponent has pieces attacking that square) then not a valid move for king 
			}

			//get possible moves for this piece from this square and check for attacks
			String sq = m.substring(0,2)+mySquare.substring(2);
			Vector<String> attcks = JDBC.searchMoves(board.getBoard(), sq);
			for(String a:attcks){
				if(a.length()==3){
					String p = a.substring(2);
					if(isAttack(p)){
						eval+=Board.pval(a.substring(0));
					}
				}
			}

			//compare current eval to highest ranked move currently in stack 
			if(eval>rank){
				rank=eval;
				while(!best.empty()){
					best.pop();
				}
				best.push(m);
			}else{
				if(eval==rank){
					best.push(m);
				}
			}
			board=start;
		}
		setSquare(mySquare);
		return best;
	}

	public Vector<String> attacking(String sqr, String colr) {
		Vector<String> moves=new Vector<String>();
		//parse square
		String col=sqr.substring(0,1);
		int cval = board.getfileval(col);
		int row=Integer.parseInt(sqr.substring(1,2));
		int nextrank=row+1;
		int prevrank=row-1;
		//define pieces
		String r= colr.equals("w")? "r":"R";
		String b= colr.equals("w")? "b":"B";
		String q= colr.equals("w")? "q":"Q";
		String pn= colr.equals("w")? "p":"P";
		String k= colr.equals("w")? "k":"K";
		//set up square to check
		String p;
		String m;

		String nextFile= cval+1>8 ? null : board.getfile(cval+1);
		String prevFile= cval-1<1 ? null : board.getfile(cval-1);

		while(nextrank<=8){
			p = board.getPieceAt(col,nextrank);
			m=col+nextrank+p;
			if(r.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(nextrank==row+1 && k.contains(p)){
					moves.add(m);
					break;
				}else{
					if(isBlocked(p)){
						break;
					}
				}
			}
			nextrank+=1;		
		}

		while(prevrank>=1){
			p = board.getPieceAt(col,prevrank);
			m=col+prevrank+p;
			if(r.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(prevrank==row-1 && k.contains(p)){
					moves.add(m);
					break;
				}if(isBlocked(p)){
					break;
				}
			}
			prevrank-=1;	
		}

		while(nextFile!=null){
			int nfval = board.getfileval(nextFile);
			p = board.getPieceAt(nextFile,row);
			m=nextFile+row+p;
			if(r.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(nfval==cval+1 && k.contains(p)){
					moves.add(m);
					break;
				}if(isBlocked(p)){
					break;
				}
			}
			nfval+=1;
			nextFile= nfval>8 ? null : board.getfile(nfval);
		}

		while(prevFile!=null){
			int pfval = board.getfileval(prevFile);
			p = board.getPieceAt(prevFile,row);
			m=prevFile+row+p;
			if(r.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(pfval==cval-1 && k.contains(p)){
					moves.add(m);
					break;
				}if(isBlocked(p)){
					break;
				}
			}
			pfval-=1;
			prevFile= pfval<1 ? null : board.getfile(pfval);
		}

		nextrank=row+1;
		nextFile= cval+1>8 ? null : board.getfile(cval+1);
		while(nextrank<=8 && nextFile!=null){
			int nfval = board.getfileval(nextFile);
			p = board.getPieceAt(nextFile,nextrank);
			m=nextFile+nextrank+p;
			if(b.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(nfval==cval+1){
					if(k.contains(p)){
						moves.add(m);
						break;
					}else{
						if(pn.contains(p) && p.equals(p.toLowerCase())){
							moves.add(m);
							break;
						}if(isBlocked(p)){
							break;
						}
					}
				}
			}
			nextrank+=1;
			nfval+=1;
			nextFile= nfval>8 ? null : board.getfile(nfval);
		}

		nextrank=row+1;
		prevFile= cval-1<1 ? null : board.getfile(cval-1);
		while(nextrank<=8 && prevFile!=null){
			int pfval = board.getfileval(prevFile);
			p = board.getPieceAt(prevFile,nextrank);
			m=prevFile+nextrank+p;
			if(b.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(pfval==cval-1){
					if(k.contains(p)){
						moves.add(m);
						break;
					}else{
						if(pn.contains(p) && p.equals(p.toLowerCase())){
							moves.add(m);
							break;
						}if(isBlocked(p)){
							break;
						}
					}
				}
			}
			nextrank+=1;
			pfval-=1;
			prevFile= pfval<1 ? null : board.getfile(pfval);
		}

		prevrank=row-1;
		nextFile= cval+1>8 ? null : board.getfile(cval+1);
		while(prevrank>=1 && nextFile!=null){
			int nfval = board.getfileval(nextFile);
			p = board.getPieceAt(nextFile,prevrank);
			m=nextFile+prevrank+p;
			if(b.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(nfval==cval+1){
					if(k.contains(p)){
						moves.add(m);
						break;
					}else{
						if(pn.contains(p) && p.equals(p.toUpperCase())){
							moves.add(m);
							break;
						}if(isBlocked(p)){
							break;
						}
					}
				}
			}
			prevrank-=1;
			nfval+=1;
			nextFile= nfval>8 ? null : board.getfile(nfval);
		}

		prevrank=row-1;
		prevFile= cval-1<1 ? null : board.getfile(cval-1);
		while(prevrank>=1 && prevFile!=null){
			int pfval = board.getfileval(prevFile);
			p = board.getPieceAt(prevFile,prevrank);
			m=prevFile+prevrank+p;
			if(b.contains(p) || q.contains(p)) {
				moves.add(m);
				break;
			}else{
				if(pfval==cval-1){
					if(k.contains(p)){
						moves.add(m);
						break;
					}else{
						if(pn.contains(p) && p.equals(p.toUpperCase())){
							moves.add(m);
							break;
						}if(isBlocked(p)){
							break;
						}
					}
				}
			}
			prevrank-=1;
			pfval-=1;
			prevFile= pfval<1 ? null : board.getfile(pfval);
		}


		return moves;
	}

	public Vector<String> attackingKnights(String file, int rank){
		Vector<String> moves=new Vector<>();
		String k = "nN";

		int row=rank;
		FileInt col=FileInt.file(file);
		int cval=col.getval();

		int r1=row+1;
		int r2=row+2;
		int r3=row-1;
		int r4=row-2;

		String f1= cval+1<9 ? FileInt.file(cval+1).name() : null;
		String f2= cval+2<9 ? FileInt.file(cval+2).name() : null;
		String f3= cval-1>0 ? FileInt.file(cval-1).name() : null;
		String f4= cval-2>0 ? FileInt.file(cval-2).name() : null;

		String occupiedBy;
		String m;

		if(f1!=null && r2<9){
			occupiedBy = board.getPieceAt(f1,r2);
			m=f1+r2+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		if(f3!=null && r2<9){
			occupiedBy = board.getPieceAt(f3, r2);
			m=f3+r2+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		if(f1!=null && r4>0){
			occupiedBy = board.getPieceAt(f1, r4);
			m=f1+r4+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		if(f3!=null && r4>0){
			occupiedBy = board.getPieceAt(f3, r4);
			m=f3+r4+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		if(f2!=null && r1<9){
			occupiedBy = board.getPieceAt(f2, r1);
			m=f2+r1+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		if(f2!=null && r3>0){
			occupiedBy = board.getPieceAt(f2, r3);
			m=f2+r3+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		if(f4!=null && r1<9){
			occupiedBy = board.getPieceAt(f4, r1);
			m=f4+r1+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		if(f4!=null && r3>0){
			occupiedBy = board.getPieceAt(f4, r3);
			m=f4+r3+occupiedBy;
			if(k.contains(occupiedBy)) {
				if (isAttack(occupiedBy)) {
					moves.add(m);
				}
			}
		}
		return moves;
	}

	public Vector<String> defSquares(){
		Vector<String>  defensive = new Vector<>();
		Vector<String> attcks = attacking(this.square,this.color);

		for(String m : attcks){
			String f=Board.getfile(m);
			int r = Board.getrank(m);
			if(f.equals(getFile())){
				if(r<getRank()){
					int nr=r+1;
					while(nr<getRank()){
						String sqr = f+r;
						defensive.add(sqr);
						nr+=1;
					}
				}else{
					if(r>getRank()){
						int nr=r-1;
						while(nr>getRank()){
							String sqr = f+r;
							defensive.add(sqr);
							nr-=1;
						}
					}
				}
			}else{
				int cval = Board.getfileval(getFile());
				int fval = Board.getfileval(f);
				if(r==getRank()){
					if(fval<cval){
						int nf=fval+1;
						while(nf<cval){
							String sqr = Board.getfile(nf)+r;
							defensive.add(sqr);
							nf+=1;
						}
					}else{
						if(fval>cval){
							int nf=fval-1;
							while(nf>cval){
								String sqr = Board.getfile(nf)+r;
								defensive.add(sqr);
								nf-=1;
							}
						}
					}
				}else{
					if(fval<cval && r>getRank()){
						int nf =fval+1;
						int nr = r-1;
						while(nf<cval && nr>getRank()){
							String sqr = Board.getfile(nf)+nr;
							defensive.add(sqr);
							nf+=1;
							nf-=1;
						}
					}else{
						if(fval>cval && r>getRank()){
							int nf =fval-1;
							int nr = r-1;
							while(nf>cval && nr>getRank()){
								String sqr = Board.getfile(nf)+nr;
								defensive.add(sqr);
								nf-=1;
								nf-=1;
							}
						}else{
							if(fval<cval && r<getRank()){
								int nf =fval+1;
								int nr = r+1;
								while(nf<cval && nr<getRank()){
									String sqr = Board.getfile(nf)+nr;
									defensive.add(sqr);
									nf+=1;
									nf+=1;
								}
							}else{
								if(fval>cval && r<getRank()){
									int nf =fval-1;
									int nr = r+1;
									while(nf>cval && nr<getRank()){
										String sqr = Board.getfile(nf)+nr;
										defensive.add(sqr);
										nf-=1;
										nf+=1;
									}
								}
							}
						}
					}
				}
			}
		}
		return defensive;
	}

	public void makeMove(String move){
		this.file=Board.getfile(move);
		this.rank=Board.getrank(move);
		setSquare(file+rank+piecesymbol);
	}

	public Vector<String> parsBLMoves(){
		Vector<String> legal = new Vector<>();
		int cval = Board.getfileval(this.file);

		int nextRank = this.rank + 1;
		String nextFile = (cval+1) < 9 ? Board.getfile(cval+1) : null;
		String m = (nextFile!=null && nextRank<9)? nextFile + nextRank + board.getPieceAt(nextFile, nextRank):"";
		while(nextFile!=null && nextRank<9 && !bmoves.contains(m)) {
			legal.add(m);

			int nfv = nextFile.equals("h") ? 9 : Board.getfileval(nextFile) + 1;
			nextFile = nfv == 9 ? null : Board.getfile(nfv);
			nextRank += 1;
			m = nextFile + nextRank + board.getPieceAt(nextFile, nextRank);
		}

		int prevRank = this.rank - 1;
		nextFile = (cval+1) < 9 ? Board.getfile(cval+1) : null;
		m = (nextFile!=null && prevRank>0)? nextFile + prevRank + board.getPieceAt(nextFile, prevRank):"";
		while (nextFile!=null && prevRank>0 && !bmoves.contains(m)) {
			legal.add(m);

			int nfv = nextFile.equals("h") ? 9 : Board.getfileval(nextFile) + 1;
			nextFile = nfv == 9 ? null : Board.getfile(nfv);
			prevRank -= 1;
			m = nextFile + prevRank + board.getPieceAt(nextFile, prevRank);
		}

		prevRank = this.rank - 1;
		String prevFile = (cval-1) > 0 ? Board.getfile(cval-1) : null;
		m = (prevFile!=null && prevRank>0)? prevFile + prevRank + board.getPieceAt(prevFile, prevRank):"";
		while (prevFile!=null && prevRank>0 && !bmoves.contains(m)) {
			legal.add(m);

			int pfv = prevFile.equals("a") ? 0 : Board.getfileval(prevFile) - 1;
			prevFile = pfv == 0 ? null : Board.getfile(pfv);
			prevRank -= 1;
			m = prevFile + prevRank + board.getPieceAt(prevFile, prevRank);
		}

		nextRank = this.rank + 1;
		prevFile = (cval-1) > 0 ? Board.getfile(cval-1) : null;
		m = (prevFile!=null && nextRank<9)? prevFile + nextRank + board.getPieceAt(prevFile, nextRank):"";
		while (prevFile != null && nextRank < 9 && !bmoves.contains(m)) {
			legal.add(m);

			int pfv = nextFile.equals("a") ? 0 : Board.getfileval(prevFile) - 1;
			prevFile = pfv == 0 ? null : Board.getfile(pfv);
			nextRank += 1;
			m = prevFile + nextRank + board.getPieceAt(prevFile, nextRank);
		}

		return legal;
	}

	public Vector<String> parsRLMoves(){
		Vector<String> legal = new Vector<>();

		int cval=Board.getfileval(this.file);

		String nextFile= cval+1==9 ? null : Board.getfile(cval+1);
		String prevFile= cval-1==0 ? null : Board.getfile(cval-1);
		int nextRank=this.rank+1;
		int prevRank=this.rank-1;

		String m= (nextRank<9)? this.file+nextRank+board.getPieceAt(this.file, nextRank):"";
		while(nextRank<9 && !bmoves.contains(m)){
			legal.add(m);

			nextRank+=1;
			m=this.file+nextRank+board.getPieceAt(this.file, nextRank);
		}

		m=(prevRank>0)? this.file+prevRank+board.getPieceAt(this.file, prevRank):"";
		while(prevRank>0 && !bmoves.contains(m)){
			legal.add(m);

			prevRank-=1;
			m = this.file + prevRank + board.getPieceAt(this.file, prevRank);
		}

		m = (nextFile!=null)? nextFile + this.rank + board.getPieceAt(nextFile, this.rank):"";
		while(nextFile!=null && !bmoves.contains(m)){
			legal.add(m);

			int nfv = nextFile.equals("h") ? 9 : Board.getfileval(nextFile) + 1;
			nextFile = nfv == 9 ? null : Board.getfile(nfv);
			m = nextFile + this.rank  + board.getPieceAt(nextFile, this.rank);
		}

		m = (prevFile!=null)? prevFile + this.rank + board.getPieceAt(prevFile, this.rank):"";
		while(prevFile!=null && !bmoves.contains(m)){
			legal.add(m);

			int pfv = prevFile.equals("a") ? 0 : Board.getfileval(prevFile) - 1;
			prevFile = pfv == 0 ? null : Board.getfile(pfv);
			m = prevFile + this.rank + board.getPieceAt(prevFile, this.rank);
		}

		return legal;
	}

	public Vector<String> parsBLMoves(String f, int r){
		Vector<String> legal = new Vector<>();
		int cval = Board.getfileval(f);

		int nextRank = r + 1;
		String nextFile = (cval+1) < 9 ? Board.getfile(cval+1) : null;
		String m = (nextFile!=null && nextRank<9)? nextFile + nextRank + board.getPieceAt(nextFile, nextRank):"";
		while(nextFile!=null && nextRank<9 && !bmoves.contains(m)) {
			legal.add(m);

			int nfv = nextFile.equals("h") ? 9 : Board.getfileval(nextFile) + 1;
			nextFile = nfv == 9 ? null : Board.getfile(nfv);
			nextRank += 1;
			m = nextFile + nextRank + board.getPieceAt(nextFile, nextRank);
		}

		int prevRank = r - 1;
		nextFile = (cval+1) < 9 ? Board.getfile(cval+1) : null;
		m = (nextFile!=null && prevRank>0)? nextFile + prevRank + board.getPieceAt(nextFile, prevRank):"";
		while (nextFile!=null && prevRank>0 && !bmoves.contains(m)) {
			legal.add(m);

			int nfv = nextFile.equals("h") ? 9 : Board.getfileval(nextFile) + 1;
			nextFile = nfv == 9 ? null : Board.getfile(nfv);
			prevRank -= 1;
			m = nextFile + prevRank + board.getPieceAt(nextFile, prevRank);
		}

		prevRank = r - 1;
		String prevFile = (cval-1) > 0 ? Board.getfile(cval-1) : null;
		m = (prevFile!=null && prevRank>0)? prevFile + prevRank + board.getPieceAt(prevFile, prevRank):"";
		while (prevFile!=null && prevRank>0 && !bmoves.contains(m)) {
			legal.add(m);

			int pfv = prevFile.equals("a") ? 0 : Board.getfileval(prevFile) - 1;
			prevFile = pfv == 0 ? null : Board.getfile(pfv);
			prevRank -= 1;
			m = prevFile + prevRank + board.getPieceAt(prevFile, prevRank);
		}

		nextRank = r + 1;
		prevFile = (cval-1) > 0 ? Board.getfile(cval-1) : null;
		m = (prevFile!=null && nextRank<9)? prevFile + nextRank + board.getPieceAt(prevFile, nextRank):"";
		while (prevFile != null && nextRank < 9 && !bmoves.contains(m)) {
			legal.add(m);

			int pfv = nextFile.equals("a") ? 0 : Board.getfileval(prevFile) - 1;
			prevFile = pfv == 0 ? null : Board.getfile(pfv);
			nextRank += 1;
			m = prevFile + nextRank + board.getPieceAt(prevFile, nextRank);
		}

		return legal;
	}

	public Vector<String> parsRLMoves(String f, int r){
		Vector<String> legal = new Vector<>();

		int cval=Board.getfileval(f);

		String nextFile= cval+1==9 ? null : Board.getfile(cval+1);
		String prevFile= cval-1==0 ? null : Board.getfile(cval-1);
		int nextRank=r+1;
		int prevRank=r-1;

		String m= (nextRank<9)? f+nextRank+board.getPieceAt(f, nextRank):"";
		while(nextRank<9 && !bmoves.contains(m)){
			legal.add(m);

			nextRank+=1;
			m=f+nextRank+board.getPieceAt(f, nextRank);
		}

		m=(prevRank>0)? f+prevRank+board.getPieceAt(f, prevRank):"";
		while(prevRank>0 && !bmoves.contains(m)){
			legal.add(m);

			prevRank-=1;
			m = f + prevRank + board.getPieceAt(f, prevRank);
		}

		m = (nextFile!=null)? nextFile + r + board.getPieceAt(nextFile, r):"";
		while(nextFile!=null && !bmoves.contains(m)){
			legal.add(m);

			int nfv = nextFile.equals("h") ? 9 : Board.getfileval(nextFile) + 1;
			nextFile = nfv == 9 ? null : Board.getfile(nfv);
			m = nextFile + r + board.getPieceAt(nextFile, r);
		}

		m = (prevFile!=null)? prevFile + r + board.getPieceAt(prevFile, r):"";
		while(prevFile!=null && !bmoves.contains(m)){
			legal.add(m);

			int pfv = prevFile.equals("a") ? 0 : Board.getfileval(prevFile) - 1;
			prevFile = pfv == 0 ? null : Board.getfile(pfv);
			m = prevFile + r + board.getPieceAt(prevFile, r);
		}

		return legal;
	}


	public Piece clone()throws CloneNotSupportedException{  
		return (Piece) super.clone();  
	}  

}
