package listeners.runnables;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import handlers.DamageHandler;
import handlers.Inserter;
import parser.Event;
import parser.LogParser;

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
			
			ParseHandle.execute(fileName);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.printf("Client %s caused a read exception durring file upload, Disconnecting Client\n", client.getInetAddress());
		}
	}

}
