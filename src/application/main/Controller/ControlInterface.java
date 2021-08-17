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
	// 이전화면에서 PlantName 넘어오게끔 만드는 메서드
	// 레벨도 동시에 넘어오면서 바로 레벨 별 사진 등장
	public void setPname(String pName);
	
	// 레벨업, 운세 등장시 dataRefresh
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
	
	// 버튼클릭 시 랜덤 확률로 포츈쿠키 등장
	public void fortunecookie() throws SQLException;
	
	//light , water 버튼을 누를때마다 수행되는 메서드
	//운세, 레벨업, 챗버블 등장
	public void buttonPressing(int eachCount, String element, PauseTransition pause) throws SQLException;

	////////////////////////// 4가지 버튼 액션
	
	public void audioClipping();
	public void waterAction(ActionEvent e) throws SQLException;

	public void lightAction(ActionEvent e) throws SQLException;
		
	// 사랑버튼
	public void loveAction(ActionEvent e) throws SQLException;

	public void snailAction(ActionEvent e) throws SQLException;

	public void goPrevious(ActionEvent e) throws IOException;

	//127.0.0.1
	//서버 on > thread를통해 > server 전달 메시지 수신
	@FXML
	public void serverOn(MouseEvent event);

	// 서버 종료 버튼
	@FXML
	public void serverOff(MouseEvent event);
		
	
	// 전송 버튼
	// sending 메서드의 매개변수로 전달할 메시지를 담아준다.
	public void chatAction(ActionEvent e);
	
	// 서버 메시지 전송 메서드
	// send (thread) > thread(serverClient in) > thread(serverClient out) > receive
	// 입력된 메시지를 서버에 전달(UTF-8)
	public void sending(String message);
		
	//클라이언트 프로그램 종료 메서드
	public void stopClient();

	// 날씨 불러오기 버튼
	// 날씨를 크롤해와서 lbl text를 set 해줌.
	public void weatherAction(ActionEvent e);
		

}
