using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Wow_Raid.Stat;

namespace Wow_Raid.LogClasses
{
    public partial class DamageEvent
    {
        public static long totalDamage(DamageEvent[] events)
        {
            return events.Sum(x => x.damage);
        }

        public static IEnumerable<UnitTotalDamage> groupBySource(DamageEvent[] events)
        {
            return events.GroupBy(x => x.Source).Select(p => new UnitTotalDamage(p.Key, p.Sum(d => d.damage), p.Count() ,p.Average(d => Convert.ToInt32(d.critical)), p.Average(d => Convert.ToInt32(d.multistrike))) );
        }

        public static IEnumerable<UnitSpellSum> groupBySpell(DamageEvent[] events)
        {
            return events.GroupBy(x => x.SpellName).Select(p => new UnitSpellSum(p.Key, p.Sum(d => d.damage),p.Count(), p.Average(d => Convert.ToInt32(d.critical)), p.Average(d => Convert.ToInt32(d.multistrike))));
        }
    }
}
