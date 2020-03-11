package com.bol.api.entity;

public class InputMessage {
    private String type;
    private String playerId;
    private String matchId;
    private String fingerprint;
    private Integer playPosition;

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
     * @return the playerId
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId the playerId to set
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * @return the matchId
     */
    public String getMatchId() {
        return matchId;
    }

    /**
     * @param matchId the matchId to set
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    /**
     * @return the fingerprint
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * @param fingerprint the fingerprint to set
     */
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * @return the playPosition
     */
    public Integer getPlayPosition() {
        return playPosition;
    }

    /**
     * @param playPosition the playPosition to set
     */
    public void setPlayPosition(Integer playPosition) {
        this.playPosition = playPosition;
    }

    @Override
    public String toString() {
        return "type: " + getType() + " playerId: " + getPlayerId() + " fingerprint: " + getFingerprint();
    }
}