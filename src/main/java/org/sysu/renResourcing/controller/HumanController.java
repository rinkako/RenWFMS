package org.sysu.renResourcing.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.dto.ReturnElement;
import org.sysu.renResourcing.dto.ReturnModel;
import org.sysu.renResourcing.dto.StatusCode;
import org.sysu.renResourcing.util.TimestampUtil;

/**
 * Author: gd
 * Date  : 2017/12/14
 * Usage : Handle requests about human resource management.
 */
@RestController
@RequestMapping("/human")
public class HumanController {

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
     * Get a human resource.
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel GetHuman(@RequestParam(value="token")String token,
                                   @RequestParam(value="id")String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetHuman");
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
     * Set a human resource.
     * @param token
     * @param id
     * @param firstname
     * @param lastname
     * @param note
     * @return
     */
    @RequestMapping(value = "/set", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel SetHuman(@RequestParam(value="token")String token,
                                @RequestParam(value="id")String id,
                                @RequestParam(value="firstname")String firstname,
                                @RequestParam(value="lastname")String lastname,
                                @RequestParam(value="note")String note) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SetHuman");
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
     * Check the validity of the human resource.
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/contain", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel ContainHuman(@RequestParam(value="token")String token,
                                   @RequestParam(value="id")String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("ContainHuman");
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
     * Add a human resource.
     * @param token
     * @param firstname
     * @param lastname
     * @param note
     * @return
     */
    @RequestMapping(value = "/add", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel AddHuman(@RequestParam(value="token")String token,
                                @RequestParam(value="firstname")String firstname,
                                @RequestParam(value="lastname")String lastname,
                                @RequestParam(value="note")String note) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AddHuman");
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
     * Remove the human resource.
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/remove", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel RemoveHuman(@RequestParam(value="token")String token,
                                   @RequestParam(value="id")String id) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RemoveHuman");
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
     * Add a role to the human resource.
     * @param token
     * @param id
     * @param role
     * @return
     */
    @RequestMapping(value = "/addrole", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel AddHumanRole(@RequestParam(value="token")String token,
                                    @RequestParam(value="id")String id,
                                    @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AddHumanRole");
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
     * Remove the role from the human resource.
     * @param token
     * @param id
     * @param role
     * @return
     */
    @RequestMapping(value = "/removerole", produces = { "application/json", "application/xml"},
            method = RequestMethod.POST)
    @ResponseBody
    public ReturnModel RemoveHumanRole(@RequestParam(value="token")String token,
                                       @RequestParam(value="id")String id,
                                       @RequestParam(value="role")String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RemoveHumanRole");
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
