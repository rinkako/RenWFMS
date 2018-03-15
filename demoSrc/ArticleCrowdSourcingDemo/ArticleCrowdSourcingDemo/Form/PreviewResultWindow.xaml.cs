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
    public partial class PreviewResultWindow : MetroWindow
    {
        public PreviewResultWindow(string taskName, string taskDescription, string solution)
        {
            InitializeComponent();
            this.TextBox_Title.Text = $"[Description: {taskName}]";
            this.TextBox_Description.Text = taskDescription;
            this.TextBox_Solution.Text = solution;
        }
    }
}
