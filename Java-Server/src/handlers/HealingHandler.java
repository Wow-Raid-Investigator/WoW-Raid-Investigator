package handlers;

import java.util.ArrayList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import parser.Event;

public class HealingHandler extends Handler {

	private ArrayList<HealingHandlerContainer> containers;

	private PreparedStatement healing_dealt;
	private PreparedStatement healing_taken;

	public HealingHandler(Session session) {
		super(session);
		containers = new ArrayList<HealingHandlerContainer>();
		healing_dealt = this.session.prepare(
				"insert into healing_dealt (raid, encounter, logno, timestamp, source, target, healing, spell_id, critical, multistrike) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		healing_taken = this.session.prepare(
				"insert into healing_taken (raid, encounter, logno, timestamp, source, target, healing, spell_id, critical, multistrike) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	}

	@Override
	public void receive(Event event) {
		HealingHandlerContainer container = new HealingHandlerContainer();

		container.timestamp = event.time;
		container.source = event.data.get("SourceGUID");
		container.target = event.data.get("TargetGUID");
		container.healing = Integer.valueOf(event.data.get("HealAmount"));
		container.spell_id = Integer.valueOf(event.data.get("CastSpellId"));
		container.critical = event.data.get("Critical").equals("1");
		container.multistrike = event.data.get("Multistrike").equals("1");

		containers.add(container);

	}

	public static class HealingHandlerContainer implements Container {

		public long timestamp;
		public String source;
		public String target;
		public int healing;
		public int spell_id;
		public boolean critical;
		public boolean multistrike;

		@Override
		public BoundStatement getStatement(BoundStatement input) {
			input.setLong("timestamp", timestamp);
			input.setString("source", source);
			input.setString("target", target);
			input.setInt("healing", healing);
			input.setInt("spell_id", spell_id);
			input.setBool("critical", critical);
			input.setBool("multistrike", multistrike);

			return input;
		}
	}

	@Override
	public void flush(int raid, int encounter) {
		for (HealingHandlerContainer container : containers) {
			BoundStatement bound_healing_dealt = container.getStatement(healing_dealt.bind());
			BoundStatement bound_healing_taken = container.getStatement(healing_taken.bind());

			do_flush(raid, encounter, bound_healing_dealt);
			do_flush(raid, encounter, bound_healing_taken);
		}
		
		containers.clear();
	}
}
