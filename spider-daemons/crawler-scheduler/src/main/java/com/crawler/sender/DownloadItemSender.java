package com.crawler.sender;

import com.crawler.constants.KafkaTopic;
import com.hu.protobuf.crawer.GeneralCrawer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadItemSender extends CrawlerKafKaSender {

    private static final Logger logger = LoggerFactory.getLogger(DownloadItemSender.class);

    protected void send(GeneralCrawer.DownloadItem downloadItem) {
        byte[] result = downloadItem.toByteArray();
        super.send(result);
    }

    @Override
    public byte[] generateData(byte[] input) {
        return input;
    }
}
