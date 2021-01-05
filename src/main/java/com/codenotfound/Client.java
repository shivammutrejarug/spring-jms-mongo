package com.codenotfound;

import com.codenotfound.jms.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import java.util.concurrent.CountDownLatch;

public class Client {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private Sender sender;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(JmsServer.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @JmsListener(destination = "client.q")
    public void receive(String message) {
        LOGGER.info("received message='{}'", message);
        latch.countDown();
    }
}
