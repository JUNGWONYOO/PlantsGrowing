package application.main.View;
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {

 
 public static void main(String[] args) {
  launch(args);
 }

 @Override
 public void start(Stage stage) throws Exception {
  Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
  Scene scene = new Scene(root);
  String css = this.getClass().getResource("View/css/main.css").toExternalForm();
  scene.getStylesheets().add(css);
  //////////// 아이콘설정
  //Image icon = new Image("icon.png");
  //stage.getIcons().add(icon);
  //stage.setTitle("Plant");
  //stage.setWidth(1000);
  //stage.setHeight(600);
  //stage.setResizable(false);
  //stage.setFullScreen(true);
  
  /////////////// 전체화면 기본값은 esc다
  //stage.setFullScreenExitHint("YOU CAN'T ESCAPE unless you press q");
  //stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));
  
  stage.setScene(scene);
  stage.show();
//  stage.setOnCloseRequest(event -> {
//	  event.consume();
//	  logout(stage); 
//  });
 }
//	public void logout(Stage stage){	
//		
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Logout");
//		alert.setHeaderText("You're about to logout!");
//		alert.setContentText("Do you want to save before exiting?");
//		
//		if (alert.showAndWait().get() == ButtonType.OK){
//			System.out.println("You successfully logged out");
//			stage.close();
//		} 
//	}




}
