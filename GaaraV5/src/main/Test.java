package main;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import boardRep.Board;
import boardRep.Search;
import search.JDBC;

public class Test extends JFrame {

	public static void main(String[] args) throws CloneNotSupportedException {
		JFrame frame = new JFrame("Gaara");
		JTextArea pane= new JTextArea();
		frame.add(pane);
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(800,600));
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String getMove = "Enter move (startfile|startrank|startpiece|-nextfile|nextrank): ";
		String move=null;
		String mymove=null;
		Board b = new Board();
		Search s = new Search(b,"w",1);
		boolean play = true;

		while(play){
			for(int i=0;i<8;i++){
				HashMap<String,String> row = b.board[i];
				ArrayList<String> squares = new ArrayList<>();
				for(HashMap.Entry<String,String> entry: row.entrySet()){
					squares.add(entry.getValue());
				}
				String[] printable = squares.toArray(new String[8]);
				pane.append(Arrays.toString(printable)+"\n");
			}
			
			pane.append("\n"+"\n"+"\n");
			
			try {
				float stime = (System.nanoTime());
				move = s.findMove();
				float etime = (System.nanoTime())-stime;
				pane.append("Search time: " +Float.toString(((stime/1000000000)/60)) +"mins"+"\n"+"\n");
				
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}			
			pane.append("nodes traversed: "+ Board.nodes);
			pane.append(move+"\n"+"\n");
			b.makeMove(move.split("-")[0], move.split("-")[1]);
			
			for(int i=0;i<8;i++){
				HashMap<String,String> row = b.board[i];
				ArrayList<String> squares = new ArrayList<>();
				for(HashMap.Entry<String,String> entry: row.entrySet()){
					squares.add(entry.getValue());
				}
				String[] printable = squares.toArray(new String[8]);
				pane.append(Arrays.toString(printable)+"\n");
			}			
			
			pane.append("\n"+"\n"+"\n");
			
			pane.append(getMove);
			Scanner input = new Scanner(System.in);
			mymove=input.nextLine();
			
			b.makeMove(mymove.split("-")[0], mymove.split("-")[1]);	
			
			pane.append("\n"+"\n"+"\n");
			
			play=true;
		}
	}
	
	public Test() throws CloneNotSupportedException{
		super("Gaara");
		


	}
	
	

}
