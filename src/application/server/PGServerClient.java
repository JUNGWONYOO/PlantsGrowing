package application.server;

import java.io.*;
import java.net.Socket;

// 서버 클라이언트
public class PGServerClient {
	
	Socket socket;
	
	public PGServerClient(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	
	// 클라이언트로부터 확성기를 전달받는 메서드
	// runnable 객체를 생성하여 항상 input을 받고 메세지를 작성하여 output하는 (벡터 내 모든 클라이언트의)소켓으로 보내준다.
	// runnable 객체를 Main의 threadpool에서 관리해준다.
	public void receive() {
		
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[1024];
						int length = in.read(buffer);
						
						while(length == -1) throw new IOException();
						System.out.println("[확성기 수신 성공]" 
								+ socket.getRemoteSocketAddress() + " : " 
								+ Thread.currentThread().getName());
						
						String message = new String(buffer, 0, length, "UTF-8");
						for(PGServerClient client : Main.clients) {
							client.send(message);
						}
					}
				} catch (Exception e) {
					try {
						System.out.println("[확성기 수신 오류]"
								+ socket.getRemoteSocketAddress() + " : "
								+ Thread.currentThread().getName());
						
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			
		};
		Main.threadPool.submit(thread);
	}
	
	// 클라이언트에게 확성기를 전달하는 메서드
	// runnable 객체를 생성하여 output을 받고 메세지를 작성하여 클라이언트에게 input시킬 준비를한다.
	// runnable 객체를 Main의 threadpool에서 관리해준다.
	public void send(String message) {
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
					
					
				} catch (Exception e) {
					try {
						System.out.println("[확성기 송신 오류]" 
								+ socket.getRemoteSocketAddress() + " : " 
								+ Thread.currentThread().getName());
						
						Main.clients.remove(PGServerClient.this);
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
				
			}
		};
		Main.threadPool.submit(thread);
	}
	
}
