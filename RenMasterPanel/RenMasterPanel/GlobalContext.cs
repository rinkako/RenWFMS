using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RenMasterPanel
{
    internal static class GlobalContext
    {
        public static readonly string URL_Auth_Connect = "http://localhost:10234/auth/connect";
        
        public static readonly string URL_GetProcessByRenId = "http://localhost:10234/ns/getProcessByRenId";

        public static readonly string URL_GetProcessBOByPid = "http://localhost:10234/ns/getProcessBOList";

        public static readonly string URL_CreateProcess = "http://localhost:10234/ns/createProcess";

        public static readonly string URL_UploadBO = "http://localhost:10234/ns/uploadBO";

        public static List<Dictionary<String, String>> Current_Ren_Process_List = null;

    }
}
