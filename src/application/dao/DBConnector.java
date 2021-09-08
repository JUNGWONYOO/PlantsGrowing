package application.dao;

import java.sql.*;

import application.firstLogin.Users.UserInfo;


public class DBConnector {
	
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;
	private PreparedStatement psmt = null;
	
	public Connection getConnection() throws SQLException {	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection("jdbc:mysql://localhost/plantgrowing?autoReconnect=true", "root", "1234");
	}
	
	// ID �α��� �� ID Password �� �´��� Ȯ��
	// sql�� �ۼ��Ͽ� Statement�� �����ְ�, ResultSet���� ��� �޾���
	public boolean check(String ID, String password) throws SQLException {
		String sql = "SELECT * FROM members WHERE id = '" + ID + "' and password = '" + password + "'";
		try {
			conn = getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()) {
				System.out.println("[Id / pw match]������ ���̽� check �Ϸ� : " + ID +" " + password);
				return true;
			}
					
		} catch (Exception e) {
			System.out.println("������ ���̽� check �˻� ���� : "  + e.getMessage());
		}finally {
			close(conn, rs, st, psmt);
		}
		return false;
	}
	
	// ID �ߺ�Ȯ��
	// sql�� �ۼ��Ͽ� Statement�� �����ְ�, ResultSet���� ��� �޾���
	public boolean checkDuplicate(String ID) throws SQLException {
		String sql = "SELECT * FROM members WHERE id = '" + ID + "'";
		try {
			conn = getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()) {
				System.out.println("[�ߺ��� id] ������ ���̽� check �Ϸ�: " + ID);
				return true;
			}
				// Ŀ��	
		} catch (Exception e) {
			System.out.println("������ ���̽� checkDuplicate �˻� ���� : "  + e.getMessage());
			// �� ��
		}finally {
			close(conn, rs, st, psmt);
			// ���� close
		}
		return false;
	}
	
	// ���̵� ������ DB������Ʈ
	// userinfo id, pw�� �ִ� ������ ���� ����ؼ� �����ϸ� ��.
	public void createId(UserInfo userInfo) throws SQLException {

		String sql = "INSERT INTO members VALUES(?,?,'null','0','0','0','0',?,?,'0','1')";
		
		try {
			conn = getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userInfo.getId());
			psmt.setString(2, userInfo.getPassword());
			psmt.setString(3, userInfo.getIP());
			psmt.setInt(4, userInfo.getPort());
			psmt.executeUpdate();
			
			System.out.println("[�������� �Ϸ�] id = " + userInfo.getId() +" pw = " + userInfo.getPassword());
		} catch(SQLException se) {
			System.out.println("���� ���� ����");
			se.printStackTrace();
		} finally {
			close(conn, rs, st, psmt);
		}
	}
	
	// �Ĺ� ���� ����
	public void updatePlantSpecies(UserInfo userInfo) throws SQLException {
		String sql = "UPDATE members SET species = ? WHERE id = ?";
		try {

			conn = getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userInfo.getSpecies());
			psmt.setString(2, userInfo.getId());
			psmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s / plantName = %1s] \n",
								userInfo.getId(), userInfo.getSpecies() );
		} catch(SQLException se) {
			System.out.println("�Ĺ� �� ������Ʈ ����");
			se.printStackTrace();
		} finally {
			close(conn, rs, st, psmt);
		}
	}
	
	
	// �г��� ���� �� ���̵�� ����
	public void updatePlantName(UserInfo userInfo) throws SQLException {
		String sql = "UPDATE members SET plantName = ? WHERE id = ?";
		try {
			
			conn = getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userInfo.getPlantName());
			psmt.setString(2, userInfo.getId());
			psmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s / plantName = %1s] \n",
								userInfo.getId(), userInfo.getPlantName() );
		} catch(SQLException se) {
			System.out.println("�Ĺ� �̸� ������Ʈ ����");
			se.printStackTrace();
		}finally {
			close(conn, rs, st, psmt);
		}
	}

	// �������������� ���̵� �巯���� ������ �г������� �����ϴ±��
	public void updateAll(UserInfo userInfo) throws SQLException {
		String sql = "UPDATE members SET watering = ?,caring = ?,tanning = ?, nutrition = ?, level = ? WHERE plantName = ?";
		try {
			
			conn = getConnection();
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userInfo.getWatering());
			psmt.setInt(2, userInfo.getCaring());
			psmt.setInt(3, userInfo.getTanning());
			psmt.setInt(4, userInfo.getNutrition());
			psmt.setInt(5, userInfo.getLevel());
			psmt.setString(6, userInfo.getPlantName());
			psmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s] �� = %1d ��� = %1d �޺� = %1d ��� = %1d \n",
								userInfo.getId(), userInfo.getWatering(), userInfo.getCaring(), userInfo.getTanning(), userInfo.getNutrition() );
		} catch(SQLException se) {
			System.out.println("������ ������Ʈ ����");
			se.printStackTrace();
		} finally {
			close(conn, rs, st, psmt);
		}
	}
	
	//������ ���ε�, db�� �����͸� ���� �� ������ ��������
	public void loadInfo(UserInfo userInfo,String id) throws SQLException {
		String sql = "SELECT * FROM members WHERE id = '" +id +"'";
		try {
			
			conn = getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			if(rs.next()) {
				userInfo.setId(rs.getString(1));
				userInfo.setPassword(rs.getString(2));
				userInfo.setPlantName(rs.getString(3));
				userInfo.setWatering(rs.getInt(4));
				userInfo.setCaring(rs.getInt(5));
				userInfo.setTanning(rs.getInt(6));
				userInfo.setNutrition(rs.getInt(7));
				userInfo.setIP(rs.getString(8));
				userInfo.setPort(rs.getInt(9));
				userInfo.setSpecies(rs.getInt(10));
				userInfo.setLevel(rs.getInt(11));
				System.out.printf("[userInfo �ҷ����� �Ϸ�] id = %s / PlantName = %s / Watering = %d / Caring = %d / Tanning = %d / Nutrition = %d / Level = %d / Species = %d\n" ,
						userInfo.getId() , userInfo.getPlantName(), userInfo.getWatering(), userInfo.getCaring(), 
						userInfo.getTanning(), userInfo.getNutrition(), userInfo.getLevel(),userInfo.getSpecies());
			}
			
			
		}catch (Exception e) {
			System.out.println("������ �ҷ����� ����");
			e.printStackTrace();
		}finally {
			close(conn, rs, st, psmt);
		}
	}
	
	// ����� ������Ű �̱�
	public String pickFortune() throws SQLException {
		String sql = "SELECT content FROM fortune ORDER BY rand() LIMIT 1";
		try {
			
			conn = getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()) {
				String ft =rs.getString(1);
				System.out.println("[���� ��Ű]� �������� ���� : " + ft);
				
				return ft;
			}
			
		}catch(Exception e) {
			System.out.println("��������� ����");
		}finally {
			close(conn, rs, st, psmt);
		}
		return "�� ��������";
	}
	
	public void close(Connection conn, ResultSet rs, Statement st, PreparedStatement psmt ) {
		
		try {
			if(conn != null) {
			conn.close();
			}
			if(rs != null) {
				rs.close();
			}
			if(st != null) {
				st.close();
			}
			if(psmt != null) {
				psmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
//Connection conn = null;
//Statement st = null;
//ResultSet rs = null;
//PreparedStatement psmt = null;