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
 * Usage : Handle requests about human resource management.
 */
@RestController
@RequestMapping("/human")
public class HumanController {

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
     * Get a human resource.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/get", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetHuman(@RequestParam(value="token", required = false)String token,
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
    @PostMapping(value = "/set", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SetHuman(@RequestParam(value="token", required = false)String token,
                                @RequestParam(value="id", required = false)String id,
                                @RequestParam(value="firstname", required = false)String firstname,
                                @RequestParam(value="lastname", required = false)String lastname,
                                @RequestParam(value="note", required = false)String note) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (id == null) missingParams.add("id");
            if (firstname == null) missingParams.add("firstname");
            if (lastname == null) missingParams.add("lastname");
            if (note == null) missingParams.add("note");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/contain", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel ContainHuman(@RequestParam(value="token", required = false)String token,
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
    @PostMapping(value = "/add", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AddHuman(@RequestParam(value="token", required = false)String token,
                                @RequestParam(value="firstname", required = false)String firstname,
                                @RequestParam(value="lastname", required = false)String lastname,
                                @RequestParam(value="note", required = false)String note) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (firstname == null) missingParams.add("firstname");
            if (lastname == null) missingParams.add("lastname");
            if (note == null) missingParams.add("note");

            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/remove", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RemoveHuman(@RequestParam(value="token", required = false)String token,
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
    @PostMapping(value = "/addrole", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AddHumanRole(@RequestParam(value="token", required = false)String token,
                                    @RequestParam(value="id", required = false)String id,
                                    @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (id == null) missingParams.add("id");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/removerole", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RemoveHumanRole(@RequestParam(value="token", required = false)String token,
                                       @RequestParam(value="id", required = false)String id,
                                       @RequestParam(value="role", required = false)String role) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (id == null) missingParams.add("id");
            if (role == null) missingParams.add("role");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
