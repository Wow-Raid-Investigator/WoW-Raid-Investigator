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
		jedis.set(event.data.get(Event.SPELL_DAMAGE.CastSpellId),event.data.get(Event.SPELL_DAMAGE.CastSpellName));
	}

}
