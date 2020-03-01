package com.bol.repository;

import com.bol.entity.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  
  User findByName(String name);
}