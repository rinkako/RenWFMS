package org.sysu.renResourcing.restful.dto;

import org.sysu.renResourcing.util.TimestampUtil;

import java.util.List;

/**
 * Author: Gordan
 * Date  : 2018/1/19
 * Usage :
 */
public class DTOUtil {

    public static ReturnModel HandleMissingParameters(List<String> params) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Fail);
        rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
        ReturnElement returnElement = new ReturnElement();
        StringBuffer sb = new StringBuffer();
        sb.append("miss parameters:");
        for (String s : params) {
            sb.append(s+" ");
        }
        returnElement.setMessage(sb.toString());
        rnModel.setReturnElement(returnElement);
        return rnModel;
    }

}
