using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RenMasterPanel.Controller
{
    [Serializable]
    internal sealed class TransactionPackage
    {
        public String AuthToken { get; set; } = null;

        public String RenUsername { get; set; } = null;

        public String ProcessLocalPath { get; set; } = null;

        public String StaticResourcesXML { get; set; } = null;

        public List<Dictionary<String, String>> BOVector { get; set; }
    }
}
