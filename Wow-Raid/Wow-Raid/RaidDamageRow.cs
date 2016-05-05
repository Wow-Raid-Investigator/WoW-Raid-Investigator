using Wow_Raid.Stat;

namespace Wow_Raid
{
    public class RaidDamageRow
    {
        private UnitTotalDamage damage;
        private long encounterTime;

        public string Source
        {
            get
            {
                return damage.Source;
            }
        }

        public long TotalDamage
        {
            get
            {
                return damage.Damage;
            }
        }

        public double DamagePerSecond
        {
            get
            {
                return TotalDamage / encounterTime;
            }
        }

        public RaidDamageRow(UnitTotalDamage damage, long v)
        {
            this.damage = damage;
            this.encounterTime = v;
        }
    }
}