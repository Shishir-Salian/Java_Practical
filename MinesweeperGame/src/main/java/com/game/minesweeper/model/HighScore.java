package com.game.minesweeper.model;

import java.sql.Timestamp;

public class HighScore {
    private int id;
    private String playerName;
    private int scoreTime; // Time in seconds
    private Timestamp playedAt;

    // Constructors
    public HighScore(String playerName, int scoreTime) {
        this.playerName = playerName;
        this.scoreTime = scoreTime;
    }

    public HighScore(int id, String playerName, int scoreTime, Timestamp playedAt) {
        this.id = id;
        this.playerName = playerName;
        this.scoreTime = scoreTime;
        this.playedAt = playedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getPlayerName() { return playerName; }
    public int getScoreTime() { return scoreTime; }
    public Timestamp getPlayedAt() { return playedAt; }

    public void setId(int id) { this.id = id; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setScoreTime(int scoreTime) { this.scoreTime = scoreTime; }
    public void setPlayedAt(Timestamp playedAt) { this.playedAt = playedAt; }
}
