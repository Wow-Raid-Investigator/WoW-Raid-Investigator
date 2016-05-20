package redis;

import java.util.HashMap;

import parser.WowEventListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/* 
 * Something that responds to WoW events
 * and dumps data to REDIS as it goes.
 * 
 * Pretty straightforward.
 */
public abstract class RedisSpigot implements WowEventListener {
	protected Jedis jedis;
	protected HashMap<String,String> cache;
	
	public RedisSpigot(Jedis jedis) {
		this.jedis = jedis;
		this.cache = new HashMap<String,String>();
	}
	
	public void flush() {
		Pipeline pipe = jedis.pipelined();
		String name = getCollectionName();
		for (String key : cache.keySet()) {
			System.out.println(key + " " + cache.get(key));
			pipe.hset(name, key, cache.get(key));
		}
		
		System.out.println("Syncing" + System.currentTimeMillis());
		pipe.sync();
		System.out.println("Flushed" + System.currentTimeMillis());
		
		cache.clear();
	}
	
	protected abstract String getCollectionName();
}
