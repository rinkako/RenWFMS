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
    public partial class DecomposeVoteWindow : MetroWindow
    {
        private readonly string workitemId;
        private readonly string nodeId;
        private readonly string rtid;
        private readonly List<Tuple<string, List<string>>> decomposes;

        public DecomposeVoteWindow(string taskName, string description, string workitemId, string rtid, string nodeId)
        {
            InitializeComponent();
            this.workitemId = workitemId;
            this.nodeId = nodeId;
            this.rtid = rtid;
            this.TextBox_Title.Text = $"[Description: {taskName}]";
            this.TextBox_Description.Text = description;
            this.decomposes = CSCore.GetDecomposeList(rtid, nodeId);
            this.ListBox_Composes.Items.Clear();
            for (var i = 0; i < this.decomposes.Count; i++)
            {
                var lbi = new ListBoxItem()
                {
                    Content = "Plan " + (i + 1),
                    Tag = this.decomposes[i]
                };
                this.ListBox_Composes.Items.Add(lbi);
            }
        }

        private void ListBox_Composes_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.ListBox_Composes.SelectedIndex != -1)
            {
                var plansItem = this.decomposes[this.ListBox_Composes.SelectedIndex];
                this.ListBox_Plans.Items.Clear();
                foreach (var plan in plansItem.Item2)
                {
                    this.ListBox_Plans.Items.Add(plan);
                }
            }
        }

        private void Button_VoteThis_Click(object sender, RoutedEventArgs e)
        {
            if (this.ListBox_Composes.SelectedIndex == -1)
            {
                return;
            }
            var dr = MessageBox.Show("Sure to choose this decompose plan?", "Information", MessageBoxButton.OKCancel, MessageBoxImage.Information);
            if (dr == MessageBoxResult.Cancel)
            {
                return;
            }
            var selectedItem = (this.ListBox_Composes.SelectedItem as ListBoxItem).Tag as Tuple<string, List<string>>;
            CSCore.VoteForDecompose(this.rtid, this.nodeId, selectedItem.Item1, this.workitemId);
            this.Close();
        }
    }
}
