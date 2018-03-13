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
    public partial class SolveWindow : MetroWindow
    {
        private readonly string workitemId;
        private readonly string nodeId;
        private readonly string rtid;

        public SolveWindow(string taskName, string description, string workitemId, string rtid, string nodeId)
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
            var dr = MessageBox.Show("Sure to commit?", "Information", MessageBoxButton.OKCancel);
            if (dr == MessageBoxResult.Cancel)
            {
                return;
            }
            CSCore.Solve(this.rtid, this.workitemId, this.nodeId, this.TextBox_Solution.Text);
            this.Close();
        }
    }
}
