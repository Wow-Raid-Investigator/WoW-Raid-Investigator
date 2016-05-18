using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid.Stat
{
    public class UnitTotalDamage : UnitTotalEffect
    {
        String source;
        long damage;
        private int hitCount;
        private double critPercet;
        private double multistrikePercent;

        public long Damage
        {
            get { return damage; }
        }

        public UnitTotalDamage(String source, long damage, int v1, double v2, double v3) : base(source, v1, v2, v3)
        {
            this.damage = damage;
        }
    }
}
