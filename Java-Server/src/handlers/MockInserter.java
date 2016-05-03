package handlers;

import java.util.HashMap;

import com.datastax.driver.core.Session;

// Just used for testing. Prints instead of
// trying to hit the database
public class MockInserter extends Inserter {

	private int index;
	public MockInserter(Session session, int raid) {
		super(session, raid);
		index = 1;
	}

	@Override
	public void insert(String table, HashMap<String, String> data) {
		String paramStr = "";
		String valueStr = "";
		
		index++;
		
		for (String str : data.keySet()) {
			paramStr += str + ",";
			if (str.equals(Handler.LOGNO)) {
				valueStr += "'" + index + "',";
			} else {
				valueStr += "'" + data.get(str) + "',";
			}
			
		}
		
		paramStr = paramStr.substring(0,paramStr.length()-1);
		valueStr = valueStr.substring(0,valueStr.length()-1);
		
		System.out.println("INSERT INTO " + table + "(" + paramStr + ") VALUES (" + valueStr +")");
	}

}
