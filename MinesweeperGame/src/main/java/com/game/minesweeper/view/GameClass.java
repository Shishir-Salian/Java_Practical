// src/main/java/com/game/minesweeper/view/GameClass.java
package com.game.minesweeper.view;

import com.game.minesweeper.dao.HighScoreDAO;
import com.game.minesweeper.model.HighScore;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Main class for the Minesweeper game.
 * Handles game logic, UI rendering, and integration with the high score database.
 */
public class GameClass extends JFrame {

    // Constants for the game
    private static final int COLS = 10;          // Number of columns in the grid
    private static final int ROWS = 10;          // Number of rows in the grid
    private static final int MINE_COUNT = 10;    // Total number of mines
    private static final int CELL_SIZE = 40;     // Size of each cell in pixels

    // 2D arrays to represent the game state
    private JButton[][] cells = new JButton[COLS][ROWS];
    private boolean[][] mines = new boolean[COLS][ROWS];
    private boolean[][] revealed = new boolean[COLS][ROWS];
    private boolean[][] flagged = new boolean[COLS][ROWS];
    private int[][] nearby = new int[COLS][ROWS];

    // Game state variables
    private boolean gameOver = false;
    private boolean victory = false;
    private Date startDate;
    private Date winDate;

    // High score management
    private HighScoreDAO highScoreDAO;

    // UI Components
    private JMenuBar menuBar;
    private JMenu optionsMenu;
    private JMenuItem viewHighScoresItem;
    private JMenuItem resetGameItem;

    /**
     * Constructor to initialize the game.
     */
    public GameClass() {
        // Initialize high score DAO
        highScoreDAO = new HighScoreDAO(); // HighScoreDAO handles DB credentials internally

        // Set the Look and Feel to the system's default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set Look and Feel.");
            e.printStackTrace();
        }

        // Set up the main window
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize menu bar
        initializeMenuBar();

        // Initialize game board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(ROWS, COLS)); // Corrected order: rows first, then columns

        // Add a temporary border and background color for debugging
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        boardPanel.setBackground(Color.WHITE);

        initializeBoard(boardPanel);

        add(boardPanel, BorderLayout.CENTER);

        // Initialize top panel with timer and reset button
        JPanel topPanel = new JPanel();
        JLabel statusLabel = new JLabel("Time: 0");
        topPanel.add(statusLabel);
        initializeResetButton(topPanel);
        add(topPanel, BorderLayout.NORTH);

