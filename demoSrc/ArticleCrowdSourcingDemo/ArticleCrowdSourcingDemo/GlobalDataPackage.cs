using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ArticleCrowdSourcingDemo
{
    internal static class GlobalDataPackage
    {
        public const string DBServerIPAddress = "127.0.0.1";
        
        public const string DBUsername = "boengine";
        
        public const string DBPassword = "boengine";
        
        public const string DBName = "rencsdemo";

        public static string CurrentUsername = "";

        public static UserViewRole CurrentUserViewRole = UserViewRole.Solver;
    }

    internal enum UserViewRole
    {
        Solver,
        Requester
    }
}
