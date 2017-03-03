package ChessEngine;

import java.util.ArrayList;

import ChessEngine.Board.FileInt;

public class King extends Piece {

	King(String player, String file, int rank){
		super(player,true,file,rank);
		this.piecesymbol= this.player=="w" ? "K" : "k";
	}

	public void generatemoves(){
		int fileval=FileInt.file(this.file).getval();
		if(this.player=="w"){
			if(Board.castling.contains("K")){
				String nfile=FileInt.file(fileval+2).name();
				moves.add(nfile+":"+this.rank+":"+"Kcastle");
			}
			if(Board.castling.contains("Q")){
				String nfile=FileInt.file(fileval-2).name();
				moves.add(nfile+":"+this.rank+":"+"Qcastle");
			}
		}
		if(this.player=="b"){
			if(Board.castling.contains("k")){
				String nfile=FileInt.file(fileval+2).name();
				moves.add(nfile+":"+this.rank+":"+"kcastle");
			}
			if(Board.castling.contains("q")){
				String nfile=FileInt.file(fileval-2).name();
				moves.add(nfile+":"+this.rank+":"+"qcastle");
			}
		}
		int nextrank=this.rank+1;
		int prevrank=this.rank-1;

		String nextFile= FileInt.file(fileval+1)==null ? null : FileInt.file(fileval+1).name();
		String prevFile= FileInt.file(fileval-1)==null ? null : FileInt.file(fileval-1).name();

		if(nextrank<9){
			String move=this.file+":"+nextrank+":"+Board.getPieceAt(this.file,nextrank);
			moves.add(move);

		}
		if(prevrank>0){
			String move=this.file+":"+prevrank+":"+Board.getPieceAt(this.file,prevrank);
			moves.add(move);

		}
		if(nextFile!=null){
			String move=nextFile+":"+this.rank+":"+Board.getPieceAt(nextFile, this.rank);
			moves.add(move);
		}
		if(prevFile!=null){
			String move=prevFile+":"+this.rank+":"+Board.getPieceAt(prevFile, this.rank);
			moves.add(move);
		}
		if(nextrank<=8 && nextFile!=null){
			String move=nextFile+":"+nextrank+":"+Board.getPieceAt(nextFile, nextrank);
			moves.add(move);
		}
		if(nextrank<=8 && prevFile!=null){
			String move=prevFile+":"+nextrank+":"+Board.getPieceAt(prevFile, nextrank);
			moves.add(move);
		}
		if(prevrank>=1 && nextFile!=null){
			String move=nextFile+":"+prevrank+":"+Board.getPieceAt(nextFile, prevrank);
			moves.add(move);
		}
		if(prevrank>=1 && prevFile!=null){
			String move=prevFile+":"+prevrank+":"+Board.getPieceAt(prevFile, prevrank);
			moves.add(move);
		}
	}

	public void parsMoves(){
		int fileval=FileInt.file(this.file).getval();
		if(this.player=="w"){
			if(Board.castling.contains("K")){
				//refactor to make sure moving into a checked square 
				String nfile=FileInt.file(fileval+2).name();
				String s1 = Board.getPieceAt(FileInt.file(fileval+1).name(), this.rank);
				String s2 = Board.getPieceAt(nfile, this.rank);
				String sq =nfile+":"+this.rank+":";
				String m="";
				for(String move : this.moves){
					if(move.contains(sq)){
						m=move;
					}
				}
				if(s1=="" && s2==""){
					lmoves.add(m);
				}
				else{
					bmoves.add(m);
				}
			}
			if(Board.castling.contains("Q")){
				String nfile=FileInt.file(fileval-2).name();
				String s1 = Board.getPieceAt(FileInt.file(fileval-1).name(), this.rank);
				String s2 = Board.getPieceAt(nfile, this.rank);
				String sq =nfile+":"+this.rank+":";
				String m="";
				for(String move : this.moves){
					if(move.contains(sq)){
						m=move;
					}
				}
				if(s1=="" && s2==""){
					lmoves.add(m);
				}
				else{
					bmoves.add(m);
				}
			}
		}
		if(this.player=="b"){
			if(Board.castling.contains("k")){
				String nfile=FileInt.file(fileval+2).name();
				String s1 = Board.getPieceAt(FileInt.file(fileval+1).name(), this.rank);
				String s2 = Board.getPieceAt(nfile, this.rank);
				String sq =nfile+":"+this.rank+":";
				String m="";
				for(String move : this.moves){
					if(move.contains(sq)){
						m=move;
					}
				}
				if(s1=="" && s2==""){
					lmoves.add(m);
				}
				else{
					bmoves.add(m);
				}
			}
			if(Board.castling.contains("q")){
				String nfile=FileInt.file(fileval-2).name();
				String s1 = Board.getPieceAt(FileInt.file(fileval-1).name(), this.rank);
				String s2 = Board.getPieceAt(nfile, this.rank);
				String sq =nfile+":"+this.rank+":";
				String m="";
				for(String move : this.moves){
					if(move.contains(sq)){
						m=move;
					}
				}
				if(s1=="" && s2==""){
					lmoves.add(m);
				}
				else{
					bmoves.add(m);
				}
			}
		}

		int nextrank=this.rank+1;
		int prevrank=this.rank-1;

		String nextFile= FileInt.file(fileval+1)==null ? null : FileInt.file(fileval+1).name();
		String prevFile= FileInt.file(fileval-1)==null ? null : FileInt.file(fileval-1).name();

		if(nextrank<9){
			String occupiedBy = Board.getPieceAt(this.file, nextrank);
			String m=this.file+":"+nextrank+":"+occupiedBy;
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
		if(prevrank>0){
			String occupiedBy = Board.getPieceAt(this.file, prevrank);
			String m=this.file+":"+prevrank+":"+occupiedBy;
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
		if(nextFile!=null){
			String occupiedBy = Board.getPieceAt(nextFile, this.rank);
			String m=nextFile+":"+this.rank+":"+occupiedBy;
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
		if(prevFile!=null){
			String occupiedBy = Board.getPieceAt(prevFile, this.rank);
			String m=prevFile+":"+this.rank+":"+occupiedBy;
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
		if(nextFile!=null && nextrank<9){
			String occupiedBy = Board.getPieceAt(nextFile, nextrank);
			String m=nextFile+":"+nextrank+":"+occupiedBy;

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

		if(nextFile!=null && prevrank>0){
			String occupiedBy = Board.getPieceAt(nextFile, prevrank);
			String m=nextFile+":"+prevrank+":"+occupiedBy;
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
		if(prevFile!=null && prevrank>0){
			String occupiedBy = Board.getPieceAt(prevFile, prevrank);
			String m=prevFile+":"+prevrank+":"+occupiedBy;

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
		if(prevFile!=null && nextrank<9){
			String occupiedBy = Board.getPieceAt(prevFile, nextrank);
			String m=prevFile+":"+nextrank+":"+occupiedBy;
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

	public void makeMove(String file, int rank){
		//make move if it does not lead to check or too close to other king.
	}

}
