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

        public RaidHeader(Row row)
        {
            this.raid = (int)row["raid"];
            this.encounter = (int)row["encounter"];
            this._date = DateTime.Now;
            this._description = "Not Set";
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
