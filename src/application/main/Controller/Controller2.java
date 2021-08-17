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
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TextArea;

public class Controller2 implements Initializable, ControlInterface {

	/////////////// ImageView = 그림 틀
	@FXML
	private ImageView myPlantView, waterEffect, chatBubbleView, lightEffect,loveEffect, snailEffect,helpView;
	@FXML
	private Button btn_play, btn_pause, btn_water, btn_sun, btn_heart, btn_snail, btn_weather, btnShowSlide,
			btnHideSlide, btn_serverOn, btn_serverOff, btn_previouspage,btn_helpPrev, btn_helpPrev1, btn_helpPrev2, 
			btn_helpPrev3, btn_helpPrev4, btn_helpNxt2,btn_helpNxt3, btn_helpNxt4, btn_helpNxt5, btn_helpNxt6;
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
	
	@FXML
	private AnchorPane panehelp;
	
	
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
	Image loveEffect1 = new Image(getClass().getResourceAsStream("../View/css/loveEffect.png"));
	Image snailEffect1 = new Image(getClass().getResourceAsStream("../View/css/snailEffect.png"));
	Image chatBubble1 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_smile.png"));
	Image chatBubble2 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_soso.png"));
	Image chatBubble3 = new Image(getClass().getResourceAsStream("../View/css/chatBubble_angery.png"));
	Image helpPage1 = new Image(getClass().getResourceAsStream("../View/css/help_1.png"));
	Image helpPage2 = new Image(getClass().getResourceAsStream("../View/css/help_2.png"));
	Image helpPage3 = new Image(getClass().getResourceAsStream("../View/css/help_3.png"));
	Image helpPage4 = new Image(getClass().getResourceAsStream("../View/css/help_4.png"));
	Image helpPage5 = new Image(getClass().getResourceAsStream("../View/css/help_5.png"));
	
	
	
