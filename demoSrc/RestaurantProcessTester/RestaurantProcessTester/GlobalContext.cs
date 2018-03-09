using System;
using System.Collections.Generic;

namespace RestaurantProcessTester
{
    internal static class GlobalContext
    {
        public static readonly Dictionary<String, String> LocationDict = new Dictionary<string, string>();

        public static readonly string LocationConfigFilePath = "Location.txt";
    }
}
