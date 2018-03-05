/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.restful;

import org.sysu.renCommon.dto.ReturnElement;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renCommon.dto.StatusCode;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.workflow.GlobalContext;
import org.sysu.workflow.utility.LogUtil;

import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/3/1
 * Usage : Helper methods for ReturnModel construction.
 */
public class ReturnModelHelper {
    /**
     * Warp success response to a ReturnModel
     *
     * @param rnModel return model package to be updated
     * @param code    status code enum
     * @param retStr  execution return data
     */
    public static void StandardResponse(ReturnModel rnModel, StatusCode code, String retStr) {
        rnModel.setCode(code);
        rnModel.setServiceId(String.format("%s %s", TimestampUtil.GetTimestampString(), GlobalContext.ENGINE_GLOBAL_ID));
        ReturnElement returnElement = new ReturnElement();
        returnElement.setData(retStr);
        rnModel.setReturnElement(returnElement);
    }

    /**
     * Router exception handler.
     *
     * @param exception exception descriptor
     */
    public static void ExceptionResponse(ReturnModel rnModel, String exception) {
        rnModel.setCode(StatusCode.Exception);
        rnModel.setServiceId(String.format("%s %s", TimestampUtil.GetTimestampString(), GlobalContext.ENGINE_GLOBAL_ID));
        ReturnElement returnElement = new ReturnElement();
        returnElement.setMessage(exception);
        rnModel.setReturnElement(returnElement);
    }

    /**
     * Router unauthorized service request handler.
     *
     * @param token unauthorized token
     * @return response package
     */
    public static ReturnModel UnauthorizedResponse(String token) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Unauthorized);
        rnModel.setServiceId(String.format("%s %s", TimestampUtil.GetTimestampString(), GlobalContext.ENGINE_GLOBAL_ID));
        ReturnElement returnElement = new ReturnElement();
        returnElement.setMessage(token);
        rnModel.setReturnElement(returnElement);
        LogUtil.Log(String.format("Unauthorized service request (token:%s)", token),
                ReturnModelHelper.class.getName(), LogLevelType.UNAUTHORIZED, "");
        return rnModel;
    }

    /**
     * Router request parameter missing handler.
     *
     * @param params missing parameter list
     * @return response package
     */
    public static ReturnModel MissingParametersResponse(List<String> params) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Fail);
        rnModel.setServiceId(String.format("%s %s", TimestampUtil.GetTimestampString(), GlobalContext.ENGINE_GLOBAL_ID));
        ReturnElement returnElement = new ReturnElement();
        StringBuilder sb = new StringBuilder();
        sb.append("miss required parameters:");
        for (String s : params) {
            sb.append(s).append(" ");
        }
        returnElement.setMessage(sb.toString());
        rnModel.setReturnElement(returnElement);
        return rnModel;
    }
}
