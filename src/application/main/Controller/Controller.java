package application.main.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.dao.DBConnector;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import application.main.weather.weatherCrawl;
import application.main.weather.weatherVO;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.control.TextArea;

public class Controller implements Initializable {

	/////////////// ImageView = 그림 틀
	@FXML
	private ImageView myPlantView, waterEffect, chatBubbleView, lightEffect;
	@FXML
	private Button btn_play, btn_pause, btn_water, btn_sun, btn_heart, btn_snail, btn_weather, btnShowSlide,
			btnHideSlide, btn_serverOn, btn_serverOff;
	@FXML 
	private Label lbl_plantName, lbl_wthr_location, lbl_wthr_temperature, lbl_wthr_status;
	
	@FXML
	private AnchorPane scenePane;
	
	
	private File directory;
	private File[] files;
	private ArrayList<File> songs;

	// db, 서버 연동용 유저리스트와 소켓
	UserInfo user = LoginController.UserList.get(0);
	DBConnector db = new DBConnector();
	Socket socket;
	

	private Media bgm;
	private MediaPlayer mP;
	
	// user 객체에 저장되어있는 점수와 레벨을 받아오는 변수
	private int loveCount = user.getCaring();
	private int lightCount = user.getTanning();
	private int waterCount = user.getWatering();
	private int snailCount = user.getNutrition();
	private int level = user.getLevel();
	private int randRate = 0;

	@FXML
	private TextField TextField;
	@FXML
	private TextArea TextArea;
	@FXML
	private AnchorPane paneslide;
	
	
	
	// 이전화면에서 PlantName 넘어오게끔 만드는 메서드
	// 레벨도 동시에 넘어오면서 바로 레벨 별 사진 등장
	public void setPname(String pName) {
		lbl_plantName.setText(user.getPlantName());
		if(user.getLevel()>=3) {
			myPlantView.setImage(plantLevel3);
		}
		else if(user.getLevel() == 2) {
			myPlantView.setImage(plantLevel2);
		}
	}
	
	// 레벨업, 운세 등장시 dataRefresh
	public void dataRefresh() throws SQLException {
		
		loveCount = 0;
		waterCount = 0;
		lightCount = 0;
		snailCount = 0;
		
		user.setCaring(0);
		user.setNutrition(0);
		user.setTanning(0);
		user.setWatering(0);
		db.updateAll(user);
	
	}
	
	
	/////////////// 레벨별 식물, 효과, 상태 이미지
	Image plantLevel2 = new Image(getClass().getResourceAsStream("../View/css/sf2.png"));
	Image plantLevel3 = new Image(getClass().getResourceAsStream("../View/css/sf3.png"));
	Image waterEffect1 = new Image(getClass().getResourceAsStream("../View/css/waterEffect.png"));
	Image lightEffect1 = new Image(getClass().getResourceAsStream("../View/css/lightEffect.png"));
	Image chatBubble1 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_smile.png"));
	Image chatBubble2 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_soso.png"));
	Image chatBubble3 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_angery.png"));
	
	
	
