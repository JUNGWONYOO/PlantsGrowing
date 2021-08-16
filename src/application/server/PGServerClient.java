package application.server;

import java.io.*;
import java.net.Socket;

// ���� Ŭ���̾�Ʈ
public class PGServerClient {
	
	Socket socket;
	
	public PGServerClient(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	
	// Ŭ���̾�Ʈ�κ��� Ȯ���⸦ ���޹޴� �޼���
	// runnable ��ü�� �����Ͽ� �׻� input�� �ް� �޼����� �ۼ��Ͽ� output�ϴ� (���� �� ��� Ŭ���̾�Ʈ��)�������� �����ش�.
	// runnable ��ü�� Main�� threadpool���� �������ش�.
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
						System.out.println("[Ȯ���� ���� ����]" 
								+ socket.getRemoteSocketAddress() + " : " 
								+ Thread.currentThread().getName());
						
						String message = new String(buffer, 0, length, "UTF-8");
						for(PGServerClient client : Main.clients) {
							client.send(message);
						}
					}
				} catch (Exception e) {
					try {
						System.out.println("[Ȯ���� ���� ����]"
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
	
	// Ŭ���̾�Ʈ���� Ȯ���⸦ �����ϴ� �޼���
	// runnable ��ü�� �����Ͽ� output�� �ް� �޼����� �ۼ��Ͽ� Ŭ���̾�Ʈ���� input��ų �غ��Ѵ�.
	// runnable ��ü�� Main�� threadpool���� �������ش�.
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
						System.out.println("[Ȯ���� �۽� ����]" 
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
