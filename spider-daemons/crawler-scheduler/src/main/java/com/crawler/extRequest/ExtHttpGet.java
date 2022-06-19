package com.crawler.extRequest;

import com.hu.protobuf.crawer.GeneralCrawer;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ExtHttpGet extends HttpGet implements ExtRequest {

    private static final Logger logger = LoggerFactory.getLogger(ExtHttpGet.class);
    private String dataCategory;
    private String parserKafkaTopic;
    private String transparentTransport = "{}";
    private String uri;
    private boolean isUseProxy = false;
    private GeneralCrawer.Proxy proxy = null;
    private Map<String, String> headers = new HashMap<>();


    public void setProxy(String host, int port) {
        GeneralCrawer.Proxy.Builder builder = GeneralCrawer.Proxy.newBuilder();
        builder.setHost(host);
        builder.setPort(port);
        proxy = builder.build();
    }

    public ExtHttpGet(String url, String dataCategory) {
        super(url);
        setDataCategory(dataCategory);
    }

    @Override
    public GeneralCrawer.DownloadItem toDownloadItem() {
        GeneralCrawer.DownloadItem.Builder downloadItemBuilder = GeneralCrawer.DownloadItem.newBuilder();
        GeneralCrawer.HttpRequestConfig.Builder requestConfigBuilder = GeneralCrawer.HttpRequestConfig.newBuilder();

        downloadItemBuilder.setUrl(String.valueOf(this.getURI()));
        requestConfigBuilder.setMethod(GeneralCrawer.Method.GET);

        if (isUseProxy) {
            requestConfigBuilder.setNotUseProxy(true);
        }

        if (proxy != null) {
            requestConfigBuilder.setProxy(proxy);
        }

        Header[] allHeaders = this.getAllHeaders();
        boolean hasUserAgent = false;
        for (Header header : allHeaders) {
            requestConfigBuilder.putHeaders(header.getName(), header.getValue());
            if (header.getName().toLowerCase().contains("user-agent")) {
                hasUserAgent = true;
            }
        }

        if (!hasUserAgent) {
            requestConfigBuilder.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        }

        downloadItemBuilder.setDataCategory(dataCategory).setTransparentTransport(transparentTransport).setSendTs(System.currentTimeMillis());
        downloadItemBuilder.setHttpRequestConfig(requestConfigBuilder.build());
        logger.debug("downloadItemBuilder={}", downloadItemBuilder);

        return downloadItemBuilder.build();
    }

    @Override
    public String getDataCategory() {
        return this.dataCategory;
    }

    @Override
    public void setDataCategory(String dataCategory) {
        this.dataCategory = dataCategory;
    }

    @Override
    public String getParserKafKaTopic() {
        return parserKafkaTopic;
    }

    @Override
    public void setParserKafKaTopic(String parserKafKaTopic) {
        this.parserKafkaTopic = parserKafKaTopic;
    }

    @Override
    public String getTransparentTransport() {
        return transparentTransport;
    }

    @Override
    public void setTransparentTransport(String transparentTransport) {
        this.transparentTransport = transparentTransport;
    }
}
