using System;
using System.Collections.Generic;
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
using MahApps.Metro.Controls;

namespace ArticleCrowdSourcingDemo.Form
{
    /// <summary>
    /// PreviewResultWindow.xaml 的交互逻辑
    /// </summary>
    public partial class DecomposeWindow : MetroWindow
    {
        private readonly string workitemId;
        private readonly string nodeId;
        private readonly string rtid;

        public DecomposeWindow(string taskName, string description, string workitemId, string rtid, string nodeId)
        {
            InitializeComponent();
            this.workitemId = workitemId;
            this.nodeId = nodeId;
            this.rtid = rtid;
            this.TextBox_Title.Text = $"[Description: {taskName}]";
            this.TextBox_Description.Text = description;
        }

        private void Button_Submit_Click(object sender, RoutedEventArgs e)
        {
            if (this.ListBox_MyPlans.Items.Count < 2)
            {
                MessageBox.Show("Decompose should contain at least 2 steps");
                return;
            }
            var plist = (from object item in this.ListBox_MyPlans.Items select item as string).ToList();
            CSCore.Decompose(this.rtid, this.workitemId, this.nodeId, plist);
            this.Close();
        }

        private void Button_PlanAdd_Click(object sender, RoutedEventArgs e)
        {
            this.ListBox_MyPlans.Items.Add(this.TextBox_StepPlan.Text);
            this.TextBox_StepPlan.Text = String.Empty;
        }

        private void Button_PlanRemove_Click(object sender, RoutedEventArgs e)
        {
            if (this.ListBox_MyPlans.SelectedIndex == -1)
            {
                return;
            }
            this.ListBox_MyPlans.Items.RemoveAt(this.ListBox_MyPlans.SelectedIndex);
        }
    }
}
