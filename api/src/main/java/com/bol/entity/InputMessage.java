package com.bol.entity;

public class InputMessage {
    private String type;
    private String playerId;
    private String fingerprint;

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
}