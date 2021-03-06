package parser;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The event data is pulled from
// https://github.com/NeilBostian/CombatLogParser/blob/master/CombatLogParser/EventInfo.cs
public class Event {
	public final static boolean HasUnitKeys = false;
	public final Map<String, String> data;
	public final Class<?> type;
	public final long time;
	// no instantiation for you!

	private Event() {
		this.data = new HashMap<String, String>();
		this.type = Event.class;
		this.time = 0;
	}

	private Event(Map<String, String> data, Class<?> type, long time) {
		this.data = data;
		this.type = type;
		this.time = time;
	}

	@SuppressWarnings("deprecation")
	public static Event parseLine(String line) throws IllegalArgumentException, IllegalAccessException {
		List<Class<?>> classes = Arrays.asList(Event.class.getDeclaredClasses());
		
		long time;
		
		String date = line.replaceAll("  .*", "");

		SimpleDateFormat parser = new SimpleDateFormat("dd/MM HH:mm:ss.SSS");

		try {
			Date dateVal = parser.parse(date);
			dateVal.setYear(2016-1900);
			time = dateVal.getTime();
			System.out.println(dateVal);
		} catch (ParseException e1) {
			System.out.println("Couldn't parse a date!");
			time = 0;
		}

		String noDate = line.replaceAll("^.*? .*?  ", "");
		String eventType = noDate.split(",")[0];

		String data[] = QuoteSplitter.split(noDate.replaceFirst("^.*?,", ""), ',');

		Class<?> selected = null;
		for (Class<?> clazz : classes) {
			String nameOnly = clazz.getName().replaceAll("parser\\.Event\\$", "");

			if (nameOnly.equals(eventType)) {
				selected = clazz;
				break;
			}
		}

		if (selected == null) {
			// Some kind of event we don't know about. Ignore it
			return new Event();
		}

		Map<String, String> fieldData = new HashMap<String, String>();

		// Check if this thing has the "basic" keys

		// I put this before the other loop so that
		// if the schema below overwrites any fields,
		// the unique ones from the particular event type
		// will win out. DestGUID is overwritten by UnitGUID, for example.
		try {
			if (selected.getField("HasUnitKeys").getBoolean(null)) {
				for (Field field : UnitKeys.class.getFields()) {
					fieldData.put(field.getName(), data[field.getInt(null)]);
				}
			}
		} catch (NoSuchFieldException | SecurityException e) {
			// This shouldn't really happen, since this
			// only gets invoked if we find a class that matches the
			// event name

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Field field : selected.getFields()) {
			if (field.getType() == Integer.TYPE) {
				fieldData.put(field.getName(), data[field.getInt(null)]);
			}
		}

		return new Event(fieldData, selected, time);
	}

	public class UnitKeys {
		public static final int SourceGUID = 0;
		public static final int SourceName = 1;
		public static final int SourceFlags = 2;
		public static final int SourceFlags2 = 3;
		public static final int DestGUID = 4;
		public static final int DestName = 5;
		public static final int DestFlags = 6;
		public static final int DestFlags2 = 7;
	}

	public class ENCOUNTER_START {
		public static final int EncounterId = 0;
		public static final int EncounterName = 1;
		public static final int DifficultyId = 2;
		public static final int RaidSize = 3;
		public static final boolean HasUnitKeys = false;
	}

	public class ENCOUNTER_END {
		public static final int EncounterId = 0;
		public static final int EncounterName = 1;
		public static final int DifficultyId = 2;
		public static final int RaidSize = 3;
		public static final int Wiped = 4;
		public static final boolean HasUnitKeys = false;
	}

