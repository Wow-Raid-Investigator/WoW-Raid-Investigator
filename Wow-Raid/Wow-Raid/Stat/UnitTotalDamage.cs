using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid.Stat
{
    public class UnitTotalDamage
    {
        String source;
        long damage;
        private int hitCount;
        private double critPercet;
        private double multistrikePercent;

        public String Source
        {
            get {
                return source;
            }
        }

        public long Damage
        {
            get { return damage; }
        }

        private UnitTotalDamage(String source, long damage)
        {
            this.source = source;
            this.damage = damage;
        }

        public UnitTotalDamage(String source, long damage, int v1, double v2, double v3) : this(source, damage)
        {
            this.hitCount = v1;
            this.critPercet = v2;
            this.multistrikePercent = v3;
        }
    }
}
