package com.codenotfound;

import com.codenotfound.jms.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static com.codenotfound.ServerMessage.unmarshal;


@SpringBootApplication
public class JmsServer {
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

  private String handleFetch(FetchData data, String requestId){
    ServerResponse response = new ServerResponse();
    response.requestId = requestId;
    response.action = "fetch-response";

    try {
      Optional<Customer> customer = repository.findById(data.Id);
      if (customer.isPresent()) {
        System.out.println("Fetched customer : " + customer.get());

        response.data = customer.toString();
        response.message = "Fetch Successful";
        response.statusCode = 200;
      } else {
        response.data = "Fetch Failed: Invalid Customer ID";
        response.message = "Bad Request";
        response.statusCode = 400;
      }
      return response.marshal();

    } catch (Exception e) {
      System.out.println("[JMSServer.HandleFetch] Error while fetching customer for id " + data.Id + " : " + e.toString());

      response.data = "Fetch Failed";
      response.message = "Internal Server Error";
      response.statusCode = 500;

      return response.marshal();
    }
  }

  @JmsListener(destination = "server.q")
  public void receive(String message) {
    LOGGER.info("received message on server = '{}'", message);
    latch.countDown();

    ServerMessage msg = unmarshal(message);
    switch (msg.action) {
      case "fetch":
        FetchData fetchData = FetchData.unmarshal(msg.data);
        handleFetch(fetchData, msg.requestId);
    }
  }
}
