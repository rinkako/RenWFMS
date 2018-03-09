package org.sysu.renCommon.dto;

import java.io.Serializable;

/**
 * Author: Gordan
 * Date  : 2017/12/14
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
