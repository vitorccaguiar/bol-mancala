package com.bol.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bol.api.entity.Match;
import com.bol.api.service.MatchService;

import org.junit.jupiter.api.Test;

class MatchServiceTest {

  @Test
  void whenJoinMessageNonExistentPlayer_shouldReturnErrorMessage() {

  }

  @Test
  void whenJoinMessageNonExistentMatch_shouldReturnErrorMessage() {

  }

  @Test
  void whenJoinMessageDifferentThanWaitingPlayerStatus_shouldReturnErrorMessage() {

  }

  @Test
  void whenJoinMessageException_shouldReturnErrorMessage() {

  }

  @Test
  void whenJoinMessage_shouldReturnReadyMessage() {

  }

  @Test
  void whenPlayMessageNotYourTurn_shouldReturnErrorMessage() {

  }

  @Test
  void whenPlayMessageMatchNotGoing_shouldReturnErrorMessage() {

  }

  @Test
  void whenPlayMessageNotPlayerMatch_shouldReturnErrorMessage() {

  }

  @Test
  void whenPlayMessageException_shouldReturnErrorMessage() {

  }

  @Test
  void whenPlayMessage_shouldReturnPlayingMessage() {

  }

  @Test
  void whenIsPlayerTurn_shouldReturnTrue() {

  }

  @Test
  void whenIsNotPlayerTurn_shouldReturnFalse() {

  }

  @Test
  void whenIsMatchOnGoing_shouldReturnTrue() {

  }

  @Test
  void whenIsNotMatchOnGoing_shouldReturnFalse() {

  }

  @Test
  void whenIsPlayerMatch_shouldReturnTrue() {

  }

  @Test
  void whenMatchIdIsNotEqual_shouldReturnFalse() {

  }

  @Test
  void whenFingerprintIsNotEqual_shouldReturnFalse() {

  }

  //handleLastStoneEmptyPit
  //firstPlayerPlay
  //secondPlayerPlay

  @Test
  void whenFirstPlayerPitsAreAllEmpty_shouldEndGame() {
    MatchService matchService = new MatchService();
    Match match = new Match();
    Integer[] firstPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertTrue(matchService.isEndGame(match));
  }

  @Test
  void whenSecondPlayerPitsAreAllEmpty_shouldEndGame() {
    MatchService matchService = new MatchService();
    Match match = new Match();
    Integer[] firstPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertTrue(matchService.isEndGame(match));
  }

  @Test
  void whenBothPitsAreAllEmpty_shouldEndGame() {
    MatchService matchService = new MatchService();
    Match match = new Match();
    Integer[] firstPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertTrue(matchService.isEndGame(match));
  }

  @Test
  void whenBothPitsAreNotEmpty_shouldNotEndGame() {
    MatchService matchService = new MatchService();
    Match match = new Match();
    Integer[] firstPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertFalse(matchService.isEndGame(match));
  }
}