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

        public static IEnumerable<UnitSpellSum> groupBySource(HealingEvent[] events)
        {
            return events.GroupBy(x => x.Source).Select(p => new UnitSpellSum(p.Key, p.Sum(d => d.healing), p.Count(), p.Average(d => Convert.ToInt32(d.critical)), p.Average(d => Convert.ToInt32(d.multistrike))));
        }

        public static IEnumerable<UnitSpellSum> groupBySpell(HealingEvent[] events)
        {
            return events.GroupBy(x => x.SpellName).Select(p => new UnitSpellSum(p.Key, p.Sum(d => d.healing), p.Count(), p.Average(d => Convert.ToInt32(d.critical)), p.Average(d => Convert.ToInt32(d.multistrike))));
        }
    }
}
