package handlers;

import java.util.HashMap;

import com.datastax.driver.core.Session;

public class Inserter {
	private Session session;
	
	protected final int raid;
	protected int encounter;
	
	private int index;
	
	public Inserter(Session session, int raid) {
		this.session = session;
		this.raid = raid;
		this.encounter = 0;
		this.index = 0;
	}
	
	public void incrementEncounter() {
		this.encounter++;
	}
	
	public void incrementIndex() {
		this.index++;
	}
	
	public void insert(String table, HashMap<String,String> data) {
		String paramStr = "";
		String valueStr = "";
		
		for (String str : data.keySet()) {
			paramStr += str + ",";
			if (str.equals(Handler.RAID)) {
				valueStr += raid + ",";
			} else if (str.equals(Handler.ENCOUNTER)){
				valueStr += encounter + ",";
			} else if (str.equals(Handler.LOGNO)) {
				valueStr += index + ",";
			} else {
				valueStr += data.get(str) + ",";
			}
			
		}
		
		paramStr = paramStr.substring(0,paramStr.length()-1);
		valueStr = valueStr.substring(0,valueStr.length()-1);
		
		session.execute("INSERT INTO " + table + "(" + paramStr + ") VALUES (" + valueStr +")");
	}
}
