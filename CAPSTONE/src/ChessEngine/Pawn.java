package ChessEngine;

import ChessEngine.Board.FileInt;

public class Pawn extends Piece {

	Pawn(String player,String file,int rank){
		super(player,true,file,rank);
		this.piecesymbol= this.player=="w" ? "P" : "p";
	}

	public void generatemoves(){	
		if(this.player=="w"){
			if(firstmove){
				this.moves.add(this.file+":"+(this.rank+2)+":"+Board.getPieceAt(this.file, this.rank+2));
			}

			if(this.rank<8){
				this.moves.add(this.file+":"+(this.rank+1)+":"+Board.getPieceAt(this.file, this.rank+1));
			}

			int fval=FileInt.file(this.file).getval();
			int nf=fval+1;
			int pf=fval-1;
			if(nf<9 && this.rank<8){
				String captrcol1=FileInt.file(nf).name();
				this.moves.add(captrcol1+":"+(this.rank+1)+":"+Board.getPieceAt(captrcol1, this.rank+1));
			}
			if(pf>0 && this.rank<8){
				String captrcol2=FileInt.file(pf).name();
				this.moves.add(captrcol2+":"+(this.rank+1)+":"+Board.getPieceAt(captrcol2, this.rank+1));
			}

			String enpfile=Board.enpassant.substring(0, 1);
			String enprank=Board.enpassant.substring(1);
			String enpcaptrmove=enpfile+":"+enprank+":";
			if(moves.contains(enpcaptrmove)){
				int ecm=moves.indexOf(enpcaptrmove);
				moves.add(ecm, enpcaptrmove+"enpassant");
			}
		}
		else{
			if(firstmove){
				this.moves.add(this.file+":"+(this.rank-2)+":"+Board.getPieceAt(this.file, this.rank-2));
			}

			if(this.rank>1){
				this.moves.add(this.file+":"+(this.rank-1)+":"+Board.getPieceAt(this.file, this.rank-1));
			}

			int fval=FileInt.file(this.file).getval();
			int nf=fval+1;
			int pf=fval-1;
			if(nf<9 && this.rank>1){
				String captrcol1=FileInt.file(nf).name();
				this.moves.add(captrcol1+":"+(this.rank-1)+":"+Board.getPieceAt(captrcol1, this.rank-1));
			}
			if(pf>0 && this.rank>1){
				String captrcol2=FileInt.file(pf).name();
				this.moves.add(captrcol2+":"+(this.rank-1)+":"+Board.getPieceAt(captrcol2, this.rank-1));
			}

			String enpfile=Board.enpassant.substring(0, 1);
			String enprank=Board.enpassant.substring(1);
			String enpcaptrmove=enpfile+":"+enprank+":";
			if(moves.contains(enpcaptrmove)){
				int ecm=moves.indexOf(enpcaptrmove);
				moves.add(ecm, enpcaptrmove+"enpassant");
			}
		}

	}

	public void parsMoves(){
		if(this.player=="b"){
			if(firstmove){
				String p =Board.getPieceAt(this.file, this.rank-1);
				String q=Board.getPieceAt(this.file, this.rank-2);
				String fm =this.file+":"+(this.rank-2)+":"+Board.getPieceAt(this.file, this.rank-2);
				if(p=="" || q==""){
					lmoves.add(fm);
				}
				else{
					bmoves.add(fm);
				}
			}
			if(rank>1){
				String p =Board.getPieceAt(this.file, this.rank-1);
				String m =this.file+":"+(this.rank-1)+":"+p;
				if(p==""){
					lmoves.add(m);
				}
				else{
					bmoves.add(m);
				}
			}
			int fval=FileInt.file(this.file).getval();
			int nf=fval+1;
			int pf=fval-1;
			if(nf<9 && this.rank>1){
				String captrcol1=FileInt.file(nf).name();
				String p = Board.getPieceAt(captrcol1, this.rank-1);
				String m=captrcol1+":"+(this.rank-1)+":"+p;
				if(p!=""){
					if(p==p.toLowerCase()){
						bmoves.add(m);
					}
					else{
						amoves.add(m);
						lmoves.add(m);
					}
				}
			}
			if(pf>1 && this.rank>1){
				String captrcol1=FileInt.file(pf).name();
				String p = Board.getPieceAt(captrcol1, this.rank-1);
				String m=captrcol1+":"+(this.rank-1)+":"+p;
				if(p!=""){
					if(p==p.toLowerCase()){
						bmoves.add(m);
					}
					else{
						amoves.add(m);
						lmoves.add(m);
					}
				}
			}
		}
		else{

			if(firstmove){
				String p =Board.getPieceAt(this.file, this.rank+1);
				String q=Board.getPieceAt(this.file, this.rank+2);
				String fm =this.file+":"+(this.rank+2)+":"+Board.getPieceAt(this.file, this.rank+2);
				if(p=="" || q==""){
					lmoves.add(fm);
				}
				else{
					bmoves.add(fm);
				}
			}
			if(rank<8){
				String p =Board.getPieceAt(this.file, this.rank+1);
				String m =this.file+":"+(this.rank+1)+":"+p;
				if(p==""){
					lmoves.add(m);
				}
				else{
					bmoves.add(m);
				}
			}
			int fval=FileInt.file(this.file).getval();
			int nf=fval+1;
			int pf=fval-1;
			if(nf<9 && this.rank<8){
				String captrcol1=FileInt.file(nf).name();
				String p = Board.getPieceAt(captrcol1, this.rank+1);
				String m=captrcol1+":"+(this.rank+1)+":"+p;
				if(p!=""){
					if(p==p.toUpperCase()){
						bmoves.add(m);
					}
					else{
						amoves.add(m);
						lmoves.add(m);
					}
				}
			}
			if(pf>1 && this.rank<8){
				String captrcol1=FileInt.file(pf).name();
				String p = Board.getPieceAt(captrcol1, this.rank+1);
				String m=captrcol1+":"+(this.rank+1)+":"+p;
				if(p!=""){
					if(p==p.toUpperCase()){
						bmoves.add(m);
					}
					else{
						amoves.add(m);
						lmoves.add(m);
					}
				}
			}
		}
		for(String m : this.moves){
			if(m.contains("empassant")){
				this.amoves.add(m);
				this.lmoves.add(m);
			}
		}
	}

}