	public class SPELL_CAST_SUCCESS {
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
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_CAST_START {
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_CAST_FAILED {
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final int CastFailedReason = 11;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_ENERGIZE {
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
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_PERIODIC_ENERGIZE extends Event // unsure of 17
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
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_SUMMON {
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_AURA_APPLIED {
		public static final int AuraSpellId = 8;
		public static final int AuraSpellName = 9;
		public static final int AuraSpellSchool = 10;
		public static final int AuraBuffType = 11;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_AURA_APPLIED_DOSE {
		public static final int AuraSpellId = 8;
		public static final int AuraSpellName = 9;
		public static final int AuraSpellSchool = 10;
		public static final int AuraBuffType = 11;
		public static final int AuraDosesAdded = 12;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_AURA_REMOVED {
		public static final int AuraSpellId = 8;
		public static final int AuraSpellName = 9;
		public static final int AuraSpellSchool = 10;
		public static final int AuraBuffType = 11;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_AURA_REFRESH {
		public static final int AuraSpellId = 8;
		public static final int AuraSpellName = 9;
		public static final int AuraSpellSchool = 10;
		public static final int AuraBuffType = 11;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_AURA_REMOVED_DOSE {
		public static final int AuraSpellId = 8;
		public static final int AuraSpellName = 9;
		public static final int AuraSpellSchool = 10;
		public static final int AuraBuffType = 11;
		public static final int AuraDosesRemoved = 12;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_AURA_BROKEN_SPELL {
		public static final int CastAuraSpellId = 8;
		public static final int CastAuraSpellName = 9;
		public static final int CastAuraSpellSchool = 10;
		public static final int RemovedAuraSpellId = 11;
		public static final int RemovedAuraSpellName = 12;
		public static final int RemovedAuraSpellSchool = 13;
		public static final int CastAuraBuffType = 14;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_MISSED extends Event // 12-13 unknown
	{
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final int MissedReason = 11;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_DAMAGE extends Event // 27-33 unknown
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
		public static final int Resisted = 26;
		public static final int Blocked = 27;
		public static final int Absorbed = 28;
		public static final int Critical = 29;
		public static final int Glancing = 30;
		public static final int Crushing = 31;
		public static final int IsOffHand = 32;
		public static final int Multistrike = 33;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_PERIODIC_DAMAGE extends Event // 27-33 unknown
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
		public static final int Resisted = 26;
		public static final int Blocked = 27;
		public static final int Absorbed = 28;
		public static final int Critical = 29;
		public static final int Glancing = 30;
		public static final int Crushing = 31;
		public static final int IsOffHand = 32;
		public static final int Multistrike = 33;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_PERIODIC_MISSED extends Event // 12-14 unknown
	{
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final int MissedReason = 11;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_ABSORBED extends Event // 8-15 unknown
	{
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_HEAL extends Event // 27-28 unknown
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
		public static final int Critical = 27;
		public static final int Multistrike = 28;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_PERIODIC_HEAL extends Event // 27-28 unknown
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
		public static final int Critical = 27;
		public static final int Multistrike = 28;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_CREATE {
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_INTERRUPT {
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final int InterruptedSpellId = 11;
		public static final int InterruptedSpellName = 12;
		public static final int InterruptedSpellSpellSchool = 13;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_RESURRECT {
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final boolean HasUnitKeys = true;
	}

	public class SPELL_INSTAKILL {
		public static final int CastSpellId = 8;
		public static final int CastSpellName = 9;
		public static final int CastSpellSchool = 10;
		public static final boolean HasUnitKeys = true;
	}

	public class RANGE_DAMAGE extends Event // unknown 26-33
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
		public static final int Resisted = 26;
		public static final int Blocked = 27;
		public static final int Absorbed = 28;
		public static final int Critical = 29;
		public static final int Glancing = 30;
		public static final int Crushing = 31;
		public static final int IsOffHand = 32;
		public static final int Multistrike = 33;
		// 26 is probably damage spell school
		public static final boolean HasUnitKeys = true;
	}

	public class SWING_DAMAGE extends Event // 24-30 unknown
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
		public static final int Resisted = 23;
		public static final int Blocked = 24;
		public static final int Absorbed = 25;
		public static final int Critical = 26;
		public static final int Glancing = 27;
		public static final int Crushing = 28;
		public static final int IsOffHand = 29;
		public static final int Multistrike = 30;
		public static final boolean HasUnitKeys = true;
	}

	public class SWING_DAMAGE_LANDED extends Event // 24-30 unknown
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
		public static final boolean HasUnitKeys = true;
	}

	public class SWING_MISSED extends Event // 9-11 missing
	{
		public static final int MissedReason = 8;
		public static final boolean HasUnitKeys = true;
	}

	public class UNIT_DIED {
		public static final int UnitGUID = 4;
		public static final int UnitName = 5;
		public static final int UnitSourceFlags = 6;
		public static final int UnitSourceFlags2 = 7;
		public static final boolean HasUnitKeys = true;
	}

	public class UNIT_DESTROYED {
		public static final int UnitGUID = 4;
		public static final int UnitName = 5;
		public static final int UnitFlags = 6;
		public static final int UnitFlags2 = 7;
		public static final boolean HasUnitKeys = true;
	}

	public class PARTY_KILL {
		public static final int FriendlyGUID = 0;
		public static final int FriendlyName = 1;
		public static final int FriendlyFlags = 2;
		public static final int FriendlyFlags2 = 3;
		public static final int EnemyGUID = 4;
		public static final int EnemyName = 5;
		public static final int EnemyFlags = 6;
		public static final int EnemyFlags2 = 7;
		public static final boolean HasUnitKeys = true;
	}

	public class ENVIRONMENTAL_DAMAGE extends Event // 22-31 unknown
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
		public static final boolean HasUnitKeys = true;
	}

	public class RANGE_MISSED extends Event // 12-13 unknown
	{
		public static final int SpellId = 8;
		public static final int SpellName = 9;
		public static final int SpellSchool = 10;
		public static final int MissedReason = 11;
		public static final boolean HasUnitKeys = true;
	}
}
