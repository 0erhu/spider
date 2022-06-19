package com.crawler.schduler;

import com.crawler.sender.ICrawlerSender;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 数据下发器
 */
public abstract class AbstractScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractScheduler.class);
    private static final long DEFAULT_SLEEP_INTERVAL_IN_MS = 1000L;
    private static final String DEFUALT_SENDER_PREFIX = "dp_sender_";
    private long sendCount = 0;
    private long sleepIntervalInMs;
    private long batchItemsPerSend;
    private ICrawlerSender sender;

    public AbstractScheduler(long totalSendItemsPerDay, ICrawlerSender sender) {
        sleepIntervalInMs = (long) Math.ceil((double) TimeUnit.DAYS.toMillis(1) / totalSendItemsPerDay);
        if (sleepIntervalInMs == 0) {
            batchItemsPerSend = (long) Math.ceil((double) totalSendItemsPerDay / TimeUnit.DAYS.toMillis(1));
            sleepIntervalInMs = DEFAULT_SLEEP_INTERVAL_IN_MS;
        } else if (sleepIntervalInMs < DEFAULT_SLEEP_INTERVAL_IN_MS) {
            double tempSleepIntervalTimeInMS = Math.floor((double) TimeUnit.DAYS.toMillis(1) * 100 /totalSendItemsPerDay) / 100;
            batchItemsPerSend = (long) Math.ceil((double) DEFAULT_SLEEP_INTERVAL_IN_MS / tempSleepIntervalTimeInMS);
            sleepIntervalInMs = DEFAULT_SLEEP_INTERVAL_IN_MS;
        }
        logger.info("totalSendItemsPerDay={}, sleepIntervalTimeInMS={}, batchItemsPerSend={}", totalSendItemsPerDay, sleepIntervalInMs, batchItemsPerSend);
        this.sender = sender;
    }

    public ICrawlerSender getSender() {
        return sender;
    }

    public abstract void scheduler();

    public void sendData(byte[] data) {
        sender.send(data);
    }

    public void loadControl(String load) {
        if (sendCount % batchItemsPerSend == 0) {
            if (StringUtils.isNotBlank(load)) {
                logger.info("sendDataIndex={}, batchItemsPerSec={}, sleep={}ms, currentItem={}", sendCount, batchItemsPerSend, sleepIntervalInMs, load);
            } else {
                logger.info("sendDataIndex={}, batchItemsPerSec={}, sleep{}ms", sendCount, batchItemsPerSend, sleepIntervalInMs);
            }
            try {
                Thread.sleep(sleepIntervalInMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sendCount++;
    }

    public void loadControl() {
        loadControl(null);
    }

    public long getSendCount() {
        return this.sendCount;
    }

}
