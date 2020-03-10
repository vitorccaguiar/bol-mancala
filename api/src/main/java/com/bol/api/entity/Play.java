package com.bol.api.entity;

public class Play {
  private Integer playerNumber;
  private Integer position;

  /**
   * @return the playerNumber
   */
  public Integer getPlayerNumber() {
    return playerNumber;
  }

  /**
   * @param playerNumber the playerNumber to set
   */
  public void setPlayerNumber(Integer playerNumber) {
    this.playerNumber = playerNumber;
  }

  /**
   * @return the position
   */
  public Integer getPosition() {
    return position;
  }

  /**
   * @param position the position to set
   */
  public void setPosition(Integer position) {
    this.position = position;
  }
}