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
            var outputStr = GlobalContext.LocationDict.Aggregate("", (current, kvp) => current + $"{kvp.Key} => {kvp.Value}\n");
            LogUtils.LogLine(outputStr, LogLevel.Simple);
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
                var processName = "RestaurantTest_" + DateTime.Now.ToString().Replace(' ', '_');
                LogUtils.LogLine("Process Name is: " + processName + "\nMainBO: GuestOrder", LogLevel.Normal);
                NetClient.PostData(GlobalContext.LocationDict["CreateProcess"],
                    new Dictionary<string, string>
                    {
                        {"token", Ren.transaction.AuthToken },
                        {"renid", "admin@admin" },
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
            LogUtils.LogLine("Generate Mappings OK, update at next step", LogLevel.Important);
        }

        private static string rtid = "";

        public static void PerformSubmitAndStart()
        {
            Ren.transaction.LaunchType = 0;
            Ren.transaction.IsolationType = 0;
            Ren.transaction.FailureType = 0;
            Ren.transaction.AuthType = 0;
            Ren.transaction.RenUsername = "admin@admin";
            try
            {
                NetClient.PostData(GlobalContext.LocationDict["SubmitProcess"], new Dictionary<string, string>
                    {
                        { "token", Ren.transaction.AuthToken },
                        { "pid", Ren.transaction.ProcessPID },
                        { "from", "ProcessTester.NET" },
                        { "renid", Ren.transaction.RenUsername },
                        { "bindingType", Ren.transaction.IsolationType.ToString() },
                        { "launchType", Ren.transaction.LaunchType.ToString() },
                        { "failureType", Ren.transaction.FailureType.ToString() },
                        { "authType", Ren.transaction.AuthType.ToString() },
                        { "binding", "" }  // todo static Resources.xml
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var submitRes = ReturnDataHelper.DecodeString(response);
                var submitResItem = submitRes.Split(',');
                Ren.rtid = submitResItem[0];
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
                LogUtils.LogLine("Mapping BRole Send OK, Response: " + ReturnDataHelper.DecodeString(response), LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Register mappings, exception occurred" + ex, LogLevel.Error);
            }

            try
            {
                NetClient.PostData(GlobalContext.LocationDict["LoadParticipant"], new Dictionary<string, string>
                    {
                        { "token", Ren.transaction.AuthToken },
                        { "rtid", Ren.rtid },
                        { "renid", Ren.transaction.RenUsername }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                LogUtils.LogLine("LoadParticipant Send OK, Response: " + ReturnDataHelper.DecodeString(response), LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Register mappings, exception occurred" + ex, LogLevel.Error);
            }

            try
            {
                NetClient.PostData(GlobalContext.LocationDict["StartProcess"], new Dictionary<string, string>
                    {
                        { "token", Ren.transaction.AuthToken },
                        { "rtid", Ren.rtid }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                LogUtils.LogLine("StartProcess send OK, Response: " + ReturnDataHelper.DecodeString(response), LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Register mappings, exception occurred" + ex, LogLevel.Error);
            }
        }

        public static void PerformCallbackSubmit()
        {
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList[0];
            var response = Ren.PostWorkitemRequest("Submit accept", GlobalContext.LocationDict["WorkitemAccept"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Submit start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Submit complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, null);
        }

        public static void PerformCallbackProduced()
        {
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList[0];
            var response = Ren.PostWorkitemRequest("Produce start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Produce complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, null);
        }

        public static void PerformCallbackTestCompleted()
        {
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList[0];
            var payload = "{\"passed\":1}";
            var response = Ren.PostWorkitemRequest("QTest start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("QTest complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, payload);
        }

        public static void PerformCallbackDelivered()
        {
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList[0];
            var payload = "{\"passed\":1}";
            var response = Ren.PostWorkitemRequest("Delivere start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Delivere complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, payload);
        }

        public static void PerformCallbackArchivedKO()
        {
            // 这里会有2个工作项：updateDeliTimeTask和Archived
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList.FirstOrDefault(wpp => String.Compare(wpp.Item3, "archiveTask", StringComparison.CurrentCultureIgnoreCase) == 0);
            var response = Ren.PostWorkitemRequest("Archived(KO) start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Archived(KO) complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, null);
        }

        public static void PerformCallbackUpdateDeliTimeKO()
        {
            // 这里会有2个工作项：updateDeliTimeTask和Archived
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList.FirstOrDefault(wpp => String.Compare(wpp.Item3, "updateDeliTimeTask", StringComparison.CurrentCultureIgnoreCase) == 0);
            var response = Ren.PostWorkitemRequest("UpdateDeliTime start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("UpdateDeliTime complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, null);
        }

        public static void PerformCallbackRequestCheck()
        {
            // 这个callback是用户发起的，与工作项无关，和其他不一样
            try
            {
                NetClient.PostData(GlobalContext.LocationDict["Callback"], new Dictionary<string, string>
                    {
                        { "signature", Ren.transaction.Signature },
                        { "rtid", Ren.rtid },
                        { "id", "GuestOrder" },
                        { "on", "Complete" },
                        { "event", "requestCheck" }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                LogUtils.LogLine("Request Check callback send OK: " + ReturnDataHelper.DecodeString(response), LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Request Check callback send, exception occurred" + ex, LogLevel.Error);
            }
        }

        public static void PerformCallbackCalculated()
        {
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList[0];
            var response = Ren.PostWorkitemRequest("Calculate start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Calculate complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, null);
        }

        public static void PerformCallbackPaid()
        {
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList[0];
            var response = Ren.PostWorkitemRequest("Pay start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Pay complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, null);
        }

        public static void PerformCallbackArchivedGC()
        {
            var wpList = Ren.GetWorkitemNameIdOneHandlerInVector();
            var wp = wpList.FirstOrDefault(wpp => String.Compare(wpp.Item3, "archiveTask", StringComparison.CurrentCultureIgnoreCase) == 0);
            var response = Ren.PostWorkitemRequest("Archived(GC) start", GlobalContext.LocationDict["WorkitemStart"], wp.Item1, wp.Item2, null);
            response = Ren.PostWorkitemRequest("Archived(GC) complete", GlobalContext.LocationDict["WorkitemComplete"], wp.Item1, wp.Item2, null);
        }

        public static void PerformCheckFinish()
        {
            try
            {
                NetClient.PostData(GlobalContext.LocationDict["CheckFinish"], new Dictionary<string, string>
                    {
                        { "signature", Ren.transaction.Signature },
                        { "rtid", Ren.rtid }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var retDict = ReturnDataHelper.DecodeToStringStringDictionary(response);
                LogUtils.LogLine("CheckFinish send OK: " + ReturnDataHelper.DecodeString(response), LogLevel.Important);
                if (retDict["IsFinished"] == "true")
                {
                    LogUtils.LogLine(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IsFinish flag is TRUE! Test Passed!", LogLevel.Important);
                }
                else
                {
                    LogUtils.LogLine(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IsFinish flag is FALSE!", LogLevel.Error);
                }
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("CheckFinish send, exception occurred" + ex, LogLevel.Error);
            }
        }

        #region SupportFuncs
        
        public static void LoadProcessFromDirectory(String folderPath)
        {
            var dirInfo = new DirectoryInfo(folderPath);
            var fileInfos = dirInfo.GetFiles();
            Ren.transaction.ProcessLocalPath = folderPath;
            Ren.transaction.BOVector = new List<Dictionary<string, string>>();
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
                        { "content", System.Web.HttpUtility.UrlEncode(content, Encoding.UTF8) }
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

        public static StdResponseEntity PostWorkitemRequest(string desc, string urlKey, string workerId, string workitemId, string payload = null)
        {
            try
            {
                var argDict = new Dictionary<string, string>
                {
                    { "signature", Ren.transaction.Signature },
                    { "workitemId", workitemId },
                    { "workerId", workerId },
                };
                if (payload != null)
                {
                    argDict["payload"] = payload;
                }
                NetClient.PostData(urlKey, argDict, out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                LogUtils.LogLine(desc + "(" + urlKey + ") get response: " + ReturnDataHelper.DecodeString(response), LogLevel.Important);
                return response;
            }
            catch (Exception ex)
            {
                LogUtils.LogLine(desc + ", exception occurred" + ex, LogLevel.Error);
                return null;
            }
        }
        
        public static List<Tuple<String, String, String>> GetWorkitemNameIdOneHandlerInVector()
        {
            var retList = new List<Tuple<String, String, String>>();
            try
            {
                var argDict = new Dictionary<string, string>
                {
                    { "signature", Ren.transaction.Signature },
                    { "rtid", Ren.rtid }
                };
                NetClient.PostData(GlobalContext.LocationDict["GetAllWorkitem"], argDict, out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var workitemList = ReturnDataHelper.DecodeList(response);
                foreach (var workitem in workitemList)
                {
                    var wd = ReturnDataHelper.DecodeDictionaryByString(workitem.ToString());
                    var workerIdListDesc = wd["WorkerIdList"].ToString();
                    var workerIdList = JsonConvert.DeserializeObject<List<String>>(workerIdListDesc);
                    retList.Add(new Tuple<string, string, string>(workerIdList.Last(), wd["Wid"].ToString(), wd["TaskName"].ToString()));
                }
                return retList;
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Get all workitems, exception occurred" + ex, LogLevel.Error);
                return null;
            }
        }

        #endregion
    }
}
