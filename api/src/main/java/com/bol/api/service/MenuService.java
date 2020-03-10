package com.bol.api.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.bol.api.constant.MatchStatus;
import com.bol.api.entity.Match;
import com.bol.api.entity.User;
import com.bol.api.repository.MatchRepository;

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
        Match newMatch = getInitialMatchObject(player.get(), fingerprint);
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

  public Match getInitialMatchObject(User firstPlayer, String fingerprint) {
    Match newMatch = new Match();
    newMatch.setFirstPlayer(firstPlayer);
    newMatch.setStatus(MatchStatus.WAITING_PLAYER);
    Integer firstPlayerPits[] = new Integer[] { 6, 6, 6, 6, 6, 6, 0 };
    Integer secondPlayerPits[] = new Integer[] { 6, 6, 6, 6, 6, 6, 0 };
    newMatch.setFirstPlayerPits(firstPlayerPits);
    newMatch.setSecondPlayerPits(secondPlayerPits);
    newMatch.setPlayerTurn(firstPlayer);

    return newMatch;
  }
}