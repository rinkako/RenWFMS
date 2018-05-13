/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.restful;

import org.springframework.web.bind.annotation.*;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renCommon.dto.StatusCode;
import org.sysu.renCommon.enums.RServiceType;
import org.sysu.renResourcing.RScheduler;
import org.sysu.renResourcing.context.ResourcingContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2017/12/14
 * Usage : Handle requests about work queue.
 */
@RestController
@RequestMapping("/queue")
public class WorkQueueController {

    /**
     * Get a specific work queue of a worker.
     * @param rtid process rtid
     * @param workerId worker global id
     * @param type queue type name
     * @return response package
     */
    @RequestMapping(value = "/get", produces = { "application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetWorkQueue(@RequestParam(value="rtid", required = false)String rtid,
                                    @RequestParam(value="workerId", required = false)String workerId,
                                    @RequestParam(value="type", required = false)String type) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (workerId == null) missingParams.add("workerId");
            if (type == null) missingParams.add("type");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("type", type);
            args.put("rtid", rtid);
            args.put("workerId", workerId);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, rtid, RServiceType.GetQueue, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get a specific work queue of a list of workers.
     * @param rtid process rtid
     * @param workerIdList worker global id list, split by `,`
     * @param type queue type name
     * @return response package
     */
    @RequestMapping(value = "/getlist", produces = { "application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel GetWorkQueueList(@RequestParam(value="rtid", required = false)String rtid,
                                        @RequestParam(value="workerIdList", required = false)String workerIdList,
                                        @RequestParam(value="type", required = false)String type) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (workerIdList == null) missingParams.add("workerIdList");
            if (type == null) missingParams.add("type");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("rtid", rtid);
            args.put("type", type);
            args.put("workerIdList", workerIdList);
            ResourcingContext rCtx = ResourcingContext.GetContext(null, rtid, RServiceType.GetQueueList, args);
            String jsonifyResult = RScheduler.GetInstance().ScheduleSync(rCtx);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
