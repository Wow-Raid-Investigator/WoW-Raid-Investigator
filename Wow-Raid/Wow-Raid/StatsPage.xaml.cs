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

namespace Wow_Raid
{
    /// <summary>
    /// Interaction logic for StatsPage.xaml
    /// </summary>
    public partial class StatsPage : Page
    {
        public ObservableCollection<RaidRow> raids = new ObservableCollection<RaidRow>();
        public ObservableCollection<PlayerRow> players = new ObservableCollection<PlayerRow>();
        public StatsPage(RaidHeader row)
        {
            DamageEvent[] events = Perst.Instance.getDamgeForRaidEncoutner(row.Raid, row.Encounter);

            // Temporary solution because I got tired of figuring out a way to bind them.
            raids.Add(new RaidRow() { player = "Druid1", totalHealing = "10M", healingPerSecond = "250Khps", totalDamage = "5M", damagePerSecond = "125Kdps" });
            raids.Add(new RaidRow() { player = "Paladin1", totalHealing = "4M", healingPerSecond = "125Khps", totalDamage = "15M", damagePerSecond = "325Kdps" });
            raids.Add(new RaidRow() { player = "Druid2", totalHealing = "10M", healingPerSecond = "250Khps", totalDamage = "5M", damagePerSecond = "125Kdps" });
            raids.Add(new RaidRow() { player = "Paladin2", totalHealing = "4M", healingPerSecond = "125Khps", totalDamage = "15M", damagePerSecond = "325Kdps" });
            raids.Add(new RaidRow() { player = "Druid2", totalHealing = "10M", healingPerSecond = "250Khps", totalDamage = "5M", damagePerSecond = "125Kdps" });
            raids.Add(new RaidRow() { player = "Paladin2", totalHealing = "4M", healingPerSecond = "125Khps", totalDamage = "15M", damagePerSecond = "325Kdps" });


            players.Add(new PlayerRow() { spell = "Wrath", effect = "5K", effectPerSecond = "1Kdps", hitCount = 100, crit = 1.2F, multistrike = 11.3F });
            players.Add(new PlayerRow() { spell = "Wrath", effect = "5K", effectPerSecond = "1Kdps", hitCount = 100, crit = 1.2F, multistrike = 11.3F });
            players.Add(new PlayerRow() { spell = "Wrath", effect = "5K", effectPerSecond = "1Kdps", hitCount = 100, crit = 1.2F, multistrike = 11.3F });
            players.Add(new PlayerRow() { spell = "Wrath", effect = "5K", effectPerSecond = "1Kdps", hitCount = 100, crit = 1.2F, multistrike = 11.3F });
            players.Add(new PlayerRow() { spell = "Wrath", effect = "5K", effectPerSecond = "1Kdps", hitCount = 100, crit = 1.2F, multistrike = 11.3F });
            players.Add(new PlayerRow() { spell = "Wrath", effect = "5K", effectPerSecond = "1Kdps", hitCount = 100, crit = 1.2F, multistrike = 11.3F });


            InitializeComponent();
            statsDescription.DataContext = new statText("Average HPS: 190Khps\nAverage DPS: 30Kdps\nPlayeres: 10\nTotal healing: 20M\nTotal Damage: 10M\nFight Length: 2m 34s");
            raidTable.DataContext = raids;
            playerTable.DataContext = players;

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

    public class RaidRow
    {
        private String _player;
        private String _totalHealing;
        private String _healingPerSecond;
        private String _totalDamage;
        private String _damagePerSecond;

        public String player
        {
            get { return _player; }
            set { _player = value; }
        }

        public String totalHealing
        {
            get { return _totalHealing; }
            set { _totalHealing = value; }
        }

        public String healingPerSecond
        {
            get { return _healingPerSecond; }
            set { _healingPerSecond = value; }
        }

        public String totalDamage
        {
            get { return _totalDamage; }
            set { _totalDamage = value; }
        }

        public String damagePerSecond
        {
            get { return _damagePerSecond; }
            set { _damagePerSecond = value; }
        }
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
