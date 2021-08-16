package application.dao;

import java.sql.*;

import application.firstLogin.Users.UserInfo;


public class DBConnector {
	
	private Connection conn;
	private Statement st;
	private ResultSet rs;
	
	// 생성자로 db 커넥트하기
	public DBConnector() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/plantgrowing?autoReconnect=true", "root", "1234");
			st = conn.createStatement();
		}catch(ClassNotFoundException ce) {
			System.out.println("시작시 데이터 연결 실패");
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	// DB 연결 connecter
	public Connection connect() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/plantgrowing?autoReconnect=true", "root", "1234");
			st = conn.createStatement();
		}catch(ClassNotFoundException ce) {
			System.out.println("드라이버 로딩 실패");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	// ID 로그인 시 ID Password 가 맞는지 확인
	// sql문 작성하여 Statement로 보내주고, ResultSet으로 결과 받아줌
	public boolean check(String ID, String password) {
		try {
			String sql = "SELECT * FROM members WHERE id = '" + ID + "' and password = '" + password + "'";
			rs = st.executeQuery(sql);
			if(rs.next()) {
				System.out.println("[Id / pw match]데이터 베이스 check 완료 : " + ID +" " + password);
				return true;
			}
					
		} catch (Exception e) {
			System.out.println("데이터 베이스 check 검색 오류 : "  + e.getMessage());
		}
		return false;
	}
	
	// ID 중복확인
	// sql문 작성하여 Statement로 보내주고, ResultSet으로 결과 받아줌
	public boolean checkDuplicate(String ID) {
		try {
			String sql = "SELECT * FROM members WHERE id = '" + ID + "'";
			rs = st.executeQuery(sql);
			if(rs.next()) {
				System.out.println("[중복된 id] 데이터 베이스 check 완료: " + ID);
				return true;
			}
					
		} catch (Exception e) {
			System.out.println("데이터 베이스 checkDuplicate 검색 오류 : "  + e.getMessage());
		}
		return false;
	}
	
	// 아이디 생성시 DB업데이트
	// userinfo id, pw만 있는 생성자 버전 사용해서 업뎃하면 됨.
	public void createId(UserInfo userInfo) {
		PreparedStatement psmt = null;
		try {
			
			String sql = "INSERT INTO members VALUES(?,?,'null','0','0','0','0',?,?,'0','1')";
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userInfo.getId());
			psmt.setString(2, userInfo.getPassword());
			psmt.setString(3, userInfo.getIP());
			psmt.setInt(4, userInfo.getPort());
			psmt.executeUpdate();
			
			System.out.println("[계정생성 완료] id = " + userInfo.getId() +" pw = " + userInfo.getPassword());
		} catch(SQLException se) {
			System.out.println("계정 생성 오류");
			se.printStackTrace();
		}
	}
	
	// 닉네임 세팅 때 아이디로 세팅
	public void updatePlantSpecies(UserInfo userInfo) {
		
		PreparedStatement psmt = null;
		try {
			String sql = "UPDATE members SET species = ? WHERE id = ?";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userInfo.getSpecies());
			psmt.setString(2, userInfo.getId());
			psmt.executeUpdate();
			
			System.out.printf("데이터 업데이트 완료[id = %1s / plantName = %1s] \n",
								userInfo.getId(), userInfo.getSpecies() );
		} catch(SQLException se) {
			System.out.println("식물 종 업데이트 오류");
			se.printStackTrace();
		}
	}
	
	
	// 닉네임 세팅 때 아이디로 세팅
	public void updatePlantName(UserInfo userInfo) {
		
		PreparedStatement psmt = null;
		try {
			String sql = "UPDATE members SET plantName = ? WHERE id = ?";
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userInfo.getPlantName());
			psmt.setString(2, userInfo.getId());
			psmt.executeUpdate();
			
			System.out.printf("데이터 업데이트 완료[id = %1s / plantName = %1s] \n",
								userInfo.getId(), userInfo.getPlantName() );
		} catch(SQLException se) {
			System.out.println("식물 이름 업데이트 오류");
			se.printStackTrace();
		}
	}

	// 메인페이지에서 아이디가 드러나지 않을때 닉네임으로 세팅하는기능
	public void updateAll(UserInfo userInfo) {
		
		PreparedStatement psmt = null;
		try {
			String sql = "UPDATE members SET watering = ?,caring = ?,tanning = ?, nutrition = ?, level = ? WHERE plantName = ?";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userInfo.getWatering());
			psmt.setInt(2, userInfo.getCaring());
			psmt.setInt(3, userInfo.getTanning());
			psmt.setInt(4, userInfo.getNutrition());
			psmt.setInt(5, userInfo.getLevel());
			psmt.setString(6, userInfo.getPlantName());
			psmt.executeUpdate();
			
			System.out.printf("데이터 업데이트 완료[id = %1s] 물 = %1d 사랑 = %1d 햇빛 = %1d 양분 = %1d \n",
								userInfo.getId(), userInfo.getWatering(), userInfo.getCaring(), userInfo.getTanning(), userInfo.getNutrition() );
		} catch(SQLException se) {
			System.out.println("데이터 업데이트 오류");
			se.printStackTrace();
		}
	}
	
	public void loadInfo(UserInfo userinfo,String id) {
		
		try {
			String sql = "SELECT * FROM members WHERE id = '" +id +"'";
			
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			if(rs.next()) {
				userinfo.setId(rs.getString(1));
				userinfo.setPassword(rs.getString(2));
				userinfo.setPlantName(rs.getString(3));
				userinfo.setWatering(rs.getInt(4));
				userinfo.setCaring(rs.getInt(5));
				userinfo.setTanning(rs.getInt(6));
				userinfo.setNutrition(rs.getInt(7));
				userinfo.setIP(rs.getString(8));
				userinfo.setPort(rs.getInt(9));
				userinfo.setSpecies(rs.getInt(10));
				userinfo.setLevel(rs.getInt(11));
				System.out.printf("[userInfo 불러오기 완료] id = %s / PlantName = %s / Watering = %d / Caring = %d / Tanning = %d / Nutrition = %d / Level = %d / Species = %d\n" ,
								userinfo.getId() , userinfo.getPlantName(), userinfo.getWatering(), userinfo.getCaring(), 
								userinfo.getTanning(), userinfo.getNutrition(), userinfo.getLevel(),userinfo.getSpecies());
			}
			
			
		}catch (Exception e) {
			System.out.println("데이터 불러오기 오류");
			e.printStackTrace();
		}
	}
	
	// 행운의 포춘쿠키 뽑기
	public String pickFortune() {
		try {
			String sql = "SELECT content FROM fortune ORDER BY rand() LIMIT 1";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()) {
				String ft =rs.getString(1);
				System.out.println("[포춘 쿠키]운세 가져오기 성공 : " + ft);
				
				return ft;
			}
			
		}catch(Exception e) {
			System.out.println("운세가져오기 오류");
		}
		return "잘 지내봐요";
	}
	
	
}
