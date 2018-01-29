using System;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Input;
using MahApps.Metro.Controls;
using RenMasterPanel.Controller;
using RenMasterPanel.Util;

namespace RenMasterPanel.Forms
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : MetroWindow
    {
        public MainWindow()
        {
            InitializeComponent();
            var processList = MPController.GetProcess();
            GlobalContext.Current_Ren_Process_List = processList;
            foreach (var process in processList)
            {
                this.ComboBox_Step1_Processes.Items.Add(process["processName"]);
            }

        }

        private void ButtonBase_OnClick(object sender, RoutedEventArgs e)
        {
            var args = new Dictionary<String, String> {{"renid", "testren"}};
            NetClient.PostData(GlobalContext.URL_GetProcessByRenId, args, out var fetched);
            
        }

        private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.ComboBox_Step1_Processes.SelectedIndex != 0)
            {
                this.Button_Step1_Open.Visibility = Visibility.Hidden;
                this.TextBox_Step1_Open.Visibility = Visibility.Hidden;
                var processEntity = GlobalContext.Current_Ren_Process_List[this.ComboBox_Step1_Processes.SelectedIndex - 1];
                var boList = MPController.GetProcessBO(processEntity["pid"]);
                this.ListBox_Step1_BO.Items.Clear();
                this.ComboBox_Step1_MainBO.Items.Clear();
                foreach (var bo in boList)
                {
                    var boName = bo["bo_name"];
                    this.ListBox_Step1_BO.Items.Add(boName);
                    this.ComboBox_Step1_MainBO.Items.Add(boName);
                }
                if (this.ComboBox_Step1_MainBO.Items.Count > 0)
                {
                    this.ComboBox_Step1_MainBO.SelectedIndex = 0;
                }
                MPController.CurrentTransaction.BOVector = boList;
            }
            else
            {
                this.Button_Step1_Open.Visibility = Visibility.Visible;
                this.TextBox_Step1_Open.Visibility = Visibility.Visible;
                MPController.CurrentTransaction.BOVector = new List<Dictionary<string, string>>();
                this.ListBox_Step1_BO.Items.Clear();
                this.ComboBox_Step1_MainBO.Items.Clear();
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
                var pf = new TextPreviewForm(boName, MPController.GetBOContent(boName));
                pf.ShowDialog();
            }
        }
    }
}
