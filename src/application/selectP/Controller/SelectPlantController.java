package application.selectP.Controller;

import java.sql.SQLException;
import java.util.Vector;
import application.Singletone;
import application.dao.PlantsGrowingDaoImple;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import application.selectP.Service.SelectCTImple;
import application.selectP.Service.SelectSFImple;
import application.selectP.Service.SelectService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class SelectPlantController implements SPControlInterface {

	@FXML
	private AnchorPane scenePane;
	@FXML
	private Button ct1, sf1;

	UserInfo user = LoginController.UserList.get(0);
	PlantsGrowingDaoImple db = Singletone.getInstance();

	public static Vector<UserInfo> UserList = new Vector<UserInfo>();

	private void executeSelectP(SelectService selectService) {
		selectService.executeSelectP(scenePane, ct1, sf1);
	}

	// 방울토마토 버튼 액션
	public void selectCT(ActionEvent event) throws SQLException {
		executeSelectP(new SelectCTImple());
	}

	// 해바라기 버튼 액션
	public void selectSF(ActionEvent event) throws SQLException {
		executeSelectP(new SelectSFImple());
	}
}
