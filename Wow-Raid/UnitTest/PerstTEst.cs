using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Wow_Raid;

namespace UnitTest
{
    [TestClass]
    public class PerstTest
    {
        [TestMethod]
        public void TestPerstBasic()
        {
            DateTime time = DateTime.Now;

            Perst.Instance["TEST_KEY"] = time;

            Assert.AreEqual(time, Perst.Instance["TEST_KEY"]);
        }

        [TestMethod]
        public void ComplexPerstObject()
        {
            String[] array = new String[] {"A", "B", "C", "D", "E" };

            Perst.Instance["COMPLEX_KEY"] = array;
         
            String[] returned = (String[]) Perst.Instance["COMPLEX_KEY"];

            for(int i = 0; i < array.Length; i++)
            {
                Assert.AreEqual(array[i], returned[i]);
            }
        }

        [TestMethod]
        public void RedisPerstTest()
        {
            String key1 = "355";
            string val1 = Perst.Instance.GetRedisTestValue(key1);
            Assert.AreEqual(val1, "\"Taunt\"");

            String key2 = "xxx";
            string val2 = Perst.Instance.GetRedisTestValue(key2);
            Assert.AreEqual(val2, null);
        }

    }
}
