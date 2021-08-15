package application.selectP.Controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Scene1Controller {

	@FXML
	TextField nameTextField;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void login(ActionEvent event) throws IOException {

		String username = nameTextField.getText(); // 식물 이름 입력

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/CreateNameScene2.fxml")); // fxml 불러오기
		root = loader.load();

		Scene2Controller scene2Controller = loader.getController();
		scene2Controller.displayName(username);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
}