	@FXML
	private void hideSlide(MouseEvent event) {
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.4));
		slide.setNode(paneslide);
		slide.setToX(700);
		slide.play();

		paneslide.setTranslateX(0);
	}

	@FXML
	private void showSlide(MouseEvent event) {
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.4));
		slide.setNode(paneslide);
		slide.setToX(0);
		slide.play();

		paneslide.setTranslateX(0);
	}

	/////////////// 브금 및 효과음
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		songs = new ArrayList<File>();
		directory = new File("music");
		files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				songs.add(file);
			}
		}

		{
			paneslide.setTranslateX(650);

		}

		/////////////////// 브금 및 효과음
		bgm = new Media(songs.get(0).toURI().toString());
		mP = new MediaPlayer(bgm);

	}

	public void playMedia(ActionEvent e) {
		mP.setVolume(0.1);
		mP.setOnEndOfMedia(new Runnable() {
			public void run() {
				mP.seek(Duration.ZERO);
			}
		});
		mP.play();
	}

	public void pauseMedia(ActionEvent e) {
		mP.pause();
	}
	
	
	// 버튼클릭 시 랜덤 확률로 포츈쿠키 등장
	public void fortunecookie() throws SQLException {

		randRate = (int)(Math.random()*10 +1);	
		
		if(randRate < 3) {
			
			Alert oonseAlert = new Alert(AlertType.INFORMATION);
			oonseAlert.setTitle("오늘의 한마디");
			oonseAlert.setContentText(db.pickFortune());
			oonseAlert.showAndWait();
			
			
		}
	}
	
	//light , water 버튼을 누를때마다 수행되는 메서드
	//운세, 레벨업, 챗버블 등장
	public void buttonPressing(int eachCount, String element) throws SQLException {

		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(a -> waterEffect.setImage(null));
		pause.play();

		int numberOfClicks = 0;
		String s = element;
		if (s.equals("water")) {
			numberOfClicks = 3;
		} else if (s.equals("light")) {
			numberOfClicks = 2;
		}
		
		if (eachCount == numberOfClicks) {

			chatBubbleView.setImage(chatBubble1);
			pause = new PauseTransition(Duration.seconds(2));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		} else if (eachCount == numberOfClicks+1) {

			chatBubbleView.setImage(chatBubble2);
			pause = new PauseTransition(Duration.seconds(2.25));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		} else if (eachCount == numberOfClicks+2) {

			chatBubbleView.setImage(chatBubble3);
			pause = new PauseTransition(Duration.seconds(2.5));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		} else if (20 < waterCount + lightCount + snailCount) {
			dataRefresh();
		} 
		
		// 레벨업 조건
		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
			
			Alert oonseAlert = new Alert(AlertType.INFORMATION);
			oonseAlert.setTitle("레벨 업!");
			if(level <= 3) {
				level++;
				user.setLevel(level);
				db.updateAll(user);
				dataRefresh();

				oonseAlert.setContentText("레벨"+ level + " 로 오르셨습니다!");
				oonseAlert.showAndWait();
			}else if(level > 3) {	
				dataRefresh();
				
				oonseAlert.setTitle("성공");
				oonseAlert.setContentText("식물이 잘 자랐어요!!!! 식물과 함께 즐거운 삶을 보내봐요");
				oonseAlert.showAndWait();
				
			}

		} 
		
		switch (level) {
		case 2:
			myPlantView.setImage(plantLevel2);
			break;
		case 3:
			myPlantView.setImage(plantLevel3);
			break;
		}
		
	}

	////////////////////////// 4가지 버튼 액션
	public void waterAction(ActionEvent e) throws SQLException {

		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();

		waterCount++;
		waterEffect.setImage(waterEffect1);
		
		fortunecookie();
		buttonPressing(waterCount, "water");
		System.out.println(waterCount);
		
		
		user.setWatering(waterCount);
		db.updateAll(user);

		
	}
	
	
	public void lightAction(ActionEvent e) throws SQLException {
		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		
		lightEffect.setImage(lightEffect1);

		lightCount++;
		System.out.println(lightCount);
		
		fortunecookie();
		buttonPressing(lightCount, "light");
		
		user.setTanning(lightCount);
		db.updateAll(user);
		
		
	}
	// 사랑버튼
	public void loveAction(ActionEvent e) throws SQLException {

		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		
		loveCount++;
		
		System.out.println(loveCount);
		chatBubbleView.setImage(chatBubble1);
		
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(a -> chatBubbleView.setImage(null));
		pause.play();

		fortunecookie();
		
		user.setCaring(loveCount);
		db.updateAll(user);

		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
			level++;
			dataRefresh();
			user.setLevel(level);
			db.updateAll(user);
			
		} else if (10 < waterCount + lightCount + snailCount) {
			dataRefresh();
			
		}
		
