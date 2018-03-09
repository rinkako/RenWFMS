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
 * Usage : Handle requests about the cluster.
 */
@RestController
@RequestMapping("/cluster")
public class ClusterController {

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
     * Diki
     * @param token
     * @param from
     * @param timestamp
     * @return
     */
    @PostMapping(value = "/doki", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel Diki(@RequestParam(value="token", required = false)String token,
                            @RequestParam(value="from", required = false)String from,
                            @RequestParam(value="timestamp", required = false)String timestamp) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (from == null) missingParams.add("from");
            if (timestamp == null) missingParams.add("timestamp");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/sync", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel Sync(@RequestParam(value="token", required = false)String token,
                            @RequestParam(value="from", required = false)String from,
                            @RequestParam(value="timestamp", required = false)String timestamp) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (from == null) missingParams.add("from");
            if (timestamp == null) missingParams.add("timestamp");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
     * @param timestamp
     * @return
     */
    @PostMapping(value = "/delegate", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel Delegate(@RequestParam(value="token", required = false)String token,
                                @RequestParam(value="from", required = false)String from,
                                @RequestParam(value="to", required = false)String to,
                                @RequestParam(value="timestamp", required = false)String timestamp) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (from == null) missingParams.add("from");
            if (to == null) missingParams.add("to");
            if (timestamp == null) missingParams.add("timestamp");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/flush", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel Flush(@RequestParam(value="token", required = false)String token,
                             @RequestParam(value="to", required = false)String to) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (to == null) missingParams.add("to");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/fin", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel Fin(@RequestParam(value="token", required = false)String token,
                           @RequestParam(value="to", required = false)String to) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (to == null) missingParams.add("to");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
