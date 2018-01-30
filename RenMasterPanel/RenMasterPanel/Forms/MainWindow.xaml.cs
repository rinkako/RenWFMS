using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Input;
using MahApps.Metro.Controls;
using Newtonsoft.Json;
using RenMasterPanel.Controller;
using RenMasterPanel.Util;
using MessageBox = System.Windows.Forms.MessageBox;

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
                this.ComboBox_Step1_MainBO.IsEnabled = false;
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
                    foreach (var item in this.ComboBox_Step1_MainBO.Items)
                    {
                        if (item.ToString() == processEntity["mainBo"])
                        {
                            this.ComboBox_Step1_MainBO.SelectedItem = item;
                            break;
                        }
                    }
                }
                MPController.CurrentTransaction.BOVector = boList;
            }
            else
            {
                this.Button_Step1_Open.Visibility = Visibility.Visible;
                this.TextBox_Step1_Open.Visibility = Visibility.Visible;
                this.ComboBox_Step1_MainBO.IsEnabled = true;
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
                MPController.CurrentTransaction.ProcessLocalPath = null;
                MPController.CurrentTransaction.BOVector = new List<Dictionary<string, string>>();
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

        private void Button_Step1_Next_OnClick(object sender, RoutedEventArgs e)
        {
            if (this.ComboBox_Step1_Processes.SelectedIndex == 0)  // new process
            {
                if (this.ListBox_Step1_BO.Items.Count == 0)
                {
                    MessageBox.Show(@"A process should contain at lease one BO");
                    return;
                }
                if (this.ComboBox_Step1_MainBO.SelectedIndex == -1)
                {
                    MessageBox.Show(@"There must have a main BO");
                    return;
                }
                var npf = new ProcessNameForm();
                npf.ShowDialog();
                if (npf.ProcessName == null)
                {
                    return;
                }
                // create new process
                var pid = MPController.CreateProcess(npf.ProcessName, this.ComboBox_Step1_MainBO.SelectedItem.ToString().Trim());
                if (String.IsNullOrEmpty(pid))
                {
                    MessageBox.Show(@"Failed to create new process");
                    return;
                }
                MPController.CurrentTransaction.ProcessName = npf.ProcessName;
                MPController.CurrentTransaction.ProcessPID = pid;
                var BRList = new List<string>();
                // upload BO Content
                foreach (var boDict in MPController.CurrentTransaction.BOVector)
                {
                    var retKVP = MPController.UploadBO(boDict["bo_name"], boDict["bo_content"]).First();
                    boDict["boid"] = retKVP.Key;
                    var response = JsonConvert.DeserializeObject<StdResponseEntity>(retKVP.Value);
                    var responseDict = ReturnDataHelper.DecodeList(response);
                    BRList.AddRange(responseDict.Select(br => br.ToString()));
                }
                MPController.CurrentTransaction.BusinessRoleList = new HashSet<string>();
                foreach (var br in BRList)
                {
                    MPController.CurrentTransaction.BusinessRoleList.Add(br);
                }
            }
            else  // exist process
            {
                if (this.ComboBox_Step1_MainBO.Items.Count == 0)
                {
                    MessageBox.Show(@"There must be a Main BO");
                    return;
                }
                MPController.CurrentTransaction.ProcessName = this.ComboBox_Step1_Processes.SelectedItem.ToString();
                MPController.CurrentTransaction.ProcessPID = GlobalContext.Current_Ren_Process_List[this.ComboBox_Step1_Processes.SelectedIndex - 1]["pid"];
            }
            this.tabControl.SelectedIndex += 1;
        }

        private void Button_Step2_ManageMap_Click(object sender, RoutedEventArgs e)
        {
            new ManageMappingForm().ShowDialog();
        }
    }
}
