package application.selectP.Service;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public interface SelectService {

	// ��ư Ŭ�� �� ���� �޼���
	public void execute(AnchorPane scenePane, Button ct1, Button sf1);

	// �� ���� �޼���
	public void settingScene(Scene scene, Stage primaryStage, String title);

}
