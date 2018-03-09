using System;
using System.Collections.Generic;

namespace RestaurantProcessTester.Entity
{
    [Serializable]
    internal sealed class TransactionPackage
    {
        public String Signature = "PrUpNw1dM3zRH6j3eviklCHE9Zbvk9NavGcJ_CibW19h50Yvr-ZZYZqn5Gi_SG1cPVQEIZf2wAJgBmq4dhNj7w7t9wUEz2pcGhn-6kIRO--QqWy121gksPE8B103RtMzuOsQDcErk4LriRQRO7-Xqks-RtpBUnpInnS_lkkajQs";

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
