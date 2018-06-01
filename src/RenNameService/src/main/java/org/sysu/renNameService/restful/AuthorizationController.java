/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful;

import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.authorization.AuthTokenManager;
import org.sysu.renNameService.authorization.AuthorizationService;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renNameService.restful.ReturnModelHelper;
import org.sysu.renCommon.dto.StatusCode;
import org.sysu.renNameService.utility.SerializationUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/1/22
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
    @PostMapping(value = "/connect", produces = {"application/json"})
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
    @PostMapping(value = "/disconnect", produces = {"application/json"})
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
    @PostMapping(value = "/check", produces = {"application/json"})
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
     * Add a new domain.
     * @param token auth token (required)
     * @param name domain unique name (required)
     * @param password admin password (required)
     * @param level level
     * @param corgan COrgan gateway URL
     * @return response package
     */
    @PostMapping(value = "/domain/add", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel AddDomain(@RequestParam(value="token", required = false)String token,
                                 @RequestParam(value="name", required = false)String name,
                                 @RequestParam(value="password", required = false)String password,
                                 @RequestParam(value="level", required = false)String level,
                                 @RequestParam(value="corgan", required = false)String corgan) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (name == null) missingParams.add("name");
            if (password == null) missingParams.add("password");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (AuthorizationService.CheckValidLevel(token) < 2) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            if (level == null) {
                level = "0";
            }
            String jsonifyResult = AuthorizationService.AddDomain(name, password, level, corgan);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Make an domain invalid.
     * @param token auth token (required)
     * @param name domain unique name (required)
     * @return response package
     */
    @PostMapping(value = "/domain/remove", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RemoveDomain(@RequestParam(value="token", required = false)String token,
                                    @RequestParam(value="name", required = false)String name) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (name == null) missingParams.add("name");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (AuthorizationService.CheckValidLevel(token) < 2) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RemoveDomain(name),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Update a domain.
     * @param token auth token (required)
     * @param name domain unique name (required)
     * @param level new level
     * @param status new deletion state
     * @param corgan new COrgan gateway URL
     * @return response package
     */
    @PostMapping(value = "/domain/update", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel UpdateDomain(@RequestParam(value="token", required = false)String token,
                                    @RequestParam(value="name", required = false)String name,
                                    @RequestParam(value="level", required = false)String level,
                                    @RequestParam(value="status", required = false)String status,
                                    @RequestParam(value="corgan", required = false)String corgan) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (name == null) missingParams.add("name");
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
            if (level != null) {
                if (tokenLevel < 2) {  // change user level is WFMS ADMIN ONLY
                    return ReturnModelHelper.UnauthorizedResponse(token);
                }
                updateArgs.put("level", level);
            }
            if (status != null) {
                if (tokenLevel < 2) {  // change user deletion status is WFMS ADMIN ONLY
                    return ReturnModelHelper.UnauthorizedResponse(token);
                }
                updateArgs.put("status", status);
            }
            if (corgan != null) {
                updateArgs.put("corgan", corgan);
            }
            // return
            if (updateArgs.size() == 0) {
                ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, "OK");
            }
            else {
                String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.UpdateDomain(name, updateArgs, tokenLevel >= 2), "");
                ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
            }
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Check if a domain exist.
     * @param token auth token
     * @param name domain unique name
     * @return response package
     */
    @PostMapping(value = "/domain/contain", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel ContainDomain(@RequestParam(value="token", required = false)String token,
                                     @RequestParam(value="name", required = false)String name) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (name == null) missingParams.add("name");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (AuthorizationService.CheckValidLevel(token) < 2) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.ContainDomain(name),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get domain by its name.
     * @param token auth token
     * @param name domain unique name
     * @return response package
     */
    @PostMapping(value = "/domain/get", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetDomain(@RequestParam(value="token", required = false)String token,
                                 @RequestParam(value="name", required = false)String name) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (name == null) missingParams.add("name");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (AuthorizationService.CheckValidLevel(token) < 2) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RetrieveDomain(name),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get domain by its name.
     * @param token auth token
     * @return response package
     */
    @PostMapping(value = "/domain/getall", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAllDomain(@RequestParam(value="token", required = false)String token) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (AuthorizationService.CheckValidLevel(token) < 2) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RetrieveAllDomain(),"");
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
     * @param domain domain name (required)
     * @return response package
     */
    @PostMapping(value = "/user/add", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel AddAuthorization(@RequestParam(value="token", required = false)String token,
                                        @RequestParam(value="username", required = false)String username,
                                        @RequestParam(value="password", required = false)String password,
                                        @RequestParam(value="level", required = false)String level,
                                        @RequestParam(value="domain", required = false)String domain,
                                        @RequestParam(value="gid", required = false)String gid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (password == null) missingParams.add("password");
            if (level == null) missingParams.add("level");
            if (domain == null) missingParams.add("domain");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if ((AuthorizationService.CheckValidLevel(token) < 1 &&
                    AuthTokenManager.GetDomain(token).equals(domain)) &&
                    !token.equals(GlobalContext.INTERNAL_TOKEN)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            assert level != null;
            String jsonifyResult = AuthorizationService.AddAuthUser(username, password, Integer.valueOf(level), domain, gid);
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
     * @param domain domain name (required)
     * @return response package
     */
    @PostMapping(value = "/user/remove", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RemoveAuthorization(@RequestParam(value="token", required = false)String token,
                                           @RequestParam(value="username", required = false)String username,
                                           @RequestParam(value="domain", required = false)String domain) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (domain == null) missingParams.add("domain");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if ((AuthorizationService.CheckValidLevel(token) < 1 &&
                    AuthTokenManager.GetDomain(token).equals(domain)) &&
                    !token.equals(GlobalContext.INTERNAL_TOKEN)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RemoveAuthorizationUser(username, domain),"");
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
     * @param domain domain name (required)
     * @param password new password
     * @param level new level
     * @param status new deletion status
     * @return response package
     */
    @PostMapping(value = "/user/update", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel UpdateAuthorization(@RequestParam(value="token", required = false)String token,
                                           @RequestParam(value="username", required = false)String username,
                                           @RequestParam(value="domain", required = false)String domain,
                                           @RequestParam(value="password", required = false)String password,
                                           @RequestParam(value="level", required = false)String level,
                                           @RequestParam(value="status", required = false)String status,
                                           @RequestParam(value="gid", required = false)String gid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (domain == null) missingParams.add("domain");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check token
            int tokenLevel = AuthorizationService.CheckValidLevel(token);
            if (tokenLevel == -1 || token.equals(GlobalContext.INTERNAL_TOKEN) &&
                    !token.equals(GlobalContext.INTERNAL_TOKEN)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> updateArgs = new HashMap<>();
            if (password != null) {
                updateArgs.put("password", password);
            }
            if (gid != null) {
                updateArgs.put("gid", gid);
            }
            if (level != null) {
                if (tokenLevel < 1) {  // change user level is ADMIN ONLY
                    return ReturnModelHelper.UnauthorizedResponse(token);
                }
                updateArgs.put("level", level);
            }
            if (status != null) {
                if (tokenLevel < 1) {  // change user deletion state is ADMIN ONLY
                    return ReturnModelHelper.UnauthorizedResponse(token);
                }
                updateArgs.put("status", status);
            }
            // return
            if (updateArgs.size() == 0) {
                ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, "OK");
            }
            else {
                String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.UpdateAuthorizationUser(username, domain, updateArgs, tokenLevel > 0), "");
                ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
            }
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get authorization context by its user name.
     * @param token auth token
     * @param username user unique name
     * @return response package
     */
    @PostMapping(value = "/user/contain", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel ContainAuthorization(@RequestParam(value="token", required = false)String token,
                                            @RequestParam(value="username", required = false)String username,
                                            @RequestParam(value="domain", required = false)String domain) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (domain == null) missingParams.add("domain");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if ((AuthorizationService.CheckValidLevel(token) < 0 &&
                    AuthTokenManager.GetDomain(token).equals(domain)) &&
                    !token.equals(GlobalContext.INTERNAL_TOKEN)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.ContainAuthorizationUser(username, domain),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get authorization context by its user name.
     * @param token auth token
     * @param username user unique name
     * @return response package
     */
    @PostMapping(value = "/user/get", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAuthorization(@RequestParam(value="token", required = false)String token,
                                        @RequestParam(value="username", required = false)String username,
                                        @RequestParam(value="domain", required = false)String domain) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (username == null) missingParams.add("username");
            if (domain == null) missingParams.add("domain");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if ((AuthorizationService.CheckValidLevel(token) < 0 &&
                    AuthTokenManager.GetDomain(token).equals(domain)) &&
                    !token.equals(GlobalContext.INTERNAL_TOKEN)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RetrieveAuthorizationUser(username, domain),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get all authorization contexts.
     * @param token auth token
     * @return response package
     */
    @PostMapping(value = "/user/getall", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAllAuthorization(@RequestParam(value="token", required = false)String token,
                                           @RequestParam(value="domain", required = false)String domain) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (domain == null) missingParams.add("domain");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if ((AuthorizationService.CheckValidLevel(token) < 0 &&
                    AuthTokenManager.GetDomain(token).equals(domain)) &&
                    !token.equals(GlobalContext.INTERNAL_TOKEN)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            String jsonifyResult = SerializationUtil.JsonSerialization(AuthorizationService.RetrieveAllAuthorizationUser(domain),"");
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