///////////////////레벨 오르면 이미지 변경
		switch (level) {
		case 2:
			myPlantView.setImage(plantLevel2);
			break;
		case 3:
			myPlantView.setImage(plantLevel3);
			break;
		
		}
		
	}

	public void snailAction(ActionEvent e) throws SQLException {
		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		
		snailCount++;
		
		fortunecookie();
		
		user.setNutrition(snailCount);
		db.updateAll(user);
		
		if (waterCount == 3 && lightCount == 2 && loveCount > 2 && snailCount == 1) {
			
			level++;
			dataRefresh();
			user.setLevel(level);
			db.updateAll(user);
			
		} else if (snailCount == 2) {
			
			snailCount = 1;
			System.out.println("저 이제 배불러요");
			chatBubbleView.setImage(chatBubble2);
			PauseTransition pause = new PauseTransition(Duration.seconds(2));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();
			
		} else if (10 < waterCount + lightCount + snailCount) {
			
			dataRefresh();
			
		} 
///////////////////레벨 오르면 이미지 변경
		switch (level) {
		case 2:
			myPlantView.setImage(plantLevel2);
			break;
		case 3:
			myPlantView.setImage(plantLevel3);
			break;

		}
		
	}
	//127.0.0.1
	//서버 on > thread를통해 > server 전달 메시지 수신
	@FXML
	private void serverOn(MouseEvent event) {
		btn_serverOn.setVisible(false);
		btn_serverOff.setVisible(true);
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket("127.0.0.1",user.getPort());
					System.out.println("[소켓 연결]");
					receive();	
				} catch (Exception e) {
					if(!socket.isClosed()) {
						try {
							socket.close();
							System.out.println("[서버접속 실패]");
							Platform.exit();
						} catch (IOException e1) {
						
							e1.printStackTrace();
						}
					}
				}
			}
		};
		thread.start();
	}
	
	// 서버 종료 버튼
	@FXML
	private void serverOff(MouseEvent event) {
		btn_serverOn.setVisible(true);
		btn_serverOff.setVisible(false);
		stopClient();
	}

	// 서버에서 수신해오는 메시지
	// send (thread) > thread > thread > receive
	// receive하고 UTF-8로 TextArea로 append
	public void receive() {
		while(true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[1024];
				int length = in.read(buffer);
				while(length == -1) throw new IOException();
				String message = new String(buffer, 0 , length, "UTF-8");			
				System.out.println("[클라이언트 메시지 수신 성공] : " +  message);
				Platform.runLater(()->{
					TextArea.appendText(message);
				});
			}catch (Exception e) {
				stopClient();
				break;
			}
		}
		
	}
	
	// 전송 버튼
	// sending 메서드의 매개변수로 전달할 메시지를 담아준다.
	public void chatAction(ActionEvent e) {
		
		sending(user.getPlantName() + " : " + TextField.getText() + "\n");
		System.out.println("[클라이언트 메시지 전송 성공] : " + user.getPlantName() +" : "+  TextField.getText());
		TextField.setText("");
		TextField.requestFocus();
		
	}
	
	// 서버 메시지 전송 메서드
	// send (thread) > thread(serverClient in) > thread(serverClient out) > receive
	// 입력된 메시지를 서버에 전달(UTF-8)
	public void sending(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
					
				}catch (Exception e) {
					
					if(socket != null && !socket.isClosed()) {
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}	
			}
		};
		thread.start();
	}
	
	//클라이언트 프로그램 종료 메서드
	public void stopClient() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
				System.out.println("[클라이언트 접속 종료]");
			}
		} catch (Exception e) {
			System.out.println("[클라이언트 종료 오류]");
			e.printStackTrace();
		}
		
		
	}
	
	// 날씨 불러오기 버튼
	// 날씨를 크롤해와서 lbl text를 set 해줌.
	public void weatherAction(ActionEvent e) {
		weatherVO wVO = new weatherVO();
		try {
			weatherCrawl.Crawling(wVO);
			
			lbl_wthr_location.setText(wVO.getLocation());
			lbl_wthr_temperature.setText(String.valueOf(wVO.getTemperature()));
			lbl_wthr_status.setText(wVO.getStatus());
		} catch (IOException ie) {
			
			ie.printStackTrace();
		}

	}

}
