package com.crawler.schduler;

import com.crawler.extRequest.ExtRequest;
import com.crawler.sender.ICrawlerSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;

public class FileBaseScheduler extends AbstractScheduler {

    private static final Logger logger = LoggerFactory.getLogger(FileBaseScheduler.class);
    private String inputFilePath;
    private Method generatorMethod;
    private FileBaseGenerator generator;

    public FileBaseScheduler(long sendSpeedPerDay, String inputFilePath, ICrawlerSender sender, FileBaseGenerator generator, Method generatorMethod) {
        super(sendSpeedPerDay, sender);
        this.inputFilePath = inputFilePath;
        this.generator = generator;
        this.generatorMethod = generatorMethod;
    }

    public FileBaseScheduler(long sendSpeedPerDay, String inputFilePath, ICrawlerSender sender) {
        super(sendSpeedPerDay, sender);
        this.inputFilePath = inputFilePath;
    }

    @Override
    public void scheduler() {
        try (FileReader fr = new FileReader(inputFilePath);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            for(line = br.readLine(); line != null; line = br.readLine()) {
                logger.info("input line={}", line);
                try {
                    if (generator != null) {
                        ExtRequest request = (ExtRequest) this.generatorMethod.invoke(generator, line);
                        if (request == null) {
                            continue;
                        }
                        byte[] toBeSentData = request.toDownloadItem().toByteArray();
                        if (toBeSentData != null) {
                            sendData(toBeSentData);
                            loadControl();
                        }
                    }
                } catch (Exception e) {
                    logger.info("", e);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) throws Exception {
        final int argsLen = 6;
        if (args.length != argsLen) {
            System.exit(0);
        }
        FileBaseScheduler scheduler;
        long sendSpeed = Integer.parseInt(args[0]);
        String inputPath = args[1];
        String senderClassName = args[2];
        String generatorClassName = args[3];
        String generatormethodName = args[4];
        logger.info("sendSpeed={}, inputPath={}, senderClassName={}, generatorClassName={}, generatorMethodName={}", sendSpeed, inputPath, senderClassName, generatorClassName, generatormethodName);
        ICrawlerSender sender = (ICrawlerSender) Class.forName(senderClassName).newInstance();
        FileBaseGenerator generator = (FileBaseGenerator) Class.forName(generatorClassName).newInstance();
        Method method = generator.getClass().getDeclaredMethod(generatormethodName, String.class);
        scheduler = new FileBaseScheduler(sendSpeed, inputPath, sender, generator, method);
        scheduler.scheduler();
        Thread.sleep(10000);
        logger.info("invoke System.exit(0)");
        System.exit(0);
    }
}
