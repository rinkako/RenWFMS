using System;
using System.Collections.Generic;
using System.Data;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace RestaurantProcessTester.Entity
{
    /// <summary>
    /// 
    /// </summary>
    internal static class ReturnDataHelper
    {
        /// <summary>
        /// Decode a standard response to string.
        /// </summary>
        /// <param name="response">StdResponseEntity instance to be decoded</param>
        /// <returns>List in .NET</returns>
        public static string DecodeString(StdResponseEntity response)
        {
            return response.returnElement.data.ToString();
        }

        /// <summary>
        /// Decode a standard response to a .NET list data struct.
        /// </summary>
        /// <param name="response">StdResponseEntity instance to be decoded</param>
        /// <returns>List in .NET</returns>
        public static List<Object> DecodeList(StdResponseEntity response)
        {
            var returnElement = response.returnElement.data.ToString();
            var responseList = (JArray)JsonConvert.DeserializeObject<Object>(returnElement);
            return responseList.ToObject<List<Object>>();
        }

        /// <summary>
        /// Decode a json string to a .NET dictionary data struct.
        /// </summary>
        /// <param name="response">string to be decoded</param>
        /// <returns>Dictionary in .NET</returns>
        public static Dictionary<String, String> DecodeDictionaryByString(string response)
        {
            var responseList = JsonConvert.DeserializeObject<JObject>(response);
            return responseList.ToObject<Dictionary<String, String>>();
        }

        /// <summary>
        /// Decode a standard response to a .NET dictionary(string, string) data struct.
        /// </summary>
        /// <param name="response">StdResponseEntity instance to be decoded</param>
        /// <returns>Dictionary in .NET</returns>
        public static Dictionary<String, String> DecodeToStringStringDictionary(StdResponseEntity response)
        {
            var returnElement = response.returnElement.data.ToString();
            var jtoken = (JToken)JsonConvert.DeserializeObject<Object>(returnElement);
            return ReturnDataHelper.DecodeDictionaryJToken(jtoken);
        }

        /// <summary>
        /// Decode a standard response to a .NET dataset data struct.
        /// </summary>
        /// <param name="response">StdResponseEntity instance to be decoded</param>
        /// <returns>DataSet in .NET</returns>
        public static DataSet DecodeToDataSet(StdResponseEntity response)
        {
            var returnElement = response.returnElement.data.ToString();
            var jtoken = (JToken)JsonConvert.DeserializeObject<Object>(returnElement);
            var jtokenStr = jtoken.ToString();
            var dataSet = JsonConvert.DeserializeObject<DataSet>(jtokenStr);
            return dataSet;
        }

        /// <summary>
        /// Decode a JToken into a .NET dictionary data struct.
        /// </summary>
        /// <param name="jtoken">JToken instance to be decoded</param>
        /// <returns>Dictionary in .NET</returns>
        public static Dictionary<String, String> DecodeDictionaryJToken(JToken jtoken)
        {
            return (Dictionary<String, String>)jtoken.ToObject(typeof(Dictionary<String, String>)); ;
        }
    }
}
