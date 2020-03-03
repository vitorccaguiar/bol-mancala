package com.bol.service;

import java.util.Optional;

import com.bol.entity.User;
import com.bol.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;
  
  public User createUser(User user) {
    try {
      User returnedUser = userRepository.findByName(user.getName());
      if (returnedUser != null) {
        throw new Exception("Name already taken!");
      }
      return userRepository.save(user);
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public void deleteUser(String id) {
    try {
      userRepository.deleteById(id);
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public User getUserByName(String name) {
    try {
      return userRepository.findByName(name);
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public Optional<User> getUserById(String id) {
    try {
      return userRepository.findById(id);
    } catch(Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}