using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using ArticleCrowdSourcingDemo.Entity;
using ArticleCrowdSourcingDemo.Utility;
using Newtonsoft.Json;

namespace ArticleCrowdSourcingDemo
{
    internal static class CSCore
    {
        public static bool DoLogin(string username, string password)
        {
            var resDt = DBUtil.CommitToDB("select * from ren_user where ren_user.username = \"" + username + "\"").Tables[0];
            if (resDt.Rows.Count <= 0)
            {
                return false;
            }
            var checkpw = (string) resDt.Rows[0]["password"];
            if (!String.Equals(checkpw, password))
            {
                return false;
            }
            GlobalDataPackage.CurrentUserViewRole = (UserViewRole) resDt.Rows[0]["level"];
            GlobalDataPackage.CurrentUsername = username;
            GlobalDataPackage.CurrentUserWorkerId = resDt.Rows[0]["workerId"].ToString();
            return true;
        }

        public static DataTable RefreshRequest(string requester)
        {
            return DBUtil.CommitToDB("select * from ren_request where ren_request.requester = \"" + requester + "\"").Tables[0];
        }
        
        public static void NewRequest(string taskName, string taskDesc, int jc, int sc, int svc, int dc, int dvc)
        {
            var ArgDict = new Dictionary<String, Object>
            {
                {"taskName", taskName},
                {"taskDescription", taskDesc},
                {"judgeCount", jc},
                {"decomposeCount", dc},
                {"decomposeVoteCount", dvc},
                {"solveCount", sc},
                {"solveVoteCount", svc}
            };
            InteractionManager.DoCallback("submit", "Request", ArgDict);
            DBUtil.CommitToDB($"insert into ren_request(name, requester, description, status, judgeCount, decomposeCount, decomposeVoteCount, solveCount, solveVoteCount, solution, rtid) values (\"{taskName}\", \"{GlobalDataPackage.CurrentUsername}\", \"{taskDesc}\", \"{SolvePhase.Solving.ToString()}\", {jc}, {dc}, {dvc}, {sc}, {svc}, \"\", \"{GlobalDataPackage.RTID}\")");
        }

        public static DataRow GetRequestByRTID(String rtid)
        {
            return DBUtil.CommitToDB("select * from ren_request where ren_request.rtid = \"" + rtid + "\"").Tables[0].Rows[0];
        }

        public static List<string> GetAllActiveRTID()
        {
            var ret = DBUtil.CommitToDB($"select ren_request.rtid from ren_request where ren_request.status = \"{SolvePhase.Solving.ToString()}\"").Tables[0];
            return (from object row in ret.Rows select (row as DataRow)["rtid"] as string).ToList();
        }

