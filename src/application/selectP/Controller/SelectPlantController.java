package application.selectP.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class SelectPlantController implements Initializable {
	@FXML
	private Button btn1; // Cherry Tomato
	@FXML
	private Button btn2; // Sunflower

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// ������ ��ư�� �׼� �ֱ�
		btn1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				handleBtn1Action(e);
			}
		});
		btn2.setOnAction(event -> handleBtn2Action(event));
	}

	public void handleBtn1Action(ActionEvent e) {
		System.out.println("1"); // Cherry Tomato ���ý� '1' ���
	}

	public void handleBtn2Action(ActionEvent e) {
		System.out.println("2"); // Sunflower ���ý� '2' ���
	}

}
