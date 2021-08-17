package application.firstLogin.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Vector;

import application.dao.DBConnector;
import application.firstLogin.Users.UserInfo;
import application.main.Controller.Controller;
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
	
	DBConnector connect = new DBConnector(); // DB연동 생성자

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
	
	
	// 로그인 기능 (DB의 회원가입 정보를 가져올 수 있음)
	public void Login(ActionEvent ae) throws IOException, SQLException {
			
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
			
			//user의 데이터를 바탕으로 화면의 이동을 달리하는 코드
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
			
			
			root = loader.load(); // root fxml의 controller를 가져와서 Controller 객체를 불러온 뒤, 다음 view에서도 user의 데이터가 적용될 수 있게끔
		
			if(!userinfo.getPlantName().equals("null")) {
				m = loader.getController();
				String pName = userinfo.getPlantName();
				m.setPname(pName);
			}

			Scene scene = new Scene(root);
			
			
			if(!css.equals("null")) { // 메인화면으로 바로 넘어가는 계정은 css add
				scene.getStylesheets().add(css);
			}
			
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
			primaryStage.setTitle("식물키우기~");
			primaryStage.show();
			
			System.out.println( "id = " + userNameTextField.getText() + " login 완료 [IP = " + userinfo.getIP() + " / port = " + userinfo.getPort() +"]");
		
			//로그인 계정 없거나, 아이디 비번이 맞지않을경우 error
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
	public void createDone(ActionEvent ae) throws IOException, SQLException {
		
		InetAddress ia = InetAddress.getLocalHost();
		String ip = ia.toString().substring(16,ia.toString().length()); // 나중에 혹시나 활용할 방안이있을까 접속 인원들의 ip주소 저장
		
		if(tfd_createPw.getText().equals(tfd_createPw2.getText()) && tfd_create_ID.getText().length() > 4 && !connect.checkDuplicate(tfd_create_ID.getText())) { //회원가입 조건
		
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
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
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
	public void checkID(ActionEvent ae) throws IOException, SQLException {
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
