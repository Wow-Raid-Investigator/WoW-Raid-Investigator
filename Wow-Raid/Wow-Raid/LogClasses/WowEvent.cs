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
        public bool critical;
        public bool multistrike;
        public long timestamp;


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

        public String SpellName
        {
            get
            {
                return Perst.Instance.getSpellNameFromSpellID(spellID);
            }
        }



        public WowEvent(Row row)
        {
            this.raid = (int)row["raid"];
            this.encounter = (int)row["encounter"];
            this.logno = (int)row["logno"];
            this.spellID = (int)row["spell_id"];
            bool? crit = (bool?)row["critical"]; ;
            bool? multi = (bool?)row["multistrike"];
            this.critical = crit == null ? false : (bool)crit;
            this.multistrike = multi == null ? false: (bool)multi;

            this.timestamp = (long)row["timestamp"];


            this.source = (String)row["source"];
            this.target = (String)row["target"];
        }

        public string getKey()
        {
            return String.Format("{0}:{1}:{2}:", raid, encounter, source);
        }

        public static string getRaidEncounterPrefix(int raid, int encounter)
        {
            return String.Format("{0}:{1}:", raid, encounter);
        }

        public static List<T> filterBySource<T>(String source, T[] array) where T : WowEvent
        {
            List<T> toReturn = new List<T>();

            foreach(T evt in array)
            {
                if(evt.Source.Equals(source))
                {
                    toReturn.Add(evt);
                }
            }

            return toReturn;
        }
    }
}
