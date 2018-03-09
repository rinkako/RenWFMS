using System;
using System.Windows;
using MahApps.Metro.Controls;
using RenMasterPanel.Controller;

namespace RenMasterPanel.Forms
{
    /// <summary>
    /// LoginForm.xaml 的交互逻辑
    /// </summary>
    public partial class LoginForm : MetroWindow
    {
        public LoginForm()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (String.IsNullOrEmpty(this.TextBox_username.Text) ||
                String.IsNullOrEmpty(this.PasswordBox_password.Password))
            {
                MessageBox.Show("请完整填写");
                return;
            }
            var retVal = MPController.Login(this.TextBox_username.Text.Trim(), this.PasswordBox_password.Password);
            if (retVal.Key)
            {
                MPController.CurrentTransaction.AuthToken = retVal.Value;
                MPController.CurrentTransaction.RenUsername = this.TextBox_username.Text.Trim();
                var mw = new MainWindow();
                mw.Title += $"  [{this.TextBox_username.Text.Trim()}]";
                mw.Show();
                this.Close();
            }
            else
            {
                MessageBox.Show("Fail");
            }
        }
    }
}
