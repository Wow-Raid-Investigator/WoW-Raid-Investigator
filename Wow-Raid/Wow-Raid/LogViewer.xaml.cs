using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
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
    /// Interaction logic for LogViewer.xaml
    /// </summary>
    public partial class LogViewer : Page
    {
        public ObservableCollection<RaidHeader> data = new ObservableCollection<RaidHeader>();
        public LogViewer()
        {
            InitializeComponent();
            RaidHeader[] headers = Perst.Instance.getRaidHeaders();
            foreach(RaidHeader header in headers)
            {
                data.Add(header);
            }
            logTable.DataContext = data;
        }

        private void viewButton_Click(object sender, RoutedEventArgs e)
        {
            if (logTable.SelectedItems.Count != 0)
            {
                RaidHeader row = (RaidHeader)logTable.SelectedItems[0];
                NavigationService ns = this.NavigationService;
                ns.Navigate(new StatsPage(row));
            }

        }
    }
}
