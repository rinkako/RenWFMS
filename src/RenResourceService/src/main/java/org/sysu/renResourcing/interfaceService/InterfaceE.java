/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.sysu.renResourcing.GlobalContext;
import org.sysu.renCommon.enums.RSEventType;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.context.steady.RenRseventlogEntity;
import org.sysu.renResourcing.plugin.AsyncPluginRunner;
import org.sysu.renResourcing.plugin.EventLogWriterPlugin;
import org.sysu.renCommon.utility.TimestampUtil;

import java.util.HashSet;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Implementation of Interface E of Resource Service.
 *         Interface E is responsible for resourcing logging service. Here log means
 *         event records of workitems and work queues.
 */
public class InterfaceE {

    /**
     * Service blocked rtid set.
     */
    private static HashSet<String> banSet = new HashSet<>();

    /**
     * Enable RS event log service.
     */
    public static synchronized void EnableLogService() {
        GlobalContext.EVENTLOG_ENABLE = true;
    }

    /**
     * Disable RS event log service.
     */
    public static synchronized void DisableLogService() {
        GlobalContext.EVENTLOG_ENABLE = false;
    }

    /**
     * Disable RS event log service for a specific process runtime.
     *
     * @param rtid process rtid
     */
    public static synchronized void DisableLogServiceForRTID(String rtid) {
        InterfaceE.banSet.add(rtid);
    }

    /**
     * Resume RS event log service for a specific process runtime.
     *
     * @param rtid process rtid
     */
    public static synchronized void ResumeLogServiceForRTID(String rtid) {
        InterfaceE.banSet.remove(rtid);
    }

    /**
     * Check if RS event log service has been ban for a specific process runtime.
     *
     * @param rtid process rtid
     */
    public static synchronized boolean IsDisabledLogServiceForRTID(String rtid) {
        return InterfaceE.banSet.contains(rtid);
    }

    /**
     * Add a event log to steady.
     *
     * @param workitem workitem context
     * @param workerId worker global id
     * @param event    event type enum
     */
    public static void WriteLog(WorkitemContext workitem, String workerId, RSEventType event) {
        if (!GlobalContext.EVENTLOG_ENABLE || InterfaceE.IsDisabledLogServiceForRTID(workitem.getEntity().getRtid())) {
            return;
        }
        EventLogWriterPlugin writer = new EventLogWriterPlugin();
        RenRseventlogEntity log = new RenRseventlogEntity();
        log.setRsevid(String.format("REL_%s", UUID.randomUUID().toString()));
        log.setEvent(event.name());
        log.setProcessid(workitem.getEntity().getProcessId());
        log.setTaskid(workitem.getEntity().getTaskid());
        log.setWorkerid(workerId);
        log.setTimestamp(TimestampUtil.GetCurrentTimestamp());
        log.setWid(workitem.getEntity().getWid());
        writer.AddEvent(log, workitem.getEntity().getRtid());
        AsyncPluginRunner.AsyncRun(writer);
    }
}
