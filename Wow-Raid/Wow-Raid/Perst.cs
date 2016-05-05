using Cassandra;
using Perst;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Wow_Raid.LogClasses;
using Wow_Raid.Stat;

namespace Wow_Raid
{
    public class Perst
    {
        private static Perst instance;
        private static readonly object padlock = new object();

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

        private Index root;
        private Storage db;
        private Index damageIndex;

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
            }
            else
            {
                damageIndex = (Index)root.Get("DamageIndex");
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

        public UnitTotalDamage[] getInvolvedUnitsDamage(int raid, int encounter, bool foreceRefresh = false)
        {
            List<UnitTotalDamage> array = convertToList<UnitTotalDamage>(damageIndex.GetPrefix(String.Format("TOTAL:{0}:{1}:", raid, encounter)));

            if (array.Count == 0)
            {
                try
                {
                    getTotalDamageBySource(raid, encounter, "", true);
                } catch (ArgumentOutOfRangeException) { }

                array = convertToList<UnitTotalDamage>(damageIndex.GetPrefix(String.Format("TOTAL:{0}:{1}:", raid, encounter)));
            }

            return array.ToArray();
        }

    }
}
