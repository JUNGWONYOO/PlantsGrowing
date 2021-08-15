package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {

	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("firstLogin/View/login.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		primaryStage.setTitle("Plants Growing ·Î±×ÀÎ");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
