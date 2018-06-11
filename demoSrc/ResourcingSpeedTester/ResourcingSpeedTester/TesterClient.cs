using Newtonsoft.Json;
using ResourcingSpeedTester.Entity;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace ResourcingSpeedTester
{
    internal static class NetClient
    {
        /// <summary>
        /// 回调计数器
        /// </summary>
        public static int Counter = 0;

        /// <summary>
        /// 测试开始时刻
        /// </summary>
        public static DateTime BeginTimestamp;

        /// <summary>
        /// 测试结束时刻
        /// </summary>
        public static DateTime EndTimestamp;

        public static TransactionPackage transaction = new TransactionPackage();

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
        /// 启动流程准备测试
        /// </summary>
        public static void Prepare()
        {
            NetClient.PostData(GlobalContext.LocationDict["Login"],
                    new Dictionary<string, string>
                    {
                        { "username", "admin@admin" },
                        { "password", "admin" }
                    },
                    out string retStr);
            var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
            var retToken = ReturnDataHelper.DecodeString(response);
            var successFlag = !retToken.StartsWith("#");
            transaction.AuthToken = retToken;
            LoadProcessFromDirectory(ParseURItoURL("processDir"));
            var processName = "RestaurantTest_" + DateTime.Now.ToString().Replace(' ', '_');
            NetClient.PostData(GlobalContext.LocationDict["CreateProcess"],
                new Dictionary<string, string>
                {
                        {"token", transaction.AuthToken },
                        {"renid", "admin@admin" },
                        {"name", processName },
                        {"mainbo", "GuestOrder"}
                },
                out retStr);
            response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
            transaction.ProcessPID = ReturnDataHelper.DecodeString(response);
            transaction.ProcessName = processName;
            var BRList = new List<string>();
            foreach (var boDict in transaction.BOVector)
            {
                var retKVP = UploadBO(boDict["bo_name"], boDict["bo_content"]).First();
                boDict["boid"] = retKVP.Key;
                response = JsonConvert.DeserializeObject<StdResponseEntity>(retKVP.Value);
                var responseDict = ReturnDataHelper.DecodeList(response);
                BRList.AddRange(responseDict.Select(br => br.ToString()));
            }
            transaction.BusinessRoleList = new HashSet<string>();
            foreach (var br in BRList)
            {
                transaction.BusinessRoleList.Add(br);
            }

            var CurrentMap = new List<KeyValuePair<string, string>>
            {
                new KeyValuePair<string, string>("waiter", GlobalContext.WorkerId)
            };
            transaction.Mappings = new List<KeyValuePair<string, string>>();
            foreach (var mapItem in CurrentMap)
            {
                transaction.Mappings.Add(mapItem);
            }
            transaction.LaunchType = 0;
            transaction.IsolationType = 0;
            transaction.FailureType = 0;
            transaction.AuthType = 0;
            transaction.RenUsername = "admin@admin";
            NetClient.PostData(GlobalContext.LocationDict["SubmitProcess"], new Dictionary<string, string>
                    {
                        { "token", transaction.AuthToken },
                        { "pid", transaction.ProcessPID },
                        { "from", "SpeedTester.NET" },
                        { "renid", transaction.RenUsername },
                        { "bindingType", transaction.IsolationType.ToString() },
                        { "launchType", transaction.LaunchType.ToString() },
                        { "failureType", transaction.FailureType.ToString() },
                        { "authType", transaction.AuthType.ToString() },
                        { "binding", "" }
                    },
                    out retStr);
            response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
            var submitRes = ReturnDataHelper.DecodeString(response);
            var submitResItem = submitRes.Split(',');
            NetClient.rtid = submitResItem[0];

            NetClient.PostData(GlobalContext.LocationDict["UploadMapping"], new Dictionary<string, string>
                    {
                        { "token", transaction.AuthToken },
                        { "rtid", NetClient.rtid },
                        { "organgid", "COrg_571d200f-0f35-11e8-9072-5404a6a99e5d" },
                        { "dataversion", "version1" },
                        { "map", GeneratePostMapStringOfMappings() }
                    },
                    out retStr);
            response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);

            NetClient.PostData(GlobalContext.LocationDict["LoadParticipant"], new Dictionary<string, string>
                    {
                        { "token", transaction.AuthToken },
                        { "rtid", NetClient.rtid },
                        { "renid", transaction.RenUsername }
                    },
                    out retStr);
            response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);

            NetClient.PostData(GlobalContext.LocationDict["StartProcess"], new Dictionary<string, string>
                    {
                        { "token", transaction.AuthToken },
                        { "rtid", NetClient.rtid }
                    },
                    out retStr);
            response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
            NetClient.BeginTimestamp = DateTime.Now;
        }

        public static string rtid;

        public static string GeneratePostMapStringOfMappings()
        {
            var retStr = transaction.Mappings.Aggregate("", (current, mapKVP) => current + String.Format("{0},{1};", mapKVP.Key, mapKVP.Value));
            if (retStr.Length > 0)
            {
                retStr = retStr.Substring(0, retStr.Length - 1);
            }
            return retStr;
        }

        public static Dictionary<String, String> UploadBO(string boName, string content)
        {
            NetClient.PostData(GlobalContext.LocationDict["UploadBO"], new Dictionary<string, string>
                    {
                        { "token", transaction.AuthToken },
                        { "pid", transaction.ProcessPID },
                        { "name", boName },
                        { "content", System.Web.HttpUtility.UrlEncode(content, Encoding.UTF8) }
                    },
                out var retStr);
            var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
            return ReturnDataHelper.DecodeToStringStringDictionary(response);
        }

        public static void LoadProcessFromDirectory(String folderPath)
        {
            var dirInfo = new DirectoryInfo(folderPath);
            var fileInfos = dirInfo.GetFiles();
            transaction.ProcessLocalPath = folderPath;
            transaction.BOVector = new List<Dictionary<string, string>>();
            foreach (var fileInfo in fileInfos)
            {
                if (String.Compare(fileInfo.Extension, ".xml", StringComparison.CurrentCultureIgnoreCase) != 0) { continue; }
                var fs = File.Open(fileInfo.FullName, FileMode.Open);
                var sr = new StreamReader(fs);
                var content = sr.ReadToEnd();
                sr.Close();
                fs.Close();
                var boName = fileInfo.Name.Substring(0, fileInfo.Name.Length - 4);

                transaction.BOVector.Add(new Dictionary<string, string>
                {
                    { "bo_name", boName },
                    { "bo_content", content }
                });
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
