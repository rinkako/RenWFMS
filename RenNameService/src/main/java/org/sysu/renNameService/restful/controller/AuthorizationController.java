/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.authorization.AuthorizationService;
import org.sysu.renNameService.restful.dto.ReturnModel;
import org.sysu.renNameService.restful.dto.ReturnModelHelper;
import org.sysu.renNameService.restful.dto.StatusCode;
import org.sysu.renNameService.utility.SerializationUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/1/19
 * Usage : Handle requests about BO environment user authorization.
 */

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    /**
     * Request for an auth token by an authorization username and password.
     * @param username user unique name (required)
     * @param password password (required)
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
            String jsonifyResult = AuthorizationService.Connect(username, password);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Disable an auth token.
     * @param token auth token (required)
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
            AuthorizationService.Disconnect(token);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, "OK");
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Check if an auth token is valid now.
     * @param token auth token (required)
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
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.CheckValid(token), "");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Add a new authorization user.
     * @param token auth token (required)
     * @param username user unique name (required)
     * @param password user password (required)
     * @param level user level (required)
     * @param corgan COrgan gateway URL
     * @return response package
     */
    @PostMapping(value = "/add", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel AddAuthorization(@RequestParam(value="token", required = false)String token,
                                        @RequestParam(value="username", required = false)String username,
                                        @RequestParam(value="password", required = false)String password,
                                        @RequestParam(value="level", required = false)String level,
                                        @RequestParam(value="corgan", required = false)String corgan) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (password == null) missingParams.add("password");
            if (level == null) missingParams.add("level");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (AuthorizationService.CheckValidLevel(token) < 1) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            assert level != null;
            String jsonifyResult = AuthorizationService.AddAuthorizationUser(username, password, Integer.valueOf(level), corgan);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Make an authorization user invalid.
     * @param token auth token (required)
     * @param username user unique name (required)
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
            // token check
            if (AuthorizationService.CheckValidLevel(token) < 1) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RemoveAuthorizationUser(username),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Update a authorization information.
     * @param token auth token (required)
     * @param username user unique name (required)
     * @param password new password
     * @param level new level
     * @param state new deletion state
     * @param corgan new COrgan gateway URL
     * @return response package
     */
    @PostMapping(value = "/update", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel UpdateAuthorization(@RequestParam(value="token", required = false)String token,
                                           @RequestParam(value="username", required = false)String username,
                                           @RequestParam(value="password", required = false)String password,
                                           @RequestParam(value="level", required = false)String level,
                                           @RequestParam(value="state", required = false)String state,
                                           @RequestParam(value="corgan", required = false)String corgan) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check token
            int tokenLevel = AuthorizationService.CheckValidLevel(token);
            if (tokenLevel == -1) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> updateArgs = new HashMap<>();
            if (password != null) {
                updateArgs.put("password", password);
            }
            if (level != null) {
                if (tokenLevel < 1) {  // change user level is ADMIN ONLY
                    return ReturnModelHelper.UnauthorizedResponse(token);
                }
                updateArgs.put("level", level);
            }
            if (state != null) {
                if (tokenLevel < 1) {  // change user deletion state is ADMIN ONLY
                    return ReturnModelHelper.UnauthorizedResponse(token);
                }
                updateArgs.put("state", state);
            }
            if (corgan != null) {
                updateArgs.put("corgan", corgan);
            }
            // return
            if (updateArgs.size() == 0) {
                ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, "OK");
            }
            else {
                String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.UpdateAuthorizationUser(username, updateArgs, tokenLevel > 0), "");
                ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
            }
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
    @PostMapping(value = "/contain", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel ContainAuthorization(@RequestParam(value="token", required = false)String token,
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
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.ContainAuthorizationUser(username),"");
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
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RetrieveAuthorizationUser(username),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
