package handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.datastax.driver.core.Session;

import parser.Event;

public class DamageHandler extends Handler {

	private static final String DAMAGE = "damage";
	
	public DamageHandler(Inserter inserter) {
		super(inserter);
	}

	@Override
	public void receive(Event event) {
		if (event.type.equals(Event.SWING_DAMAGE.class)) {
			HashMap<String,String> data = new HashMap<String,String>();
			
			data.put(Handler.RAID, Handler.RAID);
			data.put(Handler.ENCOUNTER, Handler.ENCOUNTER);
			data.put(Handler.LOGNO, Handler.LOGNO);
			data.put(Handler.TIMESTAMP, Long.toString(event.time));
			data.put(Handler.SOURCE_GUID, "'" + event.data.get("SourceGUID") + "'");
			data.put(Handler.TARGET_GUID, "'" + event.data.get("TargetGUID") + "'");
			data.put(DamageHandler.DAMAGE, event.data.get("DamageDone"));
			
			insert("damage_dealt",data);
		}
	}

}
