package application.selectP.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class CreateNameMain extends Application {

	@Override

	public void start(Stage primaryStage) throws Exception {

		Parent root = (Parent) FXMLLoader.load(getClass().getResource("../View/CreateNameScene.fxml")); // �̸� �Է� fxml �ҷ�����
		Scene scene = new Scene(root);
		
		primaryStage.getIcons().add(new Image("file:src/application/selectP/View/image/plant_icon.png")); // ������ �̹��� �ҷ�����
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
