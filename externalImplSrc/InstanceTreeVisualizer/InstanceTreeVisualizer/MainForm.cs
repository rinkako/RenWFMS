using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using InstanceTreeVisualizer.Entity;
using InstanceTreeVisualizer.Utility;
using Newtonsoft.Json;

namespace InstanceTreeVisualizer
{
    public partial class MainForm : Form
    {
        private FriendlyTreeNode RootNode;

        public MainForm()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                var rtid = this.textBox_rtid.Text.Trim();
                if (rtid == "")
                {
                    return;
                }
                NetClient.PostData("http://localhost:10232/gateway/getSpanTree", new Dictionary<string, string>
                    {
                        {"rtid", rtid}
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var dataStr = response.returnElement.data.ToString();
                this.RootNode = JsonConvert.DeserializeObject<FriendlyTreeNode>(dataStr);
                var curTreeNode = new TreeNode();
                MainForm.Nephren(this.RootNode, curTreeNode);
                this.treeView1.Nodes.Clear();
                this.treeView1.Nodes.Add(curTreeNode);
                this.treeView1.ExpandAll();
            }
            catch (Exception ex)
            {
                MessageBox.Show(@"Failed to show instance tree, may process finished or rtid not exist? " + ex);
                this.treeView1.Nodes.Clear();
            }
        }

        private static void Nephren(FriendlyTreeNode curNode, TreeNode curTreeNode)
        {
            curTreeNode.Text = curNode.NotifiableId;
            curTreeNode.Tag = curNode;
            foreach (var child in curNode.Children)
            {
                var tn = new TreeNode();
                curTreeNode.Nodes.Add(tn);
                Nephren(child, tn);
            }
        }

        private void treeView1_AfterSelect(object sender, TreeViewEventArgs e)
        {
            if (this.treeView1.SelectedNode == null)
            {
                return;
            }
            var nodeCtx = this.treeView1.SelectedNode.Tag as FriendlyTreeNode;
            this.textBox_Description.Text = String.Format(
                ">>Notifiable ID:{0}{1}{0}{0}{0}>>Global ID:{0}{2}{0}{0}{0}>>BO Name:{0}{3}{0}{0}{0}>>Status:{0}{4}{0}",
                Environment.NewLine, nodeCtx.NotifiableId, nodeCtx.GlobalId, nodeCtx.BOName,
                nodeCtx.StatusDescriptor);
        }
    }
}
