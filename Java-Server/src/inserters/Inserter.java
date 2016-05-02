package inserters;

import java.util.HashMap;

import com.datastax.driver.core.Session;

public class Inserter {
	private Session session;
	private int index;
	public Inserter(Session session) {
		this.session = session;
		this.index = 0;
	}
	
	public void insert(String table, HashMap<String,String> data) {
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
		
		session.execute("INSERT INTO " + table + "(" + paramStr + ") VALUES (" + valueStr +")");
	}
}
