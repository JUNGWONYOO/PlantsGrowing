package application.main.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
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

	// db, 서버 연동용 유저리스트/ 소켓
	UserInfo user = LoginController.UserList.get(0);
	DBConnector db = new DBConnector();
	Socket socket;
	

	private Media bgm;
	private MediaPlayer mP;
	

	private int loveCount = user.getCaring();
	private int lightCount = user.getTanning();
	private int waterCount = user.getWatering();
	private int snailCount = user.getNutrition();
	private int level = user.getLevel();
	private int randRate = 0;

	@FXML
	private TextField TextField;
	@FXML
	private javafx.scene.control.TextArea TextArea;
	@FXML
	private AnchorPane paneslide;
	
	
	
	// 이전화면에서 PlantName 넘어오게끔 만드는 메서드
	public void setPname(String pName) {
		lbl_plantName.setText(user.getPlantName());
	}
	
	// 레벨업, 운세 등장시 dataRefresh
	public void dataRefresh() {
		
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
	public void fortunecookie() {

		randRate = (int)(Math.random()*10 +1);	
		
		if(randRate < 3) {
			
			Alert oonseAlert = new Alert(AlertType.INFORMATION);
			oonseAlert.setTitle("오늘의 한마디");
			oonseAlert.setContentText(db.pickFortune());
			oonseAlert.showAndWait();
			
			
		}
	}
	
	//버튼을 누를때마다 달리는 조건
	public void buttonPressing(int eachCount, String element) {

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
		case 4:
			System.exit(0);
		}
	}

	////////////////////////// 4가지 버튼 액션
	public void waterAction(ActionEvent e) {

		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();

		waterCount++;
		waterEffect.setImage(waterEffect1);
		
		fortunecookie();
		buttonPressing(waterCount, "water");
		System.out.println(waterCount);
		
		
		user.setTanning(lightCount);
		db.updateAll(user);

		
	}

	public void lightAction(ActionEvent e) {
		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		
		lightEffect.setImage(lightEffect1);

		lightCount++;
		System.out.println(lightCount);
		
		fortunecookie();
		buttonPressing(lightCount, "light");
		
		
	}
	// 사랑버튼
	public void loveAction(ActionEvent e) {

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

		if (waterCount == 3 && lightCount == 2 && loveCount > 2 && snailCount == 1) {
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
		case 4:
			System.exit(0);
		}
		
	}

	public void snailAction(ActionEvent e) {
		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		
		snailCount++;
		
		fortunecookie();
		
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
		case 4:
			System.exit(0);
		}
		
	}

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
	public void receive() {
		while(true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[1024];
				int length = in.read(buffer);
				while(length == -1) throw new IOException();
				String message = new String(buffer, 0 , length, "UTF-8");			
				System.out.println("[클라이언트 메시지 수신 성공] : " + user.getPlantName() +" : "+  message);
				Platform.runLater(()->{
					TextArea.appendText(message);
				});
			}catch (Exception e) {
				stopClient();
				break;
			}
		}
		
	}
	
	// 서버 메시지 전송 메서드
	// send (thread) > thread > thread > receive
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


	public void chatAction(ActionEvent e) {
		
		sending(user.getPlantName() + " : " + TextField.getText() + "\n");
		TextField.setText("");
		TextField.requestFocus();
		System.out.println("[클라이언트 메시지 전송 성공] : " + user.getPlantName() +" : "+  TextField.getText());
		
	}

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

///////////////////// 로그아웃 버튼, 죽은 코드
//	public void logout(ActionEvent event) {
//
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("로그아웃");
//		alert.setHeaderText("게임을 종료한다!");
//		alert.setContentText("나가기 전에 저장하시겠습니까?: ");
//
//		if (alert.showAndWait().get() == ButtonType.OK) {
//			stage = (Stage) scenePane.getScene().getWindow();
//			System.out.println("로그아웃 했습니다");
//			stage.close();
//		}
//	}

	/////////////////////////////// 시간 디스플레이

}
