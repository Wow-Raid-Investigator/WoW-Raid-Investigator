using Cassandra;
using System;
using System.Linq;

namespace Wow_Raid
{
    public class Cassandra
    {
        private static Cassandra instance;
        private static readonly object padlock = new object();

        public static Cassandra Instance
        {
            get
            {
                if (instance == null)
                {
                    lock (padlock)
                    {
                        if (instance == null)
                        {
                            instance = new Cassandra();
                        }
                    }
                }

                return instance;
            }
        }

        private ISession session;

        private Cassandra()
        {
            Cluster cluster = Cluster.Builder().AddContactPoints(new String[] { "127.0.0.1", "wow-raid-1.csse.rose-hulman.edu", "wow-raid-2.csse.rose-hulman.edu", "wow-raid-3.csse.rose-hulman.edu" }).Build();
            session = cluster.Connect("wowraid");
        }

        public void CassandraTest()
        {
            RowSet result = session.Execute("select * from damage_dealt");

            foreach(Row row in result)
            {
                Console.Write("Row: ");
                for(int i = 0; i < row.Count(); i++)
                {
                    Console.Write(row[i] + ", ");
                }
                Console.WriteLine();
            }
        }

        internal RowSet GetHealingForRaidEncounter(int raid, int encounter)
        {
            return session.Execute(String.Format("select * from healing_dealt where raid = {0} and encounter = {1}", raid, encounter));
        }

        public RowSet GetDamgeForRaidEncounter(int raid, int encounter)
        {
            return session.Execute(String.Format("select * from damage_dealt where raid = {0} and encounter = {1}", raid, encounter));
        }

        public RowSet GetRaidHeaders()
        {
            RowSet set = session.Execute("select raid, encounter, timestamp, duration from metadata");
            return set;
        }
    }

}
