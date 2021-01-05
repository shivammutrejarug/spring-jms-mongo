package com.codenotfound;

import com.codenotfound.jms.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@SpringBootApplication
@RestController
public class SpringJmsApplication {

  private static final Logger logger = LogManager.getLogger(SpringJmsApplication.class);
  @Autowired
  private CustomerRepository repository;

  @Autowired
  private Sender sender;

  @RequestMapping(value = "/save")
  public void save() {
    repository.deleteAll();

    // save a couple of customers
    repository.save(new Customer("Alice", "Babu", Customer.GetAdminRole()));
    repository.save(new Customer("Bob", "Nath", Customer.GetUserRole()));
  }

  @RequestMapping(value = "/fetch/{id}")
  public ResponseEntity<String> fetch(@PathVariable String id) {
    try {
      Optional<Customer> customer = repository.findById(id);
      if (customer.isPresent()) {
        System.out.println(customer.get());
        return new ResponseEntity<>(customer.toString(), HttpStatus.OK);
      }
      return new ResponseEntity<>("Invalid Customer ID",HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/login/{id}")
  public ResponseEntity<String> login(@PathVariable String id) {
    try {
      Optional<Customer> customer = repository.findById(id);
      if (customer.isPresent()) {
        customer.get().Login();
        repository.save(customer.get());
        return new ResponseEntity<>("Logged in Successfully. Access_token: " + customer.get().accessToken, HttpStatus.OK);
      }
      return new ResponseEntity<>("Invalid Customer ID", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/logout/{id}")
  public ResponseEntity<String> logout(@PathVariable String id) {
    try {
      Optional<Customer> customer = repository.findById(id);
      if (customer.isPresent()) {
        customer.get().Logout();
        repository.save(customer.get());
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
      }
      return new ResponseEntity<>("Invalid Customer ID", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/authenticate/{id}/{accessToken}")
  public ResponseEntity<String> authenticate(@PathVariable String id, @PathVariable String accessToken) {
    try {
      Optional<Customer> customer = repository.findById(id);
      if (customer.isPresent()) {
        Boolean result = customer.get().IsAuthenticated(accessToken);
        System.out.printf("Authenticated : %b", result);
        System.out.println();

        if (result) {
          return new ResponseEntity<>("Valid Customer", HttpStatus.OK);
        } else {
          return new ResponseEntity<>("Unauthorised Customer", HttpStatus.UNAUTHORIZED);
        }
      }
      return new ResponseEntity<>("Invalid Customer ID", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      return new ResponseEntity<>("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/available")
  public void available() {
    logger.error("Error log");
    // sender.send("Hello from controller!", "helloworld.q");
  }

  @RequestMapping(value = "/jms/fetch/{id}")
  public void jmsFetch(@PathVariable String id) {
    String msg = String.format("{\"requestMessage\": {\"requestId\": \"1\",\"action\": \"fetch\",\"customerId\": \"%s\"}}", id);
    sender.send(msg, "server.q");
  }

  @RequestMapping(value = "/jms/login/{id}")
  public void jmsLogin(@PathVariable String id) {
    String msg = String.format("{\"requestMessage\": {\"requestId\": \"1\",\"action\": \"login\",\"customerId\": \"%s\"}}", id);
    sender.send(msg, "server.q");
  }

  @RequestMapping(value = "/jms/logout/{id}")
  public void jmsLogout(@PathVariable String id) {
    String msg = String.format("{\"requestMessage\": {\"requestId\": \"1\",\"action\": \"logout\",\"customerId\": \"%s\"}}", id);
    sender.send(msg, "server.q");
  }

  @RequestMapping(value = "/jms/authenticate/{id}/{accessToken}")
  public void jmsAuthenticate(@PathVariable String id, @PathVariable String accessToken) {
    String msg = String.format("{\"requestMessage\": {\"requestId\": \"1\",\"action\": \"authenticate\",\"customerId\": \"%s\",\"accessToken\": \"%s\"}}", id, accessToken);
    sender.send(msg, "server.q");
  }

  @RequestMapping(value = "/jms/incorrect")
  public void jmsIncorrect() {
    String msg = "{\"requestMessage\": {\"requestId\": \"1\",\"action\": \"random\"}}";
    logger.error(msg);
    // sender.send(msg, "server.q");
  }

  public static void main(String[] args) {
    System.setProperty("javax.xml.bind.context.factory","org.eclipse.persistence.jaxb.JAXBContextFactory");
    SpringApplication.run(SpringJmsApplication.class, args);
  }
}
