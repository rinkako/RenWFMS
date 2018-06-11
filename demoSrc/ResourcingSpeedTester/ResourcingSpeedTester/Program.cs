using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Windows.Forms;

namespace ResourcingSpeedTester
{
    static class Program
    {
        private static CSServer server = new CSServer();

        static Program()
        {
            CSServer.ReadLocationFromSteady();
            try
            {
                var t = new Thread(new ThreadStart(server.BeginAsyncAccept));
                t.Start();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }

        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Form1());
        }
    }
}
