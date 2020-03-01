package com.bol.repository;

import com.bol.entity.Match;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {

}