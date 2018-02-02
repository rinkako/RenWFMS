package org.sysu.renResourcing.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.restful.dto.DTOUtil;
import org.sysu.renResourcing.restful.dto.ReturnElement;
import org.sysu.renResourcing.restful.dto.ReturnModel;
import org.sysu.renResourcing.restful.dto.StatusCode;
import org.sysu.renResourcing.utility.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Gordan
 * Date  : 2017/12/14
 * Usage : Handle requests about work queue.
 */
@RestController
@RequestMapping("/queue")
public class WorkQueueController {

    // Todo
    public ReturnModel ExceptionHandlerFunction(String exception) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Exception);
        rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");

        ReturnElement returnElement = new ReturnElement();
        returnElement.setMessage(exception);
        rnModel.setReturnElement(returnElement);

        return rnModel;
    }

    // Todo
    public boolean CheckToken() {
        return true;
    }

    //Todo
    public ReturnModel UnauthorizeHandlerFunction() {
        return null;
    }

    /**
     * Get the work queue of the worker.
     * @param token
     * @param worker
     * @return
     */
    @PostMapping(value = "/get", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetWorkQueue(@RequestParam(value="token", required = false)String token,
                                    @RequestParam(value="worker", required = false)String worker) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (worker == null) missingParams.add("worker");
            if (missingParams.size() > 0) {
                rnModel = DTOUtil.HandleMissingParameters(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetWorkQueue");
                rnModel.setReturnElement(returnElement);
            }
            else {
                rnModel = UnauthorizeHandlerFunction();
            }

        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }

        return rnModel;
    }

    /**
     * Clear the work queue of the worker.
     * @param token
     * @param worker
     * @return
     */
    @PostMapping(value = "/clear", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel ClearWorkQueue(@RequestParam(value="token", required = false)String token,
                                      @RequestParam(value="worker", required = false)String worker) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (worker == null) missingParams.add("worker");
            if (missingParams.size() > 0) {
                rnModel = DTOUtil.HandleMissingParameters(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("ClearWorkQueue");
                rnModel.setReturnElement(returnElement);
            }
            else {
                rnModel = UnauthorizeHandlerFunction();
            }

        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }

        return rnModel;
    }

    /**
     * Get the work queue of the admin.
     * @param token
     * @return
     */
    @PostMapping(value = "/getadmin", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetWorkQueue(@RequestParam(value="token", required = false)String token) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (missingParams.size() > 0) {
                rnModel = DTOUtil.HandleMissingParameters(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetWorkQueue");
                rnModel.setReturnElement(returnElement);
            }
            else {
                rnModel = UnauthorizeHandlerFunction();
            }

        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }

        return rnModel;
    }

    /**
     * Clear the work queue of the admin.
     * @param token
     * @return
     */
    @PostMapping(value = "/clearadmin", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel ClearWorkQueue(@RequestParam(value="token", required = false)String token) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (missingParams.size() > 0) {
                rnModel = DTOUtil.HandleMissingParameters(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("ClearWorkQueue");
                rnModel.setReturnElement(returnElement);
            }
            else {
                rnModel = UnauthorizeHandlerFunction();
            }

        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }

        return rnModel;
    }
}
