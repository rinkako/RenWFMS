using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
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
                        MPController.CurrentTransaction.BOVector.Add(new Dictionary<string, string>
                        {
                            { "bo_name", boName },
                            { "bo_content", content }
                        });
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
                    new Dictionary<string, string>
                    {
                        { "username", username },
                        { "password", password }
                    },
                    out string retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var retToken = ReturnDataHelper.DecodeString(response);
                var successFlag = !retToken.StartsWith("#");
                return new KeyValuePair<bool, string>(successFlag, successFlag ? retToken : "");
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("LoginForm exception occurred" + ex, "MPController", LogLevel.Error);
                return new KeyValuePair<bool, string>(false, "");
            }
        }

        /// <summary>
        /// Get processes of a ren user.
        /// </summary>
        /// <returns>A list of dictionary of processes</returns>
        public static List<Dictionary<String, String>> GetProcess()
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_GetProcessByRenId,
                    new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "renid", MPController.CurrentTransaction.RenUsername }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var processList = ReturnDataHelper.DecodeList(response);
                return processList.Select(proc => (Dictionary<String, String>) (proc as JObject).ToObject(typeof(Dictionary<String, String>))).ToList();
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("GetProcess exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Get BO of a process.
        /// </summary>
        /// <param name="pid">process pid</param>
        /// <returns>A List of Dictonary of BO items</returns>
        public static List<Dictionary<String, String>> GetProcessBO(string pid)
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_GetProcessBOByPid,
                    new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "pid", pid }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var boList = ReturnDataHelper.DecodeList(response);
                var retList = new List<Dictionary<String, String>>();
                foreach (var bo in boList)
                {
                    var boKVP = (List<String>) (bo as JArray).ToObject<List<String>>();
                    var dict = new Dictionary<string, string>();
                    dict.Add("boid", boKVP[0]);
                    dict.Add("bo_name", boKVP[1]);
                    retList.Add(dict);
                }
                return retList;
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("GetProcessBO exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Create a new process.
        /// </summary>
        /// <returns>Process pid</returns>
        public static String CreateProcess(string processName, string mainBOName)
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_CreateProcess,
                    new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "renid", MPController.CurrentTransaction.RenUsername },
                        { "name", processName },
                        { "mainbo", mainBOName }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                return ReturnDataHelper.DecodeString(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("CreateProcess exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Upload a BO.
        /// </summary>
        /// <param name="boName"></param>
        /// <param name="content"></param>
        /// <returns>Dictionary of BO upload response</returns>
        public static Dictionary<String, String> UploadBO(string boName, string content)
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_UploadBO, new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "pid", MPController.CurrentTransaction.ProcessPID },
                        { "name", boName },
                        { "content", content }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                return ReturnDataHelper.DecodeToStringStringDictionary(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("UploadBO exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        public static string GetAllResourceInCOrgan()
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_GetAllResources, new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "renid", MPController.CurrentTransaction.RenUsername }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var dict = ReturnDataHelper.DecodeToDataSet(response);
                return "";
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Get resources in COrgan, exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Get the name list of BOs in current transaction binding process.
        /// </summary>
        /// <returns>List of BO name strings</returns>
        public static List<String> GetBOList()
        {
            return MPController.CurrentTransaction.BOVector.Select(bo => bo["bo_name"]).ToList();
        }

        /// <summary>
        /// Get BO content by its name.
        /// </summary>
        /// <param name="name">BO name</param>
        /// <returns>BO content string</returns>
        public static String GetBOContent(string name)
        {
            foreach (var bo in MPController.CurrentTransaction.BOVector)
            {
                if (bo["bo_name"] == name)
                {
                    return bo["bo_content"];
                }
            }
            return String.Empty;
        }
    }
}