        // Initialize timer
        Timer timer = new Timer(1000, new ActionListener() {
            private int elapsedSeconds = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startDate != null && !gameOver && !victory) {
                    elapsedSeconds++;
                    statusLabel.setText("Time: " + elapsedSeconds);
                }
            }
        });
        timer.start();

        // Pack the frame to fit all components
        pack();
        setLocationRelativeTo(null); // Centers the window on the screen
        setVisible(true);
    }

    /**
     * Initializes the menu bar with options.
     */
    private void initializeMenuBar() {
        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Options");
        
        viewHighScoresItem = new JMenuItem("View High Scores");
        viewHighScoresItem.addActionListener(e -> displayHighScores());

        resetGameItem = new JMenuItem("Reset Game");
        resetGameItem.addActionListener(e -> resetGame());

        optionsMenu.add(viewHighScoresItem);
        optionsMenu.add(resetGameItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Initializes the game board with buttons.
     *
     * @param boardPanel The JPanel to add the buttons to.
     */
    private void initializeBoard(JPanel boardPanel) {
        // Initialize buttons for each cell
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setFont(new Font("Arial", Font.BOLD, 12));
                
                // Make background color changes visible
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setContentAreaFilled(true);
                
                final int fx = x;
                final int fy = y;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (gameOver || victory) return;
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            handleLeftClick(fx, fy);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            handleRightClick(fx, fy);
                        }
                    }
                });
                cells[x][y] = button;
                boardPanel.add(button);
            }
        }
    }

    /**
     * Initializes the reset game button in the top panel.
     *
     * @param topPanel The JPanel to add the button to.
     */
    private void initializeResetButton(JPanel topPanel) {
        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(80, 30));
        resetButton.addActionListener(e -> resetGame());
        topPanel.add(resetButton);
    }

    /**
     * Handles left-click events on a cell.
     *
     * @param x The column index of the clicked cell.
     * @param y The row index of the clicked cell.
     */
    private void handleLeftClick(int x, int y) {
        if (flagged[x][y] || revealed[x][y]) return;

        if (startDate == null) {
            // First click, start timer and place mines safely
            startDate = new Date();
            placeMines(x, y);
            calculateNearby();
        }

        revealCell(x, y);

        checkVictory();
    }

    /**
     * Handles right-click events on a cell to toggle flags.
     *
     * @param x The column index of the clicked cell.
     * @param y The row index of the clicked cell.
     */
    private void handleRightClick(int x, int y) {
        if (revealed[x][y]) return;

        flagged[x][y] = !flagged[x][y];
        updateButton(x, y);
    }

    /**
     * Places mines on the board, ensuring the first clicked cell is safe.
     *
     * @param excludeX The column index to exclude from mine placement.
     * @param excludeY The row index to exclude from mine placement.
     */
    private void placeMines(int excludeX, int excludeY) {
        Random rand = new Random();
        int placed = 0;
        while (placed < MINE_COUNT) {
            int x = rand.nextInt(COLS);
            int y = rand.nextInt(ROWS);
            if ((x == excludeX && y == excludeY) || mines[x][y]) continue;
            mines[x][y] = true;
            placed++;
        }
    }

    /**
     * Calculates the number of adjacent mines for each cell.
     */
    private void calculateNearby() {
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if (mines[x][y]) {
                    nearby[x][y] = -1; // -1 indicates a mine
                } else {
                    nearby[x][y] = countAdjacentMines(x, y);
                }
            }
        }
    }

    /**
     * Counts the number of adjacent mines around a given cell.
     *
     * @param x The column index of the cell.
     * @param y The row index of the cell.
     * @return The count of adjacent mines.
     */
    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <=1; dx++) {
            for (int dy = -1; dy <=1; dy++) {
                if (dx ==0 && dy ==0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (isValid(nx, ny) && mines[nx][ny]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if the given cell coordinates are within the board boundaries.
     *
     * @param x The column index.
     * @param y The row index.
     * @return true if valid, false otherwise.
     */
    private boolean isValid(int x, int y) {
        return x >=0 && x < COLS && y >=0 && y < ROWS;
    }

    /**
     * Reveals a cell and handles game over if a mine is clicked.
     *
     * @param x The column index of the cell.
     * @param y The row index of the cell.
     */
    private void revealCell(int x, int y) {
        if (!isValid(x, y) || revealed[x][y] || flagged[x][y]) return;

        revealed[x][y] = true;
        updateButton(x, y);

        if (mines[x][y]) {
            // Game over
            gameOver = true;
            revealAllMines();
            JOptionPane.showMessageDialog(this, "Game Over! You clicked on a mine.", "Game Over", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nearby[x][y] == 0) {
            // Reveal adjacent cells recursively
            for (int dx = -1; dx <=1; dx++) {
                for (int dy = -1; dy <=1; dy++) {
                    if (dx ==0 && dy ==0) continue;
                    int nx = x + dx;
                    int ny = y + dy;
                    if (isValid(nx, ny) && !revealed[nx][ny] && !flagged[nx][ny]) {
                        revealCell(nx, ny);
                    }
                }
            }
        }
    }

    /**
     * Updates the visual state of a button based on its cell state.
     *
     * @param x The column index of the cell.
     * @param y The row index of the cell.
     */
    private void updateButton(int x, int y) {
        JButton button = cells[x][y];
        if (revealed[x][y]) {
            if (mines[x][y]) {
                button.setText("M"); // Represent mine
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE); // Mine text color
            } else {
                if (nearby[x][y] > 0) {
                    button.setText(String.valueOf(nearby[x][y]));
                    // Optional: Set text color based on number
                    switch (nearby[x][y]) {
                        case 1:
                            button.setForeground(Color.BLUE);
                            break;
                        case 2:
                            button.setForeground(new Color(0, 128, 0)); // Dark Green
                            break;
                        case 3:
                            button.setForeground(Color.RED);
                            break;
                        case 4:
                            button.setForeground(new Color(128, 0, 128)); // Purple
                            break;
                        case 5:
                            button.setForeground(new Color(128, 0, 0)); // Maroon
                            break;
                        case 6:
                            button.setForeground(new Color(64, 224, 208)); // Turquoise
                            break;
                        case 7:
                            button.setForeground(Color.BLACK);
                            break;
                        case 8:
                            button.setForeground(Color.GRAY);
                            break;
                        default:
                            button.setForeground(Color.BLACK);
                    }
                }
                button.setEnabled(false);
                button.setBackground(Color.LIGHT_GRAY);
                button.setForeground(Color.BLACK); // Default text color
            }
        } else {
            if (flagged[x][y]) {
                button.setText("F"); // Represent flag
                button.setBackground(Color.YELLOW);
                button.setForeground(Color.BLACK); // Flag text color
            } else {
                button.setText("");
                // Reset background to default
                button.setBackground(UIManager.getColor("Button.background"));
                button.setForeground(Color.BLACK); // Default text color
            }
            // Ensure the button is enabled
            button.setEnabled(true);
        }
        
        // Center the text
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
    }

    /**
     * Reveals all mines on the board, typically when the game is over.
     */
    private void revealAllMines() {
        for (int x =0; x < COLS; x++) {
            for (int y =0; y < ROWS; y++) {
                if (mines[x][y]) {
                    cells[x][y].setText("M");
                    cells[x][y].setBackground(Color.RED);
                    cells[x][y].setForeground(Color.WHITE); // Mine text color
                }
            }
        }
    }

    /**
     * Checks if the player has achieved victory by revealing all non-mine cells.
     */
    private void checkVictory() {
        if (victory || gameOver) return;

        int cellsRevealed =0;
        for (int x =0; x < COLS; x++) {
            for (int y=0; y < ROWS; y++) {
                if (revealed[x][y]) cellsRevealed++;
            }
        }

        if (cellsRevealed == (COLS * ROWS - MINE_COUNT)) {
            victory = true;
            winDate = new Date();
            revealAllMines();
            JOptionPane.showMessageDialog(this, "Congratulations! You won!", "Victory", JOptionPane.INFORMATION_MESSAGE);
            promptHighScore();
        }
    }

    /**
     * Prompts the player to enter their name and records the high score.
     */
    private void promptHighScore() {
        String playerName = JOptionPane.showInputDialog(this, "You won! Enter your name:", "Victory!", JOptionPane.PLAIN_MESSAGE);

        if (playerName != null) {
            playerName = playerName.trim();
            if (playerName.isEmpty()) {
                playerName = "Anonymous"; // Default name
            } else if (playerName.length() > 50) {
                playerName = playerName.substring(0, 50); // Truncate if necessary
            }

            // Calculate time in seconds
            long timeInSeconds = (winDate.getTime() - startDate.getTime()) / 1000;

            HighScore highScore = new HighScore(playerName, (int) timeInSeconds);
            boolean success = highScoreDAO.addHighScore(highScore);

            if (success) {
                JOptionPane.showMessageDialog(this, "High score recorded!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to record high score.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            displayHighScores();
        }
    }

    /**
     * Displays the top high scores in a dialog.
     */
    private void displayHighScores() {
        List<HighScore> highScores = highScoreDAO.getTopHighScores();
        if (highScores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No high scores yet!", "High Scores", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Column names
        String[] columnNames = {"ID", "Player Name", "Time (s)", "Date"};

        // Data for the table
        Object[][] data = new Object[highScores.size()][4];
        for (int i = 0; i < highScores.size(); i++) {
            HighScore hs = highScores.get(i);
            data[i][0] = hs.getId();
            data[i][1] = hs.getPlayerName();
            data[i][2] = hs.getScoreTime();
            data[i][3] = hs.getPlayedAt();
        }

        // Create table model and JTable
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        table.setEnabled(false); // Make table read-only

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Display in a dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Top High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Resets the game to its initial state.
     */
    private void resetGame() {
        // Reset all game state variables
        gameOver = false;
        victory = false;
        startDate = null;
        winDate = null;

        // Reset all cell states and update their visual representation
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                mines[x][y] = false;
                revealed[x][y] = false;
                flagged[x][y] = false;
                nearby[x][y] = 0;
                updateButton(x, y);
            }
        }

        // Revalidate and repaint to refresh the UI
        revalidate();
        repaint();

        // Notify the player that the game has been reset
        JOptionPane.showMessageDialog(this, "Game has been reset.", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * The main method to start the game.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameClass();
        });
    }
}
