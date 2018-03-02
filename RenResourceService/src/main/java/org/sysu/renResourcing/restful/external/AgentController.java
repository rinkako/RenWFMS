package org.sysu.renResourcing.restful.external;

import org.springframework.web.bind.annotation.*;
import org.sysu.renCommon.dto.ReturnElement;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renResourcing.restful.ReturnModelHelper;
import org.sysu.renCommon.dto.StatusCode;
import org.sysu.renCommon.utility.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Gordan
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
     * Get the agent resource.
     * @param token
     * @param id
     * @return
     */
    @PostMapping(value = "/get", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetAgent(@RequestParam(value="token", required = false)String token,
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
    @PostMapping(value = "/set", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel SetAgent(@RequestParam(value="token", required = false)String token,
                                @RequestParam(value="id", required = false)String id,
                                @RequestParam(value="name", required = false)String name,
                                @RequestParam(value="note", required = false)String note,
                                @RequestParam(value="location", required = false)String location) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (id == null) missingParams.add("id");
            if (name == null) missingParams.add("name");
            if (note == null) missingParams.add("note");
            if (location == null) missingParams.add("location");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/contain", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel ContainAgent(@RequestParam(value="token", required = false)String token,
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
    @PostMapping(value = "/add", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AddAgent(@RequestParam(value="token", required = false)String token,
                                @RequestParam(value="name", required = false)String name,
                                @RequestParam(value="note", required = false)String note,
                                @RequestParam(value="location", required = false)String location) {
        ReturnModel rnModel = new ReturnModel();

        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (name == null) missingParams.add("name");
            if (note == null) missingParams.add("note");
            if (location == null) missingParams.add("location");
            if (missingParams.size() > 0) {
                rnModel = ReturnModelHelper.MissingParametersResponse(missingParams);
                return rnModel;
            }

            if (CheckToken()) {
                rnModel.setCode(StatusCode.OK);
                rnModel.setServiceId(TimestampUtil.GetTimestampString() + " 0");
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
    @PostMapping(value = "/remove", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RemoveAgent(@RequestParam(value="token", required = false)String token,
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
    @PostMapping(value = "/addrole", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel AddAgentRole(@RequestParam(value="token", required = false)String token,
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
    @PostMapping(value = "/removerole", produces = { "application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RemoveAgentRole(@RequestParam(value="token", required = false)String token,
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
