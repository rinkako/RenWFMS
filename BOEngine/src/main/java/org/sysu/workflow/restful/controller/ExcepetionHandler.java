package org.sysu.workflow.restful.controller;

import org.sysu.workflow.restful.dto.ReturnElement;
import org.sysu.workflow.restful.dto.ReturnModel;
import org.sysu.workflow.restful.dto.StatusCode;
import org.sysu.workflow.restful.utility.TimestampUtil;

import java.util.List;

/**
 * Author: Ariana
 * Date  : 2018/1/21
 * Usage : Handle exceptions or missing parameters of a request
 */
public final class ExcepetionHandler {
    // Todo
    public static ReturnModel HandleException(String exception) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Exception);
        rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");

        ReturnElement returnElement = new ReturnElement();
        returnElement.setMessage(exception);
        rnModel.setReturnElement(returnElement);

        return rnModel;
    }
    /**
     * deal with the return model when missing necessary parameters
     * @param missingParams
     * @return
     */
    public static ReturnModel HandleMissingParameters(List<String> missingParams) {
        ReturnModel rnModel = new ReturnModel();
        //参数在缺少时应该以Fail的状态码返回
        rnModel.setCode(StatusCode.Fail);
        //设置返回的时间戳
        rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
        //将缺失的参数添加到returnElement的message中
        ReturnElement returnElement = new ReturnElement();
        StringBuffer sb = new StringBuffer();
        sb.append("miss parameters:");
        for (String s : missingParams) {
            sb.append(s + " ");
        }
        System.out.println("miss parameters:" + sb.toString());
        returnElement.setMessage(sb.toString());
        //设置返回的实际内容
        rnModel.setReturnElement(returnElement);
        return rnModel;
    }
}
