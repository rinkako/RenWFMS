package org.sysu.renResourcing.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.dto.ReturnElement;
import org.sysu.renResourcing.dto.ReturnModel;
import org.sysu.renResourcing.dto.StatusCode;
import org.sysu.renResourcing.util.TimestampUtil;

/**
 * Author: gd
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
        rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");

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
    @RequestMapping(value = "/get", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetWorkQueue(@RequestParam(value="token")String token,
                                    @RequestParam(value="worker")String worker) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/clear", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel ClearWorkQueue(@RequestParam(value="token")String token,
                                       @RequestParam(value="worker")String worker) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/getadmin", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetWorkQueue(@RequestParam(value="token")String token) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/clearadmin", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel ClearWorkQueue(@RequestParam(value="token")String token) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
