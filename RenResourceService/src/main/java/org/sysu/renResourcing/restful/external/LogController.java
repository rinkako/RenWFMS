package org.sysu.renResourcing.restful.external;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.restful.ReturnModelHelper;
import org.sysu.renCommon.dto.ReturnElement;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renCommon.dto.StatusCode;
import org.sysu.renCommon.utility.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Gordan
 * Date  : 2017/12/14
 * Usage : Handle requests about log.
 */
@RestController
@RequestMapping("/log")
public class LogController {
    // Todo
    public ReturnModel ExceptionHandlerFunction(String exception) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Exception);
        rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");

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
     * Get the log.
     * @param token
     * @param filter
     * @return
     */
    @PostMapping(value = "/get", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetLog(@RequestParam(value="token", required = false)String token,
                              @RequestParam(value="filter", required = false)String filter) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (filter == null) missingParams.add("filter");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetLog");
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
     * Add log.
     * @param token
     * @param type
     * @param event
     * @param timestamp
     * @return
     */
    @PostMapping(value = "/add", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AddLog(@RequestParam(value="token", required = false)String token,
                              @RequestParam(value="type", required = false)String type,
                              @RequestParam(value="event", required = false)String event,
                              @RequestParam(value="timestamp", required = false)String timestamp) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (type == null) missingParams.add("type");
            if (event == null) missingParams.add("event");
            if (timestamp == null) missingParams.add("timestamp");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AddLog");
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
     * Remove the log.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/remove", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RemoveLog(@RequestParam(value="token", required = false)String token,
                                 @RequestParam(value="id", required = false)String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (id == null) missingParams.add("id");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RemoveLog");
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
     * Get workitem log.
     * @param token
     * @param filter
     * @return
     */
    @PostMapping(value = "/getworkitem", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetWorkitemLog(@RequestParam(value="token", required = false)String token,
                                      @RequestParam(value="filter", required = false)String filter) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (filter == null) missingParams.add("filter");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetWorkitemLog");
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
     * Get engine log.
     * @param token
     * @param filter
     * @return
     */
    @PostMapping(value = "/getengine", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetEngineLog(@RequestParam(value="token", required = false)String token,
                                    @RequestParam(value="filter", required = false)String filter) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (filter == null) missingParams.add("filter");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetEngineLog");
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
     * Get resource log.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/getresource", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetResourceLog(@RequestParam(value="token", required = false)String token,
                                      @RequestParam(value="id", required = false)String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (id == null) missingParams.add("id");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetResourceLog");
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
     * Get scheduling log.
     * @param token
     * @param rsid
     * @return
     */
    @PostMapping(value = "/getscheduling", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetSchedulingLog(@RequestParam(value="token", required = false)String token,
                                        @RequestParam(value="rsid", required = false)String rsid) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rsid == null) missingParams.add("rsid");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetSchedulingLog");
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
     * Flush the log to disk.
     * @param token
     * @return
     */
    @PostMapping(value = "/flush", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel FlushLog(@RequestParam(value="token", required = false)String token) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("FlushLog");
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
     * Synchronize the log.
     * @param token
     * @return
     */
    @PostMapping(value = "/sync", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SyncLog(@RequestParam(value="token", required = false)String token) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SyncLog");
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
