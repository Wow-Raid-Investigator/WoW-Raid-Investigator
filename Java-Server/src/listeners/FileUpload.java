package listeners;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import listeners.runnables.UploadHandle;

public class FileUpload extends Thread {

	private ServerSocket socket;
	
	private int port;
	private int seed;
	
	public FileUpload(int port) {
		super();

		this.port = port;
		seed = new Random().nextInt(Integer.MAX_VALUE);
	}
	
	@Override
	public void run() {
		super.run();
		
		try {
			openSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to start Log Listener. Quiting");
			System.exit(-1);
		}
		
		while(true) {
			try {
				Socket client = socket.accept();
				System.out.printf("Client connected from %s\n", client.getInetAddress());
				
				String fileName = "LOG_UPLOAD_" + seed++;
				
				FileOutputStream stream = new FileOutputStream(fileName);
				System.out.printf("Client connected from %s\n", client.getInetAddress());
				new Thread(new UploadHandle(client, stream, fileName)).start();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Bad Client FileUpload Connection");
			}
		}
	}
	
	public synchronized void closeSocket() {
		if(socket != null && socket.isBound())
			try {
				socket.close();
			} catch (IOException e) { 
				// Don't Care closing socket anyways
			}
	}
	
	public synchronized void openSocket(int port) throws IOException {
		socket = new ServerSocket(port);
	}
}