        public static void Decompose(string rtid, string workitemId, string nodeId, List<string> decomposeList)
        {
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, workitemId);
            var decomposed = JsonConvert.SerializeObject(decomposeList);
            DBUtil.CommitToDB("insert into ren_decompose(rtid, nodeId, workerId, decompose, voted) values " +
                              $"(\"{rtid}\", \"{nodeId}\", \"{GlobalDataPackage.CurrentUserWorkerId}\", \"{decomposed.Replace("\"", "\\\"")}\", 0)");
        }

        public static List<Tuple<string, List<string>>> GetDecomposeList(string rtid, string nodeId)
        {
            var dSet = DBUtil.CommitToDB($"select * from ren_decompose where rtid = \"{rtid}\" and nodeId = \"{nodeId}\"").Tables[0];
            return (from object row
                    in dSet.Rows
                    select row as DataRow 
                    into rowItem
                    select new Tuple<string, List<string>>(rowItem["workerId"].ToString(), ReturnDataHelper.DecodeListByString(rowItem["decompose"].ToString()))).ToList();
        }

        public static void VoteForDecompose(string rtid, string nodeId, string workerId, string workitemId)
        {
            DBUtil.CommitToDB($"update ren_decompose set voted = voted + 1 where rtid = \"{rtid}\" and nodeId = \"{nodeId}\" and workerId = \"{workerId}\"");
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, workitemId);
        }

        public static void Solve(string rtid, string workitemId, string nodeId, string solution)
        {
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, workitemId);
            DBUtil.CommitToDB("insert into ren_midsolution(rtid, nodeId, workerId, solution, voted) values " +
                              $"(\"{rtid}\", \"{nodeId}\", \"{GlobalDataPackage.CurrentUserWorkerId}\", \"{solution.Replace("\"", "\\\"")}\", 0)");
        }

        public static List<Tuple<string, string>> GetSolveList(string rtid, string nodeId)
        {
            var dSet = DBUtil.CommitToDB($"select * from ren_midsolution where rtid = \"{rtid}\" and nodeId = \"{nodeId}\"").Tables[0];
            return (from object row
                    in dSet.Rows
                    select row as DataRow
                    into rowItem
                    select new Tuple<string, string>(rowItem["workerId"].ToString(), rowItem["solution"].ToString())).ToList();
        }

        public static void VoteForSolution(string rtid, string nodeId, string workerId, string workitemId)
        {
            DBUtil.CommitToDB($"update ren_midsolution set voted = voted + 1 where rtid = \"{rtid}\" and nodeId = \"{nodeId}\" and workerId = \"{workerId}\"");
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, workitemId);
        }

        public static void DoQueryBestDecompose(string rtid, string nodeId, string workitemId)
        {
            var dSet = DBUtil.CommitToDB($"select * from ren_decompose where rtid = \"{rtid}\" and nodeId = \"{nodeId}\"").Tables[0];
            var list = (from object row
                    in dSet.Rows
                    select row as DataRow
                    into rowItem
                    select new Tuple<string, int>(rowItem["decompose"].ToString(), Convert.ToInt32(rowItem["voted"].ToString()))).ToList();
            var vote = -1;
            var selected = "";
            foreach (var tuple in list)
            {
                if (tuple.Item2 > vote)
                {
                    vote = tuple.Item2;
                    selected = tuple.Item1;
                }
            }
            var rList = ReturnDataHelper.DecodeListByString(selected);
            var retDict = new Dictionary<string, object>();
            for (var i = 0; i < rList.Count; i++)
            {
                retDict[i.ToString()] = rList[i];
            }
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, workitemId, retDict);  // todo worker id of agent
        }

        public static void DoQueryBestSolution(string rtid, string nodeId, string supervisor, string ordinal, string workitemId)
        {
            var dSet = DBUtil.CommitToDB($"select * from ren_midsolution where rtid = \"{rtid}\" and nodeId = \"{nodeId}\"").Tables[0];
            var list = (from object row
                        in dSet.Rows
                        select row as DataRow
                        into rowItem
                        select new Tuple<string, int>(rowItem["solution"].ToString(), Convert.ToInt32(rowItem["voted"].ToString()))).ToList();
            var vote = -1;
            var selected = "";
            foreach (var tuple in list)
            {
                if (tuple.Item2 > vote)
                {
                    vote = tuple.Item2;
                    selected = tuple.Item1;
                }
            }
            DBUtil.CommitToDB($"insert into ren_solution(supervisor, ordinal, solution) values (\"{supervisor}\", \"{ordinal}\", \"{selected}\")");
            if (supervisor == String.Empty)
            {
                DBUtil.CommitToDB($"update ren_request set solution = \"{selected}\", status = \"{SolvePhase.Solved.ToString()}\" where rtid = \"{rtid}\"");
            }
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, workitemId);  // todo worker id of agent
        }

        public static void DoMerge(string rtid, string currentNodeId, string currentSupervisor, string currentOrdinal, string workitemId)
        {
            var solutions = DBUtil.CommitToDB($"select * from ren_solution where supervisor = \"{currentNodeId}\"").Tables[0];
            var list = (from object row
                        in solutions.Rows
                        select row as DataRow
                        into rowItem
                        select new Tuple<int, string>(Convert.ToInt32(rowItem["ordinal"].ToString()), rowItem["solution"].ToString())).ToList();
            list.Sort((tuple, tuple1) => tuple.Item1.CompareTo(tuple1.Item1));
            var merged = list.Aggregate(string.Empty, (s, tuple) => s + tuple.Item2);
            DBUtil.CommitToDB("insert into ren_solution(supervisor, ordinal, solution) values " +
                              $"(\"{currentSupervisor}\", \"{currentOrdinal}\", \"{merged}\")");
            if (currentSupervisor == String.Empty)
            {
                DBUtil.CommitToDB($"update ren_request set solution = \"{merged}\", status = \"{SolvePhase.Solved.ToString()}\" where rtid = \"{rtid}\"");
            }
            InteractionManager.StartAndComplete(GlobalDataPackage.CurrentUserWorkerId, workitemId);
            LogUtils.LogLine($"Merge Finish, at node:{currentNodeId}, supervisor:{currentSupervisor}, ordinal:{currentOrdinal}", LogLevel.Important);
        }
    }
}
