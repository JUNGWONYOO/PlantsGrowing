package application.selectP.Service;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public interface SelectService {

	// 버튼 클릭 시 실행 메서드
	public void execute(AnchorPane scenePane, Button ct1, Button sf1);

	// 씬 세팅 메서드
	public void settingScene(Scene scene, Stage primaryStage, String title);

}
