package com.bol.api.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.bol.api.constant.ErrorMessage;
import com.bol.api.constant.MatchStatus;
import com.bol.api.constant.MessageStatus;
import com.bol.api.constant.SuccessMessage;
import com.bol.api.entity.InputMessage;
import com.bol.api.entity.Match;
import com.bol.api.entity.OutputMessage;
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
      return join(message);
    } else if (message.getType().equals(MessageStatus.PLAY)) {
      return play(message);
    } else if (message.getType().equals(MessageStatus.LEAVE)) {
      return leave(message);
    }
    logger.error(MessageStatus.ERROR + " {}", "No message type found");
    return new OutputMessage(MessageStatus.ERROR, null, "Invalid action");
  }

  public OutputMessage join(InputMessage message) {
    try {
      Optional<User> player = userService.getUserById(message.getPlayerId());
      if (player.isPresent()) {
        Optional<Match> match = this.matchRepository.findById(message.getMatchId());
        if (match.isPresent() && match.get().getStatus().equals(MatchStatus.WAITING_PLAYER)) {
          match.get().setSecondPlayer(player.get());
          match.get().setStatus(MatchStatus.PLAYING);
          matchRepository.save(match.get());
  
          playerMatch.put(message.getPlayerId(), match.get().getId());
          playerMachine.put(message.getPlayerId(), message.getFingerprint());
          logger.info(MessageStatus.PLAYING + " {}", SuccessMessage.JOINED);
          return new OutputMessage(MessageStatus.READY, match.get(), SuccessMessage.JOINED);
        }
        logger.error(MessageStatus.ERROR + " {}", ErrorMessage.INVALID_MATCH);
        return new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.INVALID_MATCH);
      }
      logger.error(MessageStatus.ERROR + " {}", ErrorMessage.PLAYER_NOT_FOUND);
      return new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.PLAYER_NOT_FOUND);
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      return new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.UNEXPECTED_JOIN);
    }
  }

  public OutputMessage leave(InputMessage message) {
    try {
      if (isPlayerMatch(message.getPlayerId(), message.getFingerprint(), message.getMatchId())) {
        Match match = matchRepository.findById(message.getMatchId()).get();
        match.setStatus(MatchStatus.PLAYER_LEFT);
        if (match.getFirstPlayer().getId().equals(message.getPlayerId())) {
          match.setWinner(match.getSecondPlayer().getName());
        } else {
          match.setWinner(match.getFirstPlayer().getName());
        }
        matchRepository.save(match);

        logger.info(MessageStatus.FINISHED + " {}", SuccessMessage.PLAYER_LEFT);
        return new OutputMessage(MessageStatus.FINISHED, match, SuccessMessage.PLAYER_LEFT);
      }
      logger.error(MessageStatus.ERROR + " {}", ErrorMessage.INVALID_MATCH);
      return new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.INVALID_MATCH);
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      return new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.UNEXPECTED_LEAVE);
    }
  }

  public OutputMessage play(InputMessage message) {
    try {
      if (isPlayerTurn(message.getPlayerId()) && isMatchOnGoing(message.getMatchId())) {
        if (isPlayerMatch(message.getPlayerId(), message.getFingerprint(), message.getMatchId())) {
          Match match = matchRepository.findById(message.getMatchId()).get();
          Pair<Integer, Integer> finishedPositionAndPit = doPlay(match, message.getPlayPosition());
          if (isEndGame(match)) {
            match.setStatus(MatchStatus.FINISHED);
            setWinner(match);
            matchRepository.save(match);
            cleanCache(match);
            logger.info(MessageStatus.FINISHED + " {}", "The match is over");
            return new OutputMessage(MessageStatus.FINISHED, match, "The match is over");
          }
          if (!isLastStoneBigPit(finishedPositionAndPit.getFirst())) {
            handleLastStoneEmptyPit(match, finishedPositionAndPit);
            changePlayerTurn(match);
          }
          matchRepository.save(match);
          logger.info(MessageStatus.PLAYING + " {}", match);
          return new OutputMessage(MessageStatus.PLAYING, match, "Next turn");
        }
        logger.error(MessageStatus.ERROR + " {}", ErrorMessage.INVALID_PLAYER);
        return new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.INVALID_PLAYER);
      }
      String playerName = userService.getUserById(message.getPlayerId()).get().getName();
      logger.error(MessageStatus.ERROR + " {}", "Is not " + playerName + " turn");
      return new OutputMessage(MessageStatus.ERROR, null, "Is not " + playerName + "'s turn");
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      return new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.UNEXPECTED_PLAY);
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

  public Boolean isMatchOnGoing(String matchId) {
    Match match = matchRepository.findById(matchId).get();
    return match.getStatus().equals(MatchStatus.PLAYING);
  }

  public Boolean isPlayerMatch(String playerId, String fingerprint, String matchId) {
    String realMatchId = playerMatch.get(playerId);
    String realFingerprint = playerMachine.get(playerId);
    if (matchId.equals(realMatchId) && fingerprint.equals(realFingerprint)) {
      return true;
    } else {
      return false;
    }
  }

  public void handleLastStoneEmptyPit(Match match, Pair<Integer, Integer> finishedPositionAndPit) {
    String playerTurnId = match.getPlayerTurn().getId();
    Integer[] firstPlayerPits = match.getFirstPlayerPits();
    Integer[] secondPlayerPits = match.getSecondPlayerPits();
    Integer finishedPosition = finishedPositionAndPit.getFirst();
    Integer finishedPit = finishedPositionAndPit.getSecond();

    if (playerTurnId.equals(match.getFirstPlayer().getId())) {
      if (finishedPit.equals(1) && firstPlayerPits[finishedPosition].equals(1)) {
        firstPlayerPits[6] += firstPlayerPits[finishedPosition] + secondPlayerPits[5 - finishedPosition];
        firstPlayerPits[finishedPosition] = 0;
        secondPlayerPits[5 - finishedPosition] = 0;
        match.setFirstPlayerPits(firstPlayerPits);
        match.setSecondPlayerPits(secondPlayerPits);
      }
    } else if (playerTurnId.equals(match.getSecondPlayer().getId())) {
      if (finishedPit.equals(2) && match.getSecondPlayerPits()[finishedPosition].equals(1)) {
        secondPlayerPits[6] += secondPlayerPits[finishedPosition] + firstPlayerPits[5 - finishedPosition];
        secondPlayerPits[finishedPosition] = 0;
        firstPlayerPits[5 - finishedPosition] = 0;
        match.setFirstPlayerPits(firstPlayerPits);
        match.setSecondPlayerPits(secondPlayerPits);
      }
    }
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

  public void cleanCache(Match match) {
    playerMatch.remove(match.getFirstPlayer().getId());
    playerMatch.remove(match.getSecondPlayer().getId());
    playerMachine.remove(match.getFirstPlayer().getId());
    playerMachine.remove(match.getSecondPlayer().getId());
  }

  public Pair<Integer, Integer> doPlay(Match match, Integer playPosition) {
    String firstPlayerId = match.getFirstPlayer().getId();
    String secondPlayerId = match.getSecondPlayer().getId();
    String playerTurnId = match.getPlayerTurn().getId();

    if (playerTurnId.equals(firstPlayerId)) {
      return moveStones(match.getFirstPlayerPits(), match.getSecondPlayerPits(), playPosition, true);
    } else if (playerTurnId.equals(secondPlayerId)) {
      return moveStones(match.getSecondPlayerPits(), match.getFirstPlayerPits(), playPosition, false);
    }

    return Pair.of(null, null);
  }

  public Pair<Integer, Integer> moveStones(Integer[] firstPits, Integer[] secondPits, Integer playPosition, Boolean firstPlayer) {
    Integer numberOfStones = firstPits[playPosition];
    firstPits[playPosition] = 0;

    Integer finishedPosition = 0;
    Integer finishedPit = -1;
    Integer moves = 0;
    while (!moves.equals(numberOfStones)) {
      Integer index = moves == 0 ? playPosition + 1 : 0;
      for (Integer i = index; i < firstPits.length; i++) {
        firstPits[i]++;
        moves++;
        if (moves.equals(numberOfStones)) {
          finishedPosition = i;
          finishedPit = firstPlayer.equals(true) ? 1 : 2;
          return Pair.of(finishedPosition, finishedPit);
        }
      }
      for (int i = 0; i < secondPits.length - 1; i++) {
        secondPits[i]++;
        moves++;
        if (moves.equals(numberOfStones)) {
          finishedPosition = i;
          finishedPit = firstPlayer.equals(true) ? 2 : 1;
          return Pair.of(finishedPosition, finishedPit);
        }
      }
    }

    return Pair.of(finishedPosition, finishedPit);
  }

  public Boolean isEndGame(Match match) {
    Stream<Integer> firstPlayerPits = Arrays.stream(match.getFirstPlayerPits());
    Stream<Integer> secondPlayerPits = Arrays.stream(match.getSecondPlayerPits());

    return firstPlayerPits.limit(6).allMatch(n -> n == 0) ||
      secondPlayerPits.limit(6).allMatch(n -> n == 0);
  }

  public void setWinner(Match match) {
    Integer firstPlayerBigPit = match.getFirstPlayerPits()[6];
    Integer secondPlayerBigPit = match.getSecondPlayerPits()[6];

    if (firstPlayerBigPit > secondPlayerBigPit) {
      match.setWinner(match.getFirstPlayer().getName());
    } else if (secondPlayerBigPit > firstPlayerBigPit) {
      match.setWinner(match.getSecondPlayer().getName());
    } else {
      match.setTie(true);
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