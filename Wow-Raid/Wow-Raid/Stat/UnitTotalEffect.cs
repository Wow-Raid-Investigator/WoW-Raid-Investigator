using System;

namespace Wow_Raid.Stat
{
    public class UnitTotalEffect
    {
        public String Source
        {
            get
            {
                return source;
            }
        }

        public double critPercet;
        public int hitCount;
        public double multistrikePercent;
        private String source;

        public UnitTotalEffect(String source, int hits, double crit, double multistrike)
        {
            this.source = source;
            this.hitCount = hits;
            this.critPercet = crit;
            this.multistrikePercent = multistrike;
        }
    }
}