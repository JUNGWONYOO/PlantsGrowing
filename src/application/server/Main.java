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

// ����
public class Main extends Application {
	
	// threadPool�� �����ϰ� �Ǹ� �۾�ť���� �����带 ó���ϰ� �ٽ� ���ο� �����带 ó���Ͽ� ���ø����̼� ���� ������ ���� ��.
	public static ExecutorService threadPool;
	public static Vector<PGServerClient> clients = new Vector<PGServerClient>();
	
	ServerSocket serverSocket;
	Socket socket;
	
	
	//String IP = "127.0.0.1";
	//int port = 9876;
	// ��������, socketServer�� ����ȣ��Ʈ�� �����ְ�, Ŭ���̾�Ʈ(����)�� �޾� ���Ϳ� �����Ѵ�.
	// �ش� ������ ������� ���� ������ Ǯ���� �������ش�.
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
		
		// thread�� �����ϴ� Ŭ���̾�Ʈ ����
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						socket = serverSocket.accept();
						clients.add(new PGServerClient(socket));
						
						System.out.println("[Ŭ���̾�Ʈ ����]" + socket.getRemoteSocketAddress() + Thread.currentThread());
						
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
	
	// �������� �޼���
	// ������ ������ ��, ���͸���Ʈ�� ���� Ŭ���̾�Ʈ�� �ִ��� Ȯ���ϰ�
	// Ŭ���̾�Ʈ�� �����ִٸ� ��ȸ�� ���� Ŭ���̾�Ʈ ������ ��� �ݾ��ش�.
	// ���� ������ ������Ǯ ���ᵵ �����Ѵ�.
	public void stopServer() {
		try {
			Iterator<PGServerClient> ir = clients.iterator();
			
			while(ir.hasNext()) {
				PGServerClient client = ir.next();
				client.socket.close();
				ir.remove();
			}
			//���� ���� ��ü �ݱ�
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			//������ Ǯ ����
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
		textArea.setFont(new Font("�������", 15));
		root.setCenter(textArea);
		
		Button toggleButton = new Button("�����ϱ�");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1,0,0,0));
		root.setBottom(toggleButton);
		
		String IP = "127.0.0.1";
		int port = 9876;
		
		toggleButton.setOnAction(event -> {
			if(toggleButton.getText().equals("�����ϱ�")) {
				startServer(IP,port);
			Platform.runLater(() -> {
				String message = String.format("[��������] \n", IP, port);
				textArea.appendText(message);
				toggleButton.setText("�����ϱ�");
			});
		} else {
			stopServer();
			Platform.runLater(() -> {
				String message = String.format("[��������] \n", IP, port);
				textArea.appendText(message);
				toggleButton.setText("�����ϱ�");
			});
		}
			
		});
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle("[ Plant Growing ����  ]");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
