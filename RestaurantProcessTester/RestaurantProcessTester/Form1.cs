using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace RestaurantProcessTester
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            Ren.ReadLocationFromSteady();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Ren.PerformLogin();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Ren.PerformUploadProcess();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            Ren.PerformMappingBRole();
        }
    }
}
