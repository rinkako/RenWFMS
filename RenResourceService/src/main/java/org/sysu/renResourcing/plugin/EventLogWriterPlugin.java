/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.plugin;

import org.hibernate.Session;
import org.sysu.renResourcing.context.steady.RenRseventlogEntity;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: Rinkako
 * Date  : 2018/2/10
 * Usage : A log writer can run asynchronously.
 */
public class EventLogWriterPlugin extends AsyncRunnablePlugin {

    /**
     * Running flag.
     */
    private boolean isRunning = false;

    /**
     * Event queue for pending for writing.
     */
    private ConcurrentLinkedQueue<RenRseventlogEntity> logEvtQueue = new ConcurrentLinkedQueue<>();

    /**
     * Create a new log writer.
     */
    public EventLogWriterPlugin() { }

    /**
     * Add a log entity to be written later.
     * @param eventEntity log entity
     * @param rtid process rtid
     */
    public void AddEvent(RenRseventlogEntity eventEntity, String rtid) {
        if (this.isRunning) {
            LogUtil.Log(String.format("Try to add event to a running writer, ignored. (Wid: %s, Pid: %s, WorkerId: %s, Evt: %s)",
                    eventEntity.getWid(), eventEntity.getProcessid(), eventEntity.getWorkerid(), eventEntity.getEvent()),
                    EventLogWriterPlugin.class.getName(), LogUtil.LogLevelType.WARNING, rtid);
            return;
        }
        this.logEvtQueue.add(eventEntity);
    }

    /**
     * Run plugin asynchronously.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        this.isRunning = true;
        this.WriteWithoutTransaction();
    }

    /**
     * Write a log to steady asynchronously.
     */
    private void WriteWithoutTransaction() {
        if (logEvtQueue == null || logEvtQueue.isEmpty()) {
            return;
        }
        while (!logEvtQueue.isEmpty()) {
            RenRseventlogEntity eventEntity = logEvtQueue.poll();
            try {
                Session session = HibernateUtil.GetLocalThreadSession();
                session.save(eventEntity);
            } catch (Exception ex) {
                LogUtil.Echo(String.format("Fail to insert RS event log to steady. (Wid: %s, Pid: %s, WorkerId: %s, Evt: %s), %s",
                        eventEntity.getWid(), eventEntity.getProcessid(), eventEntity.getWorkerid(), eventEntity.getEvent(), ex),
                        EventLogWriterPlugin.class.getName(), LogUtil.LogLevelType.ERROR);
            }
        }
    }
}
