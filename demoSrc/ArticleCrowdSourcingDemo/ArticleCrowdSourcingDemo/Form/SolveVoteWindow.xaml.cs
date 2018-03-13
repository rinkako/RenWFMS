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
    public partial class SolveVoteWindow : MetroWindow
    {
        private readonly string workitemId;
        private readonly string nodeId;
        private readonly string rtid;
        private readonly List<Tuple<string, string>> solveList;

        public SolveVoteWindow(string taskName, string description, string workitemId, string rtid, string nodeId)
        {
            InitializeComponent();
            this.workitemId = workitemId;
            this.nodeId = nodeId;
            this.rtid = rtid;
            this.TextBox_Title.Text = $"[Description: {taskName}]";
            this.TextBox_Description.Text = description;
            this.solveList = CSCore.GetSolveList(this.rtid, this.nodeId);
            this.ListBox_CandidateSolutions.Items.Clear();
            for (var i = 0; i < this.solveList.Count; i++)
            {
                var lbi = new ListBoxItem
                {
                    Content = "Solution " + (i + 1)
                };
                this.ListBox_CandidateSolutions.Items.Add(lbi);
            }
        }

        private void Button_VoteThis_Click(object sender, RoutedEventArgs e)
        {
            if (this.ListBox_CandidateSolutions.SelectedIndex == -1)
            {
                return;
            }
            var dr = MessageBox.Show("Sure to vote this solution?", "Information", MessageBoxButton.OKCancel);
            if (dr == MessageBoxResult.Cancel)
            {
                return;
            }
            var worker = this.solveList[this.ListBox_CandidateSolutions.SelectedIndex].Item1;
            CSCore.VoteForSolution(this.rtid, this.nodeId, worker, this.workitemId);
            this.Close();
        }

        private void ListBox_CandidateSolutions_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.ListBox_CandidateSolutions.SelectedIndex == -1)
            {
                return;
            }
            this.TextBox_Solution.Text = this.solveList[this.ListBox_CandidateSolutions.SelectedIndex].Item2;
        }
    }
}
