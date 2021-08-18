package application.main.Controller;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;


public interface ControlInterface  {
	// ����ȭ�鿡�� PlantName �Ѿ���Բ� ����� �޼���
	// ������ ���ÿ� �Ѿ���鼭 �ٷ� ���� �� ���� ����
	public void setPname(String pName);
	
	// ������, � ����� dataRefresh
	public void dataRefresh() throws SQLException;
	
	@FXML
	public void hideSlide(MouseEvent event);

	@FXML
	public void showSlide(MouseEvent event);

	public void fadeAction();
	
	public void helpAction(ActionEvent e);
	
	public void helpPrev(ActionEvent e);

	public void helpTo2Action(ActionEvent e);

	public void helpTo3Action(ActionEvent e);

	public void helpTo4Action(ActionEvent e);

	public void helpTo5Action(ActionEvent e);
	
	public void helpTo6Action(ActionEvent e);
	public void helpTo1Prev(ActionEvent e);

	public void helpTo2Prev(ActionEvent e);

	public void helpTo3Prev(ActionEvent e);
	public void helpTo4Prev(ActionEvent e);

	
	public void playMedia(ActionEvent e);

	public void pauseMedia(ActionEvent e);
	
	// ��ưŬ�� �� ���� Ȯ���� ������Ű ����
	public void fortunecookie() throws SQLException;
	
	//light , water ��ư�� ���������� ����Ǵ� �޼���
	//�, ������, ê���� ����
	public void buttonPressing(int eachCount, String element, PauseTransition pause) throws SQLException;

	////////////////////////// 4���� ��ư �׼�
	
	public void audioClipping();
	public void waterAction(ActionEvent e) throws SQLException;

	public void lightAction(ActionEvent e) throws SQLException;
		
	// �����ư
	public void loveAction(ActionEvent e) throws SQLException;

	public void snailAction(ActionEvent e) throws SQLException;

	public void goPrevious(ActionEvent e) throws IOException;

	//127.0.0.1
	//���� on > thread������ > server ���� �޽��� ����
	@FXML
	public void serverOn(MouseEvent event);

	// ���� ���� ��ư
	@FXML
	public void serverOff(MouseEvent event);
		
	
	// ���� ��ư
	// sending �޼����� �Ű������� ������ �޽����� ����ش�.
	public void chatAction(ActionEvent e);
	
	// ���� �޽��� ���� �޼���
	// send (thread) > thread(serverClient in) > thread(serverClient out) > receive
	// �Էµ� �޽����� ������ ����(UTF-8)
	public void sending(String message);
		
	//Ŭ���̾�Ʈ ���α׷� ���� �޼���
	public void stopClient();

	// ���� �ҷ����� ��ư
	// ������ ũ���ؿͼ� lbl text�� set ����.
	public void weatherAction(ActionEvent e);
		

}
