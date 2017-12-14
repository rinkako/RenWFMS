package org.sysu.renResourcing.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.dto.ReturnElement;
import org.sysu.renResourcing.dto.ReturnModel;
import org.sysu.renResourcing.dto.StatusCode;
import org.sysu.renResourcing.util.TimestampUtil;

/**
 * Author: gd
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
     * Get a role resource.
     * @param token
     * @param role
     * @return
     */
    @RequestMapping(value = "/get", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetRole(@RequestParam(value="token")String token,
                                @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/set", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel SetRole(@RequestParam(value="token")String token,
                               @RequestParam(value="role")String role,
                               @RequestParam(value="privilege")String privilege) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/contain", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel ContainRole(@RequestParam(value="token")String token,
                                @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/add", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel AddRole(@RequestParam(value="token")String token,
                               @RequestParam(value="role")String role,
                               @RequestParam(value="privilege")String privilege) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/remove", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel RemoveRole(@RequestParam(value="token")String token,
                                @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/getinrole", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetInRole(@RequestParam(value="token")String token,
                                @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/gethumaninrole", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetHumanInRole(@RequestParam(value="token")String token,
                                @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
    @RequestMapping(value = "/getagentinrole", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetAgentInRole(@RequestParam(value="token")String token,
                                @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
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
