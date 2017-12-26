package org.sysu.renResourcing.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.dto.ReturnElement;
import org.sysu.renResourcing.dto.ReturnModel;
import org.sysu.renResourcing.dto.StatusCode;
import org.sysu.renResourcing.util.TimestampUtil;

/**
 * Author: gd
 * Date  : 2017/12/14
 * Usage : Handle requests about agent resource management.
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

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
     * Get the agent resource.
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetAgent(@RequestParam(value="token")String token,
                                @RequestParam(value="id")String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetAgent");
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
     * Set the agent resource.
     * @param token
     * @param id
     * @param name
     * @param note
     * @param location
     * @return
     */
    @RequestMapping(value = "/set", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel SetAgent(@RequestParam(value="token")String token,
                                @RequestParam(value="id")String id,
                                @RequestParam(value="name")String name,
                                @RequestParam(value="note")String note,
                                @RequestParam(value="location")String location) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SetAgent");
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
     * Check the validity of the agent resource.
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/contain", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel ContainAgent(@RequestParam(value="token")String token,
                                @RequestParam(value="id")String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("ContainAgent");
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
     * Add a agent resource.
     * @param token
     * @param name
     * @param note
     * @param location
     * @return
     */
    @RequestMapping(value = "/add", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel AddAgent(@RequestParam(value="token")String token,
                                @RequestParam(value="name")String name,
                                @RequestParam(value="note")String note,
                                @RequestParam(value="location")String location) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AddAgent");
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
     * Remove the agent resource.
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/remove", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel RemoveAgent(@RequestParam(value="token")String token,
                                @RequestParam(value="id")String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RemoveAgent");
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
     * Add a role to the agent resource.
     * @param token
     * @param id
     * @param role
     * @return
     */
    @RequestMapping(value = "/addrole", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel AddAgentRole(@RequestParam(value="token")String token,
                                    @RequestParam(value="id")String id,
                                    @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AddAgentRole");
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
     * Remove the role from the agent resource.
     * @param token
     * @param id
     * @param role
     * @return
     */
    @RequestMapping(value = "/removerole", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel RemoveAgentRole(@RequestParam(value="token")String token,
                                       @RequestParam(value="id")String id,
                                       @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RemoveAgentRole");
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
