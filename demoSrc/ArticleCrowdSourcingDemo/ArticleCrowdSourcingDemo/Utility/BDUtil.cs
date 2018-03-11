using System;
using MySql.Data.MySqlClient;
using System.Data;

namespace ArticleCrowdSourcingDemo.Utility
{
    internal abstract class DBUtil
    {
        public static readonly string Conn = String.Format("Database='{0}';Data Source='{1}';User Id='{2}';Password='{3}';charset='utf8';pooling=true",
            GlobalDataPackage.DBName, GlobalDataPackage.DBServerIPAddress, GlobalDataPackage.DBUsername, GlobalDataPackage.DBPassword);
        
        /// <summary>
        /// 提交查询，返回DataSet
        /// </summary>
        /// <param name="cmdText">存储过程名称或者sql命令语句</param>
        /// <returns>查询结果的DataSet</returns>
        public static DataSet CommitToDB(string cmdText)
        {
            var cmd = new MySqlCommand();
            var conn = new MySqlConnection(DBUtil.Conn);
            PrepareCommand(cmd, conn, null, CommandType.Text, cmdText, null);
            var adapter = new MySqlDataAdapter { SelectCommand = cmd };
            var ds = new DataSet();
            adapter.Fill(ds);
            cmd.Parameters.Clear();
            conn.Close();
            return ds;
        }
        
        /// <summary>
        /// 准备执行一个命令
        /// </summary>
        /// <param name="cmd">sql命令</param>
        /// <param name="conn">OleDb连接</param>
        /// <param name="trans">OleDb事务</param>
        /// <param name="cmdType">命令类型例如 存储过程或者文本</param>
        /// <param name="cmdText">命令文本,例如:Select * from Products</param>
        /// <param name="cmdParms">执行命令的参数</param>
        private static void PrepareCommand(MySqlCommand cmd, MySqlConnection conn, MySqlTransaction trans, CommandType cmdType, string cmdText, MySqlParameter[] cmdParms)
        {
            if (conn.State != ConnectionState.Open)
            {
                conn.Open();
            }
            cmd.Connection = conn;
            cmd.CommandText = cmdText;
            if (trans != null)
            {
                cmd.Transaction = trans;
            }
            cmd.CommandType = cmdType;
            if (cmdParms == null)
            {
                return;
            }
            foreach (var parm in cmdParms)
            {
                cmd.Parameters.Add(parm);
            }
        }
    }
}
