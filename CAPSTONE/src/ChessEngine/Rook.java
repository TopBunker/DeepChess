package ChessEngine;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Stack;

import javax.imageio.stream.FileImageInputStream;

import ChessEngine.Board.FileInt;

public class Rook extends Piece {

	Rook(String player,String file, int rank){
		super(player,true,file,rank);
		this.piecesymbol= this.player=="w" ? "R" : "r";
	}

	public void generatemoves(){
		this.moves = JBDC.getRookMoves(this.file, this.rank);
	}

	public void parsMoves(){
		int cval=FileInt.file(this.file).getval();
		String nextFile= FileInt.file(cval+1)==null ? null : FileInt.file(cval+1).name();
		String prevFile= FileInt.file(cval-1)==null ? null : FileInt.file(cval-1).name();
		int nextrank=this.rank+1;
		int prevrank=this.rank-1;
		while(nextrank<9){
			String occupiedBy = Board.getPieceAt(this.file, nextrank);
			String m=this.file+":"+nextrank+":"+occupiedBy;
			String prevm=this.file+":"+(nextrank-1)+":"+Board.getPieceAt(this.file,nextrank-1);
			if(this.bmoves.contains(prevm) || Board.getPieceAt(this.file, nextrank-1)!=""){
				bmoves.add(m);
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
				}
			}
			else{
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
			nextrank+=1;
		}
		while(prevrank>0){
			String occupiedBy = Board.getPieceAt(this.file, prevrank);
			String m=this.file+":"+prevrank+":"+occupiedBy;
			String prevm=this.file+":"+(prevrank+1)+":"+Board.getPieceAt(this.file, prevrank+1);
			if(this.bmoves.contains(prevm) || Board.getPieceAt(this.file, prevrank+1)!=""){		
				bmoves.add(m);
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
				}
			}
			else{
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
			prevrank-=1;
		}
		while(nextFile!=null){
			String occupiedBy = Board.getPieceAt(nextFile, this.rank);
			String m=nextFile+":"+this.rank+":"+occupiedBy;
			int v = FileInt.file(nextFile).getval()-1;
			String f = FileInt.file(v).name();
			String prevm=f+":"+this.rank+":"+Board.getPieceAt(f, this.rank);
			if(bmoves.contains(prevm) || Board.getPieceAt(f, this.rank)!=""){
				bmoves.add(m);
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
				}
			}
			else{
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
			int nfv = nextFile==null? 9 : FileInt.file(nextFile).getval()+1;
			nextFile= nfv==9 ? null : FileInt.file(nfv).name();
		}
		while(prevFile!=null){
			String occupiedBy = Board.getPieceAt(prevFile, this.rank);
			String m=prevFile+":"+this.rank+":"+occupiedBy;
			int pfval = FileInt.file(prevFile).getval();
			String v = FileInt.file(pfval+1).name();
			String prevm=v+":"+this.rank+":"+Board.getPieceAt(v, this.rank);
			if(bmoves.contains(prevm) || Board.getPieceAt(v, this.rank)!=""){
				bmoves.add(m);
				if(occupiedBy==occupiedBy.toUpperCase() && this.player=="b"){
					amoves.add(m);
				}
				if(occupiedBy==occupiedBy.toLowerCase() && this.player=="w"){
					amoves.add(m);
				}
			}
			else{
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
			int pfv = prevFile==null? 0 : FileInt.file(prevFile).getval()-1;
			prevFile= pfv==0 ? null : FileInt.file(pfv).name();
		}

		LinkedHashSet<String> removeduplicates = new LinkedHashSet<String>(bmoves);
		bmoves= new ArrayList<String>(removeduplicates);

		removeduplicates = new LinkedHashSet<String>(amoves);
		amoves= new ArrayList<String>(removeduplicates);

		removeduplicates = new LinkedHashSet<String>(lmoves);
		lmoves= new ArrayList<String>(removeduplicates);
	}


}
