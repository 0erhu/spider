package com.crawler.sender;

import com.crawler.extRequest.ExtRequest;

public class RequestSender extends DownloadItemSender {

    public void send(ExtRequest request) {
        super.send(request.toDownloadItem());
    }

}
