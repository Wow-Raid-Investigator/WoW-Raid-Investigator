using System;
using Wow_Raid.LogClasses;

namespace Wow_Raid
{
    public class PlayerRow
    {
        private UnitSpellSum spell;
        private long encounterTime;

        public String Source
        {
            get
            {
                return spell.Source;
            }
        }
        public long TotalEffect
        {
            get
            {
                return spell.getTotalEffect();
            }
        }

        public double HitCount
        {
            get
            {
                return spell.HitCount;
            }
        }

        public double MultistrikePercent
        {
            get
            {
                return spell.MultistrikePercent;
            }
        }
        public double CritPercent
        {
            get
            {
                return this.spell.CritPercent;
            }
        }
        public double EffectPerSecond
        {
            get
            {
                return this.spell.getTotalEffect() / encounterTime;
            }
        }
        public PlayerRow(UnitSpellSum spell, long encounterTime)
        {
            this.spell = spell;
            this.encounterTime = encounterTime;
        }
    }
}
