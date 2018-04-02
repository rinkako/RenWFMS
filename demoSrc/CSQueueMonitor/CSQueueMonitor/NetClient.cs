using System;
using System.Collections.Generic;
using System.Net;
using System.Text;
using System.Windows.Forms;

namespace CSTester
{
    internal class NetClient
    {
        public static bool PostData(string url, Dictionary<string, string> argsDict, out string result)
        {
            try
            {
                var client = new WebClient();
                var sb = new StringBuilder();
                if (argsDict != null)
                {
                    foreach (var arg in argsDict)
                    {
                        sb.Append("&" + arg.Key + "=" + arg.Value);
                    }
                }
                if (sb.Length > 0)
                {
                    sb = sb.Remove(0, 1);
                }
                var encoding = Encoding.UTF8;
                var postData = encoding.GetBytes(sb.ToString());
                client.Headers.Add("Content-Type", "application/x-www-form-urlencoded");
                client.Headers.Add("ContentLength", postData.Length.ToString());
                var respondData = client.UploadData(url, "POST", postData);
                result = encoding.GetString(respondData);
                return true;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.ToString());
                result = null;
                return false;
            }
        }
    }
}
