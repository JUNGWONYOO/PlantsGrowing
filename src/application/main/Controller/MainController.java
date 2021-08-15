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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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
	
	UserInfo user = LoginController.UserList.get(0);
	private int loveCount = user.getCaring();
	private int waterCount = user.getWatering();
	private int tanningCount = user.getTanning();
	private int snailCount = user.getNutrition();
	private int randRate = 0;
	private int level = user.getLevel();
	int i;
	
	DBConnector db = new DBConnector();
	
	public MainController() {
		
		
	}
	
	// 레벨업, 운세 알람 메서드 - 데이터 연동
	public void levelOrFortune() {
		lbl_plantName.setText(user.getPlantName());
		if(loveCount >= 3 && waterCount >= 3 && tanningCount >= 3 && snailCount >= 3) {
			randRate = (int)(Math.random()*10 +1);
			System.out.println(randRate);
			
			if(randRate < 6) {
				Alert oonseAlert = new Alert(AlertType.INFORMATION);
				oonseAlert.setTitle("오늘의 한마디");
				oonseAlert.setContentText(db.pickFortune());
				oonseAlert.showAndWait();
				
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
			else {
				Alert oonseAlert = new Alert(AlertType.INFORMATION);
				oonseAlert.setTitle("레벨 업!");
				if(level <= 3) {
					level++;
					user.setLevel(level);
					
					
					loveCount = 0;
					waterCount = 0;
					tanningCount = 0;
					snailCount = 0;
					
					user.setCaring(0);
					user.setNutrition(0);
					user.setTanning(0);
					user.setWatering(0);
					db.updateAll(user);
					oonseAlert.setTitle("레벨 업");
					oonseAlert.setContentText("레벨"+ level + " 로 오르셨습니다!");
					oonseAlert.showAndWait();
				}else if(level > 3) {
					

					loveCount = 0;
					waterCount = 0;
					tanningCount = 0;
					snailCount = 0;
					
					user.setCaring(0);
					user.setNutrition(0);
					user.setTanning(0);
					user.setWatering(0);
					db.updateAll(user);
					
					oonseAlert.setTitle("성공");
					oonseAlert.setContentText("식물이 잘 자랐어요!!!! 식물과 함께 즐거운 삶을 보내봐요");
					oonseAlert.showAndWait();
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
	@FXML
	public void onButton(ActionEvent ae) {
		
		ta_msg.setEditable(false);
		if(btn_onOff.getText().equals("ON")){
			btn_onOff.setText("OFF");
			
			Thread thread = new Thread() {
				public void run() {
					try {
						socket = new Socket("127.0.0.1",user.getPort());
						System.out.println("소켓 연결");
						receive();	
					} catch (Exception e) {
						if(!socket.isClosed()) {
							try {
								socket.close();
								System.out.println("[서버접속 실패]");
								Platform.exit();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			};
			thread.start();
		}
		
		else if (btn_onOff.getText().equals("OFF")) {
			try {
				btn_onOff.setText("ON");
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
				System.out.println("연결종료");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	@FXML
	public void send(ActionEvent ae) {
		
		sending(user.getPlantName() + " : " + tfd_msg.getText() + "\n");
		tfd_msg.setText("");
		tfd_msg.requestFocus();
			
	}

	
	public void receive() {
		while(true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[1024];
				int length = in.read(buffer);
				while(length == -1) throw new IOException();
				String message = new String(buffer, 0 , length, "UTF-8");
				Platform.runLater(()->{
					ta_msg.appendText(message);
				});
			}catch (Exception e) {
				if(socket != null && !socket.isClosed()) {
					try {
						socket.close();
						break;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		
	}
	
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
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}	
			}
		};
		thread.start();
	}
}
