package application.selectP.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Scene2Controller {

	@FXML
	Label nameLabel;

	public void displayName(String username) {
<<<<<<< HEAD
		nameLabel.setText(username + "  ¢¾"); 
=======
		nameLabel.setText("Plant " + username); 
>>>>>>> cc09892a977f8073c070180902c52f44fddf30ca
	}

}
