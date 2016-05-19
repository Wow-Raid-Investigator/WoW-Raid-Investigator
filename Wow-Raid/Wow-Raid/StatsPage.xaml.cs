using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Wow_Raid.LogClasses;
using Wow_Raid.Stat;

namespace Wow_Raid
{
    /// <summary>
    /// Interaction logic for StatsPage.xaml
    /// </summary>
    public partial class StatsPage : Page
    {
        public ObservableCollection<RaidEffectRow> raids = new ObservableCollection<RaidEffectRow>();
        public ObservableCollection<UnitSpellSum> players = new ObservableCollection<UnitSpellSum>();

        private int currentRaid;
        private int currentEncounter;

        public StatsPage(RaidHeader row)
        {
            this.currentRaid = row.Raid;
            this.currentEncounter = row.Encounter;
            DamageEvent[] damageEvents = Perst.Instance.getDamageForRaidEncounter(row.Raid, row.Encounter);
            HealingEvent[] healingEvents = Perst.Instance.getHealingForRaidEncounter(row.Raid, row.Encounter);

            UnitTotalDamage[] damageRaidArray = Perst.Instance.getInvolvedUnitsDamage(row.Raid, row.Encounter);
            UnitTotalHealing[] healingRaidArray = Perst.Instance.getInvolvedUnitsHealing(row.Raid, row.Encounter);
            

            long totalDamge = 0;
            foreach(UnitTotalDamage damage in damageRaidArray)
            {
                raids.Add(new RaidEffectRow(damage, row.EncounterTime));
                totalDamge += damage.Damage;
            }

            long totalHealing = 0;
            foreach (UnitTotalHealing healing in healingRaidArray)
            {
                raids.Add(new RaidEffectRow(healing, row.EncounterTime));
                totalHealing += healing.Healing;
            }

            InitializeComponent();
            statsDescription.DataContext = new statText(String.Format("Average HPS: {0}hps\nAverage DPS: {1}dps\nPlayeres: {2}\nTotal healing: {3}\nTotal Damage: {4}\nFight Length: {5}s", 2000, totalDamge / row.EncounterTime, damageRaidArray.Length, 50000, totalDamge, row.EncounterTime));
            raidTable.DataContext = raids;

            String unit = "\"Deathkite-Kel'Thuzad\"";

            IEnumerable<UnitSpellSum> spells = Perst.Instance.getUnitTotalSpellDamge(currentRaid, currentEncounter, unit);

            playerTable.DataContext = spells;
        }

        private void viewSelectedPlayer_Click(object sender, RoutedEventArgs e)
        {
            tabControl.SelectedIndex = 2;

            // TODO: Update Player Data
        }
    }

    public class statText
    {
        private string _description;

        public statText(string description)
        {
            _description = description;
        }
        public string statsDescriptionText
        {
            get { return _description; }
            set
            {
                _description = value;
                this.OnPropertyChanged("statsDescriptionText");
            }
        }

        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        void OnPropertyChanged(string propName)
        {
            if (this.PropertyChanged != null)
                this.PropertyChanged(
                    this, new PropertyChangedEventArgs(propName));
        }

        #endregion
    }

    public class PlayerRow
    {
        private String _spell;
        private String _effect;
        private String _effectPerSecond;
        private int _hitCount;
        private float _crit;
        private float _multistrike;

        public String spell
        {
            get { return _spell; }
            set { _spell = value; }
        }

        public String effect
        {
            get { return _effect; }
            set { _effect = value; }
        }

        public String effectPerSecond
        {
            get { return _effectPerSecond; }
            set { _effectPerSecond = value; }
        }

        public int hitCount
        {
            get { return _hitCount; }
            set { _hitCount = value; }
        }

        public float crit
        {
            get { return _crit; }
            set { _crit = value; }
        }

        public float multistrike
        {
            get { return _multistrike; }
            set { _multistrike = value; }
        }
    }

}
