package redis;

import parser.Event;
import redis.clients.jedis.Jedis;

public class SpellSpigot extends RedisSpigot {

	public SpellSpigot(Jedis jedis) {
		super(jedis);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(Event event) {
		cache.put(event.data.get("CastSpellId"), event.data.get("CastSpellName"));
	}

	@Override
	protected String getCollectionName() {
		return "spells";
	}

}
