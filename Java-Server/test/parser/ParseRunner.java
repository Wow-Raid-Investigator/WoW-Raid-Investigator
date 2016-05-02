package parser;

import listeners.runnables.ParseHandle;

/*
 * Bypasses the network layer and just runs
 * the parser on the file, albeit via the
 * handler that the server normally uses
 */
public class ParseRunner {
	public static void main(String args[]) {
		ParseHandle.execute("test/SampleData.log");
	}
}
