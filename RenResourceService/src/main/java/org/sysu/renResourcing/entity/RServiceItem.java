package org.sysu.renResourcing.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2017/11/13
 * Usage : RenResourcing service object, it is used as an
 *         internal data package for service invoking.
 */
public class RServiceItem extends RIdentifier {
    /**
     * Create new RServiceItem instance
     */
    public RServiceItem() {
        this.Id = UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Session
     */
    public RSession Session = null;

    /**
     * Type of engine service
     */
    public RServiceType Type = RServiceType.NOP;

    /**
     * Parameter Dictionary
     */
    public HashMap<String, String> Args = new HashMap<String, String>();

    /**
     * Priority in schedule queue, smaller is preferential
     */
    public int Priority = 0;

    /**
     * Timestamp of service request arrival
     */
    public Date ArriveTimestamp = new Date();

    /**
     * Timestamp of service scheduled
     */
    public Date ScheduleTimestamp = null;

    /**
     * Timestamp of service executed
     */
    public Date FinishTimestamp = null;

    /**
     * Standard format for timestamp
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
