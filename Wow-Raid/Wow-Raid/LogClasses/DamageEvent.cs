using Cassandra;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Perst;

namespace Wow_Raid.LogClasses
{
    public partial class DamageEvent : WowEvent
    {
        int damage;
     
        public DamageEvent(Row row) : base(row)
        {
            this.damage = (int)row["damage"];
        }

        public static new List<DamageEvent> convert(RowSet set)
        {
            List<DamageEvent> events = new List<DamageEvent>();
            
            foreach(Row row in set)
            {
                events.Add(new DamageEvent(row));
            }

            return events;
        }
    }
}
