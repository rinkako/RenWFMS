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
 * Usage : Handle requests about the name spacing service.
 */

@SuppressWarnings("ConstantConditions")
@RestController
@RequestMapping("/ns")
public class NameSpacingController {

    /**
     * Create a new process for a ren user.
     *
     * @param token  auth token
     * @param renid  ren user id (required)
     * @param name   process unique name (required)
     * @param mainbo main bo name (required)
     * @return response package
     */
    @RequestMapping(value = "/createProcess", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel CreateProcess(@RequestParam(value = "token", required = false) String token,
                                     @RequestParam(value = "renid", required = false) String renid,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "mainbo", required = false) String mainbo) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (renid == null) missingParams.add("renid");
            if (name == null) missingParams.add("name");
            if (mainbo == null) missingParams.add("mainbo");
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
            args.put("name", name);
            args.put("mainbo", mainbo);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "createProcess", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Upload BO content for a process.
     *
     * @param token   auth token
     * @param pid     belong to process pid (required)
     * @param name    BO name (required)
     * @param content BO content (required)
     * @return response package
     */
    @RequestMapping(value = "/uploadBO", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel UploadBO(@RequestParam(value = "token", required = false) String token,
                                @RequestParam(value = "pid", required = false) String pid,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "content", required = false) String content) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (pid == null) missingParams.add("pid");
            if (name == null) missingParams.add("name");
            if (content == null) missingParams.add("content");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("pid", pid);
            args.put("name", name);
            args.put("content", content);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "uploadBO", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get BO name list of a specific process.
     *
     * @param token auth token
     * @param pid   process id (required)
     * @return response package
     */
    @RequestMapping(value = "/getProcessBOList", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetProcessBOList(@RequestParam(value = "token", required = false) String token,
                                        @RequestParam(value = "pid", required = false) String pid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (pid == null) missingParams.add("pid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("pid", pid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "getProcessBOList", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get process list of a specific ren user.
     *
     * @param token auth token
     * @param renid ren user id (required)
     * @return response package
     */
    @RequestMapping(value = "/getProcessByRenId", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetProcessByRenId(@RequestParam(value = "token", required = false) String token,
                                         @RequestParam(value = "renid", required = false) String renid) {
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
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "getProcessByRenId", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Check if a ren user already have a process named this.
     *
     * @param token       auth token
     * @param renid       ren user id (required)
     * @param processName process unique name to be checked (required)
     * @return response package
     */
    @RequestMapping(value = "/containProcess", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel ContainProcess(@RequestParam(value = "token", required = false) String token,
                                      @RequestParam(value = "renid", required = false) String renid,
                                      @RequestParam(value = "processName", required = false) String processName) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (renid == null) missingParams.add("renid");
            if (processName == null) missingParams.add("processName");
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
            args.put("processName", processName);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "containProcess", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get a BO context by its id.
     *
     * @param token auth token
     * @param boid  bo unique id (required)
     * @param rtid  process rtid
     * @return response package
     */
    @RequestMapping(value = "/getBO", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetBO(@RequestParam(value = "token", required = false) String token,
                             @RequestParam(value = "boid", required = false) String boid,
                             @RequestParam(value = "rtid", required = false) String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (boid == null) missingParams.add("boid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("boid", boid);
            args.put("rtid", rtid == null ? "" : rtid);  // rtid not exist for selecting process to launch
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "getBO", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Submit a request for launching a specific process.
     *
     * @param token       auth token
     * @param pid         pid for process to be launched (required)
     * @param from        launch from platform identifier (required)
     * @param renid       ren user id (required)
     * @param bindingType resource binding type (required)
     * @param binding     resource binding data
     * @return response package
     */
    @RequestMapping(value = "/submitProcess", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel SubmitProcess(@RequestParam(value = "token", required = false) String token,
                                     @RequestParam(value = "pid", required = false) String pid,
                                     @RequestParam(value = "from", required = false) String from,
                                     @RequestParam(value = "renid", required = false) String renid,
                                     @RequestParam(value = "bindingType", required = false) String bindingType,
                                     @RequestParam(value = "launchType", required = false) String launchType,
                                     @RequestParam(value = "failureType", required = false) String failureType,
                                     @RequestParam(value = "authType", required = false) String authType,
                                     @RequestParam(value = "binding", required = false) String binding) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (pid == null) missingParams.add("pid");
            if (from == null) missingParams.add("from");
            if (renid == null) missingParams.add("renid");
            if (bindingType == null) missingParams.add("bindingType");
            if (launchType == null) missingParams.add("launchType");
            if (failureType == null) missingParams.add("failureType");
            if (authType == null) missingParams.add("authType");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // token check
            if (!AuthorizationService.CheckValid(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("pid", pid);
            args.put("from", from);
            args.put("renid", renid);
            args.put("authoritySession", token);  // token should be stored
            args.put("bindingType", bindingType);
            args.put("launchType", launchType);
            args.put("failureType", failureType);
            args.put("authType", authType);
            args.put("binding", binding == null ? "" : binding);  // binding not exist when using business role map service
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "submitProcess", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Start a process.
     *
     * @param token auth token (required)
     * @param rtid  process rtid (required)
     * @return response package
     */
    @RequestMapping(value = "/startProcess", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel StartProcess(@RequestParam(value = "token", required = false) String token,
                                    @RequestParam(value = "rtid", required = false) String rtid) {
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
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "startProcess", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of sending a callback event to BO engine.
     *
     * @param signature domain signature key (required)
     * @param rtid      process rtid (required)
     * @param bo        from which BO (required)
     * @param on        which callback scene (required)
     * @param event     event send to engine (required)
     * @param payload   event send to engine (required)
     * @return response package
     */
    @RequestMapping(value = "/callback", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipCallback(@RequestParam(value = "signature", required = false) String signature,
                                         @RequestParam(value = "rtid", required = false) String rtid,
                                         @RequestParam(value = "bo", required = false) String bo,
                                         @RequestParam(value = "on", required = false) String on,
                                         @RequestParam(value = "event", required = false) String event,
                                         @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (rtid == null) missingParams.add("rtid");
            if (bo == null) missingParams.add("bo");
            if (on == null) missingParams.add("on");
            if (event == null) missingParams.add("event");
            if (payload == null) missingParams.add("payload");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckRTIDSignature(signature, rtid)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            args.put("on", on);
            args.put("bo", bo);
            args.put("event", event);
            args.put("payload", event);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipCallback", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of start a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/start", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipStartWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                              @RequestParam(value = "workerId", required = false) String workerId,
                                              @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "start");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of accept a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/accept", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipAcceptWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                               @RequestParam(value = "workerId", required = false) String workerId,
                                               @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "accept");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of accept and start a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/acceptStart", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipAcceptAndStartWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                                       @RequestParam(value = "workerId", required = false) String workerId,
                                                       @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "acceptStart");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of complete a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/complete", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipCompleteWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                                 @RequestParam(value = "workerId", required = false) String workerId,
                                                 @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "complete");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of suspend a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/suspend", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipSuspendWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                                @RequestParam(value = "workerId", required = false) String workerId,
                                                @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "suspend");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of unsuspend a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/unsuspend", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipUnsuspendWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                                  @RequestParam(value = "workerId", required = false) String workerId,
                                                  @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "unsuspend");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of skip a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/skip", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipSkipWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                             @RequestParam(value = "workerId", required = false) String workerId,
                                             @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "skip");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of reallocate a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/reallocate", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipReallocateWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                                   @RequestParam(value = "workerId", required = false) String workerId,
                                                   @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "reallocate");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of deallocate a workitem by auth user.
     *
     * @param signature  auth signature
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/workitem/deallocate", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipDeallocateWorkitem(@RequestParam(value = "signature", required = false) String signature,
                                                   @RequestParam(value = "workerId", required = false) String workerId,
                                                   @RequestParam(value = "workitemId", required = false) String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckWorkitemSignature(signature, workitemId)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "deallocate");
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkitem", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Transship request of get a queue for specific worker.
     *
     * @param signature auth signature
     * @param rtid      process rtid
     * @param workerId  worker global id
     * @param type      queue type name
     * @return response package
     */
    @RequestMapping(value = "/queue/get", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipGetWorkQueue(@RequestParam(value = "signature", required = false) String signature,
                                             @RequestParam(value = "rtid", required = false) String rtid,
                                             @RequestParam(value = "workerId", required = false) String workerId,
                                             @RequestParam(value = "type", required = false) String type) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (rtid == null) missingParams.add("rtid");
            if (workerId == null) missingParams.add("workerId");
            if (type == null) missingParams.add("type");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckRTIDSignature(signature, rtid)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "get");
            args.put("type", type);
            args.put("rtid", rtid);
            args.put("workerId", workerId);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkqueue", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get a specific work queue of a list of workers.
     *
     * @param signature    auth signature
     * @param rtid         process rtid
     * @param workerIdList worker global id list, split by `,`
     * @param type         queue type name
     * @return response package
     */
    @RequestMapping(value = "/queue/getlist", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipGetWorkQueueList(@RequestParam(value = "signature", required = false) String signature,
                                                 @RequestParam(value = "rtid", required = false) String rtid,
                                                 @RequestParam(value = "workerIdList", required = false) String workerIdList,
                                                 @RequestParam(value = "type", required = false) String type) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (rtid == null) missingParams.add("rtid");
            if (workerIdList == null) missingParams.add("workerIdList");
            if (type == null) missingParams.add("type");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckRTIDSignature(signature, rtid)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("action", "getlist");
            args.put("rtid", rtid);
            args.put("type", type);
            args.put("workerIdList", workerIdList);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipWorkqueue", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get workitems in a user-friendly package by RTID.
     *
     * @param signature auth signature
     * @param rtid      process rtid
     * @return response package
     */
    @RequestMapping(value = "/workitem/getAll", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel TransshipGetAllWorkitems(@RequestParam(value = "signature", required = false) String signature,
                                                @RequestParam(value = "rtid", required = false) String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (signature == null) missingParams.add("signature");
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!AuthorizationService.CheckRTIDSignature(signature, rtid)) {
                return ReturnModelHelper.UnauthorizedResponse(signature);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("rtid", rtid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "transshipGetAll", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.ScheduleSync(t);
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
