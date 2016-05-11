package handlers;

import java.util.HashMap;

import parser.Event;

public class SpellCastHandler extends Handler {

	public SpellCastHandler(Inserter inserter) {
		super(inserter);
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
		data.put("spell_name", "'" + event.data.get("CastSpellName").replaceAll("'", "@") + "'");
		data.put("spell_school", data.get("CastSpellSchool"));

		insert("spells_cast", data);
		insert("spells_received", data);
	}

}
