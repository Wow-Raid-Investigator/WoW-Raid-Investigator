using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Wow_Raid
{
    /// <summary>
    /// Interaction logic for UploadPopup.xaml
    /// </summary>
    public partial class UploadPopup : Window
    {
        private string file;

        public UploadPopup(String file)
        {
            InitializeComponent();
            this.file = file;
            this.Loaded += UploadPopup_Loaded;
        }

        private void UploadPopup_Loaded(object sender, RoutedEventArgs e)
        {

            new Thread(() => Communication.uploadFileAsync(file,this)).Start();
        }

        public void updateValue(int value)
        {
            progressBar.Value = value;
        }
    }
}
