package com.crawler.sender;

public interface ICrawlerSender {

    byte[] generateData(byte[] input);

    void send(byte[] data);
}
