/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.restful.dto.ReturnModel;
import org.sysu.renNameService.restful.dto.ReturnModelHelper;
import org.sysu.renNameService.restful.dto.StatusCode;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Gordan
 * Date  : 2018/1/19
 * Usage : Handle requests about the cluster.
 */

@RestController
@RequestMapping("/auth")
public class AuthorityController {
    /**
     * Request for an auth token by an authorization username and password.
     * @param username user unique name
     * @param password password
     * @return response package
     */
    @PostMapping(value = "/connect", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel Connect(@RequestParam(value="username", required = false)String username,
                               @RequestParam(value="password", required = false)String password) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (username == null) missingParams.add("username");
            if (password == null) missingParams.add("password");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Disable an auth token.
     * @param token auth token
     * @return response package
     */
    @PostMapping(value = "/disconnect", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel Disconnect(@RequestParam(value="token", required = false)String token) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Check if an auth token is valid now.
     * @param token auth token
     * @return response package
     */
    @PostMapping(value = "/check", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel Check(@RequestParam(value="token", required = false)String token) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Add a new authorization user.
     * @param token auth token
     * @param username user unique name
     * @param password user password
     * @return response package
     */
    @PostMapping(value = "/add", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel AddAuthorization(@RequestParam(value="token", required = false)String token,
                                        @RequestParam(value="username", required = false)String username,
                                        @RequestParam(value="password", required = false)String password) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (password == null) missingParams.add("password");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Make an authorization user invalid.
     * @param token auth token
     * @param username user unique name
     * @return response package
     */
    @PostMapping(value = "/remove", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel RemoveAuthorization(@RequestParam(value="token", required = false)String token,
                                           @RequestParam(value="username", required = false)String username) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Update a authorization information.
     * @param token auth token
     * @param username user unique name
     * @param updateArgs update arguments descriptor
     * @return response package
     */
    @PostMapping(value = "/update", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel UpdateAuthorization(@RequestParam(value="token", required = false)String token,
                                           @RequestParam(value="username", required = false)String username,
                                           @RequestParam(value="updateArgs", required = false)String updateArgs) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (updateArgs == null) missingParams.add("updateArgs");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get authorization entity by its user name.
     * @param token auth token
     * @param username user unique name
     * @return response package
     */
    @PostMapping(value = "/get", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAuthorization(@RequestParam(value="token", required = false)String token,
                                        @RequestParam(value="username", required = false)String username) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
