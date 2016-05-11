package handlers;

import java.util.HashMap;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class Inserter {
	private Session session;
	
	protected final int raid;
	protected int encounter;
	
	private int index;
	
	public Inserter(Session session) {
		this.session = session;
		ResultSet result = session.execute("SELECT max(raid) from metadata");
		raid = result.one().getInt(0) + 1;
		this.encounter = 0;
		this.index = 0;
	}
	
	public void incrementEncounter() {
		this.encounter++;
		System.out.println("++");
	}
	
	public void incrementIndex() {
		this.index++;
	}
	
	public void insert(String table, HashMap<String,String> data) {
		String paramStr = "";
		String valueStr = "";
		
		for (String str : data.keySet()) {
			paramStr += str + ",";
			
			switch(str) {
			case Handler.RAID:
				valueStr += raid + ",";				
				break;
			case Handler.ENCOUNTER:
				valueStr += encounter + ",";
				break;
			case Handler.LOGNO:
				valueStr += index + ",";
				break;
			default:
				valueStr += data.get(str) + ",";
			}
		}
		
		paramStr = paramStr.substring(0,paramStr.length()-1);
		valueStr = valueStr.substring(0,valueStr.length()-1);
		
		session.execute("INSERT INTO " + table + "(" + paramStr + ") VALUES (" + valueStr +")");
	}
}
