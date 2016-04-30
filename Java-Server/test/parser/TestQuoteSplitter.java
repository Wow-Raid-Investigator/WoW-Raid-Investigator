package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestQuoteSplitter {
	@Test
	public void testEmpty() {
		String input = "";
		
		String result[] = QuoteSplitter.split(input, ',');
		
		assertArrayEquals(new String[]{""},result);
	}
	
	@Test
	public void testSimple() {
		String input = "ABC,DEF";
		
		String result[] = QuoteSplitter.split(input, ',');
		
		assertArrayEquals(new String[]{"ABC","DEF"}, result);
	}
	
	@Test
	public void testQuotes() {
		String input = "ABC,DE\"FE,\",G";
		
		String result[] = QuoteSplitter.split(input, ',');
		
		assertArrayEquals(new String[]{"ABC","DE\"FE,\"","G"}, result);
	}
}
