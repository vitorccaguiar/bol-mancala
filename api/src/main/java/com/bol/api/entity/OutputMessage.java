package com.bol.api.entity;

public class OutputMessage {
  private String type;
  private Match match;
  private String errorMessage;

  public OutputMessage(String type, Match match, String errorMessage) {
    this.type = type;
    this.match = match;
    this.errorMessage = errorMessage;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the match
   */
  public Match getMatch() {
    return match;
  }

  /**
   * @param match the match to set
   */
  public void setMatch(Match match) {
    this.match = match;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    OutputMessage guest = (OutputMessage) obj;
    return type.equals(guest.getType()) &&
        ((match == null && guest.getMatch() == null) || match.equals(guest.getMatch())) &&
        errorMessage.equals(guest.getErrorMessage());
  }
}