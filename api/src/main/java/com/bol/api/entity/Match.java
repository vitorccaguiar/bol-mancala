package com.bol.api.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Matches")
public class Match {
  private String id;
  private User firstPlayer;
  private User secondPlayer;
  private String status;
  private Integer[] firstPlayerPits;
  private Integer[] secondPlayerPits;
  private String winner;

  public String getId() {
    return this.id;
  }

  /**
   * @return the firstPlayerName
   */
  public User getFirstPlayer() {
    return firstPlayer;
  }

  /**
   * @param firstPlayerName the firstPlayerName to set
   */
  public void setFirstPlayer(User firstPlayer) {
    this.firstPlayer = firstPlayer;
  }

  /**
   * @return the secondPlayerName
   */
  public User getSecondPlayer() {
    return secondPlayer;
  }

  /**
   * @param secondPlayerName the secondPlayerName to set
   */
  public void setSecondPlayer(User secondPlayer) {
    this.secondPlayer = secondPlayer;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the firstPlayerPits
   */
  public Integer[] getFirstPlayerPits() {
    return firstPlayerPits;
  }

  /**
   * @param firstPlayerPits the firstPlayerPits to set
   */
  public void setFirstPlayerPits(Integer[] firstPlayerPits) {
    this.firstPlayerPits = firstPlayerPits;
  }

  /**
   * @return the secondPlayerPits
   */
  public Integer[] getSecondPlayerPits() {
    return secondPlayerPits;
  }

  /**
   * @param secondPlayerPits the secondPlayerPits to set
   */
  public void setSecondPlayerPits(Integer[] secondPlayerPits) {
    this.secondPlayerPits = secondPlayerPits;
  }

  /**
   * @return the winner
   */
  public String getWinner() {
    return winner;
  }

  /**
   * @param winner the winner to set
   */
  public void setWinner(String winner) {
    this.winner = winner;
  }

}