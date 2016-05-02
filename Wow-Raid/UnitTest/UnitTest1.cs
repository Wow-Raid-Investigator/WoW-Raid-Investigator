using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Wow_Raid;

namespace UnitTest
{
    [TestClass]
    public class UnitTest1
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

    }
}
