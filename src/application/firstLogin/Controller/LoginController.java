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
	
	DBConnector db = Singletone.getInstance();// DB연동 생성자

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
	
	// UserList 벡터에 보관하면서 , 매 컨트롤러에서 같은 유저를 불러올 수 있게끔 선언
	public static Vector<UserInfo> UserList = new Vector<UserInfo>();
	
	private void execute(LoginService loginService) {
		loginService.execute(userNameTextField, btn, enterPasswordField, tfd_create_ID, tfd_createPw, tfd_createPw2);
	}
	
	// 로그인 기능 (DB의 회원가입 정보를 가져올 수 있음)
	public void Login(ActionEvent ae) throws IOException, SQLException {
		
		if(db.check(userNameTextField.getText(), enterPasswordField.getText())) {		
			execute(new LoginImpl());
			
		}else {		
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("로그인에 실패하였습니다.");
			alert.setContentText("아이디 혹은 비밀번호를 확인하세요.");
			alert.showAndWait();
		}
		
	}
		
	// 회원 가입 페이지로 이동
	public void createNewId(ActionEvent ae) throws IOException{
		
		Stage primaryStage = (Stage) btn.getScene().getWindow();	
		Parent root = FXMLLoader.load(getClass().getResource("../View/CreateId.fxml"));
		Scene scene = new Scene(root);
		
		String title = "Plants Growing 회원가입";
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
		primaryStage.setTitle(title);
		primaryStage.show();
	}
	
	// 회원가입 완료, 비밀번호 확인과 비밀번호가 같아야 함
	public void createDone(ActionEvent ae) throws IOException, SQLException {
		
		
		if(tfd_createPw.getText().equals(tfd_createPw2.getText()) && tfd_create_ID.getText().length() > 4 && !db.checkDuplicate(tfd_create_ID.getText())) { //회원가입 조건
			execute(new CreateIdImpl());
		
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("아이디 생성에 실패하였습니다");
			alert.setContentText("아이디 혹은 비밀번호를 확인하세요.");
			alert.showAndWait();
		}
	}

	
	// 아이디 중복 확인 버튼
	public void checkID(ActionEvent ae) throws IOException, SQLException {
		String id = tfd_create_ID.getText();

		if(db.checkDuplicate(id)) {
			Alert checkIdErr = new Alert(AlertType.ERROR);
			checkIdErr.setTitle("아이디 확인");
			checkIdErr.setContentText("중복된 아이디입니다.");
			checkIdErr.showAndWait();
		}
		else {
			Alert checkIdErr = new Alert(AlertType.CONFIRMATION);
			checkIdErr.setTitle("아이디 확인");
			checkIdErr.setContentText("회원가입이 가능합니다.");
			checkIdErr.showAndWait();
		}

	}
	
}
