using System;
using System.Collections.Generic;
using System.Linq;
using ArticleCrowdSourcingDemo.Entity;
using ArticleCrowdSourcingDemo.Utility;
using Newtonsoft.Json;

namespace ArticleCrowdSourcingDemo
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
                    out var retStr);
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
                        { "pid", GlobalDataPackage.ProcessPID },
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
                        { "map", InteractionManager.GeneratePostMapStringOfMappings() }
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

        public static List<Tuple<List<string>, Dictionary<String, String>>> GetMyWorkitem(string workerId, string rtid)
        {
            try
            {
                var argDict = new Dictionary<string, string>
                {
                    { "signature", GlobalDataPackage.Signature },
                    { "rtid", rtid }
                };
                NetClient.PostData(GlobalDataPackage.URL_GetAllWorkitem, argDict, out var retStr);
                var response = JsonConvert.DeserializeObject<StdResponseEntity>(retStr);
                var workitemList = ReturnDataHelper.DecodeList(response);
                var retList = new List<Tuple<List<string>, Dictionary<String, String>>>();
                retList.AddRange(
                    from workitem 
                    in workitemList
                    select ReturnDataHelper.DecodeDictionaryByString(workitem.ToString()) 
                    into wd
                    let workerIdListDesc = wd["WorkerIdList"]
                    let workerIdList = JsonConvert.DeserializeObject<List<String>>(workerIdListDesc)
                    select new Tuple<List<string>, Dictionary<String, String>>(workerIdList, wd));
                return retList.FindAll(t => t.Item1.Contains(workerId));
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

        public static void PerformMappingBRole()
        {
            var CurrentMap = new List<KeyValuePair<string, string>>
            {
                new KeyValuePair<string, string>("judger", "Capa_e986668f-24da-11e8-972c-2c4d54f01cf2"),
                new KeyValuePair<string, string>("decomposer", "Capa_d72c864f-24da-11e8-b535-2c4d54f01cf2"),
                new KeyValuePair<string, string>("decomposeVoter", "Capa_db79994f-24da-11e8-abc0-2c4d54f01cf2"),
                new KeyValuePair<string, string>("solver", "Capa_ceecb54f-24da-11e8-84a6-2c4d54f01cf2"),
                new KeyValuePair<string, string>("solveVoter", "Capa_d3e7e29e-24da-11e8-8487-2c4d54f01cf2"),
                new KeyValuePair<string, string>("merger", "Capa_cb8b61e1-24da-11e8-a3d8-2c4d54f01cf2"),
                new KeyValuePair<string, string>("decomposeQuerier", "Capa_1f54376e-25a3-11e8-8267-2c4d54f01cf2"),
                new KeyValuePair<string, string>("solutionQuerier", "Capa_1f54376e-25a3-11e8-8267-2c4d54f01cf2")
            };
            GlobalDataPackage.Mappings = new List<KeyValuePair<string, string>>();
            foreach (var mapItem in CurrentMap)
            {
                GlobalDataPackage.Mappings.Add(mapItem);
            }
            LogUtils.LogLine("Generate Mappings OK", LogLevel.Important);
        }

        public static string GeneratePostMapStringOfMappings()
        {
            InteractionManager.PerformMappingBRole();
            var retStr = GlobalDataPackage.Mappings.Aggregate(String.Empty, (current, mapKVP) => current + String.Format("{0},{1};", mapKVP.Key, mapKVP.Value));
            if (retStr.Length > 0)
            {
                retStr = retStr.Substring(0, retStr.Length - 1);
            }
            return retStr;
        }
    }
}
