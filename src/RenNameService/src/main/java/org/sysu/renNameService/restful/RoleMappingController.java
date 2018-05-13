/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful;
import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.NSScheduler;
import org.sysu.renNameService.authorization.AuthorizationService;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renCommon.dto.StatusCode;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.transaction.TransactionCreator;
import org.sysu.renNameService.transaction.TransactionType;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/1/19
 * Usage : Handle requests about business role and worker mappings.
 */
@RestController
@RequestMapping("/rolemap")
public class RoleMappingController {
    /**
     * Get worker's id by his business role.
     * @param rtid process rtid (required)
     * @param brole business role name (required)
     * @return response package
     */
    @RequestMapping(value = "/getWorkerByBRole", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetWorkerByBusinessRole(@RequestParam(value="token", required = false)String token,
                                               @RequestParam(value="rtid", required = false)String rtid,
                                               @RequestParam(value="brole", required = false)String brole) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rtid == null) missingParams.add("rtid");
            if (brole == null) missingParams.add("brole");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            args.put("brole", brole);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "getWorkerByBRole", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get business role by the worker's id.
     * @param rtid process rtid (required)
     * @param gid worker global id (required)
     * @return response package
     */
    @RequestMapping(value = "/getBRoleByWorker", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetBusinessRoleByGlobalId(@RequestParam(value="token", required = false)String token,
                                                 @RequestParam(value="rtid", required = false)String rtid,
                                                 @RequestParam(value="gid", required = false)String gid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rtid == null) missingParams.add("rtid");
            if (gid == null) missingParams.add("gid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            args.put("gid", gid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "getBRoleByWorker", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }


    /**
     * Register a mapping to RoleMap Service.
     * @param rtid process rtid (required)
     * @param organGid organization global id (required)
     * @param dataVersion organization data version (required)
     * @param map map descriptor (required)
     * @return response package
     */
    @SuppressWarnings("ConstantConditions")
    @RequestMapping(value = "/register", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RegisterRoleMapService(@RequestParam(value="token", required = false)String token,
                                              @RequestParam(value="rtid", required = false)String rtid,
                                              @RequestParam(value="organgid", required = false)String organGid,
                                              @RequestParam(value="dataversion", required = false)String dataVersion,
                                              @RequestParam(value="map", required = false)String map) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rtid == null) missingParams.add("rtid");
            if (organGid == null) missingParams.add("organgid");
            if (dataVersion == null) missingParams.add("dataversion");
            if (map == null) missingParams.add("map");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            args.put("organGid", organGid);
            args.put("dataVersion", dataVersion);
            args.put("map", map);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "register", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Finish a process and delete cache.
     * @param rtid process rtid (required)
     * @return response package
     */
    @RequestMapping(value = "/fin", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel FinishRoleMapService(@RequestParam(value="token", required = false)String token,
                                            @RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "fin", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get all resources involved in a process.
     * @param rtid process rtid (required)
     * @return response package
     */
    @RequestMapping(value = "/getInvolved", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetInvolvedResource(@RequestParam(value="token", required = false)String token,
                                           @RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
//            if (!AuthorizationService.CheckValid(token)) {
//                return ReturnModelHelper.UnauthorizedResponse(token);
//            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "getInvolved", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Load involved resources to participant for RS.
     * @param token auth token
     * @param renid ren auth user id (required)
     * @param rtid process runtime record id (required)
     * @return response package
     */
    @RequestMapping(value = "/loadParticipant", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel LoadParticipant(@RequestParam(value="token", required = false)String token,
                                       @RequestParam(value="renid", required = false)String renid,
                                       @RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (renid == null) missingParams.add("renid");
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("renid", renid);
            args.put("rtid", rtid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "loadParticipant", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Load involved resources to participant for RS.
     * @param token auth token
     * @param rtid process runtime record id (required)
     * @return response package
     */
    @RequestMapping(value = "/unloadParticipant", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel UnLoadParticipant(@RequestParam(value="token", required = false)String token,
                                         @RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "unloadParticipant", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get all resources from a REN user binding COrgan.
     * @param token auth token
     * @param renid ren auth user id (required)
     * @return response package
     */
    @RequestMapping(value = "/getAllResourceFromCOrgan", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAllResourceFromCOrgan(@RequestParam(value="token", required = false)String token,
                                                @RequestParam(value="renid", required = false)String renid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (renid == null) missingParams.add("renid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("renid", renid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "getAllResourceFromCOrgan", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get all resources from a REN user binding COrgan.
     * @param token auth token (required)
     * @param renid ren auth user id (required)
     * @return response package
     */
    @RequestMapping(value = "/getAllConnectionFromCOrgan", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAllConnectionFromCOrgan(@RequestParam(value="token", required = false)String token,
                                                  @RequestParam(value="renid", required = false)String renid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (renid == null) missingParams.add("renid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("renid", renid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "getAllConnectionFromCOrgan", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get data version from a REN user binding COrgan.
     * @param token auth token (required)
     * @return response package
     */
    @RequestMapping(value = "/getDataVersionAndGidFromCOrgan", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetDataVersionAndGidFromCOrgan(@RequestParam(value="token", required = false)String token,
                                                @RequestParam(value="renid", required = false)String renid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (renid == null) missingParams.add("renid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("renid", renid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.BusinessRoleMapping, "getDataVersionAndGidFromCOrgan", args);
            String jsonifyResult = (String) RoleMappingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transaction scheduler.
     */
    private static NSScheduler scheduler = NSScheduler.GetInstance();
}
