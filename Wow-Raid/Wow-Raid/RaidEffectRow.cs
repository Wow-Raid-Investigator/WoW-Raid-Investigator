using Wow_Raid.Stat;

namespace Wow_Raid
{
    public class RaidEffectRow
    {
        private UnitTotalEffect effect;
        private long encounterTime;

        public string Source
        {
            get
            {
                return effect.Source;
            }
        }

        public long TotalEffect
        {
            get
            {
                return effect.getTotalEffect();
            }
        }

        public double DamagePerSecond
        {
            get
            {
                return TotalEffect / encounterTime;
            }
        }

        public RaidEffectRow(UnitTotalDamage damage, long v)
        {
            this.effect = damage;
            this.encounterTime = v;
        }

        public RaidEffectRow(UnitTotalHealing healing, long v)
        {
            this.effect = healing;
            this.encounterTime = v;
        }
    }
}