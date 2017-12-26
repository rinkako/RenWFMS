package org.sysu.renResourcing.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.dto.ReturnElement;
import org.sysu.renResourcing.dto.ReturnModel;
import org.sysu.renResourcing.dto.StatusCode;
import org.sysu.renResourcing.util.TimestampUtil;

/**
 * Author: gd
 * Date  : 2017/12/14
 * Usage : Handle requests about the cluster.
 */
@RestController
@RequestMapping("/cluster")
public class ClusterController {

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
     * Diki
     * @param token
     * @param from
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "/doki", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel Diki(@RequestParam(value="token")String token,
                            @RequestParam(value="from")String from,
                            @RequestParam(value="timestamp")String timestamp) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("Diki");
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
     * Synchronize the cluster.
     * @param token
     * @param from
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "/sync", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel Sync(@RequestParam(value="token")String token,
                            @RequestParam(value="from")String from,
                            @RequestParam(value="timestamp")String timestamp) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("Sync");
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
     * Delegate a resource service to RS.
     * @param token
     * @param from
     * @param to
     * @param timastamp
     * @return
     */
    @RequestMapping(value = "/delegate", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel Delegate(@RequestParam(value="token")String token,
                                @RequestParam(value="from")String from,
                                @RequestParam(value="to")String to,
                                @RequestParam(value="timastamp")String timastamp) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("Delegate");
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
     * Flush to the storage.
     * @param token
     * @param to
     * @return
     */
    @RequestMapping(value = "/flush", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel Flush(@RequestParam(value="token")String token,
                             @RequestParam(value="to")String to) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("Flush");
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
     * Finish the service.
     * @param token
     * @param to
     * @return
     */
    @RequestMapping(value = "/fin", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel Fin(@RequestParam(value="token")String token,
                           @RequestParam(value="to")String to) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("Fin");
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
