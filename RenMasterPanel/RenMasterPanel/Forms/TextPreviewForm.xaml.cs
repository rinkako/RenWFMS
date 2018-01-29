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

namespace RenMasterPanel.Forms
{
    /// <summary>
    /// TextPreviewForm.xaml 的交互逻辑
    /// </summary>
    public partial class TextPreviewForm : Window
    {
        public TextPreviewForm(string title, string text)
        {
            InitializeComponent();
            this.Label_PreviewTitle.Content = title;
            this.TextBox_PreviewText.Text = text;
        }
    }
}
