/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.controller;
import org.springframework.web.bind.annotation.*;
import org.sysu.renNameService.entity.RenRolemapEntity;
import org.sysu.renNameService.restful.dto.ReturnElement;
import org.sysu.renNameService.restful.dto.ReturnModel;
import org.sysu.renNameService.restful.dto.StatusCode;
import org.sysu.renNameService.roleMapping.RoleMappingService;
import org.sysu.renNameService.utility.SerializationUtil;
import org.sysu.renNameService.utility.TimestampUtil;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Date  : 2018/1/19
 * Usage : Handle requests about business role and worker mappings.
 */
@RestController
@RequestMapping("/rolemap")
public class RoleMappingController {

    /**
     * Router exception handler.
     * @param exception exception descriptor
     * @return response package
     */
    private ReturnModel ExceptionHandlerFunction(String exception) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Exception);
        rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
        ReturnElement returnElement = new ReturnElement();
        returnElement.setMessage(exception);
        rnModel.setReturnElement(returnElement);
        return rnModel;
    }

    /**
     * Router request parameter missing handler.
     * @param params missing parameter list
     * @return response package
     */
    private ReturnModel HandleMissingParameters(List<String> params) {
        ReturnModel rnModel = new ReturnModel();
        rnModel.setCode(StatusCode.Fail);
        rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
        ReturnElement returnElement = new ReturnElement();
        StringBuilder sb = new StringBuilder();
        sb.append("miss parameters:");
        for (String s : params) {
            sb.append(s).append(" ");
        }
        returnElement.setMessage(sb.toString());
        rnModel.setReturnElement(returnElement);
        return rnModel;
    }

    /**
     * Get worker's id by his business role.
     * @param rtid process rtid
     * @param brole business role name
     * @return response package
     */
    @RequestMapping(value = "/getWorkerByBRole", produces = {"application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetWorkerByBusinessRole(@RequestParam(value="rtid", required = false)String rtid,
                                               @RequestParam(value="brole", required = false)String brole) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (brole == null) missingParams.add("brole");
            if (missingParams.size() > 0) {
                rnModel = HandleMissingParameters(missingParams);
                return rnModel;
            }
            // logic
            ArrayList<String> bRoles = RoleMappingService.GetWorkerByBusinessRole(rtid, brole);
            String jsonifyBRoles = SerializationUtil.JsonSerilization(bRoles);
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData(jsonifyBRoles);
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get business role by the worker's id.
     * @param rtid process rtid
     * @param gid worker global id
     * @return response package
     */
    @RequestMapping(value = "/getBRoleByWorker", produces = {"application/json", "application/xml"})
    @ResponseBody
    public ReturnModel GetBusinessRoleByGlobalId(@RequestParam(value="rtid", required = false)String rtid,
                                                 @RequestParam(value="gid", required = false)String gid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (gid == null) missingParams.add("gid");
            if (missingParams.size() > 0) {
                rnModel = HandleMissingParameters(missingParams);
                return rnModel;
            }
            // logic
            ArrayList<String> gidList = RoleMappingService.GetBusinessRoleByGlobalId(rtid, gid);
            String jsonifyList = SerializationUtil.JsonSerilization(gidList);
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData(jsonifyList);
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }
        return rnModel;
    }


    /**
     * Register a mapping to RoleMap Service.
     * @param rtid process rtid
     * @param organGid organization global id
     * @param dataVersion organization data version
     * @param isolationType organization data isolation type
     * @param map map descriptor
     * @return response package
     */
    @RequestMapping(value = "/register", produces = {"application/json", "application/xml"})
    @ResponseBody
    public ReturnModel RegisterRoleMapService(@RequestParam(value="rtid", required = false)String rtid,
                                              @RequestParam(value="organgid", required = false)String organGid,
                                              @RequestParam(value="dataversion", required = false)String dataVersion,
                                              @RequestParam(value="isolationtype", required = false)String isolationType,
                                              @RequestParam(value="map", required = false)String map) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (map == null) missingParams.add("organgid");
            if (map == null) missingParams.add("dataversion");
            if (map == null) missingParams.add("isolationtype");
            if (map == null) missingParams.add("map");
            if (missingParams.size() > 0) {
                rnModel = HandleMissingParameters(missingParams);
                return rnModel;
            }
            // logic
            RoleMappingService.RegisterRoleMapService(rtid, organGid, dataVersion, Integer.valueOf(isolationType), map);
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData("RegisterRoleMapService");
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Finish a process and delete cache.
     * @param rtid process rtid
     * @return response package
     */
    @RequestMapping(value = "/fin", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel FinishRoleMapService(@RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                rnModel = HandleMissingParameters(missingParams);
                return rnModel;
            }
            // logic
            RoleMappingService.FinishRoleMapService(rtid);
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData("FinishRoleMapService");
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Get all resources involved in a process.
     * @param rtid process rtid
     * @return response package
     */
    @RequestMapping(value = "/getInvolved", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel GetInvolvedResource(@RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                rnModel = HandleMissingParameters(missingParams);
                return rnModel;
            }
            // logic
            ArrayList<RenRolemapEntity> involves = RoleMappingService.GetInvolvedResource(rtid);
            String jsonifyInvolves = SerializationUtil.JsonSerilization(involves);
            // return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData(jsonifyInvolves);
            rnModel.setReturnElement(returnElement);
        } catch (Exception e) {
            rnModel = ExceptionHandlerFunction(e.getClass().getName());
        }

        return rnModel;
    }
}
