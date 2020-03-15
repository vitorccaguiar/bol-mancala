package com.bol.api;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.bol.api.constant.ErrorMessage;
import com.bol.api.constant.MatchStatus;
import com.bol.api.constant.MessageStatus;
import com.bol.api.constant.SuccessMessage;
import com.bol.api.entity.InputMessage;
import com.bol.api.entity.Match;
import com.bol.api.entity.OutputMessage;
import com.bol.api.entity.User;
import com.bol.api.repository.MatchRepository;
import com.bol.api.service.MatchService;
import com.bol.api.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

@SpringBootTest
class MatchServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private MatchRepository matchRepository;

  @InjectMocks
  private MatchService matchService;

  /**
   * Tests for join method
   */
  @Test
  void whenJoinMessageNonExistentPlayer_shouldReturnErrorMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.JOIN);
    message.setPlayerId("");

    when(userService.getUserById("")).thenReturn(Optional.empty());
    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.PLAYER_NOT_FOUND);
    assertEquals(outputMessage, matchService.join(message));
  }

  @Test
  void whenJoinMessageNonExistentMatch_shouldReturnErrorMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.JOIN);
    message.setMatchId("321");

    User player = new User();
    player.setId("123");
    message.setPlayerId("123");

    when(userService.getUserById("123")).thenReturn(Optional.of(player));
    when(matchRepository.findById("321")).thenReturn(Optional.empty());
    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.INVALID_MATCH);
    assertEquals(outputMessage, matchService.join(message));
  }

  @Test
  void whenJoinMessageDifferentThanWaitingPlayerStatus_shouldReturnErrorMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.JOIN);
    message.setMatchId("321");

    User player = new User();
    player.setId("123");
    message.setPlayerId("123");

    Match match = new Match();
    match.setStatus(MatchStatus.PLAYING);

    when(userService.getUserById("123")).thenReturn(Optional.of(player));
    when(matchRepository.findById("321")).thenReturn(Optional.of(match));
    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.INVALID_MATCH);
    assertEquals(outputMessage, matchService.join(message));
  }

  @Test
  void whenJoinMessageException_shouldReturnErrorMessage() {
    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.UNEXPECTED_JOIN);
    assertEquals(outputMessage, matchService.join(null));
  }

  @Test
  void whenJoinMessage_shouldReturnReadyMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.JOIN);
    message.setMatchId("321");

    User player = new User();
    player.setId("123");
    message.setPlayerId("123");

    Match inputMatch = new Match();
    inputMatch.setStatus(MatchStatus.WAITING_PLAYER);

    Match outputMatch = new Match();
    outputMatch.setSecondPlayer(player);
    outputMatch.setStatus(MatchStatus.PLAYING);

    when(userService.getUserById("123")).thenReturn(Optional.of(player));
    when(matchRepository.findById("321")).thenReturn(Optional.of(inputMatch));
    OutputMessage outputMessage = new OutputMessage(MessageStatus.READY, outputMatch, SuccessMessage.JOINED);
    assertEquals(outputMessage, matchService.join(message));
  }

  /**
   * Tests for play method
   */
  @Test
  void whenPlayMessageNotYourTurn_shouldReturnErrorMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.PLAY);
    message.setPlayerId("123");
    message.setMatchId("321");

    User player = new User();
    player.setName("Vitor");
    player.setId("321");
    MatchService.playerMatch.put("123", "321");

    Match match = new Match();
    match.setPlayerTurn(player);
    match.setStatus(MatchStatus.PLAYING);

    when(matchRepository.findById("321")).thenReturn(Optional.of(match));
    when(userService.getUserById("123")).thenReturn(Optional.of(player));

    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, "Is not " + "Vitor" + "'s turn");
    assertEquals(outputMessage, matchService.play(message));
    MatchService.playerMatch.clear();
  }

  @Test
  void whenPlayMessageMatchNotGoing_shouldReturnErrorMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.PLAY);
    message.setPlayerId("123");
    message.setMatchId("321");

    User player = new User();
    player.setName("Vitor");
    player.setId("123");
    MatchService.playerMatch.put("123", "321");

    Match match = new Match();
    match.setPlayerTurn(player);
    match.setStatus(MatchStatus.WAITING_PLAYER);

    when(matchRepository.findById("321")).thenReturn(Optional.of(match));
    when(userService.getUserById("123")).thenReturn(Optional.of(player));

    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, "Is not " + "Vitor" + "'s turn");
    assertEquals(outputMessage, matchService.play(message));
    MatchService.playerMatch.clear();
  }

  @Test
  void whenPlayMessageNotPlayerMatch_shouldReturnErrorMessage() {
    InputMessage message = new InputMessage();
    message.setType(MessageStatus.PLAY);
    message.setPlayerId("123");
    message.setMatchId("321");
    message.setFingerprint("789");

    User player = new User();
    player.setName("Vitor");
    player.setId("123");
    MatchService.playerMatch.put("123", "321");
    MatchService.playerMachine.put("123", "987");

    Match match = new Match();
    match.setPlayerTurn(player);
    match.setStatus(MatchStatus.PLAYING);

    when(matchRepository.findById("321")).thenReturn(Optional.of(match));
    when(userService.getUserById("123")).thenReturn(Optional.of(player));

    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.INVALID_PLAYER);
    assertEquals(outputMessage, matchService.play(message));
    MatchService.playerMatch.clear();
    MatchService.playerMachine.clear();
  }

  @Test
  void whenPlayMessageException_shouldReturnErrorMessage() {
    OutputMessage outputMessage = new OutputMessage(MessageStatus.ERROR, null, ErrorMessage.UNEXPECTED_PLAY);
    assertEquals(outputMessage, matchService.play(null));
  }

  @Test
  void whenPlayMessage_shouldReturnPlayingMessage() {

  }

  /**
   * Tests for IsPlayerTurn method
   */
  @Test
  void whenIsPlayerTurn_shouldReturnTrue() {
    MatchService.playerMatch.put("123", "321");

    User player = new User();
    player.setId("123");

    Match match = new Match();
    match.setPlayerTurn(player);

    when(matchRepository.findById("321")).thenReturn(Optional.of(match));
    assertTrue(matchService.isPlayerTurn("123"));
    MatchService.playerMatch.clear();
  }

  @Test
  void whenIsNotPlayerTurn_shouldReturnFalse() {
    MatchService.playerMatch.put("123", "321");

    User player = new User();
    player.setId("321");

    Match match = new Match();
    match.setPlayerTurn(player);

    when(matchRepository.findById("321")).thenReturn(Optional.of(match));
    assertFalse(matchService.isPlayerTurn("123"));
    MatchService.playerMatch.clear();
  }

  /**
   * Tests for IsMatchOnGoing method
   */
  @Test
  void whenIsMatchOnGoing_shouldReturnTrue() {
    Match match = new Match();
    match.setStatus(MatchStatus.PLAYING);

    when(matchRepository.findById("123")).thenReturn(Optional.of(match));
    assertTrue(matchService.isMatchOnGoing("123"));
  }

  @Test
  void whenIsNotMatchOnGoing_shouldReturnFalse() {
    Match match = new Match();
    match.setStatus(MatchStatus.WAITING_PLAYER);

    when(matchRepository.findById("123")).thenReturn(Optional.of(match));
    assertFalse(matchService.isMatchOnGoing("123"));
  }

  /**
   * Tests for IsPlayerMatch method
   */
  @Test
  void whenIsPlayerMatch_shouldReturnTrue() {
    MatchService.playerMatch.put("123", "321");
    MatchService.playerMachine.put("123", "789");

    assertTrue(matchService.isPlayerMatch("123", "789", "321"));
    MatchService.playerMatch.clear();
    MatchService.playerMachine.clear();
  }

  @Test
  void whenMatchIdIsNotEqual_shouldReturnFalse() {
    MatchService.playerMatch.put("123", "987");
    MatchService.playerMachine.put("123", "789");

    assertFalse(matchService.isPlayerMatch("123", "789", "321"));
    MatchService.playerMatch.clear();
    MatchService.playerMachine.clear();
  }

  @Test
  void whenFingerprintIsNotEqual_shouldReturnFalse() {
    MatchService.playerMatch.put("123", "321");
    MatchService.playerMachine.put("123", "987");

    assertFalse(matchService.isPlayerMatch("123", "789", "321"));
    MatchService.playerMatch.clear();
    MatchService.playerMachine.clear();
  }

  /**
   * Tests for handleLastStoneEmptyPit method
   */
  @Test
  void whenNoneLastStoneEmptyPit_shouldDoNothing() {
    // After moveStones, an empty pit has 1
    Match matchWithChanges = createMatchForTest();
    Match matchWithoutChanges = createMatchForTest();
    
    Pair<Integer, Integer> finishedPositionAndPit = Pair.of(1, 1);
    matchService.handleLastStoneEmptyPit(matchWithChanges, finishedPositionAndPit);
    assertEquals(matchWithChanges, matchWithoutChanges);
  }

  Match createMatchForTest() {
    Match match = new Match();
    Integer[] firstPlayerPits = { 2, 2, 2, 2, 2, 1, 2 };
    Integer[] secondPlayerPits = { 2, 1, 2, 2, 2, 2, 2 };
    match.setFirstPlayerPits(firstPlayerPits);
    match.setSecondPlayerPits(secondPlayerPits);
    User player = new User();
    player.setId("123");
    match.setPlayerTurn(player);
    match.setFirstPlayer(player);
    User secondPlayer = new User();
    secondPlayer.setId("321");
    match.setSecondPlayer(secondPlayer);

    return match;
  }

  @Test
  void whenOtherPlayerLastStoneEmptyPit_shouldDoNothing() {
    // After moveStones, an empty pit has 1
    Match matchWithChanges = createMatchForTest();
    Match matchWithoutChanges = createMatchForTest();
    
    Pair<Integer, Integer> finishedPositionAndPit = Pair.of(1, 2);
    matchService.handleLastStoneEmptyPit(matchWithChanges, finishedPositionAndPit);
    assertEquals(matchWithChanges, matchWithoutChanges);
  }

  @Test
  void whenLastStoneEmptyPit_shouldZeroLastPitAndSumIntoBigPit() {
    // After moveStones, an empty pit has 1
    Match matchWithChanges = createMatchForTest();
    Match matchAfterChanges = createMatchForTest();
    Integer[] firstPlayerPits = { 2, 2, 2, 2, 2, 0, 5 };
    Integer[] secondPlayerPits = { 0, 1, 2, 2, 2, 2, 2 };
    matchAfterChanges.setFirstPlayerPits(firstPlayerPits);
    matchAfterChanges.setSecondPlayerPits(secondPlayerPits);
    
    Pair<Integer, Integer> finishedPositionAndPit = Pair.of(5, 1);
    matchService.handleLastStoneEmptyPit(matchWithChanges, finishedPositionAndPit);
    assertEquals(matchWithChanges, matchAfterChanges);
  }

  /**
   * Tests for moveStones method
   */
  @Test
  void whenHaveSixStonesAndPlayInLastPitFirstPlayer_shouldSumOneEachNextSixPits() {
    Integer[] firstPits = { 2, 2, 2, 2, 2, 6, 2 };
    Integer[] secondPits = { 2, 1, 2, 2, 2, 2, 2 };

    Integer[] expectedFirstPits = { 2, 2, 2, 2, 2, 0, 3 };
    Integer[] expectedSecondPits = { 3, 2, 3, 3, 3, 2, 2 };

    matchService.moveStones(firstPits, secondPits, 5, true);
    assertArrayEquals(firstPits, expectedFirstPits);
    assertArrayEquals(secondPits, expectedSecondPits);
  }

  @Test
  void whenHaveSixStonesAndPlayInLastPitSecondPlayer_shouldSumOneEachNextSixPits() {
    Integer[] firstPits = { 2, 2, 2, 2, 2, 6, 2 };
    Integer[] secondPits = { 2, 1, 2, 2, 2, 2, 2 };

    Integer[] expectedFirstPits = { 2, 2, 2, 2, 2, 0, 3 };
    Integer[] expectedSecondPits = { 3, 2, 3, 3, 3, 2, 2 };

    matchService.moveStones(firstPits, secondPits, 5, false);
    assertArrayEquals(firstPits, expectedFirstPits);
    assertArrayEquals(secondPits, expectedSecondPits);
  }

  @Test
  void whenHaveEightStonesAndPlayInLastPit_shouldSumOneEachNextEightPits() {
    Integer[] firstPits = { 2, 2, 2, 2, 2, 8, 2 };
    Integer[] secondPits = { 2, 1, 2, 2, 2, 2, 2 };

    Integer[] expectedFirstPits = { 3, 2, 2, 2, 2, 0, 3 };
    Integer[] expectedSecondPits = { 3, 2, 3, 3, 3, 3, 2 };

    matchService.moveStones(firstPits, secondPits, 5, true);
    assertArrayEquals(firstPits, expectedFirstPits);
    assertArrayEquals(secondPits, expectedSecondPits);
  }

  /**
   * Tests for isEndGame method
   */
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