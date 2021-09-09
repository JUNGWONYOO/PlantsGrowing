package application.selectP.Controller;

import java.sql.SQLException;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import javafx.event.ActionEvent;


public interface SPControlInterface  {

	public static UserInfo user = LoginController.UserList.get(0);
	
	public void selectCT(ActionEvent event) throws SQLException;
	public void selectSF(ActionEvent event) throws SQLException;

}
