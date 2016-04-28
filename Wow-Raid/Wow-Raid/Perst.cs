using Perst;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid
{
    public class Perst
    {
        private static Perst instance;
        private static readonly object padlock = new object();

        public static Perst Instance
        {
            get
            {
                if(instance == null)
                {
                    lock(padlock)
                    {
                        if (instance == null)
                        {
                            instance = new Perst();
                        }
                    }
                }

                return instance;
            }
        }

        private Index root;
        private Storage db;

        private Perst ()
        {
            db = StorageFactory.Instance.CreateStorage();
            db.Open("storage.dbs", (long) Math.Pow(2, 20));

            root = (Index)db.Root;

            if (root == null)
            {
                root = db.CreateIndex(typeof(string), true);
            }
        }

        public void Shutdown()
        {
            db.Close();
        }

        public Object this[String key]
        {
            get
            {
                return root[key];
            }
            set
            {
                root[key] = value;
            }
        }

    }
}
