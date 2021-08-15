package application.selectP.Controller;

import java.io.IOException;

import application.dao.DBConnector;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Scene1Controller {
	DBConnector connector = new DBConnector();
	@FXML
	TextField nameTextField;
	
	@FXML
	Button loginButton;
	
	

	public void login(ActionEvent event) throws IOException {

		String username = nameTextField.getText();
		
		UserInfo user = LoginController.UserList.get(0);
		user.setPlantName(username);
		System.out.println(user.getId());
		System.out.println(user.getPlantName());
		connector.updatePlantName(user);
		
		Stage stage = (Stage) loginButton.getScene().getWindow();
		stage.close();
		
		Stage primaryStage = new Stage();	
		Parent root = FXMLLoader.load(getClass().getResource("../../main/View/main1.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Plants Growing 회원가입");
		primaryStage.show();
	
	}
}