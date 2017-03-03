package ChessEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Stack;
import ChessEngine.Board.FileInt;

public class JBDC {
	 	private static String url = "jdbc:mysql://localhost:3306/CHESS";    
	    private static String driverName = "com.mysql.jdbc.Driver";   
	    private static String username = "capstone";   
	    private static String password = "enT3r21";
	    private static Connection con=null;
	    private static Statement stmt=null;
	    private static PreparedStatement stm=null;
	    private static ResultSet rs=null;

	    public static Connection getConnection() {
	        try {
	            Class.forName(driverName);
	            try {
	        	    Properties info = new Properties();
	            	info.setProperty("user", username);
	        	    info.setProperty("password", password);
	        	    info.setProperty("useSSL", "false");
	                con = DriverManager.getConnection(url, info);
	                
	            } catch (SQLException ex) {
	                System.out.println("Failed to create the database connection."); 
	            }
	        } catch (ClassNotFoundException ex) {
	            System.out.println("Driver not found."); 
	        }
	        return con;  
	        
	    }
	    
	    
	   public static void updateSquares(){
			   int i=0;
				while(i<Board.board.length){
					for(String k : Board.board[i].keySet()){
						  con = getConnection();
						String query = "UPDATE boardrank SET boardrank.";
						query+=k;
						query+=" = ?";
						query+=" WHERE boardrank.rank = ";
						query+=Integer.toString(i+1);
						query+=";";
						try {
							stm = con.prepareStatement(query);
							stm.setString(1, Board.board[i].get(k));
							stm.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							  DbUtils.closeQuietly(stm);
							  DbUtils.closeQuietly(con);
						}
						  
					}
					i++;
				}
			
			 
		   
		   
	   }
	   
	   public static ArrayList<String> getRookMoves(String file, int rank ){
		   con=getConnection();
		   ArrayList<String> rmoves=new ArrayList<String>();
		   String query = "SELECT file, rank, occupiedBy FROM squares WHERE file=? or rank=?;";
		   
		   try {
			stm = con.prepareStatement(query);
			stm.setString(1, file);
			stm.setInt(2, rank);
			rs = stm.executeQuery();
			while(rs.next()){
				rmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
		
		   }
		   } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }finally{
			   DbUtils.closeQuietly(rs);
			   DbUtils.closeQuietly(stm);
			   DbUtils.closeQuietly(con);  
		   }
		   return rmoves;
	   }
	   
	   public static ArrayList<String> getKnightMoves(String file, int rank){
		   con=getConnection();
		   ArrayList<String> kmoves = new ArrayList<String>();

		   String query = "SELECT file, rank, occupiedBy FROM squares WHERE file=? and rank=?;";
		   		
		   int row=rank;
		   FileInt col=FileInt.file(file);
		   int cval=col.getval();
		   
		   int r1=row+1;
		   int r2=row+2;
		   int r3=row-1;
		   int r4=row-2;
		   
		   String f1= cval+1<=8 ? FileInt.file(cval+1).name() : null;
		   String f2= cval+2<=8 ? FileInt.file(cval+2).name() : null;
		   String f3= cval-1>=1 ? FileInt.file(cval-1).name() : null;
		   String f4= cval-2>=1 ? FileInt.file(cval-2).name() : null;
		   try {
				stm = con.prepareStatement(query);
				if(f1!=null && r2<=8){
					   stm.setString(1, f1);
					   stm.setInt(2, r2);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				   if(f3!=null && r2<=8){
					   stm.setString(1, f3);
					   stm.setInt(2, r2);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				   if(f1!=null && r4>=1){
					   stm.setString(1, f1);
					   stm.setInt(2, r4);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				   if(f3!=null && r4>=1){
					   stm.setString(1, f3);
					   stm.setInt(2, r4);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				   if(f2!=null && r1<=8){
					   stm.setString(1, f2);
					   stm.setInt(2, r1);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				   if(f2!=null && r3>=1){
					   stm.setString(1, f2);
					   stm.setInt(2, r3);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				   if(f4!=null && r1<=8){
					   stm.setString(1, f4);
					   stm.setInt(2, r1);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				   if(f4!=null && r3>=1){
					   stm.setString(1, f4);
					   stm.setInt(2, r3);
					   rs = stm.executeQuery();
					   rs.next();
					   kmoves.add(rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy"));
					   DbUtils.closeQuietly(rs);
				   }
				  
			   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			   }finally{
				   DbUtils.closeQuietly(stm);
				   DbUtils.closeQuietly(con);
				   
			   }
		   
		   return kmoves;
	   }
	   
	   public static ArrayList<String> getBishopMoves(String file, int rank){
		   con=getConnection();
		   ArrayList<String> bmoves= new ArrayList<String>();
		   String move=null;
		   String query = "SELECT file, rank, occupiedBy FROM squares WHERE file=? and rank=?;";

		   
		   FileInt c = FileInt.file(file);
		   int cval = c.getval();
		   int nextrank=rank+1;
		   int prevrank=rank-1;
		
		   String nextFile= FileInt.file(cval+1)==null ? null : FileInt.file(cval+1).name();
		   String prevFile= FileInt.file(cval-1)==null ? null : FileInt.file(cval-1).name();
		   try{
			   stm = con.prepareStatement(query);
			   while(nextrank<=8 || prevrank>=1 || nextFile!=null || prevFile!=null){
				   if(nextrank<=8 && nextFile!=null){
						stm.setString(1, nextFile);
						stm.setInt(2, nextrank);
						rs = stm.executeQuery();
						rs.next();
						move=rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy");
						if(!bmoves.contains(move)){
							bmoves.add(move);
						}
						DbUtils.closeQuietly(rs);
					}
					if(nextrank<=8 && prevFile!=null){
						stm.setString(1, prevFile);
						stm.setInt(2, nextrank);
						rs = stm.executeQuery();
						rs.next();
						move=rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy");
						if(!bmoves.contains(move)){
							bmoves.add(move);
						}
						DbUtils.closeQuietly(rs);
					}
					if(prevrank>=1 && nextFile!=null){
						stm.setString(1, nextFile);
						stm.setInt(2, prevrank);
						rs = stm.executeQuery();
						rs.next();
						move=rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy");
						if(!bmoves.contains(move)){
							bmoves.add(move);
						}
						DbUtils.closeQuietly(rs);
					}
					if(prevrank>=1 && prevFile!=null){
						stm.setString(1, prevFile);
						stm.setInt(2, prevrank);
						rs = stm.executeQuery();
						rs.next();
						move=rs.getString("file")+":"+rs.getInt("rank")+":"+rs.getString("occupiedBy");
						if(!bmoves.contains(move)){
							bmoves.add(move);
						}
						DbUtils.closeQuietly(rs);
					}
					
					nextrank+=1;
					prevrank-=1;
					
					int nfval = FileInt.file(nextFile!=null ? nextFile : "h").getval();
					int pfval = FileInt.file(prevFile!=null ? prevFile : "a").getval();
					nextFile= FileInt.file(nfval+1)==null ? null : FileInt.file(FileInt.file(nextFile).getval()+1).name();
					prevFile= FileInt.file(pfval-1)==null ? null : FileInt.file(FileInt.file(prevFile).getval()-1).name();
			   }
		   }catch(Exception e){
			   e.printStackTrace();
		   }finally{
		   DbUtils.closeQuietly(stm);
		   DbUtils.closeQuietly(con);
		   }
		   return bmoves;
	   }


		


}
