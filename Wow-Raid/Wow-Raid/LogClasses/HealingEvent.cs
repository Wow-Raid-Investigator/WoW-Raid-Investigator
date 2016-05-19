using Cassandra;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Perst;

namespace Wow_Raid.LogClasses
{
    public partial class HealingEvent : WowEvent
    {
        int healing;

        public HealingEvent(Row row) : base(row)
        {
            this.healing = (int)row["healing"];
        }

        public static List<HealingEvent> convert(RowSet set)
        {
            List<HealingEvent> events = new List<HealingEvent>();

            foreach (Row row in set)
            {
                events.Add(new HealingEvent(row));
            }

            return events;
        }
    }
}
