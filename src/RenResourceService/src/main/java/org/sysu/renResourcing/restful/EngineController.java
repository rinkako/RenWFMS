/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.restful;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renResourcing.interfaceService.InterfaceA;
import org.sysu.renCommon.dto.StatusCode;

import java.util.ArrayList;
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
     * @param rtid process runtime record id (required)
     * @param boname bo name (required)
     * @param nodeId id of instance tree node which produce this task (required)
     * @param taskname task polymorphism name (required)
     * @param args argument
     * @return response package
     */
    @PostMapping(value = "/submitTask", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel SubmitTask(@RequestParam(value="rtid", required = false)String rtid,
                                  @RequestParam(value="boname", required = false)String boname,
                                  @RequestParam(value="nodeId", required = false)String nodeId,
                                  @RequestParam(value="taskname", required = false)String taskname,
                                  @RequestParam(value="args", required = false)String args) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (boname == null) missingParams.add("boname");
            if (nodeId == null) missingParams.add("nodeId");
            if (taskname == null) missingParams.add("taskname");
            if (args == null) missingParams.add("args");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = InterfaceA.EngineSubmitTask(rtid, boname, nodeId, taskname, args);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Signal that a process runtime has already finished.
     * @param rtid process runtime record id (required)
     * @param successFlag success flag, 0 unknown, 1 success, -1 failed, default by 1
     * @return response package
     */
    @PostMapping(value = "/finRtid", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel FinRtid(@RequestParam(value="rtid", required = false)String rtid,
                               @RequestParam(value="successFlag", required = false)String successFlag) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = InterfaceA.EngineFinishProcess(rtid, successFlag);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
