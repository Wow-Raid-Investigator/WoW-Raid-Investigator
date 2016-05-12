package handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import parser.Event;

public class DamageHandler extends Handler {

	private PreparedStatement damage_dealt;
	private PreparedStatement damage_taken;

	public static void main(String args[]) {
		new DamageHandler.DamageHandlerContainer().getStatement(null);
	}

	private ArrayList<DamageHandlerContainer> containers;

	public DamageHandler(Session session) {
		super(session);
		containers = new ArrayList<DamageHandlerContainer>();
		damage_dealt = this.session.prepare(
				"insert into damage_dealt (raid, encounter, logno, timestamp, source, target, damage, spell_id) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?)");
		damage_taken = this.session.prepare(
				"insert into damage_taken (raid, encounter, logno, timestamp, source, target, damage, spell_id) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?)");
	}

	@Override
	public void receive(Event event) {
		DamageHandlerContainer container = new DamageHandlerContainer();
		
		container.timestamp = event.time;
		container.source = event.data.get("SourceGUID");
		container.target = event.data.get("TargetGUID");
		container.damage = Integer.valueOf(event.data.get("DamageDone"));
		if (event.type == Event.SWING_DAMAGE.class) {
			container.spell_id = 6603;
		} else if (event.type == Event.RANGE_DAMAGE.class) {
			container.spell_id = 75;
		} else {
			container.spell_id = Integer.valueOf(event.data.get("CastSpellId"));
		}
		
		containers.add(container);

	}

	public static class DamageHandlerContainer implements Container {

		public long timestamp;
		public String source;
		public String target;
		public int damage;
		public int spell_id;

		@Override
		public BoundStatement getStatement(BoundStatement input) {
			input.setLong("timestamp", timestamp);
			input.setString("source", source);
			input.setString("target", target);
			input.setInt("damage", damage);
			input.setInt("spell_id", spell_id);

			return input;
		}
	}

	@Override
	public void flush(int raid, int encounter) {
		for (DamageHandlerContainer container : containers) {
			BoundStatement bound_damage_dealt = container.getStatement(damage_dealt.bind());
			BoundStatement bound_damage_taken = container.getStatement(damage_taken.bind());

			do_flush(raid, encounter, bound_damage_dealt);
			do_flush(raid, encounter, bound_damage_taken);
		}
	}

}
