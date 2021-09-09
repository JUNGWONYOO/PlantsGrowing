package application.main.Service;

import java.sql.SQLException;

import application.Singletone;
import application.dao.DBConnector;
import application.main.Controller.ControlInterface;
import application.main.Controller.Controller;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LightButtonImpl extends MainButtonService{

	DBConnector db = Singletone.getInstance();
	
	// �� ���� �޼���
	@Override
	public void execute(int loveCount, int lightCount, int waterCount, int snailCount, int level, ImageView imageView1, ImageView imageView2, ImageView plantView, int controller) {
		
		imageView1.setImage(lightEffect1); // �̹��� �� ����ȭ
		
		showChatBubble(lightCount, imageView1, imageView2); // ê���� ����ȭ
		giveFortunecookie();

		if (20 < waterCount + lightCount + snailCount) {
			refreshData(loveCount, lightCount, waterCount, snailCount);
		}
		else if (20 >= waterCount + lightCount + snailCount) {

			System.out.println(lightCount); // �α� ���
			ControlInterface.user.setTanning(lightCount); // ����Ʈ ī��Ʈ ���� ������Ʈ 
			
			try {
				db.updateAll(ControlInterface.user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		leveling(loveCount, lightCount, waterCount, snailCount, level, plantView, controller);
		
		
	}
	
	// ������ ���ذ� ������ �޼���
	@Override
	public void leveling(int loveCount, int lightCount, int waterCount, int snailCount, int level, ImageView myPlantView, int controller) {
		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
			
			Alert oonseAlert = new Alert(AlertType.INFORMATION);
			oonseAlert.setTitle("���� ��!");
			if(level < 3) {
				level++;
				setImagePerLevel(myPlantView, level, controller);
				ControlInterface.user.setLevel(level);
				try {
					db.updateAll(ControlInterface.user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				refreshData(loveCount, lightCount, waterCount, snailCount);

				oonseAlert.setContentText("����"+ level + " �� �����̽��ϴ�!");
				oonseAlert.showAndWait();
			}else if(level >= 3) {	
				refreshData(loveCount, lightCount, waterCount, snailCount);
				
				oonseAlert.setTitle("����");
				oonseAlert.setContentText("�Ĺ��� �� �ڶ����!!!! �Ĺ��� �Բ� ��ſ� ���� ��������");
				oonseAlert.showAndWait();
				
			}
			

		} 
		
	}

	// ê���� �����ֱ�
	@Override
	public void showChatBubble(int count , ImageView imageView1, ImageView chatBubbleView) {
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(a -> imageView1.setImage(null));
		pause.play();
		
		if (count == 2) {

			chatBubbleView.setImage(chatBubble1);
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		} else if (count == 3) {

			chatBubbleView.setImage(chatBubble2);
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		} else if (count == 4) {

			chatBubbleView.setImage(chatBubble3);
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		}
		
	}


}
