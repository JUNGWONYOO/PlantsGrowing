package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTemplate {
	
	public static Connection getConnection() {
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/plantgrowing?autoReconnect=true", "root", "1234");
			conn.setAutoCommit(false);
			 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("jdbc Driver 탐색 오류");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("mysql 커넥션 오류");
		}
		return conn;
	}
	
	public static boolean isConnected(Connection conn) {
		
		boolean check = true;
		try {
			if(conn.isClosed() || conn == null) {
				check =  false;
			}else {
				check =  true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return check;
	}
	
	public static void close(Connection conn) {
		
		try {
			if(isConnected(conn)) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(!pstmt.isClosed() || pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(Statement stmt) {
		try {
			if(!stmt.isClosed() || stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(ResultSet rs) {
		try {
			if(!rs.isClosed() || rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void commit(Connection conn) {
		try {
			if(isConnected(conn)) {
				conn.commit();
			}
			System.out.println("[커밋 성공]");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollBack(Connection conn) {
		try {
			if(isConnected(conn)) {
				conn.rollback();
			}
			System.out.println("[롤백 성공]");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
