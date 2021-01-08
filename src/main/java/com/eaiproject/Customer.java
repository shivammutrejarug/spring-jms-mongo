package com.eaiproject;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;


public class Customer {
  @Id
  public String id;

  public String firstName;
  public String lastName;
  public Role role;
  public Boolean isLoggedIn;
  public String accessToken;
  public Date createdAt;
  public Date updatedAt;

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
    this.createdAt = new Date();
    this.updatedAt = new Date();
    this.isLoggedIn = false;
    this.accessToken = "";
  }

  public void Login() {
    this.isLoggedIn = true;
    this.accessToken = UUID.randomUUID().toString();
    this.updatedAt = new Date();
  }

  public void Logout() {
    this.isLoggedIn = false;
    this.accessToken = "";
    this.updatedAt = new Date();
  }

  public boolean IsAuthenticated(String accessToken) {
    if (this.isLoggedIn && this.accessToken.equals(accessToken)) {
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
        "Customer[id=%s, firstName='%s', lastName='%s', role='%s', isLoggedIn='%b']",
        id, firstName, lastName, role, isLoggedIn);
  }

}
