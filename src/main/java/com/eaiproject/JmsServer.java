package com.eaiproject;

import com.eaiproject.jms.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static com.eaiproject.ServerMessage.unmarshal;


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

  private ServerResponse handleFetch(String customerId, String requestId){
    ServerResponse response = new ServerResponse();
    response.requestId = requestId;
    response.action = "fetch-response";

    try {
      Optional<Customer> customer = repository.findById(customerId);
      if (customer.isPresent()) {
        System.out.println("Fetched customer : " + customer.get());

        response.data = customer.toString();
        response.message = "Fetch Successful";
        response.statusCode = 200;
      } else {
        System.out.println("Invalid Customer ID : " + customerId);

        response.data = "Fetch Failed: Invalid Customer ID";
        response.message = "Bad Request";
        response.statusCode = 400;
      }
      return response;

    } catch (Exception e) {
      System.out.println("[JMSServer.HandleFetch] Error while fetching customer for id " + customerId + " : " + e.toString());

      response.data = "Fetch Failed";
      response.message = "Internal Server Error";
      response.statusCode = 500;

      return response;
    }
  }

  private ServerResponse handleLogin(String customerId, String requestId){
    ServerResponse response = new ServerResponse();
    response.requestId = requestId;
    response.action = "login-response";

    try {
      Optional<Customer> customer = repository.findById(customerId);
      if (customer.isPresent()) {
        customer.get().Login();
        repository.save(customer.get());

        System.out.println("Logged in customer : " + customer.get());

        response.data = "Access Token: " + customer.get().accessToken;
        response.message = "Login Successful";
        response.statusCode = 200;
      } else {
        System.out.println("Invalid Customer ID : " + customerId);

        response.data = "Login Failed: Invalid Customer ID";
        response.message = "Bad Request";
        response.statusCode = 400;
      }
      return response;

    } catch (Exception e) {
      System.out.println("[JMSServer.HandleFetch] Error while logging in customer for id " + customerId + " : " + e.toString());

      response.data = "Login Failed";
      response.message = "Internal Server Error";
      response.statusCode = 500;

      return response;
    }
  }

  private ServerResponse handleLogout(String customerId, String requestId){
    ServerResponse response = new ServerResponse();
    response.requestId = requestId;
    response.action = "login-response";

    try {
      Optional<Customer> customer = repository.findById(customerId);
      if (customer.isPresent()) {
        customer.get().Logout();
        repository.save(customer.get());

        System.out.println("Logged out customer : " + customer.get());

        response.data = "Logout Successful";
        response.message = "Logout Successful";
        response.statusCode = 200;
      } else {
        System.out.println("Invalid Customer ID : " + customerId);

        response.data = "Logout Failed: Invalid Customer ID";
        response.message = "Bad Request";
        response.statusCode = 400;
      }
      return response;

    } catch (Exception e) {
      System.out.println("[JMSServer.HandleFetch] Error while logging out customer for id " + customerId + " : " + e.toString());

      response.data = "Logout Failed";
      response.message = "Internal Server Error";
      response.statusCode = 500;

      return response;
    }
  }

  private ServerResponse handleAuthenticate(String customerId, String accessToken, String requestId){
    ServerResponse response = new ServerResponse();
    response.requestId = requestId;
    response.action = "authenticate-response";

    try {
      Optional<Customer> customer = repository.findById(customerId);

      if (customer.isPresent()) {
        Boolean result = customer.get().IsAuthenticated(accessToken);
        System.out.printf("Customer ID : %s : Authenticated : %b", customerId, result);
        System.out.println();

        if (result) {
          response.data = "Valid Customer";
          response.message = "Authentication Successful";
          response.statusCode = 200;
        } else {
          response.data = "Unauthorised Customer";
          response.message = "Authentication Failed";
          response.statusCode = 401;
        }

      } else {
        System.out.println("Invalid Customer ID : " + customerId);

        response.data = "Authentication Failed: Invalid Customer ID";
        response.message = "Bad Request";
        response.statusCode = 400;
      }
      return response;

    } catch (Exception e) {
      System.out.println("[JMSServer.HandleFetch] Error while authenticating customer for id " + customerId + " : " + e.toString());

      response.data = "Authentication Failed";
      response.message = "Internal Server Error";
      response.statusCode = 500;

      return response;
    }
  }

  @JmsListener(destination = "server.q")
  public void receive(String message) {
    LOGGER.info("received message on server = '{}'", message);
    latch.countDown();

    ServerResponse response = null;
    ServerMessage msg = unmarshal(message);
    switch (msg.action) {
      case "fetch":
        response = handleFetch(msg.customerId, msg.requestId);
        break;
      case "login":
        response = handleLogin(msg.customerId, msg.requestId);
        break;
      case "logout":
        response = handleLogout(msg.customerId, msg.requestId);
        break;
      case "authenticate":
        response = handleAuthenticate(msg.customerId, msg.accessToken, msg.requestId);
        break;
      default:
        response = new ServerResponse();
        response.requestId = msg.requestId;
        response.action = msg.action;
        response.data = "Invalid Action";
        response.message = "Action not defined";
        response.statusCode = 404;
    }

    sender.send(response.marshal(), "client.q");
  }
}
