package org.sysu.renResourcing.log;

import org.sysu.renResourcing.entity.RIdentifier;

import java.util.Date;

/**
 * Author: Rinkako
 * Date  : 2017/11/19
 * Usage :
 */
public abstract class REvent extends RIdentifier {
    /**
     * Event Timestamp
     */
    public Date TimeStamp = null;

    /**
     * Event name
     */
    public String EventName = "";

    /**
     * Which engine module caused this event
     */
    public String Producer = "RenResourcing";

    /**
     * Which engine module solve this event
     */
    public String Solver = "RenResourcing";

    /**
     * Generate the event XML String
     * @return XML string of this event
     */
    public abstract String ToXML();

    /**
     * Generate instance from event XML string.
     * @param xml XML data in string
     * @return a RIdentifier object
     */
    public abstract RIdentifier FromXML(String xml);
}
