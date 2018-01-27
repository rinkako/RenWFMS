/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.NSScheduler;
import org.sysu.renNameService.restful.dto.ReturnModel;
import org.sysu.renNameService.restful.dto.ReturnModelHelper;
import org.sysu.renNameService.restful.dto.StatusCode;
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

@RestController
@RequestMapping("/ns")
public class NameSpacingController {
    /**
     * Generate a new RTID.
     * @return response package
     */
    @RequestMapping(value = "/generateRtid", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GenerateRTID() {
        ReturnModel rnModel = new ReturnModel();
        try {
            // logic
            HashMap<String, String> args = new HashMap<>();
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "generateRtid", args);
            String jsonifyResult = (String) NameSpacingController.scheduler.Schedule(t);
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }

        return rnModel;
    }

    /**
     * Create a new process for a ren user.
     * @param renid ren user id (required)
     * @param name process unique name (required)
     * @param mainbo main bo name (required)
     * @return response package
     */
    @RequestMapping(value = "/createProcess", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel CreateProcess(@RequestParam(value="renid", required = false)String renid,
                                     @RequestParam(value="name", required = false)String name,
                                     @RequestParam(value="mainbo", required = false)String mainbo) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (renid == null) missingParams.add("renid");
            if (name == null) missingParams.add("name");
            if (mainbo == null) missingParams.add("mainbo");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
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
     * Upload BO content for a process
     * @param pid belong to process pid (required)
     * @param name BO name (required)
     * @param content BO content (required)
     * @return response package
     */
    @RequestMapping(value = "/uploadBO", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel UploadBO(@RequestParam(value="pid", required = false)String pid,
                                @RequestParam(value="name", required = false)String name,
                                @RequestParam(value="content", required = false)String content) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (pid == null) missingParams.add("pid");
            if (name == null) missingParams.add("name");
            if (content == null) missingParams.add("content");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
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
     * @param pid process id (required)
     * @return response package
     */
    @RequestMapping(value = "/getProcessBONameList", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetProcessBONameList(@RequestParam(value="pid", required = false)String pid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (pid == null) missingParams.add("pid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            HashMap<String, String> args = new HashMap<>();
            args.put("pid", pid);
            NameServiceTransaction t = TransactionCreator.Create(TransactionType.Namespacing, "getProcessBONameList", args);
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
     * @param renid ren user id (required)
     * @return response package
     */
    @RequestMapping(value = "/getProcessByRenId", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetProcessByRenId(@RequestParam(value="renid", required = false)String renid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (renid == null) missingParams.add("renid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
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
     * @param renid ren user id (required)
     * @param processName process unique name to be checked (required)
     * @return response package
     */
    @RequestMapping(value = "/containProcess", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel ContainProcess(@RequestParam(value="renid", required = false)String renid,
                                      @RequestParam(value="processName", required = false)String processName) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (renid == null) missingParams.add("renid");
            if (processName == null) missingParams.add("processName");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
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
     * Get a BO entity by its id.
     * @param boid bo unique id (required)
     * @param rtid process rtid
     * @return response package
     */
    @RequestMapping(value = "/getBO", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetBO(@RequestParam(value="boid", required = false)String boid,
                             @RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (boid == null) missingParams.add("boid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
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
     * Transaction scheduler.
     */
    private static NSScheduler scheduler = NSScheduler.GetInstance();
}
