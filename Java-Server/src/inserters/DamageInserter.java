package inserters;

import com.datastax.driver.core.Session;

import parser.Event;

public class DamageInserter extends Inserter {

	public DamageInserter(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(Event event) {
		if (event.type.equals(Event.SWING_DAMAGE.class)) {
			System.out.println("Saw a damage event.");
		}
	}

}
