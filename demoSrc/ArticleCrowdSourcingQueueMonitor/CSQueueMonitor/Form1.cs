using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Newtonsoft.Json;

namespace CSTester
{
    public partial class Form1 : Form
    {
        private static readonly Dictionary<string, string> HumanDict = new Dictionary<string, string>
        {
            {"Human_10cc2721-24dc-11e8-b995-2c4d54f01cf2", "Iris"},
            {"Human_22127480-24dc-11e8-8a9b-2c4d54f01cf2", "Jesmine"},
            {"Human_7cbeffd1-24db-11e8-897c-2c4d54f01cf2", "Alice"},
            {"Human_868c2791-24db-11e8-af0e-2c4d54f01cf2", "Bob"},
            {"Human_952a384f-24db-11e8-89cc-2c4d54f01cf2", "Cynthia"},
            {"Human_a21e69a1-24db-11e8-b5d4-2c4d54f01cf2", "Darling"},
            {"Human_bc6e409e-24db-11e8-a352-2c4d54f01cf2", "Erinne"},
            {"Human_c8ed5d9e-250a-11e8-9c5e-2c4d54f01cf2", "Publisher"},
            {"Human_e4810aa1-24db-11e8-8760-2c4d54f01cf2", "Finne"},
            {"Human_ed89a9de-24db-11e8-88bf-2c4d54f01cf2", "Gear"},
            {"Human_fece2280-24db-11e8-b31d-2c4d54f01cf2", "Hyacinth"}
        };

        public Form1()
        {
            InitializeComponent();
        }

        private void button_refresh_Click(object sender, EventArgs e)
        {
            this.textBox_judge.Text =
                this.textBox_decompose.Text =
                    this.textBox_decomposeVote.Text =
                        this.textBox_solve.Text =
                            this.textBox_solveVote.Text = "";
            var rtid = this.textBox1.Text.Trim();
            if (rtid == string.Empty)
            {
                return;
            }
            var workitems = GetWorkitem(rtid);
            foreach (var workitem in workitems)
            {
                var itemDict = workitem.Item2;
                switch (itemDict["TaskName"])
                {
                    case "judgeTask":
                        this.textBox_judge.Text += workitem.Item1.Aggregate("", (s, s1) => s + ParseHumanId(s1) + Environment.NewLine);
                        break;
                    case "decomposeTask":
                        this.textBox_decompose.Text += workitem.Item1.Aggregate("", (s, s1) => s + ParseHumanId(s1) + Environment.NewLine);
                        break;
                    case "decomposeVoteTask":
                        this.textBox_decomposeVote.Text += workitem.Item1.Aggregate("", (s, s1) => s + ParseHumanId(s1) + Environment.NewLine);
                        break;
                    case "solveTask":
                        this.textBox_solve.Text += workitem.Item1.Aggregate("", (s, s1) => s + ParseHumanId(s1) + Environment.NewLine);
                        break;
                    case "solveVoteTask":
                        this.textBox_solveVote.Text += workitem.Item1.Aggregate("", (s, s1) => s + ParseHumanId(s1) + Environment.NewLine);
                        break;
                }
            }
        }

        public string ParseHumanId(string key)
        {
            return HumanDict[key];
        }

        public static List<Tuple<List<string>, Dictionary<String, String>>> GetWorkitem(string rtid)
        {
            try
            {
                var argDict = new Dictionary<string, string>
                {
                    { "signature", Signature },
                    { "rtid", rtid }
                };
                NetClient.PostData(URL_GetAllWorkitem, argDict, out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var workitemList = ReturnDataHelper.DecodeList(response);
                var retList = new List<Tuple<List<string>, Dictionary<String, String>>>();
                retList.AddRange(
                    from workitem
                        in workitemList
                    select ReturnDataHelper.DecodeDictionaryByString(workitem.ToString())
                    into wd
                    let workerIdListDesc = wd["WorkerIdList"]
                    let workerIdList = JsonConvert.DeserializeObject<List<String>>(workerIdListDesc)
                    select new Tuple<List<string>, Dictionary<String, String>>(workerIdList, wd));
                return retList;
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Get workitems, exception occurred" + ex, LogLevel.Error);
                return null;
            }
        }

        public const string Signature = "PrUpNw1dM3zRH6j3eviklCHE9Zbvk9NavGcJ_CibW19h50Yvr-ZZYZqn5Gi_SG1cPVQEIZf2wAJgBmq4dhNj7w7t9wUEz2pcGhn-6kIRO--QqWy121gksPE8B103RtMzuOsQDcErk4LriRQRO7-Xqks-RtpBUnpInnS_lkkajQs";
        
        public const string URL_GetAllWorkitem = "http://127.0.0.1:10234/ns/workitem/getAll";
    }
}
