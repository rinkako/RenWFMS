package org.sysu.renResourcing.basic.enums;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2017/11/14
 * Usage : Enum of RenResourcing services.
 */
public enum RServiceType implements Serializable {
    // No operation
    NOP,
    // Start the resource service
    EngineStart,
    // Stop the resource service
    EngineStop,
    // Event: Exception raised
    EngineException
}
