package server.models;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "unit_deaths")
public class UnitDeath {

	@Embeddable
	public static class Key {
		public Integer raid;
		public Integer encounter;
		public String unit;
		public Integer logno;
	}

	@EmbeddedId
	public Key key;
	public Long timestamp;
}
