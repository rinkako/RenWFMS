using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using Newtonsoft.Json;
using RestaurantProcessTester.Entity;
using RestaurantProcessTester.Utility;

namespace RestaurantProcessTester
{
    internal static class Ren
    {
        public static TransactionPackage transaction = new TransactionPackage();

        public static void ReadLocationFromSteady()
        {
            GlobalContext.LocationDict.Clear();
            var fs = new FileStream(IOUtils.ParseURItoURL(GlobalContext.LocationConfigFilePath), FileMode.Open);
            var sr = new StreamReader(fs);
            string line;
            while ((line = sr.ReadLine()) != null)
            {
                var lineItem = line.Split('\t');
                if (lineItem.Length == 2)
                {
                    lineItem[1] = lineItem[1].Replace("\r", "").Replace("\n", "").Trim();
                    GlobalContext.LocationDict.Add(lineItem[0].Trim(), lineItem[1]);
                }
            }
            sr.Close();
            fs.Close();
            LogUtils.LogLine("Read Location: " + GlobalContext.LocationDict.Count, LogLevel.Normal);
            foreach (var kvp in GlobalContext.LocationDict)
            {
                LogUtils.LogLine($"{kvp.Key} => {kvp.Value}", LogLevel.Simple);
            }
        }

        public static void PerformLogin()
        {
            try
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
                if (successFlag)
                {
                    LogUtils.LogLine("Login OK, Token: " + retToken, LogLevel.Important);
                    transaction.AuthToken = retToken;
                }
                else
                {
                    LogUtils.LogLine("Login Failed. ", LogLevel.Error);
                }
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("PerformLogin exception occurred" + ex, LogLevel.Error);
            }
        }

