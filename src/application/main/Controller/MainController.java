package application.main.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import application.dao.DBConnector;
import application.firstLogin.Controller.LoginController;
import application.firstLogin.Users.UserInfo;
import application.main.weather.weatherCrawl;
import application.main.weather.weatherVO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class MainController {
	
	@FXML
	private Button btn_weather;
	@FXML
	private Button btn_previouspage;
	@FXML
	private Button btn_water;
	@FXML
	private Button btn_sun;
	@FXML
	private Button btn_heart;
	@FXML
	private Button btn_snail;
	@FXML
	private Button btn_onOff;
	@FXML
	private Button btn_enter;
	
	UserInfo user = LoginController.UserList.get(0);

	@FXML
	private Label lbl_wthr_location;
	@FXML
	private Label lbl_wthr_temperature;
	@FXML
	private Label lbl_wthr_status;
	
	@FXML
	private Label lbl_plantName; 

	
	@FXML
	private TextField tfd_msg;
	
	@FXML 
	private TextArea ta_msg;
	
	
	Socket socket;
	
	
	private int loveCount = user.getCaring();
	private int waterCount = user.getWatering();
	private int tanningCount = user.getTanning();
	private int snailCount = user.getNutrition();
	private int randRate = 0;
	private int level = user.getLevel();
	int i;
	
	
	DBConnector db = new DBConnector();


	// 이전화면에서 PlantName 넘어오게끔 만드는 메서드
	public void setPname(String pName) {
		lbl_plantName.setText(user.getPlantName());
	}
	
	// 레벨업, 운세 등장시 dataRefresh
	public void dataRefresh() {
		
		loveCount = 0;
		waterCount = 0;
		tanningCount = 0;
		snailCount = 0;
		
		user.setCaring(0);
		user.setNutrition(0);
		user.setTanning(0);
		user.setWatering(0);
		db.updateAll(user);
	}
	
	// 레벨업, 운세 알람 메서드 - 데이터 연동
	public void levelOrFortune() {
		
		if(loveCount >= 3 && waterCount >= 3 && tanningCount >= 3 && snailCount >= 3) {
			
			randRate = (int)(Math.random()*10 +1);	// 운세와 레벨업 확률
			int n = 6;
			if(randRate < n) {
				Alert oonseAlert = new Alert(AlertType.INFORMATION);
				oonseAlert.setTitle("오늘의 한마디");
				oonseAlert.setContentText(db.pickFortune());
				oonseAlert.showAndWait();
				dataRefresh();
				
			}
			else {
				Alert oonseAlert = new Alert(AlertType.INFORMATION);
				oonseAlert.setTitle("레벨 업!");
				if(level <= 3) {
					level++;
					user.setLevel(level);
					dataRefresh();
					
					
					oonseAlert.setContentText("레벨"+ level + " 로 오르셨습니다!");
					oonseAlert.showAndWait();
				}else if(level > 3) {	
					dataRefresh();
					
					oonseAlert.setTitle("성공");
					oonseAlert.setContentText("식물이 잘 자랐어요!!!! 식물과 함께 즐거운 삶을 보내봐요");
					oonseAlert.showAndWait();
					n = 11; // 만렙을 찍으면 더이상 레벨업 문구가 나오지 않게끔 설정하기 위해.
				}
				

			}
		}
		
	}

	
	// 각 버튼
	@FXML
	public void heart(ActionEvent ae) throws InterruptedException, IOException {
		loveCount++;

		user.setCaring(loveCount);
		db.updateAll(user);
		
		levelOrFortune();
	}
	
	@FXML
	public void snail(ActionEvent ae) throws InterruptedException, IOException {
		snailCount++;

		user.setNutrition(snailCount);
		db.updateAll(user);
		
		levelOrFortune();
	}
	
	@FXML
	public void sun(ActionEvent ae) throws InterruptedException, IOException {
		tanningCount++;

		user.setTanning(tanningCount);
		db.updateAll(user);
		
		levelOrFortune();
	}
	
	@FXML
	public void water(ActionEvent ae) throws InterruptedException, IOException {
		waterCount++;
		
		user.setWatering(waterCount);
		db.updateAll(user);
		
		levelOrFortune();
	}
	
	// 날씨 크롤링 버튼
	@FXML
	public void weatherRefresh(ActionEvent ae) {
		weatherVO wVO = new weatherVO();
		try {
			weatherCrawl.Crawling(wVO);
			
			lbl_wthr_location.setText(wVO.getLocation());
			lbl_wthr_temperature.setText(String.valueOf(wVO.getTemperature()));
			lbl_wthr_status.setText(wVO.getStatus());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void previousPage(ActionEvent ae) {
		System.out.println("이전");
	}
	
	
	
	// for Server
	// 서버 연결 버튼
	@FXML
	public void onButton(ActionEvent ae) {
		
		ta_msg.setEditable(false);
		if(btn_onOff.getText().equals("ON")){
			btn_onOff.setText("OFF");
			
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
		
		else if (btn_onOff.getText().equals("OFF")) {
			
				btn_onOff.setText("ON");
				stopClient();
				
		}
	}

	// 텍스트 전송 버튼
	@FXML
	public void send(ActionEvent ae) {
		
		sending(user.getPlantName() + " : " + tfd_msg.getText() + "\n");
		tfd_msg.setText("");
		tfd_msg.requestFocus();
		System.out.println("[클라이언트 메시지 전송 성공] : " + user.getPlantName() +" : "+  tfd_msg.getText());
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
					ta_msg.appendText(message);
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
}
