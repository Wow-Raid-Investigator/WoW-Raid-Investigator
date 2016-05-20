package parser;

import java.util.ArrayList;
import java.util.List;

// We need to split by commas, but only when it isn't located
// inside of quotation marks.
public class QuoteSplitter {
	public static String[] split(String str, char on) {
		List<Integer> indexes = new ArrayList<Integer>();
		String result[];

		boolean working = true;
		
		indexes.add(-1);
		for (int i=0; i<str.length(); i++) {
			if (str.charAt(i) == '"') {
					working = !working;
			}
			
			if (working) {
				if (str.charAt(i) == on) {
					indexes.add(i);
				}
			}
		}
		
		indexes.add(str.length());
		
		result = new String[indexes.size()-1];
		
		int nonEmpty = 0;
		
		for (int i=0; i<indexes.size()-1; i++) {
			result[i] = str.substring(indexes.get(i)+1, indexes.get(i+1));
			if (!result[i].equals("")) {
				nonEmpty++;
			}
		}
	
		String trimmed[] = new String[nonEmpty];
		
		int j = 0;
		
		for (int i=0; i<result.length; i++) {
			if (!result[i].equals("")) {
				trimmed[j] = result[i];
				j++;
			}
		}
		
		if (trimmed.length == 0) {
			return new String[]{""};
		}
		return trimmed;
	}
}
