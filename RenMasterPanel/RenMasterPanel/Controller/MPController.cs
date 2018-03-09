using System;
using System.Collections.Generic;
using System.Data;
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
                return boList.Select(bo => (bo as JArray).ToObject<List<String>>())
                    .Select(boKVP => new Dictionary<string, string>
                    {
                        {"boid", boKVP[0]},
                        {"bo_name", boKVP[1]}
                    })
                    .ToList();
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
                        { "content", System.Web.HttpUtility.UrlEncode(content, Encoding.UTF8) }
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

        /// <summary>
        /// Get all resources in the COrgan by ren auth user binding gateway.
        /// </summary>
        /// <returns>a DataSet of retrieved data</returns>
        public static DataSet GetAllResourceInCOrgan()
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
                return ReturnDataHelper.DecodeToDataSet(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Get resources in COrgan, exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Get the data version of this Ren auth binding COrgan.
        /// </summary>
        public static String GetDataVersion()
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_GetDataVersion, new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "renid", MPController.CurrentTransaction.RenUsername }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                return ReturnDataHelper.DecodeString(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Submit process, exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Submit the process to NS and get a rtid.
        /// </summary>
        public static String SubmitProcess()
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_SubmitProcess, new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "pid", MPController.CurrentTransaction.ProcessPID },
                        { "from", "MasterPanel.NET" },
                        { "renid", MPController.CurrentTransaction.RenUsername },
                        { "bindingType", MPController.CurrentTransaction.IsolationType.ToString() },
                        { "launchType", MPController.CurrentTransaction.LaunchType.ToString() },
                        { "failureType", MPController.CurrentTransaction.FailureType.ToString() },
                        { "authType", MPController.CurrentTransaction.AuthType.ToString() },  // todo auth type
                        { "binding", "" }  // todo static Resources.xml
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                return ReturnDataHelper.DecodeString(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Submit process, exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Register mappings to NS.
        /// </summary>
        public static String RegisterMappings()
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_UploadMapping, new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "rtid", GlobalContext.CurrentRTID },
                        { "organgid", GlobalContext.ResourcesCOrganGid },
                        { "dataversion", GlobalContext.ResourcesDataVersion },
                        { "map", MPController.GeneratePostMapStringOfMappings() }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                return ReturnDataHelper.DecodeString(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Register mappings, exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }
        
        /// <summary>
        /// Load resources participant for RS.
        /// </summary>
        public static String LoadParticipant()
        {
            try
            {
                NetClient.PostData(GlobalContext.URL_LoadParticipant, new Dictionary<string, string>
                    {
                        { "token", MPController.CurrentTransaction.AuthToken },
                        { "rtid", GlobalContext.CurrentRTID },
                        { "renid", MPController.CurrentTransaction.RenUsername }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                return ReturnDataHelper.DecodeString(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Load participant for RS, exception occurred" + ex, "MPController", LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Generate a string to post to NS of current mapping description.
        /// </summary>
        /// <returns>descriptor string</returns>
        public static string GeneratePostMapStringOfMappings()
        {
            var retStr = MPController.CurrentTransaction.Mappings.Aggregate("", (current, mapKVP) => current + String.Format("{0},{1};", mapKVP.Key, mapKVP.Value));
            if (retStr.Length > 0)
            {
                retStr = retStr.Substring(0, retStr.Length - 1);
            }
            return retStr;
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

        /// <summary>
        /// Upload business role mappings to Name Service.
        /// </summary>
        public static void UploadBusinessRoleMapping()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// Get resource enum by its global id string prefix.
        /// </summary>
        /// <param name="gid">Global id string</param>
        /// <returns>Resource enum</returns>
        public static ResourceType GetResourceTypeByGid(string gid)
        {
            var gidItem = gid.Split('_');
            switch (gidItem[0])
            {
                case "Human":
                    return ResourceType.Human;
                case "Agent":
                    return ResourceType.Agent;
                case "Dept":
                    return ResourceType.Group;
                case "Pos":
                    return ResourceType.Position;
                case "Capa":
                    return ResourceType.Capability;
                default:
                    throw new ArgumentOutOfRangeException();
            }
        }

        /// <summary>
        /// Parse COrgan group type enum value to enum name.
        /// </summary>
        /// <param name="enumValObj">enum value</param>
        /// <returns>enum name string</returns>
        public static string ParseGroupType(object enumValObj)
        {
            switch (enumValObj)
            {
                case 0:
                    return "Department";
                case 1:
                    return "Team";
                case 3:
                    return "Cluster";
                case 4:
                    return "Division";
                case 5:
                    return "Branch";
                case 6:
                    return "Unit";
                default:
                    return "Group";
            }
        }

        /// <summary>
        /// Parse COrgan agent type enum value to enum name.
        /// </summary>
        /// <param name="enumValObj">enum value</param>
        /// <returns>enum name string</returns>
        public static string ParseAgentType(object enumValObj)
        {
            switch (enumValObj)
            {
                case 0:
                    return "Reentrant";
                default:
                    return "NotReentrant";
            }
        }

        /// <summary>
        /// Find a DataRow in a DataTable by its Global id.
        /// </summary>
        /// <param name="dt">DataTable instance</param>
        /// <param name="gid">Global id of fetching row</param>
        /// <returns>DataRow, null if not exist</returns>
        public static DataRow FindResourceDataRow(DataTable dt, string gid)
        {
            return dt.Rows.Cast<DataRow>().FirstOrDefault(row => row["GlobalId"].ToString() == gid);
        }
    }
}
