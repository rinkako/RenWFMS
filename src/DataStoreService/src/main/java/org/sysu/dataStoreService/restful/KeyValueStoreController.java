/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.dataStoreService.restful;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sysu.renCommon.dto.ReturnModel;
import org.sysu.renCommon.dto.StatusCode;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/1/19
 * Usage : Handle requests about key value data store.
 */
@RestController
@RequestMapping("/kv")
public class KeyValueStoreController {

    /**
     * Echo API.
     * @return response package
     */
    @RequestMapping(value = "/", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel Echo() {
        ReturnModel rnModel = new ReturnModel();
        try {
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, "Welcome to Data Store: Key Value API.");
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Add a key value data to the store.
     * @param rtid process rtid (required)
     * @param key data key (required)
     * @param value data value in JSON string (required)
     * @return response package
     */
    @RequestMapping(value = "/add", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel AddKVStore(@RequestParam(value="rtid", required = false)String rtid,
                                  @RequestParam(value="key", required = false)String key,
                                  @RequestParam(value="value", required = false)String value) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (key == null) missingParams.add("key");
            if (value == null) missingParams.add("value");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "ADD";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Add a key value data to the store.
     * @param rtid process rtid (required)
     * @param keys data key in JSON list (required)
     * @param values data value in JSON string in JSON list (required)
     * @return response package
     */
    @RequestMapping(value = "/add", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel AddManyKVStore(@RequestParam(value="rtid", required = false)String rtid,
                                      @RequestParam(value="keys", required = false)String keys,
                                      @RequestParam(value="values", required = false)String values) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (keys == null) missingParams.add("keys");
            if (values == null) missingParams.add("values");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "ADD_MANY";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Retrieve a key value data from the store.
     * @param rtid process rtid (required)
     * @param key data key (required)
     * @param nilServer data value in JSON string if key not exist
     * @return response package
     */
    @RequestMapping(value = "/retrieve", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RetrieveKVStore(@RequestParam(value="rtid", required = false)String rtid,
                                       @RequestParam(value="key", required = false)String key,
                                       @RequestParam(value="nilServer", required = false)String nilServer) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (key == null) missingParams.add("key");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "RETRIEVE";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Retrieve some key value data pairs from the store.
     * @param rtid process rtid (required)
     * @param keys data key in JSON list (required)
     * @param nilServer data value in JSON string if key not exist
     * @return response package
     */
    @RequestMapping(value = "/retrieveMany", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RetrieveManyKVStore(@RequestParam(value="rtid", required = false)String rtid,
                                           @RequestParam(value="keys", required = false)String keys,
                                           @RequestParam(value="nilServer", required = false)String nilServer) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (keys == null) missingParams.add("keys");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "RETRIEVE_MANY";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Retrieve all key value data from the store.
     * @param rtid process rtid (required)
     * @return response package
     */
    @RequestMapping(value = "/retrieveAll", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RetrieveAllKVStore(@RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "RETRIEVE_ALL";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Remove a key value data from the store.
     * @param rtid process rtid (required)
     * @param key data key (required)
     * @return response package
     */
    @RequestMapping(value = "/remove", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RemoveKVStore(@RequestParam(value="rtid", required = false)String rtid,
                                     @RequestParam(value="key", required = false)String key) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (key == null) missingParams.add("key");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "REMOVE";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Remove some key value data pairs from the store.
     * @param rtid process rtid (required)
     * @param keys data key in JSON list (required)
     * @return response package
     */
    @RequestMapping(value = "/removeMany", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel RemoveManyKVStore(@RequestParam(value="rtid", required = false)String rtid,
                                         @RequestParam(value="keys", required = false)String keys) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (keys == null) missingParams.add("keys");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "REMOVE_MANY";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }

    /**
     * Remove all key value data from the store.
     * @param rtid process rtid (required)
     * @return response package
     */
    @RequestMapping(value = "/clear", produces = {"application/json"})
    @ResponseBody
    @Transactional
    public ReturnModel ClearKVStore(@RequestParam(value="rtid", required = false)String rtid) {
        ReturnModel rnModel = new ReturnModel();
        try {
            // miss params
            List<String> missingParams = new ArrayList<>();
            if (rtid == null) missingParams.add("rtid");
            if (missingParams.size() > 0) {
                return ReturnModelHelper.MissingParametersResponse(missingParams);
            }
            // logic
            String jsonifyResult = "CLEAR";
            // return
            ReturnModelHelper.StandardResponse(rnModel, StatusCode.OK, jsonifyResult);
        } catch (Exception e) {
            ReturnModelHelper.ExceptionResponse(rnModel, e.getClass().getName());
        }
        return rnModel;
    }
}
