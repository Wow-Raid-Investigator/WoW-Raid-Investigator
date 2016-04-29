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

namespace Wow_Raid
{
    public class Log
    {
        private String _id;
        private DateTime _date;
        private String _description;

        public String id
        {
            get { return _id; }
            set { _id = value; }
        }

        public DateTime date
        {
            get { return _date; }
            set { _date = value; }
        }

        public String description
        {
            get { return _description; }
            set { _description = value; }
        }
    }
    /// <summary>
    /// Interaction logic for LogViewer.xaml
    /// </summary>
    public partial class LogViewer : Page
    {
        public ObservableCollection<Log> data = new ObservableCollection<Log>();
        public LogViewer()
        {
            InitializeComponent();

            data.Add(new Log() { id = "1", date = new DateTime(), description = "First log uploaded." });
            data.Add(new Log() { id = "2", date = new DateTime(), description = "Hmm... Description?" });
            data.Add(new Log() { id = "3", date = new DateTime(), description = "How long can we make this description out of curiosity?" });
            data.Add(new Log() { id = "4", date = new DateTime(), description = "Welp. Jeez. A lot of WoW was played today." });
            data.Add(new Log() { id = "5", date = new DateTime(), description = "Is Date the Date of the encounter or the date it was uploaded?" });
            data.Add(new Log() { id = "6", date = new DateTime(), description = "Heck if I know." });
            logTable.DataContext = data;
        }

        private void viewButton_Click(object sender, RoutedEventArgs e)
        {
            if (logTable.SelectedItems.Count != 0)
            {
                Log row = (Log)logTable.SelectedItems[0];
                Console.WriteLine(row.id);
            }

            NavigationService ns = this.NavigationService;
            ns.Navigate(new StatsPage());
        }
    }
}
