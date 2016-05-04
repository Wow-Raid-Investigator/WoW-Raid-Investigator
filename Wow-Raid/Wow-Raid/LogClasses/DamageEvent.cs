using Cassandra;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Perst;

namespace Wow_Raid.LogClasses
{
    public partial class DamageEvent
    {
        int raid;
        int encounter;
        long logno;
        long damage;
        long timestamp;


        String source;
        String target;

        public DamageEvent(Row row)
        {
            this.raid = (int)row["raid"];
            this.encounter = (int)row["encounter"];
            this.logno = (long)row["logno"];
            this.damage = (long)row["damage"];
            this.timestamp = (long)row["timestamp"];


            this.source = (String)row["source"];
            this.target = (String)row["target"];
        }

        public static DamageEvent[] convert(RowSet set)
        {
            DamageEvent[] events = new DamageEvent[set.Count()];

            int index = 0;
            foreach(Row row in set)
            {
                events[index++] = new DamageEvent(row);
            }

            return events;
        }

        internal string getKey()
        {
            return String.Format("{0}:{1}:{2}", raid, encounter, source);
        }

        public static string getRaidEncounterPrefix(int raid, int encounter)
        {
            return String.Format("{0}:{1}:", raid, encounter);
        }
    }
}
