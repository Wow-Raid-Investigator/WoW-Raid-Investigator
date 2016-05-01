package parser;

public class EventRunner {
	public static void main(String args[]) {
		try {
			Event result = Event.parseLine("3/6 22:46:16.911  SPELL_PERIODIC_HEAL,Player-76-0845BEA9,\"Bannr-Sargeras\",0x512,0x0,Player-70-08631D79,\"Oldandmoldy-Mannoroth\",0x514,0x0,48438,\"Wild Growth\",0x8,Player-70-08631D79,0000000000000000,307285,308460,0,6809,0,0,155083,160000,388.60,3587.81,672,731,0,0,nil,1");
			System.out.println(result.data);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
