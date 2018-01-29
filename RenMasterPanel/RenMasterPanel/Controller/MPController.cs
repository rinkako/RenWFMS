using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using RenMasterPanel.Util;

namespace RenMasterPanel.Controller
{
    /// <summary>
    /// Master Panel Controller for view model logic.
    /// </summary>
    internal static class MPController
    {
        /// <summary>
        /// Current transaction data package.
        /// </summary>
        public static TransactionPackage CurrentTransaction { get; set; } = new TransactionPackage();

        /// <summary>
        /// Load BO from a directory.
        /// </summary>
        /// <param name="folderPath">directory local path</param>
        public static void LoadProcessFromDirectory(String folderPath)
        {
            var dirInfo = new DirectoryInfo(folderPath);
            var fileInfos = dirInfo.GetFiles();
            MPController.CurrentTransaction.ProcessLocalPath = folderPath;
            foreach (var fileInfo in fileInfos)
            {
                if (String.Compare(fileInfo.Extension, ".xml", StringComparison.CurrentCultureIgnoreCase) == 0)
                {
                    if (String.Compare(fileInfo.Name, "Resources.xml", StringComparison.CurrentCultureIgnoreCase) == 0)
                    {
                        LogUtils.LogLine("Found resources file: " + fileInfo.Name, "MPController.LoadProcessFromDirectory", LogLevel.Important);
                        var fs = File.Open(fileInfo.FullName, FileMode.Open);
                        var sr = new StreamReader(fs);
                        MPController.CurrentTransaction.StaticResourcesXML = sr.ReadToEnd();
                        sr.Close();
                        fs.Close();
                    }
                    else
                    {
                        LogUtils.LogLine("Got xml file: " + fileInfo.Name, "MPController.LoadProcessFromDirectory", LogLevel.Normal);
                        var fs = File.Open(fileInfo.FullName, FileMode.Open);
                        var sr = new StreamReader(fs);
                        var content = sr.ReadToEnd();
                        sr.Close();
                        fs.Close();
                        var boName = fileInfo.Name.Substring(0, fileInfo.Name.Length - 4);
                        MPController.CurrentTransaction.BOVector.Add(new KeyValuePair<string, string>(boName, content));
                    }
                }
                else
                {
                    LogUtils.LogLine("Ignore file" + fileInfo.Name, "MPController", LogLevel.Warning);
                }
            }
        }

        /// <summary>
        /// Authorization connect.
        /// </summary>
        /// <param name="username">user unique name</param>
        /// <param name="password">pure password</param>
        /// <returns>KVP (successflag, token)</returns>
        public static KeyValuePair<bool, string> Login(string username, string password)
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_Auth_Connect,
                    new Dictionary<string, string> {{"username", username}, {"password", password}},
                    out string retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var returnElement = (JObject) response.returnElement;
                var retTokenFlag = returnElement.TryGetValue("data", out JToken retJToken);
                var retToken = retJToken.ToString();
                var successFlag = retTokenFlag && !retToken.StartsWith("#");
                return new KeyValuePair<bool, string>(successFlag, successFlag ? retToken : "");
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Login exception occurred" + ex, "MPController", LogLevel.Warning);
                return new KeyValuePair<bool, string>(false, "");
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public static void GetProcess()
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_GetProcessByRenId,
                    new Dictionary<string, string> { { "renid", MPController.CurrentTransaction.RenUsername } },
                    out string retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var returnElement = (JObject)response.returnElement;
                var retTokenFlag = returnElement.TryGetValue("data", out JToken retJToken);
                var retToken = retJToken.ToString();
                var successFlag = retTokenFlag && !retToken.StartsWith("#");
            }
            catch (Exception ex)
            {
                
            }
        }

        /// <summary>
        /// Get the name list of BOs in current transaction binding process.
        /// </summary>
        /// <returns>List of BO name strings</returns>
        public static List<String> GetBOList()
        {
            return MPController.CurrentTransaction.BOVector.Select(kvp => kvp.Key).ToList();
        }

        /// <summary>
        /// Get BO content by its name.
        /// </summary>
        /// <param name="name">BO name</param>
        /// <returns>BO content string</returns>
        public static String GetBOContent(string name)
        {
            return MPController.CurrentTransaction.BOVector.Find(t => t.Key == name).Value;
        }
    }
}
