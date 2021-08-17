package application.selectP.Controller;

import java.io.IOException;
import java.sql.SQLException;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	public void Alert1(ActionEvent event) throws IOException, SQLException { //Cherry tomato button�� Ŭ������ ��

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select Page");
		alert.setHeaderText("Cherry Tomato" + "�� ������ �� �³���?");
		alert.setContentText("����, �ƴ϶�� '���'�� �����ϼ���.");
		alert.setGraphic(new ImageView("file:src/application/selectP/View/image/alert_icon.png")); // alert ������ �̹��� ����
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("../../selectP/View/CreateNameScene.fxml")); // �̸� �Է� �������� �̵�
			System.out.println("1"); // '1'���
			
			// user ������ ������Ʈ�ϰ� db�� plantSpecies ������Ʈ
			user.setSpecies(1);
			db.updatePlantSpecies(user);
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	public void Alert2(ActionEvent event) throws IOException, SQLException { //Sunflower button�� Ŭ������ ��

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select Page");
		alert.setHeaderText("Sunflower" + "�� ������ �� �³���?");
		alert.setContentText("����, �ƴ϶�� '���'�� �����ϼ���.");
		alert.setGraphic(new ImageView("file:src/application/selectP/View/image/alert_icon.png")); // alert ������ �̹��� ����
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("../../selectP/View/CreateNameScene.fxml")); // �̸� �Է� �������� �̵�
			System.out.println("2"); // '2'���
			
			// user ������ ������Ʈ�ϰ� db�� plantSpecies ������Ʈ
			user.setSpecies(2);
			db.updatePlantSpecies(user);
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}
}