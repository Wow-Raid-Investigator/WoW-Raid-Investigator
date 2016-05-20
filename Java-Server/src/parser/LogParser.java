package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import handlers.Handler;
import redis.RedisSpigot;

public class LogParser {

	private int raid;
	private int encounter;
	private final HashSet<Handler> handlers;
	private final HashSet<RedisSpigot> spigots;
	private final ArrayList<WowEventListener> subscribersToAll;
	private final HashMap<Class<?>, ArrayList<WowEventListener>> subscribers;
	private boolean inEncounter = false;
	
	public LogParser(int raid) {
		this.raid = raid;
		this.encounter = 0;
		handlers = new HashSet<Handler>();
		spigots = new HashSet<RedisSpigot>();
		subscribersToAll = new ArrayList<WowEventListener>();
		subscribers = new HashMap<Class<?>, ArrayList<WowEventListener>>();
	}

	public void parseFile(String path) {
		File file = new File(path);
		int counter = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				parse(line);
				counter++;
				if (counter % 1000 == 0)
					System.out.println(counter);
			}
			
			System.out.println("done");
			
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
			
			if (event.type == Event.ENCOUNTER_START.class){
				this.encounter++;
				this.inEncounter = true;
			}
			
			if (inEncounter) {
				List<WowEventListener> listeners = subscribers.get(event.type);

				if (listeners != null) {
					for (WowEventListener listener : listeners) {
						listener.receive(event);
					}
				}
				
				for (WowEventListener listener : subscribersToAll) {
					listener.receive(event);
				}
			}
			
			if (event.type == Event.ENCOUNTER_END.class){
				this.inEncounter = false;
				System.out.println("???");
				System.out.println(handlers);
				for (Handler handler : handlers) {
					handler.flush(raid, encounter);
				}
				System.out.println("done");
				System.out.println(spigots);
				for (RedisSpigot spigot : spigots) {
					spigot.flush();
				}
			}
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void register(WowEventListener listener, Class<?> type) {
		if (subscribers.get(type) == null) {
			subscribers.put(type, new ArrayList<WowEventListener>());
		}

		subscribers.get(type).add(listener);
		
		if (listener instanceof Handler) {
			handlers.add((Handler) listener);
		} else if (listener instanceof RedisSpigot) {
			spigots.add((RedisSpigot) listener);
		}
		System.out.println(type);
		System.out.println(subscribers.get(type));
	}
	
	public void registerAll(WowEventListener listener) {
		if (listener instanceof Handler) {
			handlers.add((Handler) listener);
		} else if (listener instanceof RedisSpigot) {
			spigots.add((RedisSpigot) listener);
		}
		
		subscribersToAll.add(listener);
	}
}
