using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid.LogClasses
{
    public partial class DamageEvent
    {
        public static long totalDamage(DamageEvent[] events)
        {
            return events.Sum(x => x.damage);
        }

        public static IEnumerable groupBySource(DamageEvent[] events)
        {
            var a = events.GroupBy(x => x.source).Select(p => new {Source = p.Key, TotalDamge = p.Sum(d => d.damage) });
            // Source
            // Total Damage
        }
    }
}
