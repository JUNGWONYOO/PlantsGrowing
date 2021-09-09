package application.firstLogin.Service;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public interface LoginService {
	
	// 실행 메서드
	public void executeMemberAccess(TextField userNameTextField, Button btn, PasswordField enterPasswordField, TextField tfd_create_ID, PasswordField tfd_createPw, PasswordField tfd_createPw2);
	
	// 씬 세팅 메서드
	public void settingScene(Scene scene, Stage primaryStage,String title);
	
}
