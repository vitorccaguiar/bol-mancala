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
import com.bol.api.entity.User;
import com.bol.api.repository.MatchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
      return join(message);
    } else if (message.getType().equals(MessageStatus.PLAY)) {
      return play(message);
    }
    logger.error(MessageStatus.ERROR + "{}", "Mensagem");
    return new OutputMessage(MessageStatus.ERROR, null);
  }

  public OutputMessage join(InputMessage message) {
    try {
      Optional<User> player = userService.getUserById(message.getPlayerId());
      if (player.isPresent()) {
        Optional<Match> match = this.matchRepository.findById(message.getMatchId());
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
      if (isPlayerTurn(message.getPlayerId())) {
        if (isPlayerMatch(message.getPlayerId(), message.getFingerprint(), message.getMatchId())) {
          Match match = matchRepository.findById(message.getMatchId()).get();
          Integer lastPosition = doPlay(match.getId(), message.getPlayPosition());
          if (isEndGame(match)) {
            match.setStatus(MatchStatus.FINISHED);
            setWinner(match);
            matchRepository.save(match);
            return new OutputMessage(MessageStatus.FINISHED, match);
          }
          handleLastStoneEmptyPit(match, lastPosition);
          if (!isLastStoneBigPit(lastPosition)) {
            changePlayerTurn(match);
          }
          matchRepository.save(match);
          logger.info(MessageStatus.PLAYING + "{}", match);
          return new OutputMessage(MessageStatus.PLAYING, match);
        }
        logger.error(MessageStatus.ERROR + "{}", "Mensagem");
        return new OutputMessage(MessageStatus.ERROR, null);
      }
      logger.error(MessageStatus.ERROR + "{}", "Mensagem");
      return new OutputMessage(MessageStatus.ERROR, null);
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      return new OutputMessage(MessageStatus.ERROR, null);
    }
  }

  public Boolean isPlayerTurn(String playerId) {
    String matchId = playerMatch.get(playerId);
    if (matchId != null) {
      String playerTurnId = matchRepository.findById(matchId).get().getPlayerTurn().getId();
      if (playerId.equals(playerTurnId)) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  public Boolean isPlayerMatch(String playerId, String fingerprint, String matchId) {
    String realMatchId = playerMatch.get(playerId);
    String realFingerprint = playerMachine.get(playerId);
    if (realMatchId.equals(matchId) && realFingerprint.equals(fingerprint)) {
      return true;
    } else {
      return false;
    }
  }

  public void handleLastStoneEmptyPit(Match match, Integer lastPosition) {
    String playerTurnId = match.getPlayerTurn().getId();
    Integer[] firstPlayerPits = match.getFirstPlayerPits();
    Integer[] secondPlayerPits = match.getSecondPlayerPits();

    if (playerTurnId.equals(match.getFirstPlayer().getId())) {
      if (firstPlayerPits[lastPosition].equals(0)) {
        firstPlayerPits[6] += firstPlayerPits[lastPosition] + secondPlayerPits[lastPosition];
      }
    } else if (playerTurnId.equals(match.getSecondPlayer().getId())) {
      if (match.getSecondPlayerPits()[lastPosition].equals(0)) {
        secondPlayerPits[6] += firstPlayerPits[lastPosition] + secondPlayerPits[lastPosition];
      }
    }
    firstPlayerPits[lastPosition] = 0;
    secondPlayerPits[lastPosition] = 0;
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);
  }

  public Boolean isLastStoneBigPit(Integer lastPosition) {
    return lastPosition.equals(6);
  }

  public void changePlayerTurn(Match match) {
    if (match.getPlayerTurn().getId().equals(match.getFirstPlayer().getId())) {
      match.setPlayerTurn(match.getSecondPlayer());
    } else {
      match.setPlayerTurn(match.getFirstPlayer());
    }
  }

  public Integer doPlay(String matchId, Integer playPosition) {
    Optional<Match> result = matchRepository.findById(matchId);
    if (result.isPresent()) {
      Match match = result.get();
      String firstPlayerId = match.getFirstPlayer().getId();
      String secondPlayerId = match.getSecondPlayer().getId();
      String playerTurnId = match.getPlayerTurn().getId();

      if (playerTurnId.equals(firstPlayerId)) {
        return firstPlayerPlay(match, playPosition);
      } else if (playerTurnId.equals(secondPlayerId)) {
        return secondPlayerPlay(match, playPosition);
      }
    }

    return null;
  }

  public Integer firstPlayerPlay(Match match, Integer playPosition) {
    Integer[] firstPlayerPits = match.getFirstPlayerPits();
    Integer[] secondPlayerPits = match.getSecondPlayerPits();
    Integer numberOfStones = firstPlayerPits[playPosition];
    firstPlayerPits[playPosition] = 0;

    Integer lastPosition = 0;
    Integer moves = 0;
    while (!moves.equals(numberOfStones)) {
      Integer index = moves == 0 ? playPosition + 1 : 0;
      for (Integer i = index; i < firstPlayerPits.length; i++) {
        firstPlayerPits[i]++;
        moves++;
        if (moves.equals(numberOfStones)) {
          lastPosition = i;
        }
      }
      for (int i = 0; i < secondPlayerPits.length; i++) {
        secondPlayerPits[i]++;
        moves++;
        if (moves.equals(numberOfStones)) {
          lastPosition = i;
        }
      }
    }

    return lastPosition;
  }

  public Integer secondPlayerPlay(Match match, Integer playPosition) {
    Integer[] firstPlayerPits = match.getFirstPlayerPits();
    Integer[] secondPlayerPits = match.getSecondPlayerPits();
    Integer numberOfStones = secondPlayerPits[playPosition];
    secondPlayerPits[playPosition] = 0;

    Integer lastPosition = 0;
    Integer moves = 0;
    while (!moves.equals(numberOfStones)) {
      Integer index = moves == 0 ? playPosition + 1 : 0;
      for (Integer i = index; i < secondPlayerPits.length; i++) {
        secondPlayerPits[i]++;
        moves++;
        if (moves.equals(numberOfStones)) {
          lastPosition = i;
        }
      }
      for (int i = 0; i < firstPlayerPits.length; i++) {
        firstPlayerPits[i]++;
        moves++;
        if (moves.equals(numberOfStones)) {
          lastPosition = i;
        }
      }
    }

    return lastPosition;
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