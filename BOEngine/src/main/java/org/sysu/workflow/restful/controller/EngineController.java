package org.sysu.workflow.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.workflow.restful.service.LaunchProcessService;
import org.sysu.workflow.restful.utility.SerializationUtil;
import org.sysu.workflow.restful.dto.ReturnElement;
import org.sysu.workflow.restful.dto.ReturnModel;
import org.sysu.workflow.restful.dto.StatusCode;

import javax.transaction.Transactional;
import java.util.ArrayList;

/**
 * Author: Ariana
 * Date  : 2018/1/20
 * Usage : Handle requests from other modules.
 */
@RestController
@RequestMapping("/boengine")
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
                return ExcepetionHandler.HandleMissingParameters(missingParams);
            }
            // logic
            LaunchProcessService.LaunchProcess(rtid);
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData("LaunchProcess");
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            return ExcepetionHandler.HandleException(e.getClass().getName());
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
                return ExcepetionHandler.HandleMissingParameters(missingParams);
            }
            // logic
            String jsonify = SerializationUtil.JsonSerialization(LaunchProcessService.SerializeBO(boidlist), "");
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData(jsonify);
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            return ExcepetionHandler.HandleException(e.getClass().getName());
        }
        return rnModel;
    }
}
