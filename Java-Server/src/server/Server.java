package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Server {

	private static EntityManager cassandra;
	private static EntityManager redis;
	
	static CompletionHandler<AsynchronousSocketChannel, RequestHandler> handler = new CompletionHandler<AsynchronousSocketChannel, RequestHandler>() {

		@Override
		public void failed(Throwable arg0, RequestHandler arg1) {
			executor.execute(acceptor);
			arg0.printStackTrace(System.err);
		}

		@Override
		public void completed(AsynchronousSocketChannel result, RequestHandler wow) {
			executor.execute(acceptor);
			ByteBuffer message = ByteBuffer.allocate(1024);
			result.read(message, wow, new CompletionHandler<Integer, RequestHandler>() {

				@Override
				public void completed(Integer count, RequestHandler wow) {
					if(result.isOpen()){
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						try {
							if(count.intValue() == -1){
								result.close();
								return;
							}
							String str = new String(message.array(), StandardCharsets.UTF_8).trim();
							wow.handleRequest(stream, str);
						} catch (Exception e) {
							e.printStackTrace();
							stream.reset();
							try {
								e.printStackTrace(new PrintStream(stream, false, StandardCharsets.UTF_8.name()));
							} catch (UnsupportedEncodingException e1) {
								e1.printStackTrace();
							}
						}
						ByteBuffer buf = ByteBuffer.wrap(stream.toByteArray());
						System.out.println(new String(buf.array()));
						result.write(buf);
						message.rewind();
						result.read(message, wow, this);
					}
				}

				@Override
				public void failed(Throwable exc, RequestHandler wow) {
					exc.printStackTrace(System.err);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					try {
						exc.printStackTrace(new PrintStream(stream, true, StandardCharsets.UTF_8.name()));
						result.write(ByteBuffer.wrap(stream.toByteArray()));
						result.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	};

	static ThreadPoolExecutor executor;
	static AsynchronousServerSocketChannel server;
	static Runnable acceptor = new Runnable() {
		
		@Override
		public void run() {
			server.accept(new RequestHandler(), handler);
		}
	};

	public static void main(String[] args) throws IOException {
		cassandra = Persistence.createEntityManagerFactory("cassandra").createEntityManager();
		redis = Persistence.createEntityManagerFactory("redis").createEntityManager();
		executor = new ThreadPoolExecutor(
			1, 8, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>()
		);
		AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(executor);
		server = AsynchronousServerSocketChannel.open(group);
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 9000;
		InetSocketAddress addr = new InetSocketAddress(port);
		server.bind(addr);
		System.out.println("Server binded to "+addr);
		executor.execute(acceptor);
	}

	public static EntityManager cassandra(){
		return cassandra;
	}

	public static EntityManager redis(){
		return redis;
	}
}
