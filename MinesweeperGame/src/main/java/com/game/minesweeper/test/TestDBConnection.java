// TestDBConnection.java
package com.game.minesweeper.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/minesweeper?useSSL=false&serverTimezone=UTC";
        String user = "root"; // Replace with your MySQL username
        String password = "shishir19"; // Replace with your MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Explicitly load the driver
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database successfully!");
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}
