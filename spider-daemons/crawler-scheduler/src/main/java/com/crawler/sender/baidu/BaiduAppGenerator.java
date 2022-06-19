package com.crawler.sender.baidu;

import com.crawler.constants.DateCategory;
import com.crawler.extRequest.ExtHttpGet;
import com.crawler.extRequest.ExtRequest;

public class BaiduAppGenerator {

    private static final String INDEX_URL = "https://www.baudu.com";

    public ExtRequest generateRequest() {
        String url = INDEX_URL;
        ExtHttpGet request = new ExtHttpGet(url, DateCategory.BAIDU_INDEX_PAGE);
        request.addHeader("", "");
        return request;
    }

}
