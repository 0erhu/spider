package com.crawler.extRequest;

public interface ExtRequest extends DownloadItemSerialize {

    String getDataCategory();

    void setDataCategory(String dataCategory);

    String getParserKafKaTopic();

    void setParserKafKaTopic(String parserKafKaTopic);

    String getTransparentTransport();

    void setTransparentTransport(String transparentTransport);

}
