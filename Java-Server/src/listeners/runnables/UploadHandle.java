package listeners.runnables;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class UploadHandle implements Runnable {

	private Socket client;
	private OutputStream stream;
	
	public UploadHandle(Socket client, OutputStream stream) {
		this.client = client;
		this.stream = stream;
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
			
			// TODO: Call Thread Parser / Add to Parser Thread Queue
		} catch (IOException e) {
			e.printStackTrace();
			System.out.printf("Client %s caused a read exception durring file upload, Disconnecting Client\n", client.getInetAddress());
		}
	}

}
