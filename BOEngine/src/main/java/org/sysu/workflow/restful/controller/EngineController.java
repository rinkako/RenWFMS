package org.sysu.workflow.restful.controller;

import org.springframework.web.bind.annotation.*;
import org.sysu.workflow.Evaluator;
import org.sysu.workflow.EvaluatorFactory;
import org.sysu.workflow.SCXMLExecutor;
import org.sysu.workflow.env.MulitStateMachineDispatcher;
import org.sysu.workflow.env.SimpleErrorReporter;
import org.sysu.workflow.utility.TimestampUtil;
import org.sysu.workflow.io.SCXMLReader;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.restful.dto.ReturnElement;
import org.sysu.workflow.restful.dto.ReturnModel;
import org.sysu.workflow.restful.dto.StatusCode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
     *引擎需要提供一个接口，这个接口的参数是流程的名字;当请求到达这个接口之后，引擎就去数据库把流程的xml代码读出来并解析运行
     * （注意一个流程可能有多个bo文件）
     * @param filename
     * @return
     */
    @PostMapping(value = "/read", produces = {"application/json", "application/xml"})
    @ResponseBody
    public ReturnModel ReadXML(@RequestParam(value = "filename", required = false) String filename) {
        ReturnModel rnModel = new ReturnModel();
        try{
            // miss params
            List<String> missingParams = new ArrayList();
            if (filename == null) missingParams.add("filename");
            if (missingParams.size() > 0) {
                rnModel = ExcepetionHandler.HandleMissingParameters(missingParams);
                return rnModel;
            }
            //else:
            rnModel.setCode(StatusCode.OK);
            rnModel.setRs(TimestampUtil.GetTimeStamp() + " 0");
            ReturnElement returnElement = new ReturnElement();
            returnElement.setData("ReadXML");
            rnModel.setReturnElement(returnElement);
        }catch (Exception e){
            rnModel = ExcepetionHandler.HandleException(e.getClass().getName());
            return rnModel;
        }

        //todo
        //根据filename从数据库中找到对应的string格式的xml内容
        String xmlFile = null;

        try {
            //解析成SCXML对象
            InputStream inputStream = new ByteArrayInputStream(xmlFile.getBytes());
            SCXML scxml = SCXMLReader.read(inputStream);
            //启动状态机实例
            Evaluator evaluator = EvaluatorFactory.getEvaluator(scxml);
            SCXMLExecutor executor = new SCXMLExecutor(evaluator, new MulitStateMachineDispatcher(), new SimpleErrorReporter());
            executor.setStateMachine(scxml);
            executor.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rnModel;
    }
}
