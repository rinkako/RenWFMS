package org.sysu.renResourcing.restful.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * Author: Gordan
 * Date  : 2017/12/14
 * Usage : Class ReturnElement is used to encapsulate the real data which returned.
 */
public class ReturnElement {
    /**
     * a token for authentication
     */
    private String token;

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
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
