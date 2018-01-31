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
using RenMasterPanel.Controller;

namespace RenMasterPanel.Forms
{
    /// <summary>
    /// ManageMappingForm.xaml 的交互逻辑
    /// </summary>
    public partial class ManageMappingForm : Window
    {
        public ManageMappingForm()
        {
            InitializeComponent();
            MPController.GetAllResourceInCOrgan();
        }
    }
}
