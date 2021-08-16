package application.selectP.Controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
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

	public void login(ActionEvent event) throws IOException { //Cherry tomato button을 클릭했을 때

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select Page");
		alert.setHeaderText("Cherry Tomato" + "를 선택한 게 맞나요?");
		alert.setContentText("만일, 아니라면 '취소'를 선택하세요.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("../../selectP/View/CreateNameScene.fxml")); // 이름 입력 페이지로 이동
			System.out.println("1"); // '1'출력
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	public void logout(ActionEvent event) throws IOException { //Sunflower button을 클릭했을 때

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select Page");
		alert.setHeaderText("Sunflower" + "를 선택한 게 맞나요?");
		alert.setContentText("만일, 아니라면 '취소'를 선택하세요.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("../../selectP/View/CreateNameScene.fxml")); // 이름 입력 페이지로 이동
			System.out.println("2"); // '2'출력
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}
}