using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text;

namespace ResourcingSpeedTester
{
    internal class TesterClient
    {
        /// <summary>
        /// 回调计数器
        /// </summary>
        public static int Counter = 0;

        /// <summary>
        /// 测试开始时刻
        /// </summary>
        public static DateTime? BeginTimestamp = null;

        /// <summary>
        /// 测试结束时刻
        /// </summary>
        public static DateTime? EndTimestamp = null;

        /// <summary>
        /// 对指定的URL做POST动作
        /// </summary>
        /// <param name="url">要访问的URL</param>
        /// <param name="argsDict">POST的参数字典</param>
        /// <param name="result">[out] URL的反馈</param>
        /// <param name="encoding">编码器</param>
        /// <returns>操作是否成功</returns>
        public static bool PostData(string url, Dictionary<string, string> argsDict, out string result, Encoding encoding = null)
        {
            try
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
            catch (Exception ex)
            {
                result = null;
                return false;
            }
        }

        /// <summary>
        /// 访问指定的URL并下载页面中的内容为字符串
        /// </summary>
        /// <param name="url">要访问的URL</param>
        /// <param name="result">[out] 获得的字符串</param>
        /// <returns>操作是否成功</returns>
        public static bool FetchString(string url, out string result)
        {
            try
            {
                var wb = new WebClient();
                result = wb.DownloadString(url);
                return true;
            }
            catch (Exception ex)
            {
                result = null;
                return false;
            }
        }

        /// <summary>
        /// 把一个相对URI转化为绝对路径
        /// </summary>
        /// <param name="uri">相对程序运行目录的相对路径</param>
        /// <returns>绝对路径</returns>
        public static string ParseURItoURL(string uri)
        {
            return AppDomain.CurrentDomain.BaseDirectory + uri;
        }
    }
}
