package application.main.Service;

import java.sql.SQLException;

import application.Singletone;
import application.dao.DBConnector;
import application.main.Controller.ControlInterface;
import application.main.Controller.Controller;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class MainButtonService {
	Image plantLevel2 = new Image(getClass().getResourceAsStream("../View/css/ct2.png"));
	Image plantLevel3 = new Image(getClass().getResourceAsStream("../View/css/ct3.png"));
	Image chatBubble1 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_smile.png"));
	Image chatBubble2 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_soso.png"));
	Image chatBubble3 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_angery.png"));
	Image snailEffect1 = new Image(getClass().getResourceAsStream("../View/css/snailEffect.png"));
	Image lightEffect1 = new Image(getClass().getResourceAsStream("../View/css/lightEffect.png"));
	Image loveEffect1 = new Image(getClass().getResourceAsStream("../View/css/loveEffect.png"));
	Image waterEffect1 = new Image(getClass().getResourceAsStream("../View/css/waterEffect.png"));
	DBConnector db = Singletone.getInstance();
	
	// ��ư Ŭ�� �� ���� �޼���
	public abstract void execute(int loveCount, int lightCount, int waterCount, int snailCount, int level, ImageView imageView1, ImageView imageView2, ImageView plantView, int controller);
	
	// ���� �ö󰡴� ����
	public abstract void leveling(int loveCount, int lightCount, int waterCount, int snailCount, int level,ImageView myPlantView, int controller);
	
	// ê���� �̹��� ���� ����
	public abstract void showChatBubble(int count , ImageView imageView1, ImageView chatBubbleView);
	
	
	// ������ �̹��� ����
	public void setImagePerLevel(ImageView imageView, int level, int controller) {		
		
		if(controller != 1) {
			plantLevel2 = new Image(getClass().getResourceAsStream("../View/css/sf2.png"));
			plantLevel3 = new Image(getClass().getResourceAsStream("../View/css/sf3.png"));
		}
		switch (level) {
		case 2:
			imageView.setImage(plantLevel2);
			break;
		case 3:
			imageView.setImage(plantLevel3);
			break;
		}
	}
	
	// ���� ��Ű ����
	public void giveFortunecookie() {
		int randRate = (int)(Math.random()*10 +1);	
		
		if(randRate < 3) {
			
			Alert oonseAlert = new Alert(AlertType.INFORMATION);
			oonseAlert.setTitle("������ �Ѹ���");
			try {
				oonseAlert.setContentText(db.pickFortune());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			oonseAlert.showAndWait();

		}
	}
	
	// ������ ��������
	public void refreshData(int loveCount, int lightCount, int waterCount, int snailCount) {
		loveCount = 0;
		waterCount = 0;
		lightCount = 0;
		snailCount = 0;
		
		ControlInterface.user.setCaring(0);
		ControlInterface.user.setNutrition(0);
		ControlInterface.user.setTanning(0);
		ControlInterface.user.setWatering(0);
		
		try {
			db.updateAll(ControlInterface.user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
