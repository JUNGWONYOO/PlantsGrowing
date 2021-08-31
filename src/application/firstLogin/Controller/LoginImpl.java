package application.firstLogin.Controller;

import java.io.IOException;
import java.sql.SQLException;
import application.Singletone;
import application.dao.DBConnector;
import application.firstLogin.Users.UserInfo;
import application.main.Controller.ControlInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginImpl implements LoginService {

	DBConnector db = Singletone.getInstance(); // DB���� ������
	
	// �α���
	@Override
	public void execute(TextField userNameTextField, Button btn, PasswordField enterPasswordField, 
						TextField tfd_create_ID, PasswordField tfd_createPw, PasswordField tfd_createPw2) {
		
		try {	
			// �� ȭ�鿡 �� �����ڿ� ��Ʈ�� �ٴϸ� ��
			// �� ������ user id�� ����
			UserInfo user = new UserInfo();	
			db.loadInfo(user, userNameTextField.getText());
			LoginController.UserList.add(user);
			
			Stage primaryStage = (Stage) btn.getScene().getWindow();
			
			Parent root ;
			FXMLLoader loader;
			String pName = null;
			String css = "null";
			
			//user�� �����͸� �������� ȭ���� �̵��� �޸��ϴ� �ڵ�
			if(user.getSpecies() == 0) {
				loader = new FXMLLoader(getClass().getResource("../../selectP/View/SelectPlantScene.fxml"));	
			}
			
			else if(user.getSpecies() != 0 && user.getPlantName().equals("null")) {
				loader = new FXMLLoader(getClass().getResource("../../selectP/View/CreateNameScene.fxml"));		
			}
			
			else if(!user.getPlantName().equals("null") && user.getSpecies() == 1 ) {		
				loader = new FXMLLoader(getClass().getResource("../../main/View/main.fxml"));	
				css = this.getClass().getResource("../../main/View/css/main.css").toExternalForm();
				pName = user.getPlantName();
			}
			
			else {
				loader = new FXMLLoader(getClass().getResource("../../main/View/main2.fxml"));
				css = this.getClass().getResource("../../main/View/css/main.css").toExternalForm();
				pName = user.getPlantName();
			}
			
			
			root = loader.load(); // root fxml�� controller�� �����ͼ� Controller ��ü�� �ҷ��� ��, ���� view������ user�� �����Ͱ� ����� �� �ְԲ�
			
			if(!user.getPlantName().equals("null")) {
				ControlInterface m;
				if(user.getSpecies() == 1) {
					m = loader.getController();		
					m.setPname(pName);
				}
				if(user.getSpecies() == 2) {
					m = loader.getController();
					m.setPname(pName);
				}
			}
			
			Scene scene = new Scene(root);
			
			
			if(!css.equals("null")) { // ����ȭ������ �ٷ� �Ѿ�� ������ css add
				scene.getStylesheets().add(css);
			}
			
			String title =  "PlantsGrowing";
			settingScene(scene, primaryStage, title);
			System.out.println( "id = " + userNameTextField.getText() + " login �Ϸ� [IP = " + user.getIP() + " / port = " + user.getPort() +"]");
		
			//�α��� ���� ���ų�, ���̵� ����� ����������� error
		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void settingScene(Scene scene, Stage primaryStage,String title) {
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:src/application/main/View/css/menu_plant_icon.png"));
		primaryStage.setTitle(title);
		primaryStage.show();
		
	}

}
