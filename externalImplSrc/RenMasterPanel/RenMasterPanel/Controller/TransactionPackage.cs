using System;
using System.Collections.Generic;

namespace RenMasterPanel.Controller
{
    [Serializable]
    internal sealed class TransactionPackage
    {
        public String AuthToken { get; set; } = null;

        public String RenUsername { get; set; } = null;

        public String ProcessLocalPath { get; set; } = null;

        public String StaticResourcesXML { get; set; } = null;

        public String ProcessPID { get; set; } = null;

        public String ProcessName { get; set; } = null;

        public int IsolationType { get; set; } = 0;

        public int FailureType { get; set; } = 0;

        public int LaunchType { get; set; } = 0;

        public int AuthType { get; set; } = 0;

        public List<Dictionary<string, string>> BOVector { get; set; }

        public HashSet<string> BusinessRoleList { get; set; } = new HashSet<string>();

        public List<KeyValuePair<String, String>> Mappings { get; set; } = new List<KeyValuePair<string, string>>();
    }
}
