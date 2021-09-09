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
	
	// 총 실행 메서드
	@Override
	public void execute(int loveCount, int lightCount, int waterCount, int snailCount, int level, ImageView imageView1, ImageView imageView2, ImageView plantView, int controller) {
		
		imageView1.setImage(lightEffect1); // 이미지 뷰 개별화
		
		showChatBubble(lightCount, imageView1, imageView2); // 챗버블 개별화
		giveFortunecookie();

		if (20 < waterCount + lightCount + snailCount) {
			refreshData(loveCount, lightCount, waterCount, snailCount);
		}
		else if (20 >= waterCount + lightCount + snailCount) {

			System.out.println(lightCount); // 로그 찍기
			ControlInterface.user.setTanning(lightCount); // 라이트 카운트 개수 업데이트 
			
			try {
				db.updateAll(ControlInterface.user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		leveling(loveCount, lightCount, waterCount, snailCount, level, plantView, controller);
		
		
	}
	
	// 레벨업 기준과 레벨업 메서드
	@Override
	public void leveling(int loveCount, int lightCount, int waterCount, int snailCount, int level, ImageView myPlantView, int controller) {
		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
			
			Alert oonseAlert = new Alert(AlertType.INFORMATION);
			oonseAlert.setTitle("레벨 업!");
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

				oonseAlert.setContentText("레벨"+ level + " 로 오르셨습니다!");
				oonseAlert.showAndWait();
			}else if(level >= 3) {	
				refreshData(loveCount, lightCount, waterCount, snailCount);
				
				oonseAlert.setTitle("성공");
				oonseAlert.setContentText("식물이 잘 자랐어요!!!! 식물과 함께 즐거운 삶을 보내봐요");
				oonseAlert.showAndWait();
				
			}
			

		} 
		
	}

	// 챗버블 보여주기
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
