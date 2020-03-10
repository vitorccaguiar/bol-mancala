package com.bol.api.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.bol.api.constant.MatchStatus;
import com.bol.api.constant.MessageStatus;
import com.bol.api.entity.InputMessage;
import com.bol.api.entity.Match;
import com.bol.api.entity.OutputMessage;
import com.bol.api.entity.Play;
import com.bol.api.entity.User;
import com.bol.api.repository.MatchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

  @Autowired
  private MatchRepository matchRepository;
  @Autowired
  private UserService userService;

  Logger logger = LoggerFactory.getLogger(MatchService.class);

  public static Map<String, String> playerMatch = new HashMap<>();
  public static Map<String, String> playerMachine = new HashMap<>();

  public OutputMessage send(InputMessage message) {
    if (message.getType().equals(MessageStatus.JOIN)) {
      return this.join(message);
    } else if (message.getType().equals(MessageStatus.PLAY)) {

    }
    return new OutputMessage(MessageStatus.ERROR, null);
  }

  public OutputMessage join(InputMessage message) {
    try {
      Optional<User> player = userService.getUserById(message.getPlayerId());
      if (player.isPresent()) {
        Optional<Match> match = this.matchRepository.findById(message.getMatch().getId());
        if (match.isPresent()) {
          match.get().setSecondPlayer(player.get());
          match.get().setStatus(MatchStatus.PLAYING);
          matchRepository.save(match.get());
  
          playerMatch.put(message.getPlayerId(), match.get().getId());
          playerMachine.put(message.getPlayerId(), message.getFingerprint());
          return new OutputMessage(MessageStatus.READY, match.get());
        }
        return new OutputMessage(MessageStatus.ERROR, null);
      }
      return new OutputMessage(MessageStatus.ERROR, null);
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      return new OutputMessage(MessageStatus.ERROR, null);
    }
  }

  public OutputMessage play(InputMessage message) {
    try {
      String matchId = playerMatch.get(message.getPlayerId());
      String playerFingerprint = playerMachine.get(message.getPlayerId());
      if (matchId != null && playerFingerprint.equals(message.getFingerprint())) {
        doPlay(message.getMatch(), message.getPlay());
        if (isEndGame(message.getMatch())) {
          setWinner(message.getMatch());
          return new OutputMessage(MessageStatus.FINISHED, message.getMatch());
        }
        return new OutputMessage(message.getMatch().getStatus(), message.getMatch());
      }
      return new OutputMessage(MessageStatus.ERROR, null);
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      return new OutputMessage(MessageStatus.ERROR, null);
    }
  }

  public Pair<Integer, Integer> doPlay(Match match, Play play) {
    if (play.getPlayerNumber().equals(1)) {
      Integer[] firstPlayerPits = match.getFirstPlayerPits();
      Integer[] secondPlayerPits = match.getSecondPlayerPits();
      Integer numberOfStones = firstPlayerPits[play.getPosition()];

      Integer lastPosition = 0;
      Integer lastPit = 0;
      Integer moves = 0;
      while (!moves.equals(numberOfStones)) {
        for (Integer i = play.getPosition() + 1; i < firstPlayerPits.length; i++) {
          firstPlayerPits[i]++;
          moves++;
          if (moves.equals(numberOfStones)) {
            lastPosition = i;
            lastPit = 1;
          }
        }
        for (int i = 0; i < secondPlayerPits.length; i++) {
          secondPlayerPits[i]++;
          moves++;
          if (moves.equals(numberOfStones)) {
            lastPosition = i;
            lastPit = 2;
          }
        }
      }

      return Pair.of(lastPosition, lastPit);
    }
    return Pair.of(null, null);
  }

  public Boolean isEndGame(Match match) {
    Stream<Integer> firstPlayerPits = Arrays.stream(match.getFirstPlayerPits());
    Stream<Integer> secondPlayerPits = Arrays.stream(match.getSecondPlayerPits());

    return firstPlayerPits.allMatch(n -> n == 0) || secondPlayerPits.allMatch(n -> n == 0);
  }

  public void setWinner(Match match) {
    Integer firstPlayerBigPit = match.getFirstPlayerPits()[6];
    Integer secondPlayerBigPit = match.getSecondPlayerPits()[6];

    if (firstPlayerBigPit > secondPlayerBigPit) {
      match.setWinner(match.getFirstPlayer().getName());
    } else {
      match.setWinner(match.getSecondPlayer().getName());
    }
  }

  public Match getMatchById(String matchId) {
    Optional<Match> result = matchRepository.findById(matchId);
    if (result.isPresent()) {
      return result.get();
    }
    return null;
  }
}