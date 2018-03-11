using System;
using System.Collections.Generic;
using System.Linq;
using ArticleCrowdSourcingDemo.Entity;
using ArticleCrowdSourcingDemo.Utility;
using Newtonsoft.Json;

namespace ArticleCrowdSourcingDemo.Interaction
{
    internal static class InteractionManager
    {
        public static void SubmitAndStart()
        {
            try
            {
                NetClient.PostData(GlobalDataPackage.URL_Login,
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
                    GlobalDataPackage.AuthToken = retToken;
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

            try
            {
                NetClient.PostData(GlobalDataPackage.URL_SubmitProcess, new Dictionary<string, string>
                    {
                        { "token",GlobalDataPackage.AuthToken },
                        { "pid", Ren.transaction.ProcessPID },
                        { "from", "CrowdSourcing.NET" },
                        { "renid", "admin@admin" },
                        { "bindingType", "0" },
                        { "launchType", "0" },
                        { "failureType", "0" },
                        { "authType", "0" },
                        { "binding", "" }
                    },
                    out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var submitRes = ReturnDataHelper.DecodeString(response);
                var submitResItem = submitRes.Split(',');
                GlobalDataPackage.RTID = submitResItem[0];
                LogUtils.LogLine("Submit Process OK, RTID: " + GlobalDataPackage.RTID, LogLevel.Important);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Submit process, exception occurred" + ex, LogLevel.Error);
                return;
            }

            try
            {
                NetClient.PostData(GlobalDataPackage.URL_UploadMapping, new Dictionary<string, string>
                    {
                        { "token", GlobalDataPackage.AuthToken },
                        { "rtid", GlobalDataPackage.RTID },
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
                NetClient.PostData(GlobalDataPackage.URL_LoadParticipant, new Dictionary<string, string>
                    {
                        { "token", GlobalDataPackage.AuthToken },
                        { "rtid", GlobalDataPackage.RTID },
                        { "renid", "admin@admin" }
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
                NetClient.PostData(GlobalDataPackage.URL_StartProcess, new Dictionary<string, string>
                    {
                        { "token", GlobalDataPackage.AuthToken },
                        { "rtid", GlobalDataPackage.RTID }
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

        public static string DoCallback(string evt, string notifiableId, Dictionary<String, Object> payload = null) 
        {
            try
            {
                var postArgs = new Dictionary<string, string>
                {
                    {"signature", GlobalDataPackage.Signature},
                    {"rtid", GlobalDataPackage.RTID},
                    {"id", notifiableId},
                    {"on", "Complete"},
                    {"event", evt}
                };
                if (payload != null)
                {
                    postArgs["payload"] = JsonConvert.SerializeObject(payload);
                }
                NetClient.PostData(GlobalDataPackage.URL_Callback, postArgs, out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var retResponse = ReturnDataHelper.DecodeString(response);
                LogUtils.LogLine("Request callback send OK: " + retResponse, LogLevel.Important);
                return retResponse;
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Request callback send, exception occurred" + ex, LogLevel.Error);
                return null;
            }
        }

        public static Tuple<string, string, string> GetMyWorkitem(string workerId)
        {
            var retList = new List<Tuple<String, String, String>>();
            try
            {
                var argDict = new Dictionary<string, string>
                {
                    { "signature", GlobalDataPackage.Signature },
                    { "rtid", GlobalDataPackage.RTID }
                };
                NetClient.PostData(GlobalDataPackage.URL_GetAllWorkitem, argDict, out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var workitemList = ReturnDataHelper.DecodeList(response);
                retList.AddRange(
                    from workitem 
                    in workitemList
                    select ReturnDataHelper.DecodeDictionaryByString(workitem.ToString()) 
                    into wd
                    let workerIdListDesc = wd["WorkerIdList"]
                    let workerIdList = JsonConvert.DeserializeObject<List<String>>(workerIdListDesc)
                    select new Tuple<string, string, string>(workerIdList.Last(), wd["Wid"], wd["TaskName"]));
                return retList.Find(t => t.Item1 == workerId);
            }
            catch (Exception ex)
            {
                LogUtils.LogLine("Get workitems, exception occurred" + ex, LogLevel.Error);
                return null;
            }
        }

        public static StdResponseEntity PostWorkitemRequest(string desc, string urlKey, string workerId, string workitemId, Dictionary<String, Object> payload = null)
        {
            try
            {
                var argDict = new Dictionary<string, string>
                {
                    { "signature", GlobalDataPackage.Signature },
                    { "workitemId", workitemId },
                    { "workerId", workerId },
                };
                if (payload != null)
                {
                    argDict["payload"] = JsonConvert.SerializeObject(payload);
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

        public static void StartAndComplete(string workerId, string workitemId, Dictionary<String, Object> completePayload = null, Dictionary<String, Object> startPayload = null)
        {
            InteractionManager.PostWorkitemRequest("Start", GlobalDataPackage.URL_WorkitemStart, workerId, workitemId, startPayload);
            InteractionManager.PostWorkitemRequest("Complete", GlobalDataPackage.URL_WorkitemComplete, workerId, workitemId, completePayload);
        }
    }
}
