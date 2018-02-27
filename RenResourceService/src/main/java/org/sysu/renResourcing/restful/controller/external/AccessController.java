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
 * Usage : Handle requests about access control.
 */
@RestController
public class AccessController {

    // Todo
    public ReturnModel ExceptionHandlerFunction(String exception) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Exception);
        rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");

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

    @RequestMapping(value = "/", produces = {"application/json", "application/xml"},
            method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ReturnModel Index() {
        ReturnModel rnModel = new ReturnModel();
        try {
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData("Index");
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Connect to RS to request a token.
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/connect", produces = {"application/json", "application/xml"})
    @ResponseBody
    public ReturnModel Connect(@RequestParam(value="username", required = false)String username,
                               @RequestParam(value="password", required = false)String password) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (username == null) missingParams.add("username");
            if (password == null) missingParams.add("password");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData("Connect");
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }

        return rnModel;
    }

    /**
     * Disconnect from RS.
     * @param token
     * @return
     */
    @PostMapping(value = "/disconnect", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel DisConnect(@RequestParam(value="token", required = false)String token) {
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
                rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("DisConnect");
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
     * Check the validity of the token.
     * @param token
     * @return
     */
    @PostMapping(value = "/check", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel Check(@RequestParam(value="token", required = false)String token) {
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
                rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("Check");
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
     * Check the authorization of the token
     * @param token
     * @return
     */
    @PostMapping(value = "/checkadmin", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel CheckAdmin(@RequestParam(value="token", required = false)String token) {
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
                rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("CheckAdmin");
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
     * Add a user to RS.
     * @param token
     * @param username
     * @param password
     * @param isadmin
     * @return
     */
    @PostMapping(value = "/user/add", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AddUser(@RequestParam(value="token", required = false)String token,
                               @RequestParam(value="username", required = false)String username,
                               @RequestParam(value="password", required = false)String password,
                               @RequestParam(value="isadmin", required = false)String isadmin) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (password == null) missingParams.add("password");
            if (isadmin == null) missingParams.add("isadmin");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("AddUser");
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
     * Remove a user from RS.
     * @param token
     * @param username
     * @return
     */
    @PostMapping(value = "/user/remove", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RemoveUser(@RequestParam(value="token", required = false)String token,
                                  @RequestParam(value="username", required = false)String username) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("RemoveUser");
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
     * Get information about the user.
     * @param token
     * @param username
     * @return
     */
    @PostMapping(value = "/user/get", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetUser(@RequestParam(value="token", required = false)String token,
                               @RequestParam(value="username", required = false)String username) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("GetUser");
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
     * Set information of the user.
     * @param token
     * @param username
     * @param password
     * @param isAdmin
     * @return
     */
    @PostMapping(value = "/user/set", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SetUser(@RequestParam(value="token", required = false)String token,
                               @RequestParam(value="username", required = false)String username,
                               @RequestParam(value="password", required = false)String password,
                               @RequestParam(value="isAdmin", required = false)String isAdmin) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (password == null) missingParams.add("password");
            if (isAdmin == null) missingParams.add("isAdmin");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setRs(TimestampUtil.GetTimestampString() + " 0");
                ReturnElement returnElement = new ReturnElement();
                returnElement.setData("SetUser");
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
