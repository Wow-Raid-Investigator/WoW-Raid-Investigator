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
			if (event.type.getClass().getField("HasUnitKeys").getBoolean(null)) {
				String source = event.data.get(Event.UnitKeys.SourceGUID);
				String dest = event.data.get(Event.UnitKeys.DestGUID);
				if (source.contains("PLAYER")) {
					jedis.hset("players", source, event.data.get(Event.UnitKeys.SourceName));
				}
				
				if (dest.contains("PLAYER")) {
					jedis.hset("players", dest, event.data.get(Event.UnitKeys.DestName));
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
