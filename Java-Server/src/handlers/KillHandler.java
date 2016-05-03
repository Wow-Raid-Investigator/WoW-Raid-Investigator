package handlers;

import java.util.HashMap;

import parser.Event;

public class KillHandler extends Handler {

	public KillHandler(Inserter inserter) {
		super(inserter);
	}

	@Override
	public void receive(Event event) {
		if (event.type.equals(Event.UNIT_DIED.class)) {
			HashMap<String, String> data = new HashMap<String, String>();

			data.put(Handler.RAID, Handler.RAID);
			data.put(Handler.ENCOUNTER, Handler.ENCOUNTER);
			data.put(Handler.LOGNO, Handler.LOGNO);
			data.put(Handler.TIMESTAMP, Long.toString(event.time));
			data.put("unit", "'" + event.data.get("UnitGUID") + "'");
			
			insert("unit_deaths",data);
		}
	}

}
