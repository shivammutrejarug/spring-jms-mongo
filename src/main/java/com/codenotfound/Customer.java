package com.codenotfound;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.UUID;


public class Customer {
  @Id
  public String id;

  public String firstName;
  public String lastName;
  public Role role;
  public Boolean isLoggedIn;
  public String accessToken;
  public Timestamp createdAt;
  public Timestamp updatedAt;

  enum Role {
    ADMIN,
    USER,
    DEVELOPER
  }

  public Customer() {}

  public Customer(String firstName, String lastName, Role role) {
    this.id = UUID.randomUUID().toString();
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.createdAt = new Timestamp(System.currentTimeMillis());
    this.updatedAt = new Timestamp(System.currentTimeMillis());
    this.isLoggedIn = false;
    this.accessToken = "";
  }

  public void Login() {
    this.isLoggedIn = true;
    this.accessToken = UUID.randomUUID().toString();
    this.updatedAt = new Timestamp(System.currentTimeMillis());
  }

  public void Logout() {
    this.isLoggedIn = false;
    this.updatedAt = new Timestamp(System.currentTimeMillis());
  }

  public boolean validated(String accessToken) {
    if (this.isLoggedIn && this.accessToken == accessToken) {
      return true;
    }
    return false;
  }

  public static Role GetAdminRole() {
    return Role.ADMIN;
  }

  public static Role GetUserRole() {
    return Role.USER;
  }

  public static Role GetDeveloperRole() {
    return Role.DEVELOPER;
  }

  @Override
  public String toString() {
    return String.format(
        "Customer[id=%s, firstName='%s', lastName='%s', isLoggedIn='%b']",
        id, firstName, lastName, isLoggedIn);
  }

}
