package server.models;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "damage_received")
public class DamageReceived {

	@Embeddable
	public static class Key {
		public Integer raid;
		public Integer encounter;
		public String target;
		public Integer logno;
	}

	@EmbeddedId
	public Key key;
	public Integer damage;
	public String source;
	public Long timestamp;

	@Override
	public String toString() {
		return "DamageReceived("+key.target+","+damage.toString()+","+source+")";
	}
}
