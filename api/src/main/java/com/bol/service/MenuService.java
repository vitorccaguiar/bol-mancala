package com.bol.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.bol.constants.MatchStatus;
import com.bol.entity.Match;
import com.bol.entity.User;
import com.bol.repository.MatchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

  @Autowired
  private UserService userService;
  @Autowired
  private MatchRepository matchRepository;

  public Match createMatch(String playerId, String fingerprint) {
    try {
      Optional<User> player = userService.getUserById(playerId);
      if (player.isPresent()) {
        Match newMatch = new Match();
        newMatch.setFirstPlayer(player.get());
        newMatch.setStatus(MatchStatus.WAITING_PLAYER);
        matchRepository.save(newMatch);

        MatchService.playerMatch.put(playerId, newMatch.getId());
        MatchService.playerMachine.put(playerId, fingerprint);
        return newMatch;
      }
      return null;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    }
  }

  public List<Match> getAllMatches() {
    try {
      return this.matchRepository.findAll();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return Collections.emptyList();
    }
  }
}