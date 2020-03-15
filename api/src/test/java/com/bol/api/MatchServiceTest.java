package com.bol.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.bol.api.constant.ErrorMessage;
import com.bol.api.constant.MessageStatus;
import com.bol.api.entity.InputMessage;
import com.bol.api.entity.Match;
import com.bol.api.entity.OutputMessage;
import com.bol.api.service.MatchService;
import com.bol.api.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MatchServiceTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private MatchService matchService;

  @Test
  void whenJoinMessageNonExistentPlayer_shouldReturnErrorMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.JOIN);
    message.setPlayerId("");

    matchService.setUserService(userService);
    when(userService.getUserById("")).thenReturn(Optional.empty());
    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.PLAYER_NOT_FOUND);
    assertEquals(outputMessage, matchService.join(message));
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
  //moveStones

  @Test
  void whenFirstPlayerPitsAreAllEmpty_shouldEndGame() {
    Match match = new Match();
    Integer[] firstPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertTrue(matchService.isEndGame(match));
  }

  @Test
  void whenSecondPlayerPitsAreAllEmpty_shouldEndGame() {
    Match match = new Match();
    Integer[] firstPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertTrue(matchService.isEndGame(match));
  }

  @Test
  void whenBothPitsAreAllEmpty_shouldEndGame() {
    Match match = new Match();
    Integer[] firstPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 0, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertTrue(matchService.isEndGame(match));
  }

  @Test
  void whenBothPitsAreNotEmpty_shouldNotEndGame() {
    Match match = new Match();
    Integer[] firstPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    Integer[] secondPlayerPits = { 1, 0, 0, 0, 0, 0, 5 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);

    assertFalse(matchService.isEndGame(match));
  }
}