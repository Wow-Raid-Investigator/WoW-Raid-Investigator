package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import inserters.Inserter;

public class LogParser {

	private HashMap<Class, ArrayList<Inserter>> subscribers;

	public LogParser() {
		subscribers = new HashMap<Class, ArrayList<Inserter>>();
	}

	public void parseFile(String path) {
		File file = new File(path);
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				parse(line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public void parse(String line) {
		Event event;
		try {
			event = Event.parseLine(line);
			List<Inserter> listeners = subscribers.get(event.type);

			if (listeners != null) {
				for (Inserter listener : listeners) {
					listener.receive(event);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void register(Inserter inserter, Class type) {
		if (subscribers.get(type) == null) {
			subscribers.put(type, new ArrayList<Inserter>());
		}

		subscribers.get(type).add(inserter);
	}
}
