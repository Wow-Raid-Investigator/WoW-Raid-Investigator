package handlers;

import java.util.HashMap;

import com.datastax.driver.core.PreparedStatement;

import parser.Event;

public class SpellCastHandler extends Handler {

	PreparedStatement spells_cast;
	PreparedStatement spells_received;
	
	public SpellCastHandler(Inserter inserter) {
		super(inserter);
		spells_cast = inserter.getSession().prepare("insert into spells_cast (raid, encounter, logno, date, source, target, spell_id, spell_name, "
				+ "spell_school values (?, ? ,?, ?, ?, ?, ?, ?, ?)");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void receive(Event event) {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(Handler.RAID, Handler.RAID);
		data.put(Handler.ENCOUNTER, Handler.ENCOUNTER);
		data.put(Handler.LOGNO, Handler.LOGNO);
		data.put(Handler.TIMESTAMP, Long.toString(event.time));
		data.put(Handler.SOURCE_GUID, "'" + event.data.get("SourceGUID") + "'");
		data.put(Handler.TARGET_GUID, "'" + event.data.get("DestGUID") + "'");
		data.put("spell_id", event.data.get("CastSpellId"));
		data.put("spell_name", "'" + event.data.get("CastSpellName") + "'");
		data.put("spell_school", data.get("CastSpellSchool"));

		insert(spells_cast, data);
		insert("spells_received", data);
	}

}
