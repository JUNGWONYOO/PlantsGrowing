package application.selectP.Controller;

import java.io.IOException;
import java.util.Optional;

import application.dao.DBConnector;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SelectPlantController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private AnchorPane scenePane;
	@FXML
	private Button ct1;
	@FXML
	private Button sf1;
	
	UserInfo user = LoginController.UserList.get(0);
	DBConnector db = new DBConnector();
	
	public void login(ActionEvent event) throws IOException { //Cherry tomato button�� Ŭ������ ��

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select Page");
		alert.setHeaderText("Cherry Tomato" + "�� ������ �� �³���?");
		alert.setContentText("����, �ƴ϶�� '���'�� �����ϼ���.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("../../selectP/View/CreateNameScene.fxml")); // �̸� �Է� �������� �̵�
			System.out.println("1"); // '1'���
			user.setSpecies(1);
			db.updatePlantSpecies(user);
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	public void logout(ActionEvent event) throws IOException { //Sunflower button�� Ŭ������ ��

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select Page");
		alert.setHeaderText("Sunflower" + "�� ������ �� �³���?");
		alert.setContentText("����, �ƴ϶�� '���'�� �����ϼ���.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("../../selectP/View/CreateNameScene.fxml")); // �̸� �Է� �������� �̵�
			System.out.println("2"); // '2'���
			user.setSpecies(2);
			db.updatePlantSpecies(user);
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}
}