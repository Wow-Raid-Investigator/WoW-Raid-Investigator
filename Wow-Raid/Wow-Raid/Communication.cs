using Shared;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Wow_Raid
{
    class Communication
    {
        public static void sendFile(String filePath)
        {
            TcpClient client = new TcpClient();
            client.Connect(Properties.Settings.Default.ServerIP, Constants.FILE_PORT);
            client.Client.SendFile(filePath);
            client.Close();
        }
    }
}
