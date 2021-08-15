package application.server;

import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

// 서버
public class Main extends Application {
	
	// threadPool로 관리하게 되면 작업큐에서 스레드를 처리하고 다시 새로운 스레드를 처리하여 애플리케이션 성능 유지에 도움 됨.
	public static ExecutorService threadPool;
	public static Vector<PGServerClient> clients = new Vector<PGServerClient>();
	
	ServerSocket serverSocket;
	Socket socket;
	
	
	//String IP = "127.0.0.1";
	//int port = 9876;
	// 서버시작, socketServer를 로컬호스트로 열어주고, 클라이언트(소켓)를 받아 벡터에 저장한다.
	// 해당 내용을 스레드로 보내 쓰레드 풀에서 관리해준다.
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
		}catch(Exception e) {
			e.printStackTrace();
			if(!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		// thread로 접속하는 클라이언트 관리
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						socket = serverSocket.accept();
						clients.add(new PGServerClient(socket));
						
						System.out.println("[클라이언트 접속]" + socket.getRemoteSocketAddress() + Thread.currentThread());
						
					}catch(Exception e) {
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
			
		};
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}
	
	// 서버중지 메서드
	// 서버를 중지할 때, 벡터리스트에 남은 클라이언트가 있는지 확인하고
	// 클라이언트가 남아있다면 순회를 돌며 클라이언트 소켓을 모두 닫아준다.
	// 이후 서버와 스레드풀 연결도 종료한다.
	public void stopServer() {
		try {
			Iterator<PGServerClient> ir = clients.iterator();
			
			while(ir.hasNext()) {
				PGServerClient client = ir.next();
				client.socket.close();
				ir.remove();
			}
			//서버 소켓 객체 닫기
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			//스레드 풀 종료
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("나눔고딕", 15));
		root.setCenter(textArea);
		
		Button toggleButton = new Button("시작하기");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1,0,0,0));
		root.setBottom(toggleButton);
		
		String IP = "127.0.0.1";
		int port = 9876;
		
		toggleButton.setOnAction(event -> {
			if(toggleButton.getText().equals("시작하기")) {
				startServer(IP,port);
			Platform.runLater(() -> {
				String message = String.format("[서버시작] \n", IP, port);
				textArea.appendText(message);
				toggleButton.setText("종료하기");
			});
		} else {
			stopServer();
			Platform.runLater(() -> {
				String message = String.format("[서버종료] \n", IP, port);
				textArea.appendText(message);
				toggleButton.setText("시작하기");
			});
		}
			
		});
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[ Plant Growing 서버  ]");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
