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
 * Usage : Handle requests about role management.
 */
@RestController
@RequestMapping("/role")
public class RoleController {

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
     * Get a role resource.
     * @param token
     * @param role
     * @return
     */
    @PostMapping(value = "/get", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetRole(@RequestParam(value="token", required = false)String token,
                               @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetRole");
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
     * Set a role resource.
     * @param token
     * @param role
     * @param privilege
     * @return
     */
    @PostMapping(value = "/set", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SetRole(@RequestParam(value="token", required = false)String token,
                               @RequestParam(value="role", required = false)String role,
                               @RequestParam(value="privilege", required = false)String privilege) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (privilege == null) missingParams.add("privilege");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SetRole");
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
     * Check the validity of the role.
     * @param token
     * @param role
     * @return
     */
    @PostMapping(value = "/contain", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel ContainRole(@RequestParam(value="token", required = false)String token,
                                   @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("ContainRole");
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
     * Add a role resource.
     * @param token
     * @param role
     * @param privilege
     * @return
     */
    @PostMapping(value = "/add", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AddRole(@RequestParam(value="token", required = false)String token,
                               @RequestParam(value="role", required = false)String role,
                               @RequestParam(value="privilege", required = false)String privilege) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (privilege == null) missingParams.add("privilege");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AddRole");
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
     * Remove the role resource.
     * @param token
     * @param role
     * @return
     */
    @PostMapping(value = "/remove", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RemoveRole(@RequestParam(value="token", required = false)String token,
                                  @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RemoveRole");
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
     * Get the resource of the role.
     * @param token
     * @param role
     * @return
     */
    @PostMapping(value = "/getinrole", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetInRole(@RequestParam(value="token", required = false)String token,
                                 @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetInRole");
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
     * Get the human resource of th role.
     * @param token
     * @param role
     * @return
     */
    @PostMapping(value = "/gethumaninrole", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetHumanInRole(@RequestParam(value="token", required = false)String token,
                                      @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetHumanInRole");
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
     * Get the agent resource of the role.
     * @param token
     * @param role
     * @return
     */
    @PostMapping(value = "/getagentinrole", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetAgentInRole(@RequestParam(value="token", required = false)String token,
                                      @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetAgentInRole");
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
