using Cassandra;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid.LogClasses
{
    public class RaidHeader
    {
        int raid;
        int encounter;
        long encounterTime;

        private DateTime _date;
        private String _description;

        public int Raid
        {
            get { return raid; }
        }

        public int Encounter
        {
            get { return encounter; }
        }

        public String Id
        {
            get { return String.Format("{0}:{1}", raid, encounter); }
        }

        public DateTime Date
        {
            get { return _date; }
        }

        public String Description
        {
            get { return _description; }
        }

        public long EncounterTime
        {
            get
            {
                return encounterTime;
            }
        }

        public RaidHeader(Row row)
        {
            Console.WriteLine(row.ToString());
            this.raid = (int)row["raid"];
            this.encounter = (int)row["encounter"];
            this._date = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            this._date = this._date.AddSeconds(((long)row["timestamp"]) / 10);
            this._description = "Not Set";

            this.encounterTime = (long)row["duration"];

            if(encounterTime == 0)
            {
                encounterTime = 120;
            }
        }

        public static RaidHeader[] convert(RowSet set)
        {
            List<RaidHeader> events = new List<RaidHeader>();

            foreach (Row row in set)
            {
                events.Add(new RaidHeader(row));
            }

            return events.ToArray();
        }
    }
}
