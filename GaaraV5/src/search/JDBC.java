package search;

import java.sql.*;
import java.util.*;

import boardRep.Board;

public class JDBC {
	private static String url = "jdbc:mysql://localhost:3306/chess";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String username = "capstone";
	private static String password = "enT3r21";
	private static Connection con=null;
	private static Connection conn=null;
	private static Statement stmt=null;
	private static PreparedStatement stm=null;
	private static PreparedStatement stms=null;
	private static CallableStatement cs=null;
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
	
	public static void update(HashMap<String,String>[] board){
		String query = "UPDATE squares SET occupiedBy = ? WHERE file = ? AND rank = ?";
		con=getConnection();
		try{
			con.setAutoCommit(true);
			int i=0;
			while(i<board.length) {
				for(HashMap.Entry<String,String> entry : board[i].entrySet()) {
					stm = con.prepareStatement(query);
					stm.setString(1, entry.getValue());
					stm.setString(2,entry.getKey());
					stm.setInt(3,i+1);
					stm.addBatch();
				}
				i+=1;
			}
			stm.executeBatch();
			con.commit();
		}catch(SQLException se){
			se.getSQLState();

		}catch(Exception e){
			e.getMessage();

		}finally{
			DbUtils.closeQuietly(stm);
			DbUtils.closeQuietly(con);
		}
	}

	public static Vector<String> searchMoves(HashMap<String,String>[] board, String sqr){
		Vector<String> moves = new Vector<>();
		String query = "UPDATE squares SET occupiedBy = ? WHERE file = ? AND rank = ?";
		String call = "{CALL Reset()}";
		
		String f = sqr.split("")[0];
		int r = Integer.parseInt(sqr.split("")[1]);
		String p = sqr.split("")[2];

		conn=getConnection();
		try{
			conn.setAutoCommit(true);
			int i=0;
			while(i<8) {
				for(HashMap.Entry<String,String> entry : board[i].entrySet()) {
					stms = conn.prepareStatement(query);
					stms.setString(1, entry.getValue());
					stms.setString(2,entry.getKey());
					stms.setInt(3,i+1);
					stms.execute();
				}
				i+=1;
			}			
			if(p.equals("p")){
				moves=getPawnMoves("b",f,r);
			}else{
				if(p.equals("P")){
					moves=getPawnMoves("w",f,r);
				}else{
					if("kK".contains(p)){
						moves=getKingMoves(f,r);
					}else{
						if("qQ".contains(p)){
							moves=getQueenMoves(f,r);
						}else{
							if("bB".contains(p)){
								moves=getBishopMoves(f,r);
							}else{
								if("nN".contains(p)){
									moves=getKnightMoves(f,r);
								}else{
									if("rR".contains(p)){
										moves=getRookMoves(f,r);
									}
								}
							}
						}
					}
				}
			}
			cs = conn.prepareCall(call);
			cs.execute();
		}catch(SQLException se){
			se.getSQLState();
		}catch(Exception e){
			e.getMessage();
		}finally{
			DbUtils.closeQuietly(stms);
			DbUtils.closeQuietly(cs);
			DbUtils.rollbackAndCloseQuietly(conn);
		}
		return moves;
	}



	private static Vector<String> fetchMoves(String call, String file, int rank){
		Vector<String> moves = new Vector<>();

		con=getConnection();

		try {
			cs = con.prepareCall(call);
			cs.setString(1,file);
			cs.setInt(2,rank);
			rs=cs.executeQuery();

			while(rs.next()){
				String f = rs.getString("file");
				int r = rs.getInt("rank");
				String tst = rs.getString("occupiedBy");
				String occ = rs.wasNull()? "":tst;
				moves.add(f+r+occ);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(cs);
			DbUtils.closeQuietly(con);

		}
		return moves;

	}

	public static Vector<String> getKingMoves(String file, int rank){
		String call = "{Call getKingMoves(?,?)}";
		return fetchMoves(call,file,rank);
	}

	public static Vector<String> getQueenMoves(String file, int rank){
		String call = "{Call getQueenMoves(?,?)}";
		return fetchMoves(call,file,rank);
	}
	public static Vector<String> getBishopMoves(String file, int rank){
		String call = "{Call getBishopMoves(?,?)}";
		return fetchMoves(call,file,rank);
	}
	public static Vector<String> getKnightMoves(String file, int rank){
		String call = "{Call getKnightMoves(?,?)}";
		return fetchMoves(call,file,rank);
	}
	public static Vector<String> getRookMoves(String file, int rank){
		String call = "{Call getRookMoves(?,?)}";
		return fetchMoves(call,file,rank);
	}
	public static Vector<String> getPawnMoves(String p, String file, int rank){
		String call = "{Call getPawnMoves(?,?,?)}";
        Vector<String> moves = new Vector<>();

        con=getConnection();

        try {
            cs = con.prepareCall(call);
            cs.setString(1,p);
            cs.setString(2,file);
            cs.setInt(3,rank);
            rs=cs.executeQuery();

            while(rs.next()){
            	String f = rs.getString("file");
				int r = rs.getInt("rank");
				String tst = rs.getString("occupiedBy");
				String occ = rs.wasNull()? "":tst;
				moves.add(f+r+occ);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(cs);
            DbUtils.closeQuietly(con);

        }
        return moves;
	}
	
	public static boolean updatePercept(String table, String move,String rating){
		boolean valid = false;

		String update = "INSERT INTO "+table+" (move,rating) VALUES (?, ?)";

		con=getConnection();

		try{
			con.setAutoCommit(true);

			stm = con.prepareStatement(update);
			stm.setString(1,move);
			stm.setString(2,rating);
			stm.addBatch();

			int[] count = stm.executeBatch();

			if(count.length==2 & count[0]==1 & count[1]==1){
				valid = true;
			}


		}catch(SQLException se){
			se.getSQLState();

		}catch(Exception e){
			e.getMessage();

		}finally{
			DbUtils.closeQuietly(stm);
			DbUtils.closeQuietly(con);
		}

		return valid;
	}
	
	public static boolean newSequence(String tablename){
		boolean valid = true;
		
		String update = "CREATE TABLE "+tablename+"(mcount int autoincrement not null, move varchar(5), rating int)";

		con=getConnection();

		try{
			con.setAutoCommit(true);

			stmt = con.createStatement();
			stmt.execute(update);


		}catch(SQLException se){
			valid=false;
			se.getSQLState();

		}catch(Exception e){
			valid=false;
			e.getMessage();

		}finally{
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(con);
		}

		return valid;
	}




}
