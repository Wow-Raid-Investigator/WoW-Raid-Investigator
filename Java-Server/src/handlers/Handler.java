package handlers;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;

import parser.Event;
import parser.WowEventListener;

/* 
 * An Inserter subscribes for particular event types, then
 * receives them whenever the parser encounters that event
 * 
 * Right now, they can only really work on one event at a time.
 * 
 * We'll construct one of these for every import run. Contains state
 * about the current raid and encounter #.
 */
public abstract class Handler implements WowEventListener {
	
	public final static String RAID = "raid";
	public final static String ENCOUNTER = "encounter";
	public final static String LOGNO = "logno";
	public final static String TIMESTAMP = "timestamp";
	public final static String SOURCE_GUID = "source";
	public final static String TARGET_GUID = "target";
	
	protected Session session;
	private int index;
	
	public Handler(Session session) {
		this.session = session;
		this.index = 1;
	}
	
	public abstract void flush(int raid, int encounter);
	
	protected void do_flush(int raid, int encounter, BoundStatement statement) {
		statement.setInt("raid", raid);
		statement.setInt("encounter", encounter);
		statement.setInt("logno", index);
		this.session.execute(statement);
		index++;
	}
	
	public abstract void receive(Event event);
}
