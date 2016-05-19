using System;
using Wow_Raid.Stat;

namespace Wow_Raid.LogClasses
{
    public class UnitSpellSum : UnitTotalEffect
    {
        public int effect;

        public UnitSpellSum(String source, int effect, int count, double crit, double multistrike) : base(source,count,crit,multistrike)
        {
            this.effect = effect;
        }
    }
}