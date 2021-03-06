﻿using Cassandra;
using Perst;
using System;
using System.Collections.Generic;
using Wow_Raid.LogClasses;
using Wow_Raid.Stat;
using StackExchange.Redis;

namespace Wow_Raid
{
    public class Perst
    {
        private static Perst instance;
        private static readonly object padlock = new object();
        private static IDatabase redis = ConnectionMultiplexer.Connect("wow-raid-1.csse.rose-hulman.edu:6379").GetDatabase();
        private static Dictionary<string, string> guidMap = new Dictionary<string, string>();
        private static Dictionary<int, string> spellMap = new Dictionary<int, string>();

        public static Perst Instance
        {
            get
            {
                if(instance == null)
                {
                    lock(padlock)
                    {
                        if (instance == null)
                        {
                            instance = new Perst();
                        }
                    }
                }

                return instance;
            }
        }

        public string GetRedisTestValue(string key)
        {
            return redis.HashGet("spells", key);
        }

        internal HealingEvent[] getHealingForRaidEncounter(int raid, int encounter, bool forceRefresh = false)
        {
            if (forceRefresh)
            {
                List<HealingEvent> list = HealingEvent.convert(Cassandra.Instance.GetHealingForRaidEncounter(raid, encounter));
                foreach (HealingEvent evt in list)
                {
                    healingIndex.Put(evt.getKey(), evt);
                }
                return list.ToArray();
            }

            object[] temp = healingIndex.GetPrefix(HealingEvent.getRaidEncounterPrefix(raid, encounter));

            if (temp.Length == 0)
                return getHealingForRaidEncounter(raid, encounter, true);

            return convertToList<HealingEvent>(temp).ToArray();
        }

        public UnitTotalHealing[] getInvolvedUnitsHealing(int raid, int encounter, bool foreceRefresh = false)
        {
            List<UnitTotalHealing> array = convertToList<UnitTotalHealing>(healingIndex.GetPrefix(String.Format("TOTAL:{0}:{1}:", raid, encounter)));

            if (array.Count == 0)
            {
                try
                {
                    getTotalHealingBySource(raid, encounter, "", true);
                }
                catch (ArgumentOutOfRangeException) { }

                array = convertToList<UnitTotalHealing>(healingIndex.GetPrefix(String.Format("TOTAL:{0}:{1}:", raid, encounter)));
            }

            return array.ToArray();
        }

        private UnitTotalHealing getTotalHealingBySource(int raid, int encounter, String Source, bool forceRefresh = false)
        {
            UnitTotalHealing healing;
            if (forceRefresh)
            {
                HealingEvent[] events = getHealingForRaidEncounter(raid, encounter);

                foreach (UnitTotalHealing unit in HealingEvent.groupBySource(events))
                {
                    healingIndex.Set(String.Format(String.Format("TOTAL:{0}:{1}:{2}:", raid, encounter, unit.Source)), unit);
                }

                healing = (UnitTotalHealing)healingIndex.Get(String.Format("TOTAL:{0}:{1}:{2}:", raid, encounter, Source));

                if (healing == null)
                    throw new ArgumentOutOfRangeException(Source + " does not exist in the encounter");
                else
                    return healing;
            }

            healing = (UnitTotalHealing)healingIndex.Get(String.Format("TOTAL:{0}:{1}:{2}:", raid, encounter, Source));

            if (healing == null)
            {
                return getTotalHealingBySource(raid, encounter, Source, true);
            }
            else
            {
                return (UnitTotalHealing)healing;
            }
        }

        private Index root;
        private Storage db;
        private Index damageIndex;
        private Index healingIndex;

        private Perst ()
        {
            db = StorageFactory.Instance.CreateStorage();
            db.Open("storage.dbs", 1073741824L);

            root = (Index)db.Root;

    

            if (root == null)
            {
                root = db.CreateIndex(typeof(string), true);
                db.Root = root;

                damageIndex = db.CreateIndex(typeof(string), false);
                root.Put("DamageIndex", damageIndex);

                healingIndex = db.CreateIndex(typeof(string), false);
                root.Put("HealingIndex", healingIndex);
            }
            else
            {
                damageIndex = (Index)root.Get("DamageIndex");
                healingIndex = (Index)root.Get("HealingIndex");
            }
            
            
        }

        public void Shutdown()
        {
            db.Close();
            instance = null;
        }

        public Object this[String key]
        {
            get
            {
                return root.Get(key);
            }
            set
            {
                root.Put(key, value);
            }
        }

