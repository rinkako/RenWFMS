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

namespace ArticleCrowdSourcingDemo.Form
{
    /// <summary>
    /// JudgeTaskWindow.xaml 的交互逻辑
    /// </summary>
    public partial class JudgeTaskWindow : Window
    {
        private readonly string workitemId;

        public JudgeTaskWindow(string taskName, string description, string workitemId)
        {
            InitializeComponent();
            this.workitemId = workitemId;
            this.TextBox_Title.Text = $"[Description: {taskName}]";
            this.TextBox_Description.Text = description;
        }
        
        private void Button_Simple_Click(object sender, RoutedEventArgs e)
        {
            this.HandleCallbackVote(false);
            this.Close();
        }

        private void Button_Complex_Click(object sender, RoutedEventArgs e)
        {
            this.HandleCallbackVote(true);
            this.Close();
        }

        private void HandleCallbackVote(bool isComplex)
        {
            var args = new Dictionary<string, object>
            {
                {"simple", isComplex ? 0 : 1}
            };
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, this.workitemId, args);
        }
    }
}
