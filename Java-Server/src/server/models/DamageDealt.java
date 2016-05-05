package server.models;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "damage_dealt")
public class DamageDealt {

	@Embeddable
	public static class Key {
		public Integer raid;
		public Integer encounter;
		public String source;
		public Integer log;
	}

	@EmbeddedId
	public Key key;
	public String target;
	public Integer damage;
	public Long timestamp;
}
