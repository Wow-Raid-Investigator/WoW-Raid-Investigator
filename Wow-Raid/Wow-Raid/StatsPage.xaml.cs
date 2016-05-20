using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using Wow_Raid.LogClasses;
using Wow_Raid.Stat;

namespace Wow_Raid
{
    /// <summary>
    /// Interaction logic for StatsPage.xaml
    /// </summary>
    public partial class StatsPage : Page
    {
        public ObservableCollection<RaidEffectRow> raidDamage = new ObservableCollection<RaidEffectRow>();
        public ObservableCollection<RaidEffectRow> raidHealing = new ObservableCollection<RaidEffectRow>();
        public ObservableCollection<PlayerRow> players = new ObservableCollection<PlayerRow>();

        private int currentRaid;
        private int currentEncounter;
  
        private RaidHeader row;

        private object selectedItem = null;
        public object SelectedItem
        {
            get
            {
                return selectedItem;
            }
            set
            {
                // selected item has changed
                selectedItem = value;
            }
        }

        public ObservableCollection<PlayerRow> Players { get { return players; } }

        public StatsPage(RaidHeader row)
        {
            this.row = row;
            this.currentRaid = row.Raid;
            this.currentEncounter = row.Encounter;
            DamageEvent[] damageEvents = Perst.Instance.getDamageForRaidEncounter(row.Raid, row.Encounter);
            HealingEvent[] healingEvents = Perst.Instance.getHealingForRaidEncounter(row.Raid, row.Encounter);

            UnitTotalDamage[] damageRaidArray = Perst.Instance.getInvolvedUnitsDamage(row.Raid, row.Encounter);
            UnitTotalHealing[] healingRaidArray = Perst.Instance.getInvolvedUnitsHealing(row.Raid, row.Encounter);
            

            long totalDamge = 0;
            foreach(UnitTotalDamage damage in damageRaidArray)
            {
                raidDamage.Add(new RaidEffectRow(damage, row.EncounterTime));
                totalDamge += damage.Damage;
            }

            long totalHealing = 0;
            foreach (UnitTotalHealing healing in healingRaidArray)
            {
                raidHealing.Add(new RaidEffectRow(healing, row.EncounterTime));
                totalHealing += healing.Healing;
            }

            InitializeComponent();
            statsDescription.DataContext = new statText(String.Format("Average HPS: {0}hps\nAverage DPS: {1}dps\nPlayeres: {2}\nTotal healing: {3}\nTotal Damage: {4}\nFight Length: {5}s", totalHealing/row.EncounterTime, totalDamge / row.EncounterTime, damageRaidArray.Length, totalHealing, totalDamge, row.EncounterTime));
            button_Checked(null, null);

            // Set a default player.
            if (raidDamage.Count > 0)
                updatePlayerTable(raidDamage[0].Source);
        }


        private void updatePlayerTable(string unit)
        {
            players.Clear();
            IEnumerable<UnitSpellSum> spells = Perst.Instance.getUnitTotalSpellDamge(currentRaid, currentEncounter, unit);
            foreach (UnitSpellSum spell in spells)
            {
                players.Add(new PlayerRow(spell, row.EncounterTime));
            }
            spells = Perst.Instance.getUnitTotalSpellHealing(currentRaid, currentEncounter, unit);
            foreach (UnitSpellSum spell in spells)
            {
                players.Add(new PlayerRow(spell, row.EncounterTime));
            }
            UnitName.Content = "Stats for " + unit;
            playerTable.DataContext = players;
        }
        private void viewSelectedPlayer_Click(object sender, RoutedEventArgs e)
        {
            tabControl.SelectedIndex = 2;

            // TODO: Update Player Data
        }

        private void button_Checked(object sender, RoutedEventArgs e)
        {
            if((bool)damageButton.IsChecked)
            {
                Console.WriteLine("CHANGING TO DAMAGE.");
                raidTable.DataContext = raidDamage;
                EffectPerSecondHeader.Header = "Damage Per Second";
                TotalEffectHeader.Header = "Total Damage";
            }
            else
            {
                Console.WriteLine("CHANGING TO HEALING");
                raidTable.DataContext = raidHealing;
                EffectPerSecondHeader.Header = "Healing Per Second";
                TotalEffectHeader.Header = "Total Healing";
            }
        }

        private void viewPlayerButton_Click(object sender, RoutedEventArgs e)
        {
            if (raidTable.SelectedCells.Count > 0)
            {
                RaidEffectRow row = (RaidEffectRow)raidTable.SelectedItems[0];
                updatePlayerTable(row.Source);
                tabControl.SelectedIndex = 2;
            }
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

    public class SpellRow
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
