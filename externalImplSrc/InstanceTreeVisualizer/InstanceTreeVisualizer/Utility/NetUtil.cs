using System;
using System.Collections.Generic;
using System.Net;
using System.Text;

namespace InstanceTreeVisualizer.Utility
{
    /// <summary>
    /// 提供HTTP服务
    /// </summary>
    internal class NetClient
    {
        /// <summary>
        /// 对指定的URL做POST动作
        /// </summary>
        /// <remarks>该方法是同步的，线程安全的，如果需要异步请利用信号分发机制</remarks>
        /// <param name="url">要访问的URL</param>
        /// <param name="argsDict">POST的参数字典</param>
        /// <param name="result">[out] URL的反馈</param>
        /// <param name="encoding">编码器</param>
        /// <returns>操作是否成功</returns>
        public static bool PostData(string url, Dictionary<string, string> argsDict, out string result,
            Encoding encoding = null)
        {
            var client = new WebClient();
            // 提交的内容
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
            // 编码
            if (encoding == null)
            {
                encoding = Encoding.UTF8;
            }
            byte[] postData = encoding.GetBytes(sb.ToString());
            // POST
            client.Headers.Add("Content-Type", "application/x-www-form-urlencoded");
            client.Headers.Add("ContentLength", postData.Length.ToString());
            byte[] respondData = client.UploadData(url, "POST", postData);
            result = encoding.GetString(respondData);
            return true;
        }
    }
}
