package com.bol.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bol.api.entity.User;
import com.bol.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
  }
  
}