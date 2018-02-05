package org.sysu.renResourcing.restful.controller.external;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.restful.dto.ReturnModelHelper;
import org.sysu.renResourcing.restful.dto.ReturnElement;
import org.sysu.renResourcing.restful.dto.ReturnModel;
import org.sysu.renResourcing.restful.dto.StatusCode;
import org.sysu.renResourcing.utility.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Gordan
 * Date  : 2017/12/14
 * Usage : Handle requests about workitem.
 */
@RestController
@RequestMapping("/workitem")
public class WorkitemController {
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
     * Get the workitem list.
     * @param token
     * @return
     */
    @PostMapping(value = "/getlist", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetWorkitemList(@RequestParam(value="token", required = false)String token) {
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetWorkitemList");
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
     * Get a workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/get", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetWorkitem");
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
     * Set a workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/set", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SetWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SetWorkitem");
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
     * Start the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/start", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel StartWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("StartWorkitem");
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
     * Restart the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/restart", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RestartWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RestartWorkitem");
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
     * Complete the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/complete", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel CompleteWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("CompleteWorkitem");
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
     * Accept a workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/accept", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AcceptWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AcceptWorkitem");
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
     * Allocate a workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/allocate", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AllocateWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AllocateWorkitem");
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
     * Deallacate the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/deallocate", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel DeallocateWorktem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("DeallocateWorktem");
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
     * Skip the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/skip", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SkipWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SkipWorkitem");
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
     * Suspend the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/suspend", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SuspendWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SuspendWorkitem");
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
     * Unsuspend the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/unsuspend", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel UnsuspendWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("UnsuspendWorkitem");
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
     * Offer a workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/offer", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel OfferWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("OfferWorkitem");
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
     * Unoffer the workitem.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/unoffer", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel UnofferWorkitem(@RequestParam(value="token", required = false)String token,
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
                rnModel.setRs(TimestampUtil.GetTimeStampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("UnofferWorkitem");
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
