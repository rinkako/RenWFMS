/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.dataStoreService;

import org.sysu.renCommon.interactionRouter.RInteractionRouter;
import org.sysu.renCommon.interactionRouter.RestfulRouter;
import java.util.UUID;

/**
 * Usage : This class is used to store runtime context.
 */
public final class GlobalContext {

    /**
     * Service micro-service global id.
     */
    public static final String DATA_STORE_GLOBAL_ID = String.format("WFMSComponent_DS_%s", UUID.randomUUID().toString());

    /**
     * Main internal interaction router.
     */
    public static final RInteractionRouter Interaction = new RestfulRouter();
}
