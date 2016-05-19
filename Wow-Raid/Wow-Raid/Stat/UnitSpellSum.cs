using System;
using Wow_Raid.Stat;

namespace Wow_Raid.LogClasses
{
    public class UnitSpellSum : UnitTotalEffect
    {
        private int effect;

        public int Effect
        {
            get
            {
                return effect;
            }
        }

        public UnitSpellSum(String source, int effect, int count, double crit, double multistrike) : base(source,count,crit,multistrike)
        {
            this.effect = effect;
        }
    }
}