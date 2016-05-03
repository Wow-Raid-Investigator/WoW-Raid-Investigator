package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import handlers.Handler;
import handlers.Inserter;

public class LogParser {

	private final HashMap<Class, ArrayList<Handler>> subscribers;
	private final Inserter inserter;
	
	public LogParser(Inserter inserter) {
		this.inserter = inserter;
		subscribers = new HashMap<Class, ArrayList<Handler>>();
	}

	public void parseFile(String path) {
		File file = new File(path);
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				inserter.incrementIndex();
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
			List<Handler> listeners = subscribers.get(event.type);

			if (listeners != null) {
				for (Handler listener : listeners) {
					listener.receive(event);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void register(Handler inserter, Class type) {
		if (subscribers.get(type) == null) {
			subscribers.put(type, new ArrayList<Handler>());
		}

		subscribers.get(type).add(inserter);
	}
}
