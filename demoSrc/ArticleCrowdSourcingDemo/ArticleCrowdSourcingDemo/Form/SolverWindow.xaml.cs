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
using ArticleCrowdSourcingDemo.Entity;
using MahApps.Metro.Controls;
using Newtonsoft.Json;

namespace ArticleCrowdSourcingDemo.Form
{
    /// <summary>
    /// SolverWindow.xaml 的交互逻辑
    /// </summary>
    public partial class SolverWindow : MetroWindow
    {
        public SolverWindow()
        {
            InitializeComponent();
            this.Label_Username.Content = GlobalDataPackage.CurrentUsername;
            this.RefreshList();
        }

        public void RefreshList()
        {
            this.ListBox_Solver_Tasks.Items.Clear();
            var rtids = CSCore.GetAllActiveRTID();
            foreach (var rtid in rtids)
            {
                var workitems = InteractionManager.GetMyWorkitem(GlobalDataPackage.CurrentUserWorkerId, rtid);
                foreach (var workitem in workitems)
                {
                    var itemDict = workitem.Item2;
                    var request = CSCore.GetRequestByRTID(rtid);
                    var retArgs = ReturnDataHelper.DecodeDictionaryByString(itemDict["Argument"]);
                    var lbi = new ListBoxItem
                    {
                        Content = String.Format("Task: {0} | From CrowdSourcing Request: {1} | By: {2}",
                            itemDict["TaskName"], request.ItemArray[1], request.ItemArray[2]),
                        Tag = new Tuple<Dictionary<String, String>, Dictionary<String, String>>(itemDict, retArgs)
                    };
                    this.ListBox_Solver_Tasks.Items.Add(lbi);
                }
            }
        }

        private void ListBox_Solver_Tasks_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (this.ListBox_Solver_Tasks.SelectedIndex == -1)
            {
                return;
            }
            var listItem = this.ListBox_Solver_Tasks.SelectedItem as ListBoxItem;
            var itemDict = (listItem.Tag as Tuple<Dictionary<String, String>, Dictionary<String, String>>).Item1;
            //var argDict = (listItem.Tag as Tuple<Dictionary<String, String>, Dictionary<String, String>>).Item2;
            //var request = CSCore.GetRequestByRTID(itemDict["Rtid"]);
            var passedArguments = ReturnDataHelper.DecodeDictionaryByString(itemDict["Argument"]);
            switch (itemDict["TaskName"])
            {
                case "judgeTask":
                    new JudgeTaskWindow(passedArguments["taskName"], passedArguments["taskDescription"], itemDict["Wid"]).ShowDialog();
                    break;
                case "decomposeTask":
                    new DecomposeWindow(passedArguments["taskName"], passedArguments["taskDescription"], itemDict["Wid"], itemDict["Rtid"], itemDict["CallbackNodeId"]).ShowDialog();
                    break;
                case "decomposeVoteTask":
                    new DecomposeVoteWindow(passedArguments["taskName"], passedArguments["taskDescription"], itemDict["Wid"], itemDict["Rtid"], itemDict["CallbackNodeId"]).ShowDialog();
                    break;
                case "solveTask":
                    new SolveWindow(passedArguments["taskName"], passedArguments["taskDescription"], itemDict["Wid"], itemDict["Rtid"], itemDict["CallbackNodeId"]).ShowDialog();
                    break;
                case "solveVoteTask":
                    new SolveVoteWindow(passedArguments["taskName"], passedArguments["taskDescription"], itemDict["Wid"], itemDict["Rtid"], itemDict["CallbackNodeId"]).ShowDialog();
                    break;
            }
            this.RefreshList();
        }
    }
}
