package application.firstLogin.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;

import application.dao.DBConnector;
import application.firstLogin.Users.UserInfo;
import application.main.Controller.Controller;
import application.main.Controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginController {
	
	DBConnector connect = new DBConnector();

	@FXML
	private Button btn;
	
	@FXML
	private PasswordField enterPasswordField;
	
	@FXML
	private TextField userNameTextField;
	
	@FXML
	private TextField tfd_create_ID;
	@FXML
	private PasswordField tfd_createPw;
	@FXML
	private PasswordField tfd_createPw2;
	
	public static Vector<UserInfo> UserList = new Vector<UserInfo>();
	
	
	// �α��� ��� (DB�� ȸ������ ������ ������ �� ����)
	public void Login(ActionEvent ae) throws IOException {
			
		if(connect.check(userNameTextField.getText(), enterPasswordField.getText())) {
			
		
			// �� ȭ�鿡 �� �����ڿ� ��Ʈ�� �ٴϸ� ��
			// �� ������ user id�� ����
			UserInfo userinfo = new UserInfo();	
			connect.loadInfo(userinfo, userNameTextField.getText());
			UserList.add(userinfo);
			
			Stage primaryStage = (Stage) btn.getScene().getWindow();
			Parent root ;
			FXMLLoader loader;
			Controller m;
			
			String css = "null";
			if(userinfo.getSpecies() == 0) {
				loader = new FXMLLoader(getClass().getResource("../../selectP/View/SelectPlantScene.fxml"));	
			}
			
			else if(userinfo.getSpecies() != 0 && userinfo.getPlantName().equals("null")) {
				loader = new FXMLLoader(getClass().getResource("../../selectP/View/CreateNameScene.fxml"));		
			}
			
			else if(!userinfo.getPlantName().equals("null") && userinfo.getSpecies() == 1 ) {		
				loader = new FXMLLoader(getClass().getResource("../../main/View/main.fxml"));	
				css = this.getClass().getResource("../../main/View/css/main.css").toExternalForm();
	  
			}
			
			else {
				loader = new FXMLLoader(getClass().getResource("../../main/View/main.fxml"));
				css = this.getClass().getResource("../../main/View/css/main.css").toExternalForm();
			}

			root = loader.load();
		
			if(!userinfo.getPlantName().equals("null")) {
				m = loader.getController();
				String pName = userinfo.getPlantName();
				m.setPname(pName);
			}
			
			
			Scene scene = new Scene(root);
			if(!css.equals("null")) {
				scene.getStylesheets().add(css);
			}
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("�Ĺ�Ű���~");
			primaryStage.show();
			
			System.out.println( "id = " + userNameTextField.getText() + " login �Ϸ� [IP = " + userinfo.getIP() + " / port = " + userinfo.getPort() +"]");
			
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("�α��ο� �����Ͽ����ϴ�.");
			alert.setContentText("���̵� Ȥ�� ��й�ȣ�� Ȯ���ϼ���.");
			alert.showAndWait();
		}
	}
		
	// ȸ�� ���� �������� �̵�
	public void createNewId(ActionEvent ae) throws IOException{
		
		Stage primaryStage = (Stage) btn.getScene().getWindow();	
		Parent root = FXMLLoader.load(getClass().getResource("../View/CreateId.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Plants Growing ȸ������");
		primaryStage.show();
		
	}
	
	// ȸ������ �Ϸ�, ��й�ȣ Ȯ�ΰ� ��й�ȣ�� ���ƾ� ��
	public void createDone(ActionEvent ae) throws IOException {
		
		InetAddress ia = InetAddress.getLocalHost();
		String ip = ia.toString().substring(16,ia.toString().length());
		
		if(tfd_createPw.getText().equals(tfd_createPw2.getText()) && tfd_create_ID.getText().length() > 4 && !connect.checkDuplicate(tfd_create_ID.getText())) {
		
		UserInfo userinfo = new UserInfo(tfd_create_ID.getText(), tfd_createPw.getText(),ip, 9876, 1);	
		
		
		 try {
			 connect.createId(userinfo);
			
		 }catch (Exception e) { 
			 System.out.println("���̵� ���� ����"); 
		 }
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ȸ������ �Ϸ�");
		alert.setContentText("�����մϴ� ȸ������ �Ϸ�Ǿ����ϴ�");
		alert.showAndWait();
			
		Stage primaryStage = (Stage) btn.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Plants Growing �α���");
		primaryStage.show();
		
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("���̵� ������ �����Ͽ����ϴ�");
			alert.setContentText("���̵� Ȥ�� ��й�ȣ�� Ȯ���ϼ���.");
			alert.showAndWait();
		}
	}

	
	// ���̵� �ߺ� Ȯ�� ��ư
	public void checkID(ActionEvent ae) throws IOException {
		String id = tfd_create_ID.getText();

		if(connect.checkDuplicate(id)) {
			Alert checkIdErr = new Alert(AlertType.ERROR);
			checkIdErr.setTitle("���̵� Ȯ��");
			checkIdErr.setContentText("�ߺ��� ���̵��Դϴ�.");
			checkIdErr.showAndWait();
		}
		else {
			Alert checkIdErr = new Alert(AlertType.CONFIRMATION);
			checkIdErr.setTitle("���̵� Ȯ��");
			checkIdErr.setContentText("ȸ�������� �����մϴ�.");
			checkIdErr.showAndWait();
		}

	}
	


	
}
