package com.crawler.extRequest;

import com.hu.protobuf.crawer.GeneralCrawer;

public interface DownloadItemSerialize {

    GeneralCrawer.DownloadItem toDownloadItem();
}