	@FXML
	public void hideSlide(MouseEvent event) {
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.4));
		slide.setNode(paneslide);
		slide.setToX(700);
		slide.play();

		paneslide.setTranslateX(0);
	}

	@FXML
	public void showSlide(MouseEvent event) {
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
		{
			panehelp.setTranslateX(1000);
		}

		/////////////////// 브금 및 효과음
		bgm = new Media(songs.get(3).toURI().toString());
		mP = new MediaPlayer(bgm);

	}
	
	public void fadeAction() {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), panehelp);
		fadeTransition.setFromValue(0.0f);
		fadeTransition.setToValue(1.0f);
		// fadeTransition.setCycleCount(2);
		fadeTransition.setAutoReverse(true);
		fadeTransition.play();
	}

	public void helpAction(ActionEvent e) {
		//helpView.setImage(helpPage1);
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.1));
		slide.setNode(panehelp);
		slide.setToX(0);
		slide.play();
		System.out.println("hello");
		btn_helpPrev.setVisible(true);
		btn_helpPrev1.setVisible(false);
		btn_helpPrev2.setVisible(false);
		btn_helpPrev3.setVisible(false);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(true);
		btn_helpNxt3.setVisible(false);
		btn_helpNxt4.setVisible(false);
		btn_helpNxt5.setVisible(false);
		btn_helpNxt6.setVisible(false);
		
	}
	
	public void helpPrev(ActionEvent e) {
		//fadeAction();
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.4));
		slide.setNode(panehelp);
		slide.setToX(1000);
		slide.play();
		panehelp.setTranslateX(0);
	}

	public void helpTo2Action(ActionEvent e) {
		helpView.setImage(helpPage2);
		btn_helpPrev.setVisible(false);
		btn_helpPrev1.setVisible(true);
		btn_helpPrev2.setVisible(false);
		btn_helpPrev3.setVisible(false);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(false);
		btn_helpNxt3.setVisible(true);
		btn_helpNxt4.setVisible(false);
		btn_helpNxt5.setVisible(false);
		btn_helpNxt6.setVisible(false);
		
	}

	public void helpTo3Action(ActionEvent e) {
		helpView.setImage(helpPage3);
		btn_helpPrev1.setVisible(false);
		btn_helpPrev2.setVisible(true);
		btn_helpPrev3.setVisible(false);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(false);
		btn_helpNxt3.setVisible(false);
		btn_helpNxt4.setVisible(true);
		btn_helpNxt5.setVisible(false);
		btn_helpNxt6.setVisible(false);
	}

	public void helpTo4Action(ActionEvent e) {
		helpView.setImage(helpPage4);
		btn_helpPrev.setVisible(false);
		btn_helpPrev1.setVisible(false);
		btn_helpPrev2.setVisible(false);
		btn_helpPrev3.setVisible(true);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(false);
		btn_helpNxt3.setVisible(false);
		btn_helpNxt4.setVisible(false);
		btn_helpNxt5.setVisible(true);
		btn_helpNxt6.setVisible(false);
	}

	public void helpTo5Action(ActionEvent e) {
		helpView.setImage(helpPage5);
		btn_helpPrev.setVisible(false);
		btn_helpPrev1.setVisible(false);
		btn_helpPrev2.setVisible(false);
		btn_helpPrev3.setVisible(false);
		btn_helpPrev4.setVisible(true);
		btn_helpNxt2.setVisible(false);
		btn_helpNxt3.setVisible(false);
		btn_helpNxt4.setVisible(false);
		btn_helpNxt5.setVisible(false);
		btn_helpNxt6.setVisible(true);
	}
	
	public void helpTo6Action(ActionEvent e) {
		
		
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.01));
		slide.setNode(panehelp);
		slide.setToX(1000);
		slide.play();
		panehelp.setTranslateX(0);
	}
	public void helpTo1Prev(ActionEvent e) {
		helpView.setImage(helpPage1);
		btn_helpPrev.setVisible(true);
		btn_helpPrev1.setVisible(false);
		btn_helpPrev2.setVisible(false);
		btn_helpPrev3.setVisible(false);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(true);
		btn_helpNxt3.setVisible(false);
		btn_helpNxt4.setVisible(false);
		btn_helpNxt5.setVisible(false);
		btn_helpNxt6.setVisible(false);
	}

	public void helpTo2Prev(ActionEvent e) {
		helpView.setImage(helpPage2);
		btn_helpPrev.setVisible(false);
		btn_helpPrev1.setVisible(true);
		btn_helpPrev2.setVisible(false);
		btn_helpPrev3.setVisible(false);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(false);
		btn_helpNxt3.setVisible(true);
		btn_helpNxt4.setVisible(false);
		btn_helpNxt5.setVisible(false);
		btn_helpNxt6.setVisible(false);
	}

	public void helpTo3Prev(ActionEvent e) {
		helpView.setImage(helpPage3);
		btn_helpPrev.setVisible(false);
		btn_helpPrev1.setVisible(false);
		btn_helpPrev2.setVisible(true);
		btn_helpPrev3.setVisible(false);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(false);
		btn_helpNxt3.setVisible(false);
		btn_helpNxt4.setVisible(true);
		btn_helpNxt5.setVisible(false);
		btn_helpNxt6.setVisible(false);
	}

	public void helpTo4Prev(ActionEvent e) {
		helpView.setImage(helpPage4);
		btn_helpPrev.setVisible(false);
		btn_helpPrev1.setVisible(false);
		btn_helpPrev2.setVisible(false);
		btn_helpPrev3.setVisible(true);
		btn_helpPrev4.setVisible(false);
		btn_helpNxt2.setVisible(false);
		btn_helpNxt3.setVisible(false);
		btn_helpNxt4.setVisible(false);
		btn_helpNxt5.setVisible(true);
		btn_helpNxt6.setVisible(false);
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
	public void buttonPressing(int eachCount, String element, PauseTransition pause) throws SQLException {

		int numberOfClicks = 0;
		
		if (element.equals("water")) {
			numberOfClicks = 3;
		} else if (element.equals("light")) {
			numberOfClicks = 2;
		}
		
		if (eachCount == numberOfClicks) {

			chatBubbleView.setImage(chatBubble1);
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		} else if (eachCount == numberOfClicks+1) {

			chatBubbleView.setImage(chatBubble2);
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();

		} else if (eachCount == numberOfClicks+2) {

			chatBubbleView.setImage(chatBubble3);
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
	
	public void audioClipping() {
		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
	}

	////////////////////////// 4가지 버튼 액션
	public void waterAction(ActionEvent e) throws SQLException {

		audioClipping();

		waterCount++;
		waterEffect.setImage(waterEffect1);
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(a -> waterEffect.setImage(null));
		pause.play();
		
		fortunecookie();
		buttonPressing(waterCount, "water", pause);
		System.out.println(waterCount);
		
		
		user.setWatering(waterCount);
		db.updateAll(user);

		
	}
	
	
	public void lightAction(ActionEvent e) throws SQLException {
		audioClipping();

		lightEffect.setImage(lightEffect1);

		lightCount++;
		System.out.println(lightCount);
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(a -> lightEffect.setImage(null));
		pause.play();
		
		fortunecookie();
		buttonPressing(lightCount, "light", pause);
		
		user.setTanning(lightCount);
		db.updateAll(user);
		
		
	}
	// 사랑버튼
	public void loveAction(ActionEvent e) throws SQLException {

		audioClipping();

		loveCount++;
		
		System.out.println(loveCount);
		chatBubbleView.setImage(chatBubble1);
		
		loveEffect.setImage(loveEffect1);
		
		PauseTransition pause1 = new PauseTransition(Duration.seconds(1));
		pause1.setOnFinished(a -> loveEffect.setImage(null));
		pause1.play();
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(a -> chatBubbleView.setImage(null));
		pause.play();

		fortunecookie();
		
		user.setCaring(loveCount);
		db.updateAll(user);

		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
			dataRefresh();
			
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
			
		} else if (20 < waterCount + lightCount + snailCount) {
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
		audioClipping();

		snailCount++;
		
		snailEffect.setImage(snailEffect1);
		
		PauseTransition pause1 = new PauseTransition(Duration.seconds(1));
		pause1.setOnFinished(a -> snailEffect.setImage(null));
		pause1.play();
		
		fortunecookie();
		
		user.setNutrition(snailCount);
		db.updateAll(user);
		
		
		
		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
			
			dataRefresh();
			
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
	
	public void goPrevious(ActionEvent e) throws IOException {
		Stage stage;
		Scene scene;
		Parent root;
		
		root = FXMLLoader.load(getClass().getResource("../../firstLogin/View/login.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
	
	
	//127.0.0.1
	//서버 on > thread를통해 > server 전달 메시지 수신
	@FXML
	public void serverOn(MouseEvent event) {
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
	public void serverOff(MouseEvent event) {
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
