/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.dto;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Author: Gordan
 * Date  : 2018/1/19
 * Usage : A data model which encapsulated to return.
 */
@XmlRootElement(name = "xml")
public class ReturnModel implements Serializable {
    /**
     * status code
     */
    private StatusCode code;

    /**
     * timestamp and the ID of micro-service instance
     */
    private String ns;

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
    public String getNs() {
        return ns;
    }

    public void setNs(String rs) {
        this.ns = rs;
    }

    @XmlElement(name = "return")
    public ReturnElement getReturnElement() {
        return returnElement;
    }

    public void setReturnElement(ReturnElement returnElement) {
        this.returnElement = returnElement;
    }
}
