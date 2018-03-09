using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using RenMasterPanel.Controller;

namespace RenMasterPanel.Forms
{
    /// <summary>
    /// ManageMappingForm.xaml 的交互逻辑
    /// </summary>
    public partial class ManageMappingForm : Window
    {
        /// <summary>
        /// Flag for initialization finish.
        /// </summary>
        private readonly bool initFinish = false;

        /// <summary>
        /// Buffer of Mappings.
        /// </summary>
        public List<KeyValuePair<String, String>> CurrentMap = new List<KeyValuePair<string, string>>();

        /// <summary>
        /// Create a new mapping management form.
        /// </summary>
        public ManageMappingForm()
        {
            InitializeComponent();
            // Business Roles
            foreach (var br in MPController.CurrentTransaction.BusinessRoleList)
            {
                this.ListBox_BusinessRole.Items.Add(br);
            }
            // Mappings
            foreach (var mapItem in MPController.CurrentTransaction.Mappings)
            {
                this.CurrentMap.Add(mapItem);
            }
            foreach (var mapKVP in MPController.CurrentTransaction.Mappings)
            {
                var resourceType = MPController.GetResourceTypeByGid(mapKVP.Value);
                var resourceStr = "";
                DataRow dr;
                switch (resourceType)
                {
                    case ResourceType.Human:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["human"],
                            mapKVP.Value);
                        resourceStr = String.Format("[H] {0}: {1} {2}", dr["PersonId"], dr["FirstName"],
                            dr["LastName"]);
                        break;
                    case ResourceType.Agent:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["agent"],
                            mapKVP.Value);
                        resourceStr = String.Format("[A] {0}", dr["Name"]);
                        break;
                    case ResourceType.Group:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["group"],
                            mapKVP.Value);
                        resourceStr = String.Format("[G] {0} ({1})", dr["Name"],
                            MPController.ParseGroupType(dr["GroupType"]));
                        break;
                    case ResourceType.Position:
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["position"],
                            mapKVP.Value);
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
                        dr = MPController.FindResourceDataRow(GlobalContext.ResourcesDataSet.Tables["capability"],
                            mapKVP.Value);
                        resourceStr = String.Format("[C] {0}", dr["Name"]);
                        break;
                    default:
                        throw new ArgumentOutOfRangeException();
                }
                this.ListBox_Mappings.Items.Add(String.Format("{0} => {1}", mapKVP.Key, resourceStr));
            }
            // refresh resource list
            this.RefreshLists(true, true, true, true, true);
            // filter ComboBoxs
            foreach (DataRow dataRow in GlobalContext.ResourcesDataSet.Tables["group"].Rows)
            {
                this.ComboBox_Step2_Filter_G.Items.Add($"{dataRow["Name"]}");
            }
            foreach (DataRow dataRow in GlobalContext.ResourcesDataSet.Tables["position"].Rows)
            {
                this.ComboBox_Step2_Filter_P.Items.Add($"{dataRow["Name"]}");
            }
            foreach (DataRow dataRow in GlobalContext.ResourcesDataSet.Tables["capability"].Rows)
            {
                this.ComboBox_Step2_Filter_C.Items.Add($"{dataRow["Name"]}");
            }
            // set finish flag
            this.initFinish = true;
        }

        /// <summary>
        /// Refresh all list in the form.
        /// </summary>
        private void RefreshLists(bool? showHuman, bool? showAgent, bool? showGroup, bool? showPosition, bool? showCapability)
        {
            // Resources
            this.ListBox_Resources.Items.Clear();
            var counter = 0;
            string dv1, dv2;
            DataSet ds;
            do
            {
                dv1 = MPController.GetDataVersion();
                ds = MPController.GetAllResourceInCOrgan();
                dv2 = MPController.GetDataVersion();
                counter++;
            } while (dv1 != dv2);
            if (counter > 100)
            {
                MessageBox.Show("Data version is Error!");
                throw new Exception();
            }
            GlobalContext.ResourcesDataSet = ds;
            var dvItems = dv1.Substring(1, dv1.Length - 2).Split(',');
            Debug.Assert(dvItems.Length == 2);
            GlobalContext.ResourcesDataVersion = dvItems[0];
            GlobalContext.ResourcesCOrganGid = dvItems[1];
            if (showHuman == true)
            {
                foreach (DataRow row in ds.Tables["human"].Rows)
                {
                    var lb = new Label
                    {
                        Content = String.Format("[H] {0}: {1} {2}", row["PersonId"], row["FirstName"], row["LastName"]),
                        Tag = row["GlobalId"]
                    };
                    this.ListBox_Resources.Items.Add(lb);
                }
            }
            if (showAgent == true)
            {
                foreach (DataRow row in ds.Tables["agent"].Rows)
                {
                    var lb = new Label
                    {
                        Content = String.Format("[A] {0}", row["Name"]),
                        Tag = row["GlobalId"],
                        ToolTip = String.Format("{0}, {1}", MPController.ParseAgentType(row["Type"]), row["Location"])
                    };
                    this.ListBox_Resources.Items.Add(lb);
                }
            }
            if (showGroup == true)
            {
                foreach (DataRow row in ds.Tables["group"].Rows)
                {
                    var lb = new Label
                    {
                        Content = String.Format("[G] {0} ({1})", row["Name"], MPController.ParseGroupType(row["GroupType"])),
                        Tag = row["GlobalId"]
                    };
                    this.ListBox_Resources.Items.Add(lb);
                }
            }
            if (showPosition == true)
            {
                foreach (DataRow row in ds.Tables["position"].Rows)
                {
                    var belongTo = row["BelongToGroup"] as string;
                    var belongToStr = "";
                    if (belongTo != null)
                    {
                        var fetched = ds.Tables["group"].Rows.Cast<DataRow>()
                            .FirstOrDefault(groupRow => groupRow["GlobalId"] as string == belongTo);
                        if (fetched != null)
                        {
                            belongToStr = $" (Group: {fetched["Name"]})";
                        }
                    }
                    var lb = new Label
                    {
                        Content = String.Format("[P] {0}{1}", row["Name"], belongToStr),
                        Tag = row["GlobalId"]
                    };
                    this.ListBox_Resources.Items.Add(lb);
                }
            }
            if (showCapability == true)
            {
                foreach (DataRow row in ds.Tables["capability"].Rows)
                {
                    var lb = new Label
                    {
                        Content = String.Format("[C] {0}", row["Name"]),
                        Tag = row["GlobalId"]
                    };
                    this.ListBox_Resources.Items.Add(lb);
                }
            }
        }
        
        /// <summary>
        /// When filter check box changed.
        /// </summary>
        private void CheckBox_CheckChanged(object sender, RoutedEventArgs e)
        {
            if (this.initFinish)
            {
                this.RefreshLists(this.CheckBox_Human.IsChecked,
                    this.CheckBox_Agent.IsChecked,
                    this.CheckBox_Group.IsChecked,
                    this.CheckBox_Position.IsChecked,
                    this.CheckBox_Capability.IsChecked);
            }
        }
        
        /// <summary>
        /// Button: Add a new map.
        /// </summary>
        private void Button_AddMap_Click(object sender, RoutedEventArgs e)
        {
            if (this.ListBox_BusinessRole.SelectedIndex == -1 || this.ListBox_Resources.SelectedIndex == -1)
            {
                return;
            }
            var selectedBRoleName = this.ListBox_BusinessRole.SelectedItem.ToString();
            var selectedResourceLabel = this.ListBox_Resources.SelectedItem as Label;
            var selectedResourceDescriptor = selectedResourceLabel.Content.ToString();
            var selectedResourceGid = selectedResourceLabel.Tag.ToString();
            // find if already exit
            var findedFlag = this.CurrentMap.Any(kvp => kvp.Key == selectedBRoleName && kvp.Value == selectedResourceGid);
            if (findedFlag)
            {
                MessageBox.Show(String.Format("Mapping << {0} => {1} >> already exist.", selectedBRoleName, selectedResourceDescriptor));
                return;
            }
            this.ListBox_Mappings.Items.Add(String.Format("{0} => {1}", selectedBRoleName, selectedResourceDescriptor));
            this.CurrentMap.Add(new KeyValuePair<string, string>(selectedBRoleName, selectedResourceGid));
        }

        /// <summary>
        /// Button: Remove a map.
        /// </summary>
        private void Button_RemoveMap_Click(object sender, RoutedEventArgs e)
        {
            if (this.ListBox_Mappings.SelectedIndex == -1)
            {
                return;
            }
            var removeIdx = this.ListBox_Mappings.SelectedIndex;
            this.ListBox_Mappings.Items.RemoveAt(removeIdx);
            this.CurrentMap.RemoveAt(removeIdx);
            if (this.ListBox_Mappings.Items.Count > 0)
            {
                this.ListBox_Mappings.SelectedIndex = 0;
            }
        }

        /// <summary>
        /// Button: Save and close.
        /// </summary>
        private void Button_Save_Click(object sender, RoutedEventArgs e)
        {
            MPController.CurrentTransaction.Mappings = new List<KeyValuePair<string, string>>();
            foreach (var mapItem in this.CurrentMap)
            {
                MPController.CurrentTransaction.Mappings.Add(mapItem);
            }
            this.Close();
        }
    }
}
