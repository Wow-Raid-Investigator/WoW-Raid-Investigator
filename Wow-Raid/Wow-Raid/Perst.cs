using Cassandra;
using Perst;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Wow_Raid.LogClasses;

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

        public DamageEvent[] getDamgeForRaidEncoutner(int raid, int encounter, bool forceRefresh = false)
        {
            DamageEvent[] events;
            if (forceRefresh)
            {
                events = DamageEvent.convert(Cassandra.Instance.GetDamgeForRaidEncounter(raid, encounter));
                foreach (DamageEvent evt in events)
                {
                    damageIndex.Put(evt.getKey(), evt);
                }
                return events;
            }

            events = (DamageEvent[])damageIndex.GetPrefix(DamageEvent.getRaidEncounterPrefix(raid, encounter));

            if (events.Length == 0)
                return getDamgeForRaidEncoutner(raid, encounter, true);

            return events;

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

    }
}
