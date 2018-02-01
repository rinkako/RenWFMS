using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
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
        /// Create a new mapping management form.
        /// </summary>
        public ManageMappingForm()
        {
            InitializeComponent();
            this.RefreshLists(true, true, true, true, true);
            this.initFinish = true;
        }

        /// <summary>
        /// Refresh all list in the form.
        /// </summary>
        private void RefreshLists(bool? showHuman, bool? showAgent, bool? showGroup, bool? showPosition, bool? showCapability)
        {
            // Business Roles
            this.ListBox_BusinessRole.Items.Clear();
            foreach (var br in MPController.CurrentTransaction.BusinessRoleList)
            {
                this.ListBox_BusinessRole.Items.Add(br);
            }
            // Resources
            this.ListBox_Resources.Items.Clear();
            var ds = GlobalContext.ResourcesDataSet = MPController.GetAllResourceInCOrgan();
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
                        ToolTip = String.Format("{0}, {1}", ParseAgentType(row["Type"]), row["Location"])
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
                        Content = String.Format("[G] {0} ({1})", row["Name"], ParseGroupType(row["GroupType"])),
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
            // Mappings
            this.ListBox_Mappings.Items.Clear();
        }

        /// <summary>
        /// Parse COrgan group type enum value to enum name.
        /// </summary>
        /// <param name="enumValObj">enum value</param>
        /// <returns>enum name string</returns>
        private static string ParseGroupType(object enumValObj)
        {
            if (enumValObj is int)
            {
                switch (enumValObj)
                {
                    case 0:
                        return "Department";
                    case 1:
                        return "Team";
                    case 3:
                        return "Cluster";
                    case 4:
                        return "Division";
                    case 5:
                        return "Branch";
                    case 6:
                        return "Unit";
                    default:
                        return "Group";
                }
            }
            return "Group";
        }

        /// <summary>
        /// Parse COrgan agent type enum value to enum name.
        /// </summary>
        /// <param name="enumValObj">enum value</param>
        /// <returns>enum name string</returns>
        private static string ParseAgentType(object enumValObj)
        {
            if (enumValObj is int)
            {
                switch (enumValObj)
                {
                    case 0:
                        return "Reentrant";
                    default:
                        return "NotReentrant";
                }
            }
            return "NotReentrant";
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
    }
}
