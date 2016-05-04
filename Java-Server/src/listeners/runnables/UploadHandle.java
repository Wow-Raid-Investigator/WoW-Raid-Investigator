package listeners.runnables;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class UploadHandle implements Runnable {

	private Socket client;
	private OutputStream stream;
	private String fileName;

	public UploadHandle(Socket client, OutputStream stream, String fileName) {
		this.client = client;
		this.stream = stream;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[16384];
		
		try {
			int read;
			while(true) {
				read = client.getInputStream().read(buffer);
				if(read > 0)
					stream.write(buffer, 0, read);
				else if (read == -1) {
					break;
				}
			}
			
			stream.close();
			client.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.printf("Client %s caused a read exception durring file upload, Disconnecting Client\n", client.getInetAddress());
			try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				ParseHandle.execute(fileName);
			}
		}).start();
	}

}
