using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace InstanceTreeVisualizer.Entity
{
    internal class FriendlyTreeNode
    {
        public String NotifiableId;

        public String GlobalId;

        public String BOName;

        public String StatusDescriptor;

        public List<FriendlyTreeNode> Children = new List<FriendlyTreeNode>();
    }
}
