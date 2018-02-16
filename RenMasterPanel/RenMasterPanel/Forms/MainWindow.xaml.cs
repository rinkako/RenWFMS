using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
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
        /// <summary>
        /// Form initialization finish flag.
        /// </summary>
        private readonly bool InitFlag = false;

        /// <summary>
        /// Create a new main window.
        /// </summary>
        public MainWindow()
        {
            InitializeComponent();
            var processList = MPController.GetProcess();
            GlobalContext.Current_Ren_Process_List = processList;
            foreach (var process in processList)
            {
                this.ComboBox_Step1_Processes.Items.Add(process["processName"]);
            }
            this.InitFlag = true;
        }

        #region Step1

        /// <summary>
        /// Step1: ComboBox of process choosing selection changed.
        /// </summary>
        private void ComboBox_Step1_ProcessChoose_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.ComboBox_Step1_Processes.SelectedIndex != 0)
            {
                this.Button_Step1_Open.Visibility = Visibility.Hidden;
                this.TextBox_Step1_Open.Visibility = Visibility.Hidden;
                this.ComboBox_Step1_MainBO.IsEnabled = false;
                var processEntity =
                    GlobalContext.Current_Ren_Process_List[this.ComboBox_Step1_Processes.SelectedIndex - 1];
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

        /// <summary>
        /// Step1 Button: Open Folder.
        /// </summary>
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

        /// <summary>
        /// Step1: Preview BO content by double click listbox.
        /// </summary>
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

        /// <summary>
        /// Step1 Button: Next.
        /// </summary>
        private void Button_Step1_Next_OnClick(object sender, RoutedEventArgs e)
        {
            if (this.ComboBox_Step1_Processes.SelectedIndex == 0) // new process
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
                var pid = MPController.CreateProcess(npf.ProcessName,
                    this.ComboBox_Step1_MainBO.SelectedItem.ToString().Trim());
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
            else // exist process
            {
                if (this.ComboBox_Step1_MainBO.Items.Count == 0)
                {
                    MessageBox.Show(@"There must be a Main BO");
                    return;
                }
                MPController.CurrentTransaction.ProcessName = this.ComboBox_Step1_Processes.SelectedItem.ToString();
                MPController.CurrentTransaction.ProcessPID =
                    GlobalContext.Current_Ren_Process_List[this.ComboBox_Step1_Processes.SelectedIndex - 1]["pid"];
            }
            this.tabControl.SelectedIndex += 1;
        }

        #endregion

        #region Step2

        /// <summary>
        /// Step2 Button: Open Mappings management form.
        /// </summary>
        private void Button_Step2_ManageMap_Click(object sender, RoutedEventArgs e)
        {
            new ManageMappingForm().ShowDialog();
            this.RefreshPreviewMappings();
        }

        /// <summary>
        /// Step2: Refresh Preivew Mappings Listbox.
        /// </summary>
        private void RefreshPreviewMappings()
        {
            this.ListBox_Step2_PreviewMap.Items.Clear();

            foreach (var mapKVP in MPController.CurrentTransaction.Mappings)
            {
                var resourceType = MPController.GetResourceTypeByGid(mapKVP.Value);
                var resourceStr = "";
                DataRow dr;
                switch (resourceType)
                {
                    case ResourceType.Human:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["human"], mapKVP.Value);
                        resourceStr = String.Format("[H] {0}: {1} {2}", dr["PersonId"], dr["FirstName"],
                            dr["LastName"]);
                        break;
                    case ResourceType.Agent:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["agent"], mapKVP.Value);
                        resourceStr = String.Format("[A] {0}", dr["Name"]);
                        break;
                    case ResourceType.Group:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["group"], mapKVP.Value);
                        resourceStr = String.Format("[G] {0} ({1})", dr["Name"],
                            MPController.ParseGroupType(dr["GroupType"]));
                        break;
                    case ResourceType.Position:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["position"], mapKVP.Value);
                        var belongTo = dr["BelongToGroup"] as string;
                        var belongToStr = "";
                        if (belongTo != null)
                        {
                            var fetched = GlobalContext.ResourcesDataSet.Tables["group"].Rows.Cast<DataRow>()
                                .FirstOrDefault(groupRow => groupRow["GlobalId"] as string == belongTo);
                            if (fetched != null)
                            {
                                belongToStr = $" (Group: {fetched["Name"]})";
                            }
                        }
                        resourceStr = String.Format("[P] {0}{1}", dr["Name"], belongToStr);
                        break;
                    case ResourceType.Capability:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["capability"], mapKVP.Value);
                        resourceStr = String.Format("[C] {0}", dr["Name"]);
                        break;
                    default:
                        throw new ArgumentOutOfRangeException();
                }
                this.ListBox_Step2_PreviewMap.Items.Add(String.Format("{0} => {1}", mapKVP.Key, resourceStr));
            }
        }

        /// <summary>
        /// Step2 Button: Next.
        /// </summary>
        private void Button_Step2_Next_OnClick(object sender, RoutedEventArgs e)
        {
            // validation mapping
            var notMapFlag = false;
            foreach (var br in MPController.CurrentTransaction.BusinessRoleList)
            {
                if (notMapFlag = MPController.CurrentTransaction.Mappings.All(t => t.Key != br))
                {
                    break;
                }
            }
            if (notMapFlag)
            {
                MessageBox.Show(@"You left some Business Role mapping nothing, which is illegal.");
                return;
            }
            // update UI
            this.Label_Step3_ProcessName.Content = MPController.CurrentTransaction.ProcessName;
            this.ListBox_Step3_BOList.Items.Clear();
            foreach (var boDict in MPController.CurrentTransaction.BOVector)
            {
                this.ListBox_Step3_BOList.Items.Add(boDict["bo_name"]);
            }
            this.tabControl.SelectedIndex += 1;
        }

        /// <summary>
        /// Step2: Save map to local file.
        /// </summary>
        private void Button_Step2_Save_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                if (MPController.CurrentTransaction.Mappings == null)
                {
                    return;
                }
                var fd = new SaveFileDialog
                {
                    DefaultExt = "MappingFile | *.borm"
                };
                fd.ShowDialog();
                if (String.IsNullOrEmpty(fd.FileName))
                {
                    return;
                }
                IOUtils.Serialization(MPController.CurrentTransaction.Mappings, fd.FileName);
                MessageBox.Show(@"Save role map successfully.");
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Button_Step2_Save_Click exception occurred" + ex, "MainWindow", LogLevel.Error);
            }
        }

        /// <summary>
        /// Step2: Load map from local file.
        /// </summary>
        private void Button_Step2_Load_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                var fd = new OpenFileDialog()
                {
                    DefaultExt = "MappingFile | *.borm"
                };
                fd.ShowDialog();
                if (String.IsNullOrEmpty(fd.FileName))
                {
                    return;
                }
                MPController.CurrentTransaction.Mappings = (List<KeyValuePair<String, String>>)IOUtils.Unserialization(fd.FileName);
                MessageBox.Show(@"Load role map successfully.");
                this.RefreshPreviewMappings();
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Button_Step2_Save_Click exception occurred" + ex, "MainWindow", LogLevel.Error);
            }
        }

        #endregion

        #region Step3

        /// <summary>
        /// Step3: ComboBox Changed of launch type.
        /// </summary>
        private void ComboBox_Step3_Launch_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (!this.InitFlag)
            {
                return;
            }
            this.DatePicker_Step3_LaunchByTime.Visibility =
                this.Label_Step3_h_LaunchByTime.Visibility =
                    this.Label_Step3_m_LaunchByTime.Visibility =
                        this.TextBox_Step3_Hour_LaunchByTime.Visibility =
                            this.TextBox_Step3_Minute_LaunchByTime.Visibility =
                                this.TextBox_Step3_Second_LaunchByTime.Visibility =
                                    this.ComboBox_Step3_Launch.SelectedIndex == 1
                                        ? Visibility.Visible
                                        : Visibility.Hidden;

        }

        /// <summary>
        /// Step3 Button: Next.
        /// </summary>
        private void Button_Step3_Next_OnClick(object sender, RoutedEventArgs e)
        {
            // make sure
            var dr = MessageBox.Show(@"Are you sure to submit this launch transaction?", @"Confirm",
                MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button2);
            if (dr == System.Windows.Forms.DialogResult.Cancel)
            {
                return;
            }
            // upload to NS and Launch process
            MPController.CurrentTransaction.LaunchType = this.ComboBox_Step3_Launch.SelectedIndex;
            MPController.CurrentTransaction.IsolationType = this.ComboBox_Step3_WhenMapChange.SelectedIndex;
            MPController.CurrentTransaction.FailureType = this.ComboBox_Step3_WhenFailure.SelectedIndex;
            MPController.CurrentTransaction.AuthType = this.ComboBox_Step3_AuthorizationType.SelectedIndex;
            var submitRes = MPController.SubmitProcess();
            var submitResItem = submitRes.Split(',');
            GlobalContext.CurrentRTID = submitResItem[0];
            GlobalContext.CurrentProcessSelfSignature = submitResItem[1];
            Debug.Assert(GlobalContext.CurrentRTID != null);
            MPController.RegisterMappings();
            // update UI
            this.Label_Step4_Rtid.Text = GlobalContext.CurrentRTID;
            if (String.IsNullOrEmpty(GlobalContext.CurrentProcessSelfSignature))
            {
                this.Label_Step4_AuthSign.Visibility = this.Label_Step4_AuthSign_Warn.Visibility =
                    this.TextBox_Step4_AuthSign.Visibility = Visibility.Hidden;
            }
            else
            {
                this.Label_Step4_AuthSign.Visibility = this.Label_Step4_AuthSign_Warn.Visibility =
                    this.TextBox_Step4_AuthSign.Visibility = Visibility.Visible;
                this.TextBox_Step4_AuthSign.Text = GlobalContext.CurrentProcessSelfSignature;
            }
            this.tabControl.SelectedIndex += 1;
        }

        #endregion

        #region Step4

        /// <summary>
        /// Step3 Button: Next.
        /// </summary>
        private void Button_Step4_DebugStart(object sender, RoutedEventArgs e)
        {
            MPController.LoadParticipant();
            MessageBox.Show(@"OK");
        }
        
        #endregion
        
    }
}
