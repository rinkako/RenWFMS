using System;
using System.Collections.Generic;
using System.Data;

namespace RenMasterPanel
{
    internal static class GlobalContext
    {
        public static readonly string URL_Auth_Connect = "http://localhost:10234/auth/connect";
        
        public static readonly string URL_GetProcessByRenId = "http://localhost:10234/ns/getProcessByRenId";

        public static readonly string URL_GetProcessBOByPid = "http://localhost:10234/ns/getProcessBOList";

        public static readonly string URL_CreateProcess = "http://localhost:10234/ns/createProcess";

        public static readonly string URL_UploadBO = "http://localhost:10234/ns/uploadBO";

        public static readonly string URL_SubmitProcess = "http://localhost:10234/ns/submitProcess";

        public static readonly string URL_LaunchProcess = "http://localhost:10234/ns/launchProcess";

        public static readonly string URL_GetDataVersion = "http://localhost:10234/rolemap/getDataVersionAndGidFromCOrgan";

        public static readonly string URL_GetAllResources = "http://localhost:10234/rolemap/getAllResourceFromCOrgan";

        public static readonly string URL_GetAllRelationConnections = "http://localhost:10234/rolemap/getAllConnectionFromCOrgan";
        
        public static readonly string URL_UploadMapping = "http://localhost:10234/rolemap/register";
        
        public static readonly string URL_LoadParticipant = "http://localhost:10234/rolemap/loadParticipant";

        public static List<Dictionary<String, String>> Current_Ren_Process_List = null;

        public static String ResourcesCOrganGid = null;

        public static String ResourcesDataVersion = null;

        public static DataSet ResourcesDataSet = null;

        public static String CurrentRTID = null;

        public static String CurrentProcessSelfSignature = null;
    }
}
