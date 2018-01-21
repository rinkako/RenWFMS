/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.restful.dto;
import java.io.Serializable;

/**
 * Author: Gordan
 * Date  : 2018/1/19
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