        public static void PerformUploadProcess()
        {
            try
            {
                Ren.LoadProcessFromDirectory(IOUtils.ParseURItoURL("processDir"));
                var processName = "Process_" + DateTime.Now;
                LogUtils.LogLine("Process Name is: " + processName + "\nMainBO: GuestOrder", LogLevel.Normal);
                NetClient.PostData(GlobalContext.LocationDict["CreateProcess"],
                    new Dictionary<string, string>
                    {
                        {"token", Ren.transaction.AuthToken},
                        {"renid", Ren.transaction.RenUsername},
                        {"name", processName },
                        {"mainbo", "GuestOrder"}
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                Ren.transaction.ProcessPID = ReturnDataHelper.DecodeString(response);
                Ren.transaction.ProcessName = processName;
                if (String.IsNullOrEmpty(Ren.transaction.ProcessPID))
                {
                    LogUtils.LogLine("Failed to create process. " + response, LogLevel.Error);
                }
                else
                {
                    LogUtils.LogLine("Create Process OK, PID: " + Ren.transaction.ProcessPID, LogLevel.Important);
                }
                // upload BO Content
                var BRList = new List<string>();
                foreach (var boDict in Ren.transaction.BOVector)
                {
                    var retKVP = Ren.UploadBO(boDict["bo_name"], boDict["bo_content"]).First();
                    boDict["boid"] = retKVP.Key;
                    response = JsonConvert.DeserializeObject<StdResponseEntity>(retKVP.Value);
                    var responseDict = ReturnDataHelper.DecodeList(response);
                    BRList.AddRange(responseDict.Select(br => br.ToString()));
                }
                Ren.transaction.BusinessRoleList = new HashSet<string>();
                foreach (var br in BRList)
                {
                    Ren.transaction.BusinessRoleList.Add(br);
                }
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("PerformUploadProcess exception occurred" + ex, LogLevel.Error);
            }
        }

        public static void PerformMappingBRole()
        {
            var CurrentMap = new List<KeyValuePair<string, string>>
            {
                new KeyValuePair<string, string>("waiter", "Dept_a3246d70-124f-11e8-b430-5404a6a99e5d"),
                new KeyValuePair<string, string>("paymentHandler", "Human_8c9d8ab0-1262-11e8-b057-5404a6a99e5d"),
                new KeyValuePair<string, string>("paymentHandler", "Human_abf49cf0-1262-11e8-845b-5404a6a99e5d"),
                new KeyValuePair<string, string>("archiveHandler", "Human_88a4f9c0-1235-11e8-b3fc-5404a6a99e5d"),
                new KeyValuePair<string, string>("paymentCalculator", "Agent_30e35ff0-1263-11e8-9c46-5404a6a99e5d"),
                new KeyValuePair<string, string>("cook", "Capa_9d027230-1235-11e8-b79f-5404a6a99e5d")
            };
            Ren.transaction.Mappings = new List<KeyValuePair<string, string>>();
            foreach (var mapItem in CurrentMap)
            {
                Ren.transaction.Mappings.Add(mapItem);
            }
        }

        private static string rtid = "";

        public static void PerformSubmitAndStart()
        {
            Ren.transaction.LaunchType = 0;
            Ren.transaction.IsolationType = 0;
            Ren.transaction.FailureType = 0;
            Ren.transaction.AuthType = 0;
            try
            {
                NetClient.PostData(GlobalContext.LocationDict["SubmitProcess"], new Dictionary<string, string>
                    {
                        { "token", Ren.transaction.AuthToken },
                        { "pid", Ren.transaction.ProcessPID },
                        { "from", "MasterPanel.NET" },
                        { "renid", Ren.transaction.RenUsername },
                        { "bindingType", Ren.transaction.IsolationType.ToString() },
                        { "launchType", Ren.transaction.LaunchType.ToString() },
                        { "failureType", Ren.transaction.FailureType.ToString() },
                        { "authType", Ren.transaction.AuthType.ToString() },
                        { "binding", "" }  // todo static Resources.xml
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                Ren.rtid = ReturnDataHelper.DecodeString(response);
                LogUtils.LogLine("Submit Process OK, RTID: " + Ren.rtid, LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Submit process, exception occurred" + ex, LogLevel.Error);
                return;
            }

            try
            {
                NetClient.PostData(GlobalContext.LocationDict["UploadMapping"], new Dictionary<string, string>
                    {
                        { "token", Ren.transaction.AuthToken },
                        { "rtid", Ren.rtid },
                        { "organgid", "COrg_571d200f-0f35-11e8-9072-5404a6a99e5d" },
                        { "dataversion", "version1" },
                        { "map", Ren.GeneratePostMapStringOfMappings() }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                ReturnDataHelper.DecodeString(response);
                LogUtils.LogLine("Mapping BRole OK, RTID: " + Ren.rtid, LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Register mappings, exception occurred" + ex, LogLevel.Error);
            }

            try
            {
                NetClient.PostData(GlobalContext.LocationDict["UploadMapping"], new Dictionary<string, string>
                    {
                        { "token", Ren.transaction.AuthToken },
                        { "rtid", Ren.rtid },
                        { "organgid", "COrg_571d200f-0f35-11e8-9072-5404a6a99e5d" },
                        { "dataversion", "version1" },
                        { "map", Ren.GeneratePostMapStringOfMappings() }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                ReturnDataHelper.DecodeString(response);
                LogUtils.LogLine("Mapping BRole OK, RTID: " + Ren.rtid, LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Register mappings, exception occurred" + ex, LogLevel.Error);
            }
        }

        public static void PerformCallbackSubmit()
        {
            
        }

        public static void PerformCallbackProduced()
        {
            
        }

        public static void PerformCallbackTestCompleted()
        {
            
        }

        public static void PerformCallbackDelivered()
        {
            
        }

        public static void PerformCallbackArchivedKO()
        {
            
        }

        public static void PerformCallbackRequestCheck()
        {
            
        }

        public static void PerformCallbackCalculated()
        {
            
        }

        public static void PerformCallbackPaid()
        {
            
        }

        public static void PerformCallbackArchivedGC()
        {
            
        }

        public static void PerformCheckFinish()
        {
            
        }

        #region SupportFuncs
        
        public static void LoadProcessFromDirectory(String folderPath)
        {
            var dirInfo = new DirectoryInfo(folderPath);
            var fileInfos = dirInfo.GetFiles();
            Ren.transaction.ProcessLocalPath = folderPath;
            foreach (var fileInfo in fileInfos)
            {
                if (String.Compare(fileInfo.Extension, ".xml", StringComparison.CurrentCultureIgnoreCase) != 0) { continue; }
                LogUtils.LogLine("Read XML from Local: " + fileInfo.Name, LogLevel.Simple);
                var fs = File.Open(fileInfo.FullName, FileMode.Open);
                var sr = new StreamReader(fs);
                var content = sr.ReadToEnd();
                sr.Close();
                fs.Close();
                var boName = fileInfo.Name.Substring(0, fileInfo.Name.Length - 4);
                Ren.transaction.BOVector.Add(new Dictionary<string, string>
                {
                    { "bo_name", boName },
                    { "bo_content", content }
                });
            }
        }
        
        public static Dictionary<String, String> UploadBO(string boName, string content)
        {
            try
            {
                NetClient.PostData(GlobalContext.LocationDict["UploadBO"], new Dictionary<string, string>
                    {
                        { "token", Ren.transaction.AuthToken },
                        { "pid", Ren.transaction.ProcessPID },
                        { "name", boName },
                        { "content", content }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                return ReturnDataHelper.DecodeToStringStringDictionary(response);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("UploadBO exception occurred" + ex, LogLevel.Error);
                return null;
            }
        }

        /// <summary>
        /// Get the name list of BOs in current transaction binding process.
        /// </summary>
        /// <returns>List of BO name strings</returns>
        public static List<String> GetBOList()
        {
            return Ren.transaction.BOVector.Select(bo => bo["bo_name"]).ToList();
        }

        public static string GeneratePostMapStringOfMappings()
        {
            var retStr = Ren.transaction.Mappings.Aggregate("", (current, mapKVP) => current + String.Format("{0},{1};", mapKVP.Key, mapKVP.Value));
            if (retStr.Length > 0)
            {
                retStr = retStr.Substring(0, retStr.Length - 1);
            }
            return retStr;
        }

        #endregion
    }
}
