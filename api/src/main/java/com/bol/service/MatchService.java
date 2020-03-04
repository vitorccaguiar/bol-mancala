package com.bol.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.bol.constants.MatchStatus;
import com.bol.constants.MessageStatus;
import com.bol.entity.InputMessage;
import com.bol.entity.Match;
import com.bol.entity.OutputMessage;
import com.bol.entity.User;
import com.bol.repository.MatchRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

  @Autowired
  private MatchRepository matchRepository;
  @Autowired
  private UserService userService;

  public static Map<String, String> playerMatch = new HashMap<>();
  public static Map<String, String> playerMachine = new HashMap<>();

  public OutputMessage send(InputMessage message) {
    if (message.getType() == MessageStatus.UPDATE) {
      return this.updateMatch(message);
    }
    return new OutputMessage(MessageStatus.ERROR, null);
  }

  public OutputMessage updateMatch(InputMessage message) {
    try {
      Optional<User> player = userService.getUserById(message.getPlayerId());
      if (player.isPresent()) {
        Match newMatch = new Match();
        newMatch.setFirstPlayer(player.get());
        newMatch.setStatus(MatchStatus.WAITING_PLAYER);
        matchRepository.save(newMatch);

        playerMatch.put(message.getPlayerId(), newMatch.getId());
        playerMachine.put(message.getPlayerId(), message.getFingerprint());
        return new OutputMessage(MessageStatus.NEW, newMatch);
      }
      return new OutputMessage(MessageStatus.ERROR, null);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return new OutputMessage(MessageStatus.ERROR, null);
    }
  }
}