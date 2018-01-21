package org.sysu.workflow.restful.dto;

import java.io.Serializable;

/**
 * Author: Ariana
 * Date  : 2018/1/21
 * Usage : The status code used to represent the result of the request.
 */
public enum StatusCode implements Serializable {
    // Request worked right
    OK,
    // Request worked fail
    Fail,
    // Exception exists
    Exception,
    // Token is unauthorized
    Unauthorized
}
