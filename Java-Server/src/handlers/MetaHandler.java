package handlers;

import java.util.HashMap;

import parser.Event;

public class MetaHandler extends Handler {

	public MetaHandler(Inserter inserter) {
		super(inserter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(Event event) {
		if (event.type.equals(Event.ENCOUNTER_START.class)) {
			HashMap<String, String> data = new HashMap<String, String>();

			data.put(Handler.RAID, Handler.RAID);
			data.put(Handler.ENCOUNTER, Handler.ENCOUNTER);
			data.put("date", Long.toString(event.time));
			data.put("description", "'" + event.data.get("EncounterName").replaceAll("\"", "") + "'");
			
			this.insert("metadata", data);
		}
	}

}
