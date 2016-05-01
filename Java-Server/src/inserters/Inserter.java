package inserters;

import java.util.List;

import com.datastax.driver.core.Session;

import parser.Event;

/* 
 * An Inserter subscribes for particular event types, then
 * receives them whenever the parser encounters that event
 * 
 * Right now, they can only really work on one event at a time.
 */
public abstract class Inserter {
	protected Session session;
	protected int encounter;
	
	public Inserter(Session session) {
		this.session = session;
		this.encounter = 1;
	}
	
	protected void insert(String table, List<String> params, List<String> values) {
		String paramStr = "";
		String valueStr = "";
		
		for (String str : params) {
			paramStr += str + ",";
		}
		
		for (String str : values) {
			valueStr += "'" + str + "',";
		}
		
		paramStr = paramStr.substring(0,paramStr.length()-2);
		valueStr = valueStr.substring(0,valueStr.length()-2);
		
		session.execute("INSERT INTO " + table + "(" + paramStr + ") VALUES (" + valueStr +")");
	}
	
	public abstract void receive(Event event);
}
