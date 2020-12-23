package com.codenotfound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
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

  @RequestMapping(value = "/available")
  public void available() {
    sender.send("Hello from controller!");
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringJmsApplication.class, args);
  }
}
