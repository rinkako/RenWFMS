package org.sysu.workflow.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.workflow.restful.service.ReadXMLService;
import org.sysu.workflow.restful.utility.TimestampUtil;
import org.sysu.workflow.restful.dto.ReturnElement;
import org.sysu.workflow.restful.dto.ReturnModel;
import org.sysu.workflow.restful.dto.StatusCode;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ariana
 * Date  : 2018/1/20
 * Usage : Handle requests from other modules.
 */
@RestController
@RequestMapping("/engine")
public class EngineController {
    /**
     * read xml document from database according to the file name, and then go it
     * @param pid process id
     * @param roid root BO id
     * @return
     */
    @RequestMapping(value = "/read", produces = {"application/json", "application/xml"})
    @ResponseBody
    @Transactional
    public ReturnModel ReadXML(@RequestParam(value = "pid", required = false) String pid,
                               @RequestParam(value = "roid", required = false) String roid) {
        ReturnModel rnModel = new ReturnModel();
        try{
            // miss params
            List<String> missingParams = new ArrayList();
            if (pid == null) missingParams.add("pid");
            if (roid == null) missingParams.add("roid");
            if (missingParams.size() > 0) {
                rnModel = ExcepetionHandler.HandleMissingParameters(missingParams);
                return rnModel;
            }
            //logic(haven't test)
            ReadXMLService.ReadXML(pid, roid);
            //return
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData("ReadXML");
            rnModel.setReturnElement(returnElement);
        }catch (Exception e){
            rnModel = ExcepetionHandler.HandleException(e.getClass().getName());
            return rnModel;
        }
        return rnModel;
    }
}
