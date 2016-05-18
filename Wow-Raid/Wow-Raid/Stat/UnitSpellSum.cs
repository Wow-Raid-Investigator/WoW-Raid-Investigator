namespace Wow_Raid.LogClasses
{
    public class UnitSpellSum
    {
        public string key;
        public int sum;

        public UnitSpellSum(string key, int v)
        {
            this.key = key;
            this.sum = v;
        }
    }
}