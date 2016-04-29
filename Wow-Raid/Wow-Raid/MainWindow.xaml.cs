using Microsoft.Win32;
using System;
using System.Windows;
using System.Windows.Navigation;

namespace Wow_Raid
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : NavigationWindow
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void MainWindow_Closed(object sender, EventArgs e)
        {
            Perst.Instance.Shutdown();
        }
    }
}
