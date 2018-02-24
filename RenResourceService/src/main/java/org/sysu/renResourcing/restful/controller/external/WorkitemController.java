package org.sysu.renResourcing.restful.controller.external;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.RScheduler;
import org.sysu.renResourcing.basic.enums.RServiceType;
import org.sysu.renResourcing.context.ResourcingContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.restful.dto.ReturnModelHelper;
import org.sysu.renResourcing.restful.dto.ReturnModel;
import org.sysu.renResourcing.restful.dto.StatusCode;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/2/22
 * Usage : Handle requests about workitem.
 */
@RestController
@RequestMapping("/workitem")
public class WorkitemController {

    private boolean CheckToken(String token) {
        return true;  // todo using auth server check this token (need? token check may handle in NS already?)
    }

    /**
     * Start a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/start", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel StartWorkitem(@RequestParam(value="token", required = false)String token,
                                     @RequestParam(value="workerId", required = false)String workerId,
                                     @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(), RServiceType.StartWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Accept a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/accept", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel AcceptWorkitem(@RequestParam(value="token", required = false)String token,
                                      @RequestParam(value="workerId", required = false)String workerId,
                                      @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.AcceptWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Accept and start a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/acceptStart", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel AcceptAndStartWorkitem(@RequestParam(value="token", required = false)String token,
                                              @RequestParam(value="workerId", required = false)String workerId,
                                              @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.AcceptAndStartWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Complete a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/complete", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel CompleteWorkitem(@RequestParam(value="token", required = false)String token,
                                        @RequestParam(value="workerId", required = false)String workerId,
                                        @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.CompleteWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Suspend a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/suspend", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel SuspendWorkitem(@RequestParam(value="token", required = false)String token,
                                       @RequestParam(value="workerId", required = false)String workerId,
                                       @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.SuspendWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Unsuspend a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/unsuspend", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel UnsuspendWorkitem(@RequestParam(value="token", required = false)String token,
                                         @RequestParam(value="workerId", required = false)String workerId,
                                         @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.UnsuspendWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Skip a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/skip", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel SkipWorkitem(@RequestParam(value="token", required = false)String token,
                                    @RequestParam(value="workerId", required = false)String workerId,
                                    @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.SkipWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Reallocate a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/reallocate", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel ReallocateWorkitem(@RequestParam(value="token", required = false)String token,
                                          @RequestParam(value="workerId", required = false)String workerId,
                                          @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.ReallocateWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Deallocate a workitem by auth user.
     * @param token auth token
     * @param workerId worker global id
     * @param workitemId workitem global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/deallocate", produces = { "application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel DeallocateWorkitem(@RequestParam(value="token", required = false)String token,
                                          @RequestParam(value="workerId", required = false)String workerId,
                                          @RequestParam(value="workitemId", required = false)String workitemId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (token == null) missingParams.add("token");
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // check authorization
            if (!CheckToken(token)) {
                return ReturnModelHelper.UnauthorizedResponse(token);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, workitem.getEntity().getRtid(),
                    RServiceType.DeallocateWorkitem, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);

        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
