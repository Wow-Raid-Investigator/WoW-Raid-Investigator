using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid.Stat
{
    public class UnitTotalHealing : UnitTotalEffect
    {
        long healing;

        public long Healing
        {
            get { return healing; }
        }

        public UnitTotalHealing(String source, long healing, int v1, double v2, double v3) : base(source, v1, v2, v3)
        {
            this.healing = healing;
        }
    }
}
