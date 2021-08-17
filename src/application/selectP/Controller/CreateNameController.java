package application.selectP.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import application.dao.DBConnector;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import application.main.Controller.Controller;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CreateNameController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private TextField nameTextField;
	@FXML
	private Button btn;
	
	UserInfo user = LoginController.UserList.get(0);
	DBConnector db = new DBConnector();
	
	public void Alert(ActionEvent event) throws IOException, SQLException {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Name Check");
		alert.setHeaderText("[ " + nameTextField.getText() + " ]" + " 입력한 이름이 맞는지 확인해주세요!");
		alert.setContentText("만일, 아니라면 '취소'를 선택하세요.");
		alert.setGraphic(new ImageView("file:src/application/selectP/View/image/alert_icon.png")); // alert 아이콘 이미지 변경

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// DB 추가
			user.setPlantName(nameTextField.getText());
			db.updatePlantName(user);
			
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../main/View/main.fxml"));	
			
			
			root = loader.load();
		
			
			Controller m = loader.getController(); // 메인페이지 닉네임 넘겨주는 코드
			String pName = user.getPlantName();
			//m.setPname(pName);
			
				
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			
			String css = this.getClass().getResource("../../main/View/css/main.css").toExternalForm();
			scene.getStylesheets().add(css);
			stage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
			stage.setScene(scene);
			stage.show();

		}
	}
}