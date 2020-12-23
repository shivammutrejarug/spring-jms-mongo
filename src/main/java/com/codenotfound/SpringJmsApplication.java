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
    customer.ifPresent(System.out::println);
  }

  @RequestMapping(value = "/login/{id}")
  public void login(@PathVariable String id) {
    Optional<Customer> customer = repository.findById(id);
    if (customer.isPresent()) {
      customer.get().Login();
      repository.save(customer.get());
    }
  }

  @RequestMapping(value = "/logout/{id}")
  public void logout(@PathVariable String id) {
    Optional<Customer> customer = repository.findById(id);
    if (customer.isPresent()) {
      customer.get().Logout();
      repository.save(customer.get());
    }
  }

  @RequestMapping(value = "/authenticate/{id}/{accessToken}")
  public void logout(@PathVariable String id, @PathVariable String accessToken) {
    Optional<Customer> customer = repository.findById(id);
    if (customer.isPresent()) {
      Boolean result = customer.get().IsAuthenticated(accessToken);
      System.out.printf("Authenticated : %b", result);
      System.out.println();
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
