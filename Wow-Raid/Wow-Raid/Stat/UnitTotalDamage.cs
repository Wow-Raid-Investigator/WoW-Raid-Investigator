﻿using System;
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

        public String Source
        {
            get {
                return Perst.Instance.getUnitNameFromGUID(source);
            }
        }

        public long Damage
        {
            get { return damage; }
        }

        public UnitTotalDamage(String source, long damage)
        {
            this.source = source;
            this.damage = damage;
        }
    }
}
