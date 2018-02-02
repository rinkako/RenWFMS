package org.sysu.renResourcing.adapter;

import org.sysu.renResourcing.context.ResourcingContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Author: Rinkako
 * Date  : 2017/11/13
 * Usage : Adapter for YAWL resourcing request
 */
public class YAWLRequestAdapter implements IRPCRequestAdapter {
    /**
     * Accept a request from YAWL Engine and convert
     * it to a RenResourcing Request middleware object.
     * @param req request string
     * @return RenResourcing service object
     */
    public ResourcingContext RequestReceived(String req) {
        throw new NotImplementedException();
    }
}
