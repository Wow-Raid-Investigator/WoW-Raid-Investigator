package redis;

import parser.Event;
import redis.clients.jedis.Jedis;

public class PlayerSpigot extends RedisSpigot {

	public PlayerSpigot(Jedis jedis) {
		super(jedis);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(Event event) {
		try {
			if (event.type.getField("HasUnitKeys").getBoolean(null)) {
				String source = event.data.get("SourceGUID");
				String dest = event.data.get("DestGUID");
				//jedis.hset("units", source, event.data.get("SourceName"));
				//jedis.hset("units", dest, event.data.get("DestName"));
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
