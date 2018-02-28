/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.enums;

/**
 * Author: Rinkako
 * Date  : 2018/2/10
 * Usage : Enum for participant privileges.
 */
public enum PrivilegeType {
    CAN_SUSPEND(1),
    CAN_REALLOCATE(2),
    CAN_DEALLOCATE(4),
    CAN_DELEGATE(8),
    CAN_SKIP(16);

    /**
     * Enum tag value.
     */
    private int value;

    /**
     * Get the enum tag value.
     * @return enum tag value int
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Private constructor for enum.
     * @param value tag value
     */
    PrivilegeType(int value) {
        this.value = value;
    }
}
