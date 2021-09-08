package application.firstLogin.Service;

import java.io.IOException;
import java.net.InetAddress;
import application.Singletone;
import application.dao.PlantsGrowingDaoImple;
import application.firstLogin.Users.UserInfo;
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

public class CreateIdImpl implements LoginService {
	PlantsGrowingDaoImple db = Singletone.getInstance(); // DB 싱글턴 생성
	
	// 회원가입
	@Override
	public void execute(TextField userNameTextField, Button btn, PasswordField enterPasswordField,
						TextField tfd_create_ID, PasswordField tfd_createPw, PasswordField tfd_createPw2) {
		
		try {
			
			InetAddress ia = InetAddress.getLocalHost();
			String ip = ia.toString().substring(16,ia.toString().length()); // 나중에 혹시나 활용할 방안이있을까 접속 인원들의 ip주소 저장
			UserInfo userinfo = new UserInfo(tfd_create_ID.getText(), tfd_createPw.getText(),ip, 9876, 1);	

			 try {
				 db.createId(userinfo);
				
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
			
			String title = "Plants Growing 로그인";
			settingScene(scene, primaryStage, title);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void settingScene(Scene scene, Stage primaryStage, String title) {
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
		primaryStage.setTitle(title);
		primaryStage.show();
	}

}
