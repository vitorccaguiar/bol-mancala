package com.bol.api.repository;

import com.bol.api.entity.Match;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {

}