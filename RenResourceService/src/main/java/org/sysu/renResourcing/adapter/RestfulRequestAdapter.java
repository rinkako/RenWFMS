package org.sysu.renResourcing.adapter;

import org.sysu.renResourcing.context.ResourcingContext;

/**
 * Author: Rinkako
 * Date  : 2017/11/14
 * Usage :
 */
public class RestfulRequestAdapter implements IRPCRequestAdapter {
    /**
     * Accept a request from RESTful APIs and convert
     * it to a RenResourcing Request middleware object.
     *
     * @param req request string
     * @return RenResourcing service object
     */
    public ResourcingContext RequestReceived(String req) {
        return null;
    }
}
