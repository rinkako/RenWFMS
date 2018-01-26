/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.NSScheduler;
import org.sysu.renNameService.restful.dto.ReturnElement;
import org.sysu.renNameService.restful.dto.ReturnModel;
import org.sysu.renNameService.restful.dto.ReturnModelHelper;
import org.sysu.renNameService.restful.dto.StatusCode;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.transaction.TransactionCreator;
import org.sysu.renNameService.transaction.TransactionType;

import javax.transaction.Transactional;
import java.util.HashMap;

/**
 * Author: Rinkako
 * Date  : 2018/1/19
 * Usage : Handle requests about the name spacing service.
 */

@RestController
@RequestMapping("/ns")
public class NameSpacingController {
    /**
     * @return
     */
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
            ReturnModelHelper.WrapResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            rnModel = ReturnModelHelper.ExceptionResponse(e.getClass().getName());
        }

        return rnModel;
    }

    /**
     * Transaction scheduler.
     */
    private static NSScheduler scheduler = NSScheduler.GetInstance();
}
