package server.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import server.Server;

@Entity
@Table(name = "raids")
public class Raid {

	static public Raid readFromCassandra(int raidId){
		Raid raid = new Raid();
		raid.id = raidId;
		raid.damageDealt = Server.cassandra().createQuery("SELECT d FROM DamageDealt d WHERE d.key.raid = :raid AND d.key.encounter = 1", DamageDealt.class).setParameter("raid",raidId).getResultList();
		raid.damageReceived = Server.cassandra().createQuery("SELECT d FROM DamageReceived d WHERE d.key.raid = :raid AND d.key.encounter = 1", DamageReceived.class).setParameter("raid",raidId).getResultList();
		raid.unitDeath = Server.cassandra().createQuery("SELECT d FROM UnitDeath d WHERE d.key.raid = :raid AND d.key.encounter = 1", UnitDeath.class).setParameter("raid",raidId).getResultList();
		return raid;
	}

	@Id
	public Integer id;

	@OneToMany(mappedBy = "raid")
	public List<DamageDealt> damageDealt;

	@OneToMany(mappedBy = "raid")
	public List<DamageReceived> damageReceived;

	@OneToMany(mappedBy = "raid")
	public List<UnitDeath> unitDeath;

}
