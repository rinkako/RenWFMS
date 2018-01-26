/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.nameSpacing;

import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/1/26
 * Usage : All name space service will be handled in this service module.
 */
public class NameSpacingService {
    /**
     * Generate a new RTID for new process launching.
     * @return rtid string
     */
    public static String GenerateRTID() {
        return String.format("RTID_%s", UUID.randomUUID());
    }
}
