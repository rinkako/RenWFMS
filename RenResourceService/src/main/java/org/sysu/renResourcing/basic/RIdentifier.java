package org.sysu.renResourcing.basic;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2017/11/14
 * Usage : A abstract class for the context which can be an
 *         identifier and can be stored to steady memory.
 */
public abstract class RIdentifier implements Serializable {
    /**
     * Unique id
     */
    public String Id = null;

    /**
     * Get the description string of this instance.
     * @return a string in format `ClassName?Id`
     */
    public String GetIdWithType() {
        return String.format("%s?%s", this.getClass().getName(), this.Id);
    }

    /**
     * Get the XML string of this instance.
     * @return a string in XML format
     */
    public String ToXML() {
        String instanceName = this.getClass().getSimpleName();
        return String.format("<%s id='%s'></%s>", instanceName, this.Id, instanceName);
    }

    /**
     * Generate instance from XML string.
     * @param xml XML data in string
     * @return a RIdentifier object
     */
    public RIdentifier FromXML(String xml) {
        throw new NotImplementedException();
    }
}