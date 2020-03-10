package com.bol.api.entity;

public class InputMessage {
    private String type;
    private String playerId;
    private String fingerprint;
    private Match match;
    private Play play;

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
     * @return the play
     */
    public Play getPlay() {
        return play;
    }

    /**
     * @param play the play to set
     */
    public void setPlay(Play play) {
        this.play = play;
    }

    @Override
    public String toString() {
        return "type: " + getType() + " playerId: " + getPlayerId() + " fingerprint: " + getFingerprint();
    }
}