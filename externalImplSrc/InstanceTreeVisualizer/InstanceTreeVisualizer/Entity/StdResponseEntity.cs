namespace InstanceTreeVisualizer.Entity
{
    internal sealed class StdResponseEntity
    {
        public string code;
        public string serviceId;
        public ReturnElement returnElement;
    }

    internal sealed class ReturnElement
    {
        public string sign;
        public string message;
        public object data;
    }
}
