package com.bol.service;

import java.util.Collections;
import java.util.List;

import com.bol.entity.Match;
import com.bol.repository.MatchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

  @Autowired
  private MatchRepository matchRepository;

  public Match saveMatch(Match match) {
    try {
      return matchRepository.save(match);
    } catch(Exception ex) {
      System.out.println(ex.getMessage());
    }
    return null;
  }

  public List<Match> getAllMatches() {
    try {
      return matchRepository.findAll();
    } catch(Exception ex) {
      System.out.println(ex.getMessage());
    }
    return Collections.emptyList();
  }
}