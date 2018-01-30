using System;
using System.Collections.Generic;
using System.Diagnostics;
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

namespace RenMasterPanel.Forms
{
    /// <summary>
    /// ProcessNameForm.xaml 的交互逻辑
    /// </summary>
    public partial class ProcessNameForm : MetroWindow
    {
        public String ProcessName { get; set; } = null;

        public ProcessNameForm()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            var trimedText = this.TextBox_ProcessName.Text?.Trim();
            // check empty
            if (String.IsNullOrEmpty(trimedText))
            {
                MessageBox.Show("Please enter the process name.");
                return;
            }
            // check unique
            Debug.Assert(GlobalContext.Current_Ren_Process_List != null);
            var sFlag = GlobalContext.Current_Ren_Process_List.TrueForAll(t => String.Compare(t["processName"], trimedText, StringComparison.CurrentCultureIgnoreCase) != 0);
            if (sFlag == false)
            {
                MessageBox.Show("Process name should be unique.(Ignored case)");
                return;
            }
            this.ProcessName = trimedText;
            this.Close();
        }
    }
}
