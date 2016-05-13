package redis;

import parser.WowEventListener;
import redis.clients.jedis.Jedis;

/* 
 * Something that responds to WoW events
 * and dumps data to REDIS as it goes.
 * 
 * Pretty straightforward.
 */
public abstract class RedisSpigot implements WowEventListener {
	protected Jedis jedis;
	
	public RedisSpigot(Jedis jedis) {
		this.jedis = jedis;
	}
}
