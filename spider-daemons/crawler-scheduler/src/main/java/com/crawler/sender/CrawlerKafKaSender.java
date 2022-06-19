package com.crawler.sender;

import com.crawler.constants.KafkaTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public abstract class CrawlerKafKaSender implements ICrawlerSender {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerKafKaSender.class);
    private static String kafkaTopic;
    private static KafkaProducer<byte[], byte[]> producer;
    private long sendCount = 0;

    static {
        kafkaTopic = KafkaTopic.LT_ONLINE_DP_CRAWLER_INPUT;
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.80.100:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(properties);
    }

    @Override
    public void send(byte[] data) {
        logger.debug("send {}th data to {}", sendCount++, kafkaTopic);
        producer.send(new ProducerRecord<>(kafkaTopic, data));
    }
}
