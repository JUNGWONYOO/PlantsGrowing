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
	
	
	// 로그인 기능 (DB의 회원가입 정보를 가져올 수 있음)
	public void Login(ActionEvent ae) throws IOException {
			
		if(connect.check(userNameTextField.getText(), enterPasswordField.getText())) {
			
		
			// 매 화면에 빈 생성자와 세트로 다니면 됨
			// 빈 정보에 user id에 따라
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
			primaryStage.setTitle("식물키우기~");
			primaryStage.show();
			
			System.out.println( "id = " + userNameTextField.getText() + " login 완료 [IP = " + userinfo.getIP() + " / port = " + userinfo.getPort() +"]");
			
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
		primaryStage.setScene(scene);
		primaryStage.setTitle("Plants Growing 회원가입");
		primaryStage.show();
		
	}
	
	// 회원가입 완료, 비밀번호 확인과 비밀번호가 같아야 함
	public void createDone(ActionEvent ae) throws IOException {
		
		InetAddress ia = InetAddress.getLocalHost();
		String ip = ia.toString().substring(16,ia.toString().length());
		
		if(tfd_createPw.getText().equals(tfd_createPw2.getText()) && tfd_create_ID.getText().length() > 4 && !connect.checkDuplicate(tfd_create_ID.getText())) {
		
		UserInfo userinfo = new UserInfo(tfd_create_ID.getText(), tfd_createPw.getText(),ip, 9876, 1);	
		
		
		 try {
			 connect.createId(userinfo);
			
		 }catch (Exception e) { 
			 System.out.println("아이디 생성 오류"); 
		 }
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("회원가입 완료");
		alert.setContentText("축하합니다 회원가입 완료되었습니다");
		alert.showAndWait();
			
		Stage primaryStage = (Stage) btn.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Plants Growing 로그인");
		primaryStage.show();
		
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("아이디 생성에 실패하였습니다");
			alert.setContentText("아이디 혹은 비밀번호를 확인하세요.");
			alert.showAndWait();
		}
	}

	
	// 아이디 중복 확인 버튼
	public void checkID(ActionEvent ae) throws IOException {
		String id = tfd_create_ID.getText();

		if(connect.checkDuplicate(id)) {
			Alert checkIdErr = new Alert(AlertType.ERROR);
			checkIdErr.setTitle("아이디 확인");
			checkIdErr.setContentText("중복된 아이디입니다.");
			checkIdErr.showAndWait();
		}
		else {
			Alert checkIdErr = new Alert(AlertType.CONFIRMATION);
			checkIdErr.setTitle("아이디 확인");
			checkIdErr.setContentText("회원가능이 가능합니다.");
			checkIdErr.showAndWait();
		}

	}
	


	
}
