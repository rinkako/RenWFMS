using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RenMasterPanel.Controller
{
    [Serializable]
    internal sealed class StdResponseEntity
    {
        public string code;
        public string ns;
        public ReturnElement returnElement;
    }

    internal sealed class ReturnElement
    {
        public string message;
        public object data;
    }
}
