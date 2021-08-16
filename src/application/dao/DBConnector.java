package application.dao;

import java.sql.*;

import application.firstLogin.Users.UserInfo;


public class DBConnector {
	
	private Connection conn;
	private Statement st;
	private ResultSet rs;
	
	// �����ڷ� db Ŀ��Ʈ�ϱ�
	public DBConnector() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/plantgrowing?autoReconnect=true", "root", "1234");
			st = conn.createStatement();
		}catch(ClassNotFoundException ce) {
			System.out.println("���۽� ������ ���� ����");
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	// DB ���� connecter
	public Connection connect() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/plantgrowing?autoReconnect=true", "root", "1234");
			st = conn.createStatement();
		}catch(ClassNotFoundException ce) {
			System.out.println("����̹� �ε� ����");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	// ID �α��� �� ID Password �� �´��� Ȯ��
	// sql�� �ۼ��Ͽ� Statement�� �����ְ�, ResultSet���� ��� �޾���
	public boolean check(String ID, String password) {
		try {
			String sql = "SELECT * FROM members WHERE id = '" + ID + "' and password = '" + password + "'";
			rs = st.executeQuery(sql);
			if(rs.next()) {
				System.out.println("[Id / pw match]������ ���̽� check �Ϸ� : " + ID +" " + password);
				return true;
			}
					
		} catch (Exception e) {
			System.out.println("������ ���̽� check �˻� ���� : "  + e.getMessage());
		}
		return false;
	}
	
	// ID �ߺ�Ȯ��
	// sql�� �ۼ��Ͽ� Statement�� �����ְ�, ResultSet���� ��� �޾���
	public boolean checkDuplicate(String ID) {
		try {
			String sql = "SELECT * FROM members WHERE id = '" + ID + "'";
			rs = st.executeQuery(sql);
			if(rs.next()) {
				System.out.println("[�ߺ��� id] ������ ���̽� check �Ϸ�: " + ID);
				return true;
			}
					
		} catch (Exception e) {
			System.out.println("������ ���̽� checkDuplicate �˻� ���� : "  + e.getMessage());
		}
		return false;
	}
	
	// ���̵� ������ DB������Ʈ
	// userinfo id, pw�� �ִ� ������ ���� ����ؼ� �����ϸ� ��.
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
			
			System.out.println("[�������� �Ϸ�] id = " + userInfo.getId() +" pw = " + userInfo.getPassword());
		} catch(SQLException se) {
			System.out.println("���� ���� ����");
			se.printStackTrace();
		}
	}
	
	// �г��� ���� �� ���̵�� ����
	public void updatePlantSpecies(UserInfo userInfo) {
		
		PreparedStatement psmt = null;
		try {
			String sql = "UPDATE members SET species = ? WHERE id = ?";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userInfo.getSpecies());
			psmt.setString(2, userInfo.getId());
			psmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s / plantName = %1s] \n",
								userInfo.getId(), userInfo.getSpecies() );
		} catch(SQLException se) {
			System.out.println("�Ĺ� �� ������Ʈ ����");
			se.printStackTrace();
		}
	}
	
	
	// �г��� ���� �� ���̵�� ����
	public void updatePlantName(UserInfo userInfo) {
		
		PreparedStatement psmt = null;
		try {
			String sql = "UPDATE members SET plantName = ? WHERE id = ?";
			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userInfo.getPlantName());
			psmt.setString(2, userInfo.getId());
			psmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s / plantName = %1s] \n",
								userInfo.getId(), userInfo.getPlantName() );
		} catch(SQLException se) {
			System.out.println("�Ĺ� �̸� ������Ʈ ����");
			se.printStackTrace();
		}
	}

	// �������������� ���̵� �巯���� ������ �г������� �����ϴ±��
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
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s] �� = %1d ��� = %1d �޺� = %1d ��� = %1d \n",
								userInfo.getId(), userInfo.getWatering(), userInfo.getCaring(), userInfo.getTanning(), userInfo.getNutrition() );
		} catch(SQLException se) {
			System.out.println("������ ������Ʈ ����");
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
				System.out.printf("[userInfo �ҷ����� �Ϸ�] id = %s / PlantName = %s / Watering = %d / Caring = %d / Tanning = %d / Nutrition = %d / Level = %d / Species = %d\n" ,
								userinfo.getId() , userinfo.getPlantName(), userinfo.getWatering(), userinfo.getCaring(), 
								userinfo.getTanning(), userinfo.getNutrition(), userinfo.getLevel(),userinfo.getSpecies());
			}
			
			
		}catch (Exception e) {
			System.out.println("������ �ҷ����� ����");
			e.printStackTrace();
		}
	}
	
	// ����� ������Ű �̱�
	public String pickFortune() {
		try {
			String sql = "SELECT content FROM fortune ORDER BY rand() LIMIT 1";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()) {
				String ft =rs.getString(1);
				System.out.println("[���� ��Ű]� �������� ���� : " + ft);
				
				return ft;
			}
			
		}catch(Exception e) {
			System.out.println("��������� ����");
		}
		return "�� ��������";
	}
	
	
}
