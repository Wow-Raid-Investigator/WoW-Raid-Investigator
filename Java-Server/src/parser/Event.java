package parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The event data is pulled from
// https://github.com/NeilBostian/CombatLogParser/blob/master/CombatLogParser/EventInfo.cs
public class Event {
	public final Map<String,String> data;
	
	// no instantiation for you!
	
	private Event(Map<String,String> data) {
		this.data = data;
	}
	
	public static Event parseLine(String line) throws IllegalArgumentException, IllegalAccessException {
		List<Class> classes = Arrays.asList(Event.class.getDeclaredClasses());
		
		String noDate = line.replaceAll("^.*? .*?  ", "");
		String eventType = noDate.split(",")[0];
		
		String data[] = QuoteSplitter.split(noDate.replaceFirst("^.*?,", ""), ',');
		
		Class selected = null;
		for (Class clazz : classes) {
			String nameOnly = clazz.getName().replaceAll("parser\\.Event\\$","");
			
			if (nameOnly.equals(eventType)) {
				selected = clazz;
				break;
			}
		}
		
		System.out.println(selected.getName());
		
		List<String> fields = new ArrayList<String>();
		Map<String,String> fieldData = new HashMap<String,String>();
		
		for (Field field : selected.getFields()) {
			if (field.getType() == Integer.TYPE) {
				fields.add(field.getName());
				fieldData.put(field.getName(), data[field.getInt(null)]);
			}
		}
		return new Event(fieldData);
	}
	
	public class UnitKeys
    {
        public static final int SourceGUID = 0;
        public static final int SourceName = 1;
        public static final int SourceFlags = 2;
        public static final int SourceFlags2 = 3;
        public static final int DestGUID = 4;
        public static final int DestName = 5;
        public static final int DestFlags = 6;
        public static final int DestFlags2 = 7;
    }

    public class ENCOUNTER_START
    {
        public static final int EncounterId = 0;
        public static final int EncounterName = 1;
        public static final int DifficultyId = 2;
        public static final int RaidSize = 3;
        public final boolean HasUnitKeys = false;
    }

    public class ENCOUNTER_END
    {
        public static final int EncounterId = 0;
        public static final int EncounterName = 1;
        public static final int DifficultyId = 2;
        public static final int RaidSize = 3;
        public static final int Wiped = 4;
        public final boolean HasUnitKeys = false;
    }

