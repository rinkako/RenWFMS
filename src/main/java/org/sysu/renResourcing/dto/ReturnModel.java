package org.sysu.renResourcing.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Author: gd
 * Date  : 2017/12/14
 * Usage : A data model which encapsulated to return.
 */
@XmlRootElement(name = "xml")
public class ReturnModel implements Serializable {
    /**
     * status code
     */
    private StatusCode code;

    /**
     * timestamp and the ID of resource
     */
    private String rs;

    /**
     * element to return
     */
    private ReturnElement returnElement;

    @XmlElement
    public StatusCode getCode() {
        return code;
    }

    public void setCode(StatusCode code) {
        this.code = code;
    }

    @XmlElement
    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    @XmlElement(name = "return")
    public ReturnElement getReturnElement() {
        return returnElement;
    }

    public void setReturnElement(ReturnElement returnElement) {
        this.returnElement = returnElement;
    }
}
