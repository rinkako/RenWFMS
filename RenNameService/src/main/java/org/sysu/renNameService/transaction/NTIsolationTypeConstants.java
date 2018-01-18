/*
 * Project Ren @ 2018
 * Rinkako, Arianna, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.transaction;

/**
 * Author: Rinkako
 * Date  : 2018/1/17
 * Usage : Name service transaction isolation level constant.
 */
interface NTIsolationTypeConstants {
    String RepeatableRead = "RepeatableRead";
    String ReadCommitted = "ReadCommitted";
}