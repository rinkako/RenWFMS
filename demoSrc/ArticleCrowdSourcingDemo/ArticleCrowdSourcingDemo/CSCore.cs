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
            DataTable resDt = DBUtil.CommitToDB("select * from ren_user where ren_user.username = \"" + username + "\"").Tables[0];
            if (resDt.Rows.Count > 0)
            {
                string checkpw = (string) resDt.Rows[0]["password"];
                if (String.Equals(checkpw, password))
                {
                    GlobalDataPackage.CurrentUserViewRole = (UserViewRole) resDt.Rows[0]["level"];
                    GlobalDataPackage.CurrentUsername = username;
                    return true;
                }
            }
            return false;
        }

        public static DataTable RefreshRequest(string requester)
        {
            return DBUtil.CommitToDB("select * from ren_request where ren_request.requester = \"" + requester + "\"").Tables[0];
        }


        public static void NewRequest()
        {
            
        }

    }
}
