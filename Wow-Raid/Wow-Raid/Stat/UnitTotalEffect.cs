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

        public double CritPercent
        {
            get
            {
                return critPercet;
            }
        }

        public double MultistrikePercent
        {
            get
            {
                return multistrikePercent;
            }
        }

        public double HitCount
        {
            get
            {
                return hitCount;
            }
        }

        private double critPercet;
        private int hitCount;
        private double multistrikePercent;
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