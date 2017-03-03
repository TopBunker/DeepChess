package ChessEngine;

import ChessEngine.Board.FileInt;

public class Knight extends Piece{


	Knight(String player,String file, int rank){
		super(player,true,file,rank);
		this.piecesymbol= this.player=="w" ? "N" : "n";

	}

	public void generatemoves(){
		this.moves=JBDC.getKnightMoves(this.file, this.rank);
	}

	public void parsMoves(){
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

		if(f1!=null && r2<9){
			String occupiedBy = Board.getPieceAt(f1, r2);
			String m=f1+":"+r2+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
		if(f3!=null && r2<9){
			String occupiedBy = Board.getPieceAt(f3, r2);
			String m=f3+":"+r2+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
		if(f1!=null && r4>0){
			String occupiedBy = Board.getPieceAt(f1, r4);
			String m=f1+":"+r4+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
		if(f3!=null && r4>0){
			String occupiedBy = Board.getPieceAt(f3, r4);
			String m=f3+":"+r4+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
		if(f2!=null && r1<9){
			String occupiedBy = Board.getPieceAt(f2, r1);
			String m=f2+":"+r1+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
		if(f2!=null && r3>0){
			String occupiedBy = Board.getPieceAt(f2, r3);
			String m=f2+":"+r3+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
		if(f4!=null && r1<9){
			String occupiedBy = Board.getPieceAt(f4, r1);
			String m=f4+":"+r1+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
		if(f4!=null && r3>0){
			String occupiedBy = Board.getPieceAt(f4, r3);
			String m=f4+":"+r3+":"+occupiedBy;
			if(occupiedBy==""){
				lmoves.add(m);
			}
			else{
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="b"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="w"){
					bmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
					lmoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
					lmoves.add(m);
				}
			}
		}
	}
}
