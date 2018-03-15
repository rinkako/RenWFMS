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
using MahApps.Metro.Controls;

namespace ArticleCrowdSourcingDemo.Form
{
    /// <summary>
    /// RequesterWindow.xaml 的交互逻辑
    /// </summary>
    public partial class RequesterWindow : MetroWindow
    {
        private DataTable requests;

        public RequesterWindow()
        {
            InitializeComponent();
            this.Label_Username.Content = GlobalDataPackage.CurrentUsername;
            this.RefreshRequestList();
        }

        private void RefreshRequestList()
        {
            var reqs = CSCore.RefreshRequest(GlobalDataPackage.CurrentUsername);
            this.requests = reqs.Copy();
            reqs.Columns.RemoveAt(2);
            reqs.Columns.RemoveAt(5 - 1);
            reqs.Columns.RemoveAt(6 - 2);
            reqs.Columns.RemoveAt(7 - 3);
            reqs.Columns.RemoveAt(8 - 4);
            reqs.Columns.RemoveAt(9 - 5);
            this.DataGrid_Requests.ItemsSource = reqs.DefaultView;
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            this.RefreshRequestList();
        }

        private void ButtonBase_OnClick(object sender, RoutedEventArgs e)
        {
            if (this.DataGrid_Requests.SelectedIndex == -1)
            {
                return;
            }
            var sc = this.requests.Rows[this.DataGrid_Requests.SelectedIndex];
            new NewRequestWindow(false, sc["name"].ToString(), sc["description"].ToString(),
                sc["judgeCount"].ToString(), sc["solveCount"].ToString(), sc["solveVoteCount"].ToString(),
                sc["decomposeCount"].ToString(), sc["decomposeVoteCount"].ToString()).ShowDialog();
        }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            InteractionManager.SubmitAndStart();
            new NewRequestWindow(true).ShowDialog();
            this.RefreshRequestList();
        }

        private void DataGrid_Requests_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (this.DataGrid_Requests.SelectedIndex == -1)
            {
                return;
            }
            var sc = this.requests.Rows[this.DataGrid_Requests.SelectedIndex];
            if (String.Compare(sc["status"].ToString(), SolvePhase.Solved.ToString(), StringComparison.CurrentCultureIgnoreCase) == 0)
            {
                new PreviewResultWindow(sc["name"].ToString(), sc["description"].ToString(), sc["solution"].ToString()).ShowDialog();
            }
        }
    }
}
