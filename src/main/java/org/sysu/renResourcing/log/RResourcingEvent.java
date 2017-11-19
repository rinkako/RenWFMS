package org.sysu.renResourcing.log;

import org.sysu.renResourcing.entity.RIdentifier;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Author: Rinkako
 * Date  : 2017/11/19
 * Usage :
 */
public class RResourcingEvent extends REvent {
    /**
     * Id for one specific runtime
     */
    public String RuntimeRecordId;

    /**
     * Id for process, usually name of Business Class
     */
    public String ProcessId;

    /**
     * Resourcing participant id
     */
    public String ParticipantId;

    /**
     * Resourcing workitem id
     */
    public String WorkitemId;

    /**
     * Generate the event XML String.
     * @return XML string of this event
     */
    public String ToXML() {
        throw new NotImplementedException();
    }

    /**
     * Generate instance from event XML string.
     * @param xml XML data in string
     * @return a RIdentifier object
     */
    public RIdentifier FromXML(String xml) {
        throw new NotImplementedException();
    }
}
