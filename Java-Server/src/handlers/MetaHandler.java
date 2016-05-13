package handlers;

import java.util.ArrayList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import parser.Event;

public class MetaHandler extends Handler {

	private PreparedStatement metadata;
	private ArrayList<MetaHandlerContainer> containers;
	
	public MetaHandler(Session session) {
		super(session);
		containers = new ArrayList<MetaHandlerContainer>();
		metadata = this.session.prepare("insert into metadata (raid, encounter, timestamp, logno, description) "
				+ "values (?, ?, ?, ?, ?)");
	}

	@Override
	public void receive(Event event) {
		if (event.type == Event.ENCOUNTER_START.class) {
			MetaHandlerContainer container = new MetaHandlerContainer();
			
			container.timestamp = event.time;
			container.description = event.data.get("EncounterName");
			
			containers.add(container);
		}
	}

	public static class MetaHandlerContainer {
		public long timestamp;
		public String description;
		
		public BoundStatement getStatement(BoundStatement input) {
			input.setLong("timestamp", timestamp);
			input.setString("description", description);
			return input;
		}
	}

	@Override
	public void flush(int raid, int encounter) {
		System.out.println("flushing");
		for (MetaHandlerContainer container : containers) {
			BoundStatement bound_metadata = container.getStatement(metadata.bind());
			do_flush(raid, encounter, bound_metadata);
		}
		
		containers.clear();
	}
}
