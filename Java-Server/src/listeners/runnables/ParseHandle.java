package listeners.runnables;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import handlers.DamageHandler;
import handlers.HealingHandler;
import parser.Event;
import parser.LogParser;

public class ParseHandle {
	public static void execute(String filename) {
		// Get ahold of the database

		// Change to use a dynamic IP later

		Cluster cluster = Cluster.builder().addContactPoint("137.112.104.121").build();
		Session session = cluster.connect("wowraid");

		ResultSet result = session.execute("SELECT max(raid) from metadata");
		
		LogParser parser = new LogParser(result.one().getInt(0) + 1);

		// Better way to register all the handlers?
		// We can't just do it once, since we might have
		// many parsers going at once

		DamageHandler damage = new DamageHandler(session);
		parser.register(damage, Event.SWING_DAMAGE.class);
		parser.register(damage, Event.RANGE_DAMAGE.class);
		parser.register(damage, Event.SPELL_DAMAGE.class);
		parser.register(damage, Event.SPELL_PERIODIC_DAMAGE.class);
		
		//parser.register(new KillHandler(inserter), Event.UNIT_DIED.class);
		
		HealingHandler heal = new HealingHandler(session);
		
		parser.register(heal, Event.SPELL_HEAL.class);
		parser.register(heal, Event.SPELL_PERIODIC_HEAL.class);
		
		//MetaHandler meta = new MetaHandler(inserter);
		
//		parser.register(parser, Event.ENCOUNTER_START.class);
//		parser.register(meta, Event.ENCOUNTER_START.class);
//		parser.register(meta, Event.ENCOUNTER_END.class);
		
//		SpellCastHandler spellCast = new SpellCastHandler(inserter);
//		
//		parser.register(spellCast, Event.SPELL_CAST_SUCCESS.class);
		
		parser.parseFile(filename);
		
		
		session.close();
	}
}
