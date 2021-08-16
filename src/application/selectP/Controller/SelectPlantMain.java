package application.selectP.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;


public class SelectPlantMain extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent root = (Parent) FXMLLoader.load(getClass().getResource("../View/SelectPlantScene.fxml")); // fxml 불러오기
		Scene scene = new Scene(root);
		
		primaryStage.getIcons().add(new Image("file:src/application/selectP/View/image/plant_icon.png")); // Icon Image 불러오기
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
