using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading;
using System.Windows;
using ArticleCrowdSourcingDemo.Utility;

namespace ArticleCrowdSourcingDemo
{
    /// <summary>
    /// App.xaml 的交互逻辑
    /// </summary>
    public partial class App : Application
    {
        private static readonly CSServer server = new CSServer();
        public App()
        {
            try
            {
                var t = new Thread(new ThreadStart(server.BeginAsyncAccept));
                t.Start();
            }
            catch (Exception ex)
            {
                LogUtils.LogLine(ex.ToString(), LogLevel.Warning);
            }
        }
    }
}
