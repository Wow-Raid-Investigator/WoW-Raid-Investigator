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
        int logno;
        int damage;
        long timestamp;


        String source;
        String target;

        public DamageEvent(Row row)
        {
            this.raid = (int)row["raid"];
            this.encounter = (int)row["encounter"];
            this.logno = (int)row["logno"];
            this.damage = (int)row["damage"];
            this.timestamp = (long)row["timestamp"];


            this.source = (String)row["source"];
            this.target = (String)row["target"];
        }

        public static List<DamageEvent> convert(RowSet set)
        {
            List<DamageEvent> events = new List<DamageEvent>();
            
            foreach(Row row in set)
            {
                events.Add(new DamageEvent(row));
            }

            return events;
        }

        internal string getKey()
        {
            return String.Format("{0}:{1}:{2}:", raid, encounter, source);
        }

        public static string getRaidEncounterPrefix(int raid, int encounter)
        {
            return String.Format("{0}:{1}:", raid, encounter);
        }
    }
}
