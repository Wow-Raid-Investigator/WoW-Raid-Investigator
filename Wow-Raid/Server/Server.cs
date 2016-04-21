using Shared;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Server
{
    public class Server
    {
        static void Main(string[] args)
        {
            TcpListener listener = new TcpListener(System.Net.IPAddress.Any, Constants.FILE_PORT);

            while (true)
            {
                TcpClient client = listener.AcceptTcpClient();

                Thread fileRunner = new Thread( () => receiveFile(client));
                fileRunner.Start();
            }
        }

        private static void receiveFile(TcpClient client)
        {
            NetworkStream stream = client.GetStream();
            
            FileStream fs = new FileStream("LogFile", FileMode.CreateNew);

            byte[] buffer = new byte[4098];
            int read;
            while (client.Connected)
            {
                read = stream.Read(buffer, 0, buffer.Length);
                fs.Write(buffer, 0, read);
            }
            do
            {
                read = stream.Read(buffer, 0, buffer.Length);
                fs.Write(buffer, 0, read);
            } while (read > 0);
            
            fs.Close();
        }
    }
}
