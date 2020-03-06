package com.bol.api.entity;

public class OutputMessage {
    private String type;
    private Match match;

    public OutputMessage(String type, Match match) {
        this.type = type;
        this.match = match;
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
}