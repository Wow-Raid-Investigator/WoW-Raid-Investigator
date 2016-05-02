package listeners.runnables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import inserters.DamageHandler;
import inserters.Inserter;
import parser.Event;
import parser.LogParser;

public class ParseHandle {
	public static void execute(String filename) {
		// Get ahold of the database

		// Change to use a dynamic IP later

		Cluster cluster = Cluster.builder().addContactPoint("137.112.104.121").build();
		Session session = cluster.connect("wowraid");

		// TODO raid number
		Inserter inserter = new Inserter(session, 1);

		LogParser parser = new LogParser(inserter);

		// Better way to register all the handlers?
		// We can't just do it once, since we might have
		// many parsers going at once

		parser.register(new DamageHandler(inserter), Event.SWING_DAMAGE.class);
		
		parser.parseFile(filename);
		
		session.close();
	}
}
