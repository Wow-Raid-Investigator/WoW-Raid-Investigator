package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import server.models.DamageDealt;
import server.models.DamageReceived;
import server.models.Raid;
import server.models.UnitDeath;

public class RequestHandler {

	public void handleRequest(OutputStream stream, String message) throws IOException{
		System.out.println(message);
		int raidId = Integer.parseInt(message);
		Raid raid;
		raid = Server.redis().createQuery("SELECT r FROM Raid r WHERE r.id = :id", Raid.class).setParameter("id", raidId).getSingleResult();
		if(raid == null){
			Raid.readFromCassandra(raidId);
			Server.redis().persist(raid);
		}
		System.out.println("saved raid: " + raidId + " into redis");
		Writer w = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
		String result = "Damage Dealt: " + raid.damageDealt.stream().map(DamageDealt::toString).collect(Collectors.joining(",")) + "\n" +
		                "Damage Received: " + raid.damageReceived.stream().map(DamageReceived::toString).collect(Collectors.joining(",")) + "\n" +
		                "Unit Death: " + raid.unitDeath.stream().map(UnitDeath::toString).collect(Collectors.joining(",")) + "\n";
		for(DamageDealt d : raid.damageDealt){
			System.out.println(d.target);
		}
		w.write(result);
		w.flush();
	}
}
