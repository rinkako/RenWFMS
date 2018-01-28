using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using MahApps.Metro.Controls;
using RenMasterPanel.Controller;
using RenMasterPanel.Util;

namespace RenMasterPanel
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : MetroWindow
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void ButtonBase_OnClick(object sender, RoutedEventArgs e)
        {
            //string url_get = "http://localhost:10234/rolemap/getInvolved?rtid=AA1";
            //NetClient.FetchString(url_get, out string outstr);
            //Console.WriteLine(outstr);

            //string url_post = "http://localhost:10234/rolemap/getInvolved";
            //var dict = new Dictionary<String, String>();
            //dict.Add("rtid", "AA1");
            //NetClient.PostData(url_post, dict, out string postRes);
            //Console.WriteLine(postRes);

            var args = new Dictionary<String, String> {{"renid", "testren"}};
            NetClient.PostData(GlobalContext.URL_GetProcessByRenId, args, out var fetched);
            
        }

        private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.ComboBox_Step1_Processes.SelectedIndex != 0)
            {
                this.Button_Step1_Open.Visibility = Visibility.Hidden;
                this.TextBox_Step1_Open.Visibility = Visibility.Hidden;
            }
            else
            {
                this.Button_Step1_Open.Visibility = Visibility.Visible;
                this.TextBox_Step1_Open.Visibility = Visibility.Visible;
            }
        }

        private void Button_Step1_Open_OnClick(object sender, RoutedEventArgs e)
        {
            var fd = new FolderBrowserDialog { RootFolder = Environment.SpecialFolder.Desktop };
            var dr = fd.ShowDialog();
            if (dr == System.Windows.Forms.DialogResult.OK)
            {
                this.TextBox_Step1_Open.Text = fd.SelectedPath;
                MPController.LoadProcessFromDirectory(this.TextBox_Step1_Open.Text);
                var boList = MPController.GetBOList();
                this.ComboBox_Step1_MainBO.Items.Clear();
                this.ListBox_Step1_BO.Items.Clear();
                if (boList != null)
                {
                    foreach (var boName in boList)
                    {
                        this.ComboBox_Step1_MainBO.Items.Add(boName);
                        this.ListBox_Step1_BO.Items.Add(boName);
                    }
                    if (this.ComboBox_Step1_MainBO.Items.Count > 0)
                    {
                        this.ComboBox_Step1_MainBO.SelectedIndex = 0;
                    }
                }
            }
            else
            {
                return;
            }
        }

        private void ListBox_Step1_BO_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            var idx = this.ListBox_Step1_BO.SelectedIndex;
            if (idx != -1)
            {
                var boName = this.ListBox_Step1_BO.Items[idx].ToString();
                var pf = new Forms.TextPreviewForm(boName, MPController.GetBOContent(boName));
                pf.ShowDialog();
            }
        }
    }
}