        public DamageEvent[] getDamageForRaidEncounter(int raid, int encounter, bool forceRefresh = false)
        {
            if (forceRefresh)
            {
                List<DamageEvent> list = DamageEvent.convert(Cassandra.Instance.GetDamgeForRaidEncounter(raid, encounter));
                foreach (DamageEvent evt in list)
                {
                    damageIndex.Put(evt.getKey(), evt);
                }
                return list.ToArray();
            }

            object[] temp = damageIndex.GetPrefix(DamageEvent.getRaidEncounterPrefix(raid, encounter));

            if (temp.Length == 0)
                return getDamageForRaidEncounter(raid, encounter, true);

            return convertToList<DamageEvent>(temp).ToArray();

        }

        private List<T> convertToList<T>(object[] array)
        {
            List<T> list = new List<T>();

            foreach(object j in array)
            {
                if(j is T)
                {
                    list.Add((T)j);
                }
            }

            return list;
        }

        public RaidHeader[] getRaidHeaders(bool forceRefresh = false)
        {
            RaidHeader[] events;
            if (forceRefresh)
            {
                RowSet set = Cassandra.Instance.GetRaidHeaders();
                events = RaidHeader.convert(set);

                root.Put("RaidHeaders", events);
    
                return events;
            }

            events = (RaidHeader[])root.Get("RaidHeaders");

            if (events == null || events.Length == 0)
                return getRaidHeaders(true);

            return events;
        }

        public UnitTotalDamage getTotalDamageBySource(int raid, int encounter, String Source, bool forceRefresh = false)
        {
            UnitTotalDamage damage;
            if (forceRefresh)
            {
                DamageEvent[] events = getDamageForRaidEncounter(raid, encounter);

                foreach (UnitTotalDamage unit in DamageEvent.groupBySource(events))
                {
                    damageIndex.Set(String.Format(String.Format("TOTAL:{0}:{1}:{2}:", raid, encounter, unit.Source)), unit);
                }

                damage = (UnitTotalDamage) damageIndex.Get(String.Format("TOTAL:{0}:{1}:{2}:", raid, encounter, Source));

                if (damage == null)
                    throw new ArgumentOutOfRangeException(Source + " does not exist in the encounter");
                else
                    return damage;
            }

            damage = (UnitTotalDamage) damageIndex.Get(String.Format("TOTAL:{0}:{1}:{2}:", raid, encounter, Source));

            if(damage == null)
            {
                return getTotalDamageBySource(raid, encounter, Source, true);
            } else
            {
                return (UnitTotalDamage) damage;
            }
        }

        public string getSpellNameFromSpellID(int ID)
        {
            if (spellMap.ContainsKey(ID))
                return spellMap[ID];
            else
            {
                string id = ID.ToString();
                string val = redis.HashGet("spells", id);

                if (val == null)
                {
                    spellMap[ID] = id;
                    return id;
                }

                spellMap[ID] = val;
                return val;
            }
        }

        public string getUnitNameFromGUID(string GUID)
        {
            if(guidMap.ContainsKey(GUID))
            {
                return guidMap[GUID];
            }

            try
            {
                string val = redis.HashGet("units", GUID);
                if (val == null)
                {
                    guidMap[GUID] = GUID;
                    return GUID;
                }
                guidMap[GUID] = val;
                return val;
            }
            catch (Exception e)
            {
                Console.Error.Write(e.StackTrace);
                guidMap[GUID] = GUID;
                return GUID;
            }
        }

        public UnitTotalDamage[] getInvolvedUnitsDamage(int raid, int encounter, bool foreceRefresh = false)
        {
            List<UnitTotalDamage> array = convertToList<UnitTotalDamage>(damageIndex.GetPrefix(String.Format("TOTAL:{0}:{1}:", raid, encounter)));

            if (array.Count == 0)
            {
                try
                {
                    getTotalDamageBySource(raid, encounter, "", true);
                } catch (ArgumentOutOfRangeException e) {
                    Console.Error.Write(e.StackTrace);
                }

                array = convertToList<UnitTotalDamage>(damageIndex.GetPrefix(String.Format("TOTAL:{0}:{1}:", raid, encounter)));
            }

            return array.ToArray();
        }

        public IEnumerable<UnitSpellSum> getUnitTotalSpellDamge(int raid, int encounter, String source)
        {
            DamageEvent[] events = getDamageForRaidEncounter(raid, encounter);
            List<DamageEvent> forSource = WowEvent.filterBySource<DamageEvent>(source, events);
            return DamageEvent.groupBySpell(forSource.ToArray()); ;
        }

        public IEnumerable<UnitSpellSum> getUnitTotalSpellHealing(int raid, int encounter, String source)
        {
            HealingEvent[] events = getHealingForRaidEncounter(raid, encounter);
            List<HealingEvent> forSource = WowEvent.filterBySource<HealingEvent>(source, events);
            return HealingEvent.groupBySpell(forSource.ToArray());
        }
    }
}
