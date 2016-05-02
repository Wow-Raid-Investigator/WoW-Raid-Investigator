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
            db.Open("storage.dbs", 1073741824L);

            root = (Index)db.Root;

            if (root == null)
            {
                root = db.CreateIndex(typeof(string), true);
                db.Root = root;
            }
        }

        public void Shutdown()
        {
            db.Close();
            instance = null;
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
