package main;

import java.util.*;

import boardRep.Board;
import boardRep.Search;
public class main {
    static String ENGINENAME="Gaara";
    static Board board = new Board();
    public static void main(String[] args) throws CloneNotSupportedException {
        while (true)
        {
            Scanner input = new Scanner(System.in);
            String inputString=input.nextLine();
            if ("uci".equals(inputString))
            {
                inputUCI();
            }
            else if (inputString.startsWith("setoption"))
            {
                inputSetOption(inputString);
            }
            else if ("isready".equals(inputString))
            {
                inputIsReady();
            }
            else if ("ucinewgame".equals(inputString))
            {
                inputUCINewGame();
            }
            else if (inputString.startsWith("position"))
            {
                inputPosition(inputString);
            }
            else if ("go".equals(inputString))
            {
                inputGo();
            }
            else if ("print".equals(inputString))
            {
                inputPrint();
            }
        }
    }
    public static void inputUCI() {
        System.out.println("id name "+ENGINENAME+"\n");
        System.out.println("id author J"+"\n");
        //options go here
        System.out.println("uciok"+"\n");
    }
    public static void inputSetOption(String inputString) {
        //set options
    }
    public static void inputIsReady() {
        System.out.println("readyok"+"\n");
    }
    public static void inputUCINewGame() {
    	board = new Board();
    }
    public static void inputPosition(String input) {
        input=input.substring(9).concat(" ");
        if (input.contains("startpos")) {
            input=input.substring(9);
            board = new Board();
        }
        else if (input.contains("fen")) {
            input=input.substring(4);
            board.readFen(input);
        }
        if (input.contains("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            while (input.length()>0)
            {
                algebraToMove(input);
                input=input.substring(input.indexOf(' ')+1);
            }
        }
    }

    public static void inputGo() throws CloneNotSupportedException {
        //search for best move
        String move="00-00";
        Search player = new Search(board,board.color,5);
        try {
			move=player.findMove();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
        System.out.println("bestmove "+moveToAlgebra(move)+"\n");
    }

    public static String moveToAlgebra(String move) {
        String start = move.split("-")[0];
        String dest = move.split("-")[1];        
        String returnMove="";
        returnMove+=start.substring(0,2);
        returnMove+=dest.substring(0,2);
        return returnMove;
    }

    public static void algebraToMove(String input) {
    	String[] in = input.split(" ");
        for(int i=0;i<in.length;i++){
        	String from=in[i].trim().substring(0, 2);
            String to=in[i].trim().substring(2, 4);
            String p = board.getPieceAt(Board.getfile(from), Board.getrank(from));
            String c = board.getPieceAt(Board.getfile(to), Board.getrank(to));
            from+=p;
            to+=p;
            board.makeMove(from, to);
        }
    }

    public static void inputQuit() {
        System.exit(0);
    }


    public static void inputPrint() {
        board.drawArray();
    }
}
