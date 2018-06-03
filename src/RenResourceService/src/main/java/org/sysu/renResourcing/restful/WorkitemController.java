/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.restful;

import org.springframework.web.bind.annotation.*;
import org.sysu.renResourcing.RScheduler;
import org.sysu.renCommon.enums.RServiceType;
import org.sysu.renResourcing.context.ResourcingContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renCommon.dto.StatusCode;

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

    /**
     * Start a workitem by auth user.
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/start", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel StartWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                     @RequestParam(value = "workitemId", required = false) String workitemId,
                                     @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/accept", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel AcceptWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                      @RequestParam(value = "workitemId", required = false) String workitemId,
                                      @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/acceptStart", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel AcceptAndStartWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                              @RequestParam(value = "workitemId", required = false) String workitemId,
                                              @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/complete", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel CompleteWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                        @RequestParam(value = "workitemId", required = false) String workitemId,
                                        @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/suspend", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel SuspendWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                       @RequestParam(value = "workitemId", required = false) String workitemId,
                                       @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/unsuspend", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel UnsuspendWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                         @RequestParam(value = "workitemId", required = false) String workitemId,
                                         @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/skip", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel SkipWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                    @RequestParam(value = "workitemId", required = false) String workitemId,
                                    @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/reallocate", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel ReallocateWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                          @RequestParam(value = "workitemId", required = false) String workitemId,
                                          @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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
     *
     * @param workerId   worker global id
     * @param workitemId workitem global id
     * @param payload    payload in JSON encoded string
     * @return response package in JSON
     */
    @RequestMapping(value = "/deallocate", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel DeallocateWorkitem(@RequestParam(value = "workerId", required = false) String workerId,
                                          @RequestParam(value = "workitemId", required = false) String workitemId,
                                          @RequestParam(value = "payload", required = false) String payload) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (workitemId == null) missingParams.add("workitemId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            WorkitemContext workitem = WorkitemContext.GetContext(workitemId, null);
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workitemId", workitemId);
            args.put("workerId", workerId);
            if (payload != null) {
                args.put("payload", payload);
            }
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

    /**
     * Get all workitems by rtid.
     *
     * @param rtid process rtid
     * @return response package in JSON
     */
    @RequestMapping(value = "/getAll", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAll(@RequestParam(value = "rtid", required = false) String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("rtid", rtid);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, rtid,
                    RServiceType.GetAllWorkitemsByRTID, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get all workitems by rtid.
     *
     * @param domain domain name
     * @return response package in JSON
     */
    @RequestMapping(value = "/getAllForDomain", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAllForDomain(@RequestParam(value = "domain", required = false) String domain) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (domain == null) missingParams.add("domain");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("domain", domain);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, "",
                    RServiceType.GetAllWorkitemsByDomain, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get all workitems by rtid.
     *
     * @param workerId participant worker global id
     * @return response package in JSON
     */
    @RequestMapping(value = "/getAllForParticipant", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetAllForParticipant(@RequestParam(value = "workerId", required = false) String workerId) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (workerId == null) missingParams.add("workerId");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, "",
                    RServiceType.GetAllWorkitemsByParticipant, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get a workitem.
     *
     * @param wid workitem id
     * @return response package in JSON
     */
    @RequestMapping(value = "/get", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetByWid(@RequestParam(value = "wid", required = false) String wid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (wid == null) missingParams.add("wid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("wid", wid);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, "",
                    RServiceType.GetByWid, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
