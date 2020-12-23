package com.codenotfound;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codenotfound.jms.Sender;

@SpringBootApplication
@RestController
public class SpringJmsApplication {

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
  public void fetch(@PathVariable String id) {
    Optional<Customer> customer = repository.findById(id);
    try {
      System.out.println(customer.get());
    } catch (NoSuchElementException e) {
      System.out.println("Exception : "+ e);
    }
  }

  @RequestMapping(value = "/login/{id}")
  public void login(@PathVariable String id) {
    Optional<Customer> customer = repository.findById(id);
    try {
      customer.get().Login();
      repository.save(customer.get());
    } catch (NoSuchElementException e) {
      System.out.println("Exception : "+ e);
    }
  }

  @RequestMapping(value = "/logout/{id}")
  public void logout(@PathVariable String id) {
    Optional<Customer> customer = repository.findById(id);
    try {
      customer.get().Logout();
      repository.save(customer.get());
    } catch (NoSuchElementException e) {
      System.out.println("Exception : "+ e);
    }
  }

  @RequestMapping(value = "/authenticate/{id}/{accessToken}")
  public void logout(@PathVariable String id, @PathVariable String accessToken) {
    Optional<Customer> customer = repository.findById(id);
    try {
      Boolean result = customer.get().IsAuthenticated(accessToken);
      System.out.printf("Authenticated : %b", result);
      System.out.println();
    } catch (NoSuchElementException e) {
      System.out.println("Exception : "+ e);
    }
  }

  @RequestMapping(value = "/available")
  public void available() {
    sender.send("Hello from controller!");
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringJmsApplication.class, args);
  }
}
