package parser;

import inserters.DamageHandler;
import inserters.MockInserter;

public class EventRunner {
	public static void main(String args[]) {

		LogParser parser = new LogParser();
		parser.register(new DamageHandler(new MockInserter(null), 1, 1), Event.SWING_DAMAGE.class);

		parser.parse(
				"3/6 22:46:17.156  SWING_DAMAGE,Pet-0-3020-1205-27189-58965-01021E6623,\"Lazatik\",0x1114,0x0,Vehicle-0-3020-1205-27189-76877-00007A6F46,\"Gruul\",0x10a48,0x0,Pet-0-3020-1205-27189-58965-01021E6623,Player-3693-07439366,196617,206208,4590,6892,0,3,111,200,385.42,3595.29,677,2864,-1,1,0,0,0,nil,nil,nil,nil");

	}
}
