package handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import handlers.DamageHandler.DamageHandlerContainer;
import parser.Event;

public class HealingHandler extends Handler {
	private static final String HEALING = "healing";

	private ArrayList<HealingHandlerContainer> containers;

	private PreparedStatement healing_dealt;
	private PreparedStatement healing_taken;

	public HealingHandler(Session session) {
		super(session);
		containers = new ArrayList<HealingHandlerContainer>();
		healing_dealt = this.session.prepare(
				"insert into healing_dealt (raid, encounter, logno, timestamp, source, target, healing, spell_id) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?)");
		healing_taken = this.session.prepare(
				"insert into healing_taken (raid, encounter, logno, timestamp, source, target, healing, spell_id) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?)");
	}

	@Override
	public void receive(Event event) {
		HealingHandlerContainer container = new HealingHandlerContainer();

		container.timestamp = event.time;
		container.source = event.data.get("SourceGUID");
		container.target = event.data.get("TargetGUID");
		container.healing = Integer.valueOf(event.data.get("HealAmount"));
		container.spell_id = Integer.valueOf(event.data.get("CastSpellId"));

		containers.add(container);

	}

	public static class HealingHandlerContainer implements Container {

		public long timestamp;
		public String source;
		public String target;
		public int healing;
		public int spell_id;

		@Override
		public BoundStatement getStatement(BoundStatement input) {
			input.setLong("timestamp", timestamp);
			input.setString("source", source);
			input.setString("target", target);
			input.setInt("healing", healing);
			input.setInt("spell_id", spell_id);

			return input;
		}
	}

	@Override
	public void flush(int raid, int encounter) {
		for (HealingHandlerContainer container : containers) {
			BoundStatement bound_healing_dealt = container.getStatement(healing_dealt.bind());
			BoundStatement bound_healing_taken = container.getStatement(healing_taken.bind());

			bound_healing_dealt.setInt("raid", raid);
			bound_healing_taken.setInt("raid", raid);

			bound_healing_dealt.setInt("encounter", encounter);
			bound_healing_taken.setInt("encounter", encounter);

			bound_healing_dealt.setInt("logno", index);
			bound_healing_taken.setInt("logno", index);

			this.session.execute(bound_healing_dealt);
			this.session.execute(bound_healing_taken);

			this.index++;
		}
	}
}
