using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Wow_Raid.Stat;

namespace Wow_Raid.LogClasses
{
    public partial class HealingEvent
    {
        public static long totalHealing(HealingEvent[] events)
        {
            return events.Sum(x => x.healing);
        }

        public static IEnumerable<UnitTotalHealing> groupBySource(HealingEvent[] events)
        {
            return events.GroupBy(x => x.Source).Select(p => new UnitTotalHealing(p.Key, p.Sum(d => d.healing)));
        }
    }
}
