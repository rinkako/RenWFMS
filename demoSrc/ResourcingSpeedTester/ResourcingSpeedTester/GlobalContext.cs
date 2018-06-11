using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ResourcingSpeedTester
{
    class GlobalContext
    {
        public static readonly Dictionary<String, String> LocationDict = new Dictionary<string, string>();

        public static readonly string LocationConfigFilePath = "Location.txt";

        public static readonly string WorkerId = "Agent_57dff470-6a46-11e8-9b6b-2c4d54f01cf2";
    }

}
