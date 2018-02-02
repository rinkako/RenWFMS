package org.sysu.renResourcing.adapter;

import org.sysu.renResourcing.context.ResourcingContext;

/**
 * Author: Rinkako
 * Date  : 2017/11/13
 * Usage : Adapter for RPC Services Invoking
 */
public interface IRPCRequestAdapter {
    /**
     * Accept a request from other web client and convert
     * it to a RenResourcing Request middleware object.
     * @param req request string
     * @return RenResourcing service object
     */
    ResourcingContext RequestReceived(String req);
}
