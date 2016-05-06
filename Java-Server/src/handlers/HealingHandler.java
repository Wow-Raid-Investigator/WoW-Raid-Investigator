package handlers;

import java.util.HashMap;

import parser.Event;

public class HealingHandler extends Handler {
	private static final String HEALING = "healing";

	public HealingHandler(Inserter inserter) {
		super(inserter);
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
		data.put(HealingHandler.HEALING, event.data.get("HealAmount"));

		insert("healing_dealt", data);
		insert("healing_received", data);
	}
}