    public class SPELL_CAST_SUCCESS
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_CAST_START
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public final boolean HasUnitKeys = false;
    }

    public class SPELL_CAST_FAILED
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int CastFailedReason = 11;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_ENERGIZE
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public static final int ResourceGain = 24;
        public static final int ResourceType = 25;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_PERIODIC_ENERGIZE //unsure of 17
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public static final int ResourceGain = 24;
        public static final int ResourceType = 25;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_SUMMON
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_AURA_APPLIED
    {
        public static final int AuraSpellId = 8;
        public static final int AuraSpellName = 9;
        public static final int AuraSpellSchool = 10;
        public static final int AuraBuffType = 11;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_AURA_APPLIED_DOSE
    {
        public static final int AuraSpellId = 8;
        public static final int AuraSpellName = 9;
        public static final int AuraSpellSchool = 10;
        public static final int AuraBuffType = 11;
        public static final int AuraDosesAdded = 12;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_AURA_REMOVED
    {
        public static final int AuraSpellId = 8;
        public static final int AuraSpellName = 9;
        public static final int AuraSpellSchool = 10;
        public static final int AuraBuffType = 11;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_AURA_REFRESH
    {
        public static final int AuraSpellId = 8;
        public static final int AuraSpellName = 9;
        public static final int AuraSpellSchool = 10;
        public static final int AuraBuffType = 11;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_AURA_REMOVED_DOSE
    {
        public static final int AuraSpellId = 8;
        public static final int AuraSpellName = 9;
        public static final int AuraSpellSchool = 10;
        public static final int AuraBuffType = 11;
        public static final int AuraDosesRemoved = 12;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_AURA_BROKEN_SPELL
    {
        public static final int CastAuraSpellId = 8;
        public static final int CastAuraSpellName = 9;
        public static final int CastAuraSpellSchool = 10;
        public static final int RemovedAuraSpellId = 11;
        public static final int RemovedAuraSpellName = 12;
        public static final int RemovedAuraSpellSchool = 13;
        public static final int CastAuraBuffType = 14;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_MISSED //12-13 unknown
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int MissedReason = 11;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_DAMAGE //27-33 unknown
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public static final int DamageDone = 24;
        public static final int Overkill = 25;
        public static final int DamageSpellSchool = 26;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_PERIODIC_DAMAGE //27-33 unknown
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public static final int DamageDone = 24;
        public static final int Overkill = 25;
        public static final int DamageSpellSchool = 26;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_PERIODIC_MISSED //12-14 unknown
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int MissedReason = 11;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_ABSORBED //8-15 unknown
    {
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_HEAL //27-28 unknown
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public static final int HealAmount = 24;
        public static final int Overheal = 25;
        public static final int HealSpellSchool = 26;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_PERIODIC_HEAL //27-28 unknown
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public static final int HealAmount = 24;
        public static final int Overheal = 25;
        public static final int HealSpellSchool = 26;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_CREATE
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_INTERRUPT
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int InterruptedSpellId = 11;
        public static final int InterruptedSpellName = 12;
        public static final int InterruptedSpellSpellSchool = 13;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_RESURRECT
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public final boolean HasUnitKeys = true;
    }

    public class SPELL_INSTAKILL
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public final boolean HasUnitKeys = true;
    }

    public class RANGE_DAMAGE //unknown 26-33
    {
        public static final int CastSpellId = 8;
        public static final int CastSpellName = 9;
        public static final int CastSpellSchool = 10;
        public static final int TargetGUID = 11;
        public static final int TargetGUIDOwner = 12;
        public static final int TargetHP = 13;
        public static final int TargetMaxHP = 14;
        public static final int TargetAttackPower = 15;
        public static final int TargetSpellPower = 16;
        public static final int TargetResolve = 17;
        public static final int CastSpellResource = 18;
        public static final int CurrentResource = 19;
        public static final int MaxResource = 20;
        public static final int TargetPosX = 21;
        public static final int TargetPosY = 22;
        public static final int TargetItemLvl = 23;
        public static final int DamageDone = 24;
        public static final int Overkill = 25;
        //26 is probably damage spell school
        public final boolean HasUnitKeys = true;
    }

    public class SWING_DAMAGE //24-30 unknown
    {
        public static final int TargetGUID = 8;
        public static final int TargetGUIDOwner = 9;
        public static final int TargetHP = 10;
        public static final int TargetMaxHP = 11;
        public static final int TargetAttackPower = 12;
        public static final int TargetSpellPower = 13;
        public static final int TargetResolve = 14;
        public static final int CastSpellResource = 15;
        public static final int CurrentResource = 16;
        public static final int MaxResource = 17;
        public static final int TargetPosX = 18;
        public static final int TargetPosY = 19;
        public static final int TargetItemLvl = 20;
        public static final int DamageDone = 21;
        public static final int Overkill = 22;
        public static final int DamageSpellSchool = 23;
        public final boolean HasUnitKeys = true;
    }
    public class SWING_DAMAGE_LANDED //24-30 unknown
    {
        public static final int TargetGUID = 8;
        public static final int TargetGUIDOwner = 9;
        public static final int TargetHP = 10;
        public static final int TargetMaxHP = 11;
        public static final int TargetAttackPower = 12;
        public static final int TargetSpellPower = 13;
        public static final int TargetResolve = 14;
        public static final int CastSpellResource = 15;
        public static final int CurrentResource = 16;
        public static final int MaxResource = 17;
        public static final int TargetPosX = 18;
        public static final int TargetPosY = 19;
        public static final int TargetItemLvl = 20;
        public static final int DamageDone = 21;
        public static final int Overkill = 22;
        public static final int DamageSpellSchool = 23;
        public final boolean HasUnitKeys = true;
    }
    public class SWING_MISSED //9-11 missing
    {
        public static final int MissedReason = 8;
        public final boolean HasUnitKeys = true;
    }

    public class UNIT_DIED
    {
        public static final int UnitGUID = 4;
        public static final int UnitName = 5;
        public static final int UnitSourceFlags = 6;
        public static final int UnitSourceFlags2 = 7;
        public final boolean HasUnitKeys = true;
    }

    public class UNIT_DESTROYED
    {
        public static final int UnitGUID = 4;
        public static final int UnitName = 5;
        public static final int UnitFlags = 6;
        public static final int UnitFlags2 = 7;
        public final boolean HasUnitKeys = true;
    }

    public class PARTY_KILL
    {
        public static final int FriendlyGUID = 0;
        public static final int FriendlyName = 1;
        public static final int FriendlyFlags = 2;
        public static final int FriendlyFlags2 = 3;
        public static final int EnemyGUID = 4;
        public static final int EnemyName = 5;
        public static final int EnemyFlags = 6;
        public static final int EnemyFlags2 = 7;
        public final boolean HasUnitKeys = true;
    }

    public class ENVIRONMENTAL_DAMAGE //22-31 unknown
    {
        public static final int TargetGUID = 8;
        public static final int TargetGUIDOwner = 9;
        public static final int TargetHP = 10;
        public static final int TargetMaxHP = 11;
        public static final int TargetAttackPower = 12;
        public static final int TargetSpellPower = 13;
        public static final int TargetResolve = 14;
        public static final int CastSpellResource = 15;
        public static final int CurrentResource = 16;
        public static final int MaxResource = 17;
        public static final int TargetPosX = 18;
        public static final int TargetPosY = 19;
        public static final int TargetItemLvl = 20;
        public static final int DamageName = 21;
        public final boolean HasUnitKeys = true;
    }

    public class RANGE_MISSED //12-13 unknown
    {
        public static final int SpellId = 8;
        public static final int SpellName = 9;
        public static final int SpellSchool = 10;
        public static final int MissedReason = 11;
        public final boolean HasUnitKeys = true;
    }
}
