using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using ArticleCrowdSourcingDemo.Utility;

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
            GlobalDataPackage.CurrentUserWid = resDt.Rows[0]["workerId"].ToString();
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
    }
}
