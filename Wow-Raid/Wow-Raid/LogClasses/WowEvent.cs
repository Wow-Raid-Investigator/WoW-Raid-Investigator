using Cassandra;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid.LogClasses
{
    public class WowEvent
    {
        int raid;
        int encounter;
        int logno;
        int spellID;
        bool critical;
        bool multistrike;
        long timestamp;


        private String source;
        public String Source
        {
            get
            {
                return Perst.Instance.getUnitNameFromGUID(source);
            }
        }

        private String target;
        public String Target
        {
            get
            {
                return Perst.Instance.getUnitNameFromGUID(target);
            }
        }


        public WowEvent(Row row)
        {
            this.raid = (int)row["raid"];
            this.encounter = (int)row["encounter"];
            this.logno = (int)row["logno"];
            this.spellID = (int)row["spell_id"];
            this.critical = (bool)row["critical"];
            this.multistrike = (bool)row["multistrike"];

            this.timestamp = (long)row["timestamp"];


            this.source = (String)row["source"];
            this.target = (String)row["target"];
        }

        public static List<WowEvent> convert(RowSet set)
        {
            List<WowEvent> events = new List<WowEvent>();

            foreach (Row row in set)
            {
                events.Add(new WowEvent(row));
            }

            return events;
        }

        public string getKey()
        {
            return String.Format("{0}:{1}:{2}:", raid, encounter, source);
        }

        public static string getRaidEncounterPrefix(int raid, int encounter)
        {
            return String.Format("{0}:{1}:", raid, encounter);
        }
    }
}
