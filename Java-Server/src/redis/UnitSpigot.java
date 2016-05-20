package redis;

import java.util.HashMap;

import parser.Event;
import redis.clients.jedis.Jedis;

public class UnitSpigot extends RedisSpigot {
	
	public UnitSpigot(Jedis jedis) {
		super(jedis);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(Event event) {
		try {
			if (event.type.getField("HasUnitKeys").getBoolean(null)) {
				String source = event.data.get("SourceGUID");
				String dest = event.data.get("DestGUID");
				cache.put(source,event.data.get("SourceName"));
				cache.put(dest,event.data.get("DestName"));
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected String getCollectionName() {
		return "units";
	}

}
