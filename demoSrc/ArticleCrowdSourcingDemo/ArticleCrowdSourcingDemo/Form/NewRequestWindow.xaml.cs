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
    /// LoginForm.xaml 的交互逻辑
    /// </summary>
    public partial class NewRequestWindow : MetroWindow
    {
        private readonly bool isReadonly = false;
        private bool isCancel = true;

        public NewRequestWindow(bool isNew, string taskName = "", string desc = "", string jc = "0", string sc = "0", string svc = "0", string dc = "0", string dvc = "0")
        {
            InitializeComponent();
            this.isReadonly = !isNew;
            this.TextBox_TaskName.IsReadOnly =
                this.TextBox_Description.IsReadOnly =
                    this.TextBox_SolveCount.IsReadOnly =
                        this.TextBox_JudgeCount.IsReadOnly =
                            this.TextBox_DecomposeCount.IsReadOnly =
                                this.TextBox_SolveVoteCount.IsReadOnly =
                                    this.TextBox_DecomposeVoteCount.IsReadOnly = this.isReadonly;
            if (this.isReadonly)
            {
                this.Button_Submit.Content = "OK";
                this.Label_Header.Content = this.Title = "Task Detail";
                this.TextBox_TaskName.Text = taskName;
                this.TextBox_Description.Text = desc;
                this.TextBox_JudgeCount.Text = jc;
                this.TextBox_SolveCount.Text = sc;
                this.TextBox_DecomposeCount.Text = dc;
                this.TextBox_SolveVoteCount.Text = svc;
                this.TextBox_DecomposeVoteCount.Text = dvc;
            }
        }

        private void Button_Submit_Click(object sender, RoutedEventArgs e)
        {
            this.isCancel = false;
            if (!this.isReadonly)
            {
                CSCore.NewRequest(this.TextBox_TaskName.Text.Trim(),
                    this.TextBox_Description.Text.Trim(),
                    Convert.ToInt32(this.TextBox_JudgeCount.Text),
                    Convert.ToInt32(this.TextBox_SolveCount.Text),
                    Convert.ToInt32(this.TextBox_SolveVoteCount.Text),
                    Convert.ToInt32(this.TextBox_DecomposeCount.Text),
                    Convert.ToInt32(this.TextBox_DecomposeVoteCount.Text));
            }
            this.Close();
        }

        private void MetroWindow_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            if (this.isCancel && !this.isReadonly)
            {
                InteractionManager.DoCallback("cancel", "Request", null);
            }
        }
    }
}
