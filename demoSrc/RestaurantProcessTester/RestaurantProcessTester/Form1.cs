using System;
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

        private void button4_Click(object sender, EventArgs e)
        {
            Ren.PerformSubmitAndStart();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackSubmit();
        }

        private void button6_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackProduced();
        }

        private void button7_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackTestCompleted();
        }

        private void button8_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackDelivered();
            Ren.PerformCallbackUpdateDeliTimeKO();
        }

        private void button9_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackArchivedKO();
        }

        private void button10_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackRequestCheck();
        }

        private void button11_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackCalculated();
        }

        private void button12_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackPaid();
        }

        private void button13_Click(object sender, EventArgs e)
        {
            Ren.PerformCallbackArchivedGC();
        }

        private void button14_Click(object sender, EventArgs e)
        {
            Ren.PerformCheckFinish();
        }

        private void button15_Click(object sender, EventArgs e)
        {
            button1_Click(null, null);
            button2_Click(null, null);
            button3_Click(null, null);
            button4_Click(null, null);
            button5_Click(null, null);
            button6_Click(null, null);
            button7_Click(null, null);
            button8_Click(null, null);
            button9_Click(null, null);
            button10_Click(null, null);
            button11_Click(null, null);
            button12_Click(null, null);
            button13_Click(null, null);
            button14_Click(null, null);
        }
    }
}
