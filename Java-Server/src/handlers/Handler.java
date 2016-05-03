package handlers;

import java.util.HashMap;
import java.util.List;

import com.datastax.driver.core.Session;

import parser.Event;

/* 
 * An Inserter subscribes for particular event types, then
 * receives them whenever the parser encounters that event
 * 
 * Right now, they can only really work on one event at a time.
 * 
 * We'll construct one of these for every import run. Contains state
 * about the current raid and encounter #.
 */
public abstract class Handler {
	protected final Inserter inserter;

	public final static String RAID = "raid";
	public final static String ENCOUNTER = "encounter";
	public final static String LOGNO = "logno";
	public final static String TIMESTAMP = "timestamp";
	public final static String SOURCE_GUID = "source";
	public final static String TARGET_GUID = "target";
	
	public Handler(Inserter inserter) {
		this.inserter = inserter;
	}
	
	protected void insert(String table, HashMap<String,String> data) {
		inserter.insert(table, data);
	}
	
	public abstract void receive(Event event);
}
