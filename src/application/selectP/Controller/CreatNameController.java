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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CreatNameController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private TextField nameTextField;
	@FXML
	private Button btn;

	public void login(ActionEvent event) throws IOException {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Name Check");
		alert.setHeaderText("[ " + nameTextField.getText() + " ]" + " 입력한 이름이 맞는지 확인해주세요!");
		alert.setContentText("만일, 아니라면 '취소'를 선택하세요.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("../../selectP/View/SelectPlantScene.fxml"));
			// 유저가 'OK'를 선택하면 '메인 페이지'로 이동 -> 메인 페이지 fxml로 수정해야함!!!!!!!!!!!!!

			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}
}