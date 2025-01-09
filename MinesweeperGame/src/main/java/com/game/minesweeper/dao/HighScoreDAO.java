// src/main/java/com/game/minesweeper/dao/HighScoreDAO.java
package com.game.minesweeper.dao;

import com.game.minesweeper.model.HighScore;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for HighScore.
 * Handles all database interactions related to high scores.
 */
public class HighScoreDAO {
    // Updated JDBC URL with allowPublicKeyRetrieval=true
    private static final String URL = "jdbc:mysql://localhost:3306/minesweeper?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "shishir19"; // Replace with your MySQL password

    // SQL Queries
    private static final String INSERT_HIGH_SCORE = "INSERT INTO high_scores (player_name, score_time) VALUES (?, ?)";
    private static final String SELECT_TOP_SCORES = "SELECT * FROM high_scores ORDER BY score_time ASC, played_at ASC LIMIT 10";

    /**
     * Inserts a new high score into the database.
     *
     * @param highScore The HighScore object to insert.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean addHighScore(HighScore highScore) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Explicitly load the driver
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return false;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_HIGH_SCORE)) {

            pstmt.setString(1, highScore.getPlayerName());
            pstmt.setInt(2, highScore.getScoreTime());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception while adding high score.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the top high scores from the database.
     *
     * @return A list of HighScore objects.
     */
    public List<HighScore> getTopHighScores() {
        List<HighScore> highScores = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Explicitly load the driver
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return highScores;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_TOP_SCORES);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                HighScore score = new HighScore(
                        rs.getInt("id"),
                        rs.getString("player_name"),
                        rs.getInt("score_time"),
                        rs.getTimestamp("played_at")
                );
                highScores.add(score);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception while retrieving high scores.");
            e.printStackTrace();
        }

        return highScores;
    }
}
