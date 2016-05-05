package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import server.models.DamageDealt;

public class RequestHandler {

	public void handleRequest(OutputStream stream, String message) throws IOException{
		System.out.println(message);
		Writer w = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
		List<DamageDealt> results = Server.cassandra().createQuery("select d from DamageDealt d", DamageDealt.class).getResultList();
		for(DamageDealt d : results){
			System.out.println(d.target);
		}
		w.write(results.stream().map(d -> d.target).collect(Collectors.joining(", "))+"\n");
		w.flush();
	}
}
