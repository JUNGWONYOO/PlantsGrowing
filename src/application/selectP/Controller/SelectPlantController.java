package application.selectP.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class SelectPlantController implements Initializable {

    @FXML
    private ComboBox comb;

    @FXML
    private Label label;

    @FXML
    void Select(ActionEvent event) {

    	String s = comb.getSelectionModel().getSelectedItem().toString();
    	label.setText(s);
    }

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ObservableList<String> list = FXCollections.observableArrayList("Cherry Tomato", "Sunflower");
		comb.setItems(list);

	}

}
