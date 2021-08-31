package application.firstLogin.Controller;

import java.io.IOException;
import java.sql.SQLException;
import application.Singletone;
import application.dao.DBConnector;
import application.firstLogin.Users.UserInfo;
import application.main.Controller.ControlInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginImpl implements LoginService {

	DBConnector db = Singletone.getInstance(); // DB연동 생성자
	
	// 로그인
	@Override
	public void execute(TextField userNameTextField, Button btn, PasswordField enterPasswordField, 
						TextField tfd_create_ID, PasswordField tfd_createPw, PasswordField tfd_createPw2) {
		
		try {	
			// 매 화면에 빈 생성자와 세트로 다니면 됨
			// 빈 정보에 user id에 따라
			UserInfo user = new UserInfo();	
			db.loadInfo(user, userNameTextField.getText());
			LoginController.UserList.add(user);
			
			Stage primaryStage = (Stage) btn.getScene().getWindow();
			
			Parent root ;
			FXMLLoader loader;
			String pName = null;
			String css = "null";
			
			//user의 데이터를 바탕으로 화면의 이동을 달리하는 코드
			if(user.getSpecies() == 0) {
				loader = new FXMLLoader(getClass().getResource("../../selectP/View/SelectPlantScene.fxml"));	
			}
			
			else if(user.getSpecies() != 0 && user.getPlantName().equals("null")) {
				loader = new FXMLLoader(getClass().getResource("../../selectP/View/CreateNameScene.fxml"));		
			}
			
			else if(!user.getPlantName().equals("null") && user.getSpecies() == 1 ) {		
				loader = new FXMLLoader(getClass().getResource("../../main/View/main.fxml"));	
				css = this.getClass().getResource("../../main/View/css/main.css").toExternalForm();
				pName = user.getPlantName();
			}
			
			else {
				loader = new FXMLLoader(getClass().getResource("../../main/View/main2.fxml"));
				css = this.getClass().getResource("../../main/View/css/main.css").toExternalForm();
				pName = user.getPlantName();
			}
			
			
			root = loader.load(); // root fxml의 controller를 가져와서 Controller 객체를 불러온 뒤, 다음 view에서도 user의 데이터가 적용될 수 있게끔
			
			if(!user.getPlantName().equals("null")) {
				ControlInterface m;
				if(user.getSpecies() == 1) {
					m = loader.getController();		
					m.setPname(pName);
				}
				if(user.getSpecies() == 2) {
					m = loader.getController();
					m.setPname(pName);
				}
			}
			
			Scene scene = new Scene(root);
			
			
			if(!css.equals("null")) { // 메인화면으로 바로 넘어가는 계정은 css add
				scene.getStylesheets().add(css);
			}
			
			String title =  "PlantsGrowing";
			settingScene(scene, primaryStage, title);
			System.out.println( "id = " + userNameTextField.getText() + " login 완료 [IP = " + user.getIP() + " / port = " + user.getPort() +"]");
		
			//로그인 계정 없거나, 아이디 비번이 맞지않을경우 error
		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void settingScene(Scene scene, Stage primaryStage,String title) {
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
		primaryStage.setTitle(title);
		primaryStage.show();
		
	}

}
