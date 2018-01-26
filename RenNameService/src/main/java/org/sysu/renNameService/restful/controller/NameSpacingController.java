/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.NSScheduler;
import org.sysu.renNameService.nameSpacing.NameSpacingService;
import org.sysu.renNameService.restful.dto.ReturnElement;
import org.sysu.renNameService.restful.dto.ReturnModel;
import org.sysu.renNameService.restful.dto.StatusCode;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.transaction.TransactionCreator;
import org.sysu.renNameService.transaction.TransactionType;
import org.sysu.renNameService.utility.TimestampUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/1/19
 * Usage : Handle requests about the name spacing service.
 */

@RestController
@RequestMapping("/ns")
public class NameSpacingController {

    // Todo
    public ReturnModel ExceptionHandlerFunction(String exception) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Exception);
        rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");

        ReturnElement returnElement = new ReturnElement();
        returnElement.setMessage(exception);
        rnModel.setReturnElement(returnElement);

        return rnModel;
    }

    public ReturnModel HandleMissingParameters(List<String> params) {
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


    @RequestMapping(value = "/generateRtid", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GenerateRTID() {
        ReturnModel rnModel = new ReturnModel();
        try {
            // logic
            HashMap<String, String> args = new HashMap<>();
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "generateRtid", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData(jsonifyResult);
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }

        return rnModel;
    }

    /**
     * Transaction scheduler.
     */
    private static NSScheduler scheduler = NSScheduler.GetInstance();
}
