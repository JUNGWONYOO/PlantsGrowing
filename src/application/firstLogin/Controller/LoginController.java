package application.firstLogin.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SeekableByteChannel;
import java.sql.SQLException;
import java.util.Vector;

import javax.security.auth.login.AppConfigurationEntry;

import application.Singletone;
import application.dao.DBConnector;
import application.firstLogin.Users.UserInfo;
import application.main.Controller.ControlInterface;
import application.main.Controller.Controller;
import application.main.Controller.Controller2;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController {
	
	DBConnector db = Singletone.getInstance();// DB���� ������

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
	
	// UserList ���Ϳ� �����ϸ鼭 , �� ��Ʈ�ѷ����� ���� ������ �ҷ��� �� �ְԲ� ����
	public static Vector<UserInfo> UserList = new Vector<UserInfo>();
	
	private void execute(LoginService loginService) {
		loginService.execute(userNameTextField, btn, enterPasswordField, tfd_create_ID, tfd_createPw, tfd_createPw2);
	}
	
	// �α��� ��� (DB�� ȸ������ ������ ������ �� ����)
	public void Login(ActionEvent ae) throws IOException, SQLException {
		
		if(db.check(userNameTextField.getText(), enterPasswordField.getText())) {		
			execute(new LoginImpl());
			
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
		
		String title = "Plants Growing ȸ������";
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
		primaryStage.setTitle(title);
		primaryStage.show();
	}
	
	// ȸ������ �Ϸ�, ��й�ȣ Ȯ�ΰ� ��й�ȣ�� ���ƾ� ��
	public void createDone(ActionEvent ae) throws IOException, SQLException {
		
		
		if(tfd_createPw.getText().equals(tfd_createPw2.getText()) && tfd_create_ID.getText().length() > 4 && !db.checkDuplicate(tfd_create_ID.getText())) { //ȸ������ ����
			execute(new CreateIdImpl());
		
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("���̵� ������ �����Ͽ����ϴ�");
			alert.setContentText("���̵� Ȥ�� ��й�ȣ�� Ȯ���ϼ���.");
			alert.showAndWait();
		}
	}

	
	// ���̵� �ߺ� Ȯ�� ��ư
	public void checkID(ActionEvent ae) throws IOException, SQLException {
		String id = tfd_create_ID.getText();

		if(db.checkDuplicate(id)) {
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
