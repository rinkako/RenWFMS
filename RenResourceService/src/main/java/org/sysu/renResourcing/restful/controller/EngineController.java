/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.restful.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.restful.dto.ReturnModel;
import org.sysu.renResourcing.restful.dto.ReturnModelHelper;
import org.sysu.renResourcing.restful.dto.StatusCode;
import org.sysu.renResourcing.utility.SerializationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/1/19
 * Usage : Handle requests from BO Engine and NS Engine.
 */

@RestController
@RequestMapping("/internal")
public class EngineController {
    /**
     * Submit a task resourcing request from BOEngine.
     * @param token engine signature token (required)
     * @param rtid process runtime record id (required)
     * @param boname bo name (required)
     * @param taskname task polymorphism name (required)
     * @return response package
     */
    @PostMapping(value = "/submitTask", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel SubmitTask(@RequestParam(value="token", required = false)String token,
                                  @RequestParam(value="rtid", required = false)String rtid,
                                  @RequestParam(value="boname", required = false)String boname,
                                  @RequestParam(value="taskname", required = false)String taskname) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rtid == null) missingParams.add("rtid");
            if (boname == null) missingParams.add("boname");
            if (taskname == null) missingParams.add("taskname");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";  // todo
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
