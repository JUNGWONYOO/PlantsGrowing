package application.selectP.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Scene2Controller {

	@FXML
	Label nameLabel;

	public void displayName(String username) {
		nameLabel.setText("Plant " + username);
	}

}
