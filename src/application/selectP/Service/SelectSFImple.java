package application.selectP.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import application.Singletone;
import application.dao.DBConnector;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import application.main.Controller.ControlInterface;
import application.selectP.Controller.SPControlInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SelectSFImple implements SelectService {
	// DB ����
	DBConnector db = Singletone.getInstance();

	// Sunflower ���� �� ���� �޼ҵ�
	@Override
	public void execute(AnchorPane scenePane, Button ct1, Button sf1) {

		try {
			UserInfo user = LoginController.UserList.get(0);

			Alert selectCTAlert = new Alert(AlertType.CONFIRMATION);
			selectCTAlert.setHeaderText("[ Sunflower ]" + "�� ������ �� �³���?");
			selectCTAlert.setContentText("����, �ƴ϶�� '���'�� �����ϼ���.");
			selectCTAlert.setGraphic(new ImageView("file:src/application/selectP/View/alert_icon.png"));

			Optional<ButtonType> result = selectCTAlert.showAndWait();

			if (result.get() == ButtonType.OK) {
				Parent root = FXMLLoader.load(getClass().getResource("../../selectP/View/CreateNameScene.fxml"));
				System.out.println("2");

				user.setSpecies(2);
				db.updatePlantSpecies(user);

				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) sf1.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		} catch (Exception e) {
			System.out.println("�Ĺ� ���� ����");
		}
	}

	// �⺻ ����
	@Override
	public void settingScene(Scene scene, Stage primaryStage, String title) {
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
		primaryStage.setTitle(title);
		primaryStage.show();
	}

}
