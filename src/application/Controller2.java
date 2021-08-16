package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import application.Controller2;
import application.Main;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller2 implements Initializable {

	/////////////// ImageView = 그림 틀
	@FXML
	private ImageView myPlantView, waterEffect, chatBubbleView, lightEffect;
//	private ImageView btn_play_imageView,
//	btn_pause_imageView, btn_water_imageView, btn_sun_imageView, btn_heart_imageView, btn_snail_imageView,
//	btn_weather_imageView;
	@FXML
	private Button btn_play, btn_pause, btn_water, btn_sun, btn_heart, btn_snail, btn_weather, btn_slide;
	private File directory;
	private File[] files;
	private ArrayList<File> songs;
	// private boolean musicPlaying;
	private Media bgm;
	private MediaPlayer mP;
	private int loveCount = 0;
	private int lightCount = 0;
	private int waterCount = 0;
	private int snailCount = 0;
	private int level = 1;
	private int randRate = 0;

	@FXML
	private AnchorPane paneslide;
	@FXML
	private JFXButton btnShowSlide;
	@FXML
	private JFXButton btnHideSlide;

	/////////////// 레벨별 식물, 효과, 상태 이미지
	Image plantLevel2 = new Image(getClass().getResourceAsStream("View/css/ct2.png"));
	Image plantLevel3 = new Image(getClass().getResourceAsStream("View/css/ct3.png"));
	Image waterEffect1 = new Image(getClass().getResourceAsStream("View/css/waterEffect.png"));
	Image lightEffect1 = new Image(getClass().getResourceAsStream("View/css/lightEffect.png"));
	Image chatBubble1 = new Image(getClass().getResourceAsStream("View/css/chatBubble_smile.png"));
	Image chatBubble2 = new Image(getClass().getResourceAsStream("View/css/chatBubble_soso.png"));
	Image chatBubble3 = new Image(getClass().getResourceAsStream("View/css/chatBubble_angry.png"));
	
	@FXML
	private void hideSlide(MouseEvent event) {
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.4));
		slide.setNode(paneslide);

		slide.setToX(700);
		slide.play();

		paneslide.setTranslateX(0);

		slide.setOnFinished((ActionEvent e) -> {
			btnShowSlide.setVisible(true);
			btnHideSlide.setVisible(false);
		});
	}
	@FXML
	private void showSlide(MouseEvent event) {
		TranslateTransition slide = new TranslateTransition();
		slide.setDuration(Duration.seconds(0.4));
		slide.setNode(paneslide);

		slide.setToX(0);
		slide.play();

		paneslide.setTranslateX(0);

		slide.setOnFinished((ActionEvent e) -> {
			btnShowSlide.setVisible(false);
			btnHideSlide.setVisible(true);
		});
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
			btnShowSlide.setVisible(true);
			btnHideSlide.setVisible(false);
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

	////////////////////////// 4가지 버튼 액션
	public int waterAction(ActionEvent e) {

		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		
		waterEffect.setImage(waterEffect1);
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(a -> waterEffect.setImage(null));
		pause.play();
		
		System.out.println(waterCount);
		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
				level++;
				waterCount = 0;
				snailCount = 0;
				lightCount = 0;
		}else if (waterCount == 3) {
			System.out.println("딱 좋아요 >.<");
			chatBubbleView.setImage(chatBubble1);
				pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(a -> chatBubbleView.setImage(null));
				pause.play();
			waterCount++;
		}else if (waterCount == 4) {
			System.out.println("물이 너무 많아요");
			chatBubbleView.setImage(chatBubble2);
			pause = new PauseTransition(Duration.seconds(2.25));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();
			waterCount++;
		}else if (waterCount == 5) {
			System.out.println("물은 이제 필요 없어요");
			chatBubbleView.setImage(chatBubble3);
			pause = new PauseTransition(Duration.seconds(2.5));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();
			waterCount=3;
		}else if (6<waterCount+lightCount+snailCount){
			loveCount = 1;
			waterCount = 1;
			snailCount = 1;
			lightCount = 1;
		}else {
			waterCount++;
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
			return waterCount;
		}
		
	

	public int lightAction(ActionEvent e) {
		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		System.out.println(lightCount);
		lightEffect.setImage(lightEffect1);
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(a -> lightEffect.setImage(null));
		pause.play();
		if (waterCount == 3 && lightCount == 2 && loveCount >= 2 && snailCount == 1) {
				level++;
				waterCount = 0;
				snailCount = 0;
				lightCount = 0;
		}else if (lightCount == 2) {
			lightCount++;
			chatBubbleView.setImage(chatBubble1);
			pause = new PauseTransition(Duration.seconds(2));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();
			System.out.println("이제 괜찮아요");
		}else if (lightCount == 3) {
			lightCount++;
			System.out.println("너무 뜨거워요");
			chatBubbleView.setImage(chatBubble2);
			pause = new PauseTransition(Duration.seconds(2));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();
		}else if (lightCount == 4) {
			System.out.println("빛은 이제 필요 없어요");
				lightCount=2;
				chatBubbleView.setImage(chatBubble3);
				pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(a -> chatBubbleView.setImage(null));
				pause.play();
		}else if (6<waterCount+lightCount+snailCount){
			loveCount = 1;
			waterCount = 1;
			snailCount = 1;
			lightCount = 1;
		}else {
			lightCount++;
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
			return lightCount;
		}

	public int loveAction(ActionEvent e) {

		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		loveCount++;
		System.out.println(loveCount);
		chatBubbleView.setImage(chatBubble1);
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(a -> chatBubbleView.setImage(null));
		pause.play();
		if (waterCount == 3 && lightCount == 2 && loveCount > 2 && snailCount == 1) {
				level++;
				waterCount = 0;
				snailCount = 0;
				lightCount = 0;
		}else if (6<waterCount+lightCount+snailCount){
			loveCount = 1;
			waterCount = 1;
			snailCount = 1;
			lightCount = 1;
		}else {
			loveCount *= loveCount;
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
			return loveCount;
		}
	
	
	

	public int snailAction(ActionEvent e) {
		AudioClip m1 = new AudioClip(songs.get(1).toURI().toString());
		m1.setVolume(0.1);
		m1.play();
		System.out.println(snailCount);
		if (waterCount == 3 && lightCount == 2 && loveCount > 2 && snailCount == 1) {
				level++;
				waterCount = 0;
				snailCount = 0;
				lightCount = 0;
		}else if (snailCount == 2) {
			snailCount =1;
			System.out.println("저 이제 배불러요");
			chatBubbleView.setImage(chatBubble2);
			PauseTransition pause = new PauseTransition(Duration.seconds(2));
			pause.setOnFinished(a -> chatBubbleView.setImage(null));
			pause.play();
		}else if (6<waterCount+lightCount+snailCount){
			loveCount = 1;
			waterCount = 1;
			snailCount = 0;
			lightCount = 1;
		}else {
			snailCount++;
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
			return snailCount;
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
	
	////////////////////////////////// 버튼 이미지


	/////////////////////////////// 시간 디스플레이

}
