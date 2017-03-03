package ChessEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


import Chess.UserInterface;
import ChessEngine.Board.FileInt;


public class Main extends JFrame{
	private JLabel fen = new JLabel("Enter board fen: "); 
	private JTextField fenstring = new JTextField(20);
	private JButton setbutton = new JButton("Set Board");
	private JCheckBox startfen=new JCheckBox("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	private JRadioButton whiteplayer=new JRadioButton("w",true);
	private JRadioButton blackplayer=new JRadioButton("b");
	String[] pieces = {"rook", "knight", "bishop", "pawn", "king", "queen"};
	private JComboBox piece = new JComboBox(pieces);
	private String pieceselected="rook";
	private JLabel startfile = new JLabel("File");
	private JLabel startrank = new JLabel("Rank");
	private JTextField file = new JTextField(2);
	private JTextField rank = new JTextField(2);
	private JButton getmoves = new JButton("Generate Moves");
	private JTextArea moves = new JTextArea(10,30);
	private final static String newline = "\n";
	private String color;
	private String f;
	private int r;

	public Main(){
		super("DeepChess V.1.2/5");
		Connection conn = null;

		try{
			//MYSQL database tables are created from .sql script
			conn = JBDC.getConnection();

			ScriptRunner scrpt = new ScriptRunner(conn,false,false);
			scrpt.runScript(new BufferedReader(new FileReader("/Users/Owner/Documents/CAPSTONE/CAPSTONE/src/ChessEngine/boardviews.sql")));

		}catch(SQLException se){
			//returns SQL Exception if tables already exist but does not interfere with program execution
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();;
		}
		finally {
			DbUtils.closeQuietly(conn);
		}

		Board b = new Board();

		JPanel setBoard = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		setBoard.setBackground(Color.lightGray);

		c.gridx=0;
		c.gridy=0;
		setBoard.add(fen,c);

		c.gridx=1;
		c.gridy=0;
		c.gridwidth=2;
		setBoard.add(fenstring,c);

		c.gridx=0;
		c.gridy=1;
		setBoard.add(startfen,c);
		startfen.setSelected(true);


		setbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String fen = fenstring.getText();
				if(startfen.isSelected()){
					fen=startfen.getText();
				}
				b.readFen(fen);
				int row=1;
				for(HashMap<String,String> f : b.board){
					moves.append("Row "+row+": "+f.toString()+newline);
					row+=1;
				}
			}
		});
		c.gridx=1;
		c.gridy=2;
		setBoard.add(setbutton,c);

		setBoard.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Set Board"));



		JPanel playSelection = new JPanel(new GridBagLayout());

		blackplayer.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				color="b";
			}

		});
		whiteplayer.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				color="w";
			}

		});

		ButtonGroup player=new ButtonGroup();
		player.add(blackplayer);
		player.add(whiteplayer);

		JPanel plyr=new JPanel(new GridBagLayout());
		JLabel playerselection = new JLabel("Player");
		c.gridx=0;
		c.gridy=0;
		plyr.add(playerselection,c);
		c.gridx=0;
		c.gridy=1;
		plyr.add(whiteplayer,c);
		c.gridx=0;
		c.gridy=2;
		plyr.add(blackplayer,c);
		

		c.gridx=0;
		c.gridy=0;
		playSelection.add(plyr,c);

		JPanel sqr = new JPanel(new GridBagLayout());
		c.gridx=0;
		c.gridy=0;
		sqr.add(startfile,c);
		c.gridx=2;
		c.gridy=0;
		sqr.add(file,c);

		c.gridx=0;
		c.gridy=1;
		sqr.add(startrank,c);
		c.gridx=2;
		c.gridy=1;
		sqr.add(rank,c);

		c.gridx=1;
		c.gridy=0;
		playSelection.add(sqr,c);

		piece.setSelectedIndex(0);
		piece.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				pieceselected= (String)cb.getSelectedItem();
			}

		});
		c.gridx=2;
		c.gridy=0;
		playSelection.add(piece,c);

		getmoves.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Piece selectedpiece=new Piece();
				f=file.getText();
				r=Integer.parseInt(rank.getText());
				switch(pieceselected){
				case("rook"): selectedpiece=new Rook(color,f,r); break;
				case("knight"): selectedpiece=new Knight(color,f,r); break;
				case("bishop"): selectedpiece=new Bishop(color,f,r); break;
				case("pawn"): selectedpiece=new Pawn(color,f,r); break;
				case("king"): selectedpiece=new King(color,f,r); break;
				case("queen"): selectedpiece=new Queen(color,f,r); break;
				}
				selectedpiece.generatemoves();
				selectedpiece.parsMoves();
				moves.append(newline+newline+"---"+pieceselected+" Moves"+"---"+newline);
				moves.append(newline+"All Moves :"+selectedpiece.moves+newline);
				moves.append(newline+"Blocked Moves :"+selectedpiece.bmoves+newline);
				moves.append(newline+"Attack Moves :"+selectedpiece.amoves+newline);
				moves.append(newline+"Legal Moves :"+selectedpiece.lmoves+newline);
			}

		});
		c.gridx=2;
		c.gridy=2;
		playSelection.add(getmoves,c);

		JPanel view = new JPanel(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		JScrollPane scroll = new JScrollPane(moves);
		moves.setEditable(false);
		view.add(scroll,con);

		c.gridx=1;
		c.gridy=1;
		playSelection.add(view,c);

		playSelection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Preview Move"));


		add(setBoard,BorderLayout.NORTH);
		add(playSelection,BorderLayout.CENTER);

		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws SQLException {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Main().setVisible(true);
			}

		});
	}

}
