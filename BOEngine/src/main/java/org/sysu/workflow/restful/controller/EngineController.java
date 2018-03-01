/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.workflow.restful.dto.ReturnModelHelper;
import org.sysu.workflow.restful.service.InteractionService;
import org.sysu.workflow.restful.service.LaunchProcessService;
import org.sysu.workflow.restful.utility.SerializationUtil;
import org.sysu.workflow.restful.dto.ReturnModel;
import org.sysu.workflow.restful.dto.StatusCode;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ariana
 * Date  : 2018/1/20
 * Usage : Handle requests from other modules.
 */
@RestController
@RequestMapping("/gateway")
public class EngineController {

    /**
     * launch a process by the rtid
     * @param rtid the runtime record of a process
     * @return response package
     */
    @RequestMapping(value = "/launchProcess", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel LaunchProcess(@RequestParam(value = "rtid", required = false) String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            ArrayList<String> missingParams = new ArrayList<String>();
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            LaunchProcessService.LaunchProcess(rtid);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, "OK");
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.toString());
        }
        return rnModel;
    }

    /**
     * Serialized pre-stored BO XML text and return the involved BO list.
     * @param boidlist BOs to be serialized, separated by `,`
     * @return response package
     */
    @RequestMapping(value = "/serializeBO", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel SerializeBO(@RequestParam(value = "boidlist", required = false) String boidlist) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            ArrayList<String> missingParams = new ArrayList<String>();
            if (boidlist == null) missingParams.add("boidlist");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonify = SerializationUtil.JsonSerialization(LaunchProcessService.SerializeBO(boidlist), "");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonify);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.toString());
        }
        return rnModel;
    }

    /**
     * Receive callback event from Name Service.
     * @param rtid process rtid (required)
     * @param bo from which BO (required)
     * @param on which callback scene (required)
     * @param event event send to engine (required)
     * @param payload event send to engine (required)
     * @return response package
     */
    @RequestMapping(value = "/serializeBO", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel Callback(@RequestParam(value="rtid", required = false)String rtid,
                                @RequestParam(value="bo", required = false)String bo,
                                @RequestParam(value="on", required = false)String on,
                                @RequestParam(value="event", required = false)String event,
                                @RequestParam(value="payload", required = false)String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (bo == null) missingParams.add("bo");
            if (on == null) missingParams.add("on");
            if (event == null) missingParams.add("event");
            if (payload == null) missingParams.add("payload");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            InteractionService.DispatchCallback(rtid, bo, on, event, payload);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, "OK");
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.toString());
        }
        return rnModel;
    }
}
