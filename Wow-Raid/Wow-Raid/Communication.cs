using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace Wow_Raid
{
    public class Communication
    {
        public static void uploadFileAsync(string file, UploadPopup window)
        {
            TcpClient client = new TcpClient();
            try
            {
                client.Connect(Properties.Settings.Default.ServerIP, Properties.Settings.Default.FileServerPort);
                FileStream fs = new FileStream(file, FileMode.Open);
                long length = fs.Length;

                byte[] buffer = new byte[16384];
                int read;
                long totalRead = 0;
                int lastPercent = 0;
                do
                {
                    read = fs.Read(buffer, 0, buffer.Length);
                    totalRead += read;

                    int currentPercent = (int)(totalRead * 100 / length);
                    if (currentPercent != lastPercent)
                    {
                        window.Dispatcher.InvokeAsync(new Action(() =>
                            window.updateValue(currentPercent)
                        ));

                        lastPercent = currentPercent;
                    }

                    client.Client.Send(buffer, read, SocketFlags.None);
                } while (read > 0);

                client.Close();
                fs.Close();
            }
            catch (System.Net.Sockets.SocketException)
            {
                MessageBox.Show("Unable to connect to server to Upload File", "ERROR", MessageBoxButton.OK, MessageBoxImage.Error);
            }


            window.Dispatcher.Invoke(() => window.Close());
        }
    }
}
