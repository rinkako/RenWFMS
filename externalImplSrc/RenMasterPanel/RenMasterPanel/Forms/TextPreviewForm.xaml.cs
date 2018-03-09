using System.Windows;

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
