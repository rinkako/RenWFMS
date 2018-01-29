package org.sysu.workflow.restful.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * Author: Ariana
 * Date  : 2018/1/21
 * Usage : Class ReturnElement is used to encapsulate the real data which returned.
 */
public class ReturnElement {
    /**
     * exception message
     */
    private String message;

    /**
     * data to return
     */
    private String data;

    public ReturnElement() {
    }

    @XmlElement(required = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement(required = false)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}