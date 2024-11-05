public class AlphabetWar {
    private int turn = 0;
    private int damage = 0;
    private int health1;
    private int health2;
    
    private final char[][] strength1 = {{'w', '4'}, {'p', '3'}, {'b', '2'}, {'s', '1'}};
    private final char[][] strength2 = {{'m', '4'}, {'q', '3'}, {'d', '2'}, {'z', '1'}};
    
    // Constructor to initialize both players' health
    public AlphabetWar(int health) {
        this.health1 = health;
        this.health2 = health;
    }
    
    // Helper method to validate the move based on the turn
    private boolean isValidMove(char warrior) {
        if (turn % 2 == 1) {  // Player 1's turn
            for (char[] s : strength1) {
                if (s[0] == warrior) return true;
            }
        } else {  // Player 2's turn
            for (char[] s : strength2) {
                if (s[0] == warrior) return true;
            }
        }
        return false;
    }
    
    private void playTurn(char warrior) {
        turn++;
        
        // Check if the move is valid
        if (!isValidMove(warrior)) {
            System.out.println("Invalid character played: " + warrior + ". Turn skipped.");
            turn--;  // Reset turn count as this turn is invalid
            return;
        }
        
        // If turn is odd, Player 1 attacks with strength1 characters
        if (turn % 2 == 1) {
            for (int i = 0; i < strength1.length; i++) {
                if (strength1[i][0] == warrior) {
                    damage = Character.getNumericValue(strength1[i][1]);
                }
            }
        } 
        // If turn is even, Player 2 attacks with strength2 characters
        else {
            for (int i = 0; i < strength2.length; i++) {
                if (strength2[i][0] == warrior) {
                    damage = damage -Character.getNumericValue(strength2[i][1]);
                }
            }
            if (damage < 0) {
                System.out.println("Player 2 has damaged Player 1 by " + (-damage));
                health1 += damage;  // reduce health1 by damage
            } else if (damage > 0) {
                System.out.println("Player 1 has damaged Player 2 by " + damage);
                health2 -= damage;
            }
            damage = 0;
        }
        
        // Check if any player’s health has reached zero
        if (health1 <= 0) {
            System.out.println("Player 1 has reached 0 health. Player 2 has won.");
        } else if (health2 <= 0) {
            System.out.println("Player 2 has reached 0 health. Player 1 has won.");
        } else {
            System.out.println("Player 1 Health: " + health1 + " | Player 2 Health: " + health2);
        }
    }
    
    // Method to start and continue the game until a winner is determined
    public void startGame(char[] moves) {
        for (char move : moves) {
            if (health1 > 0 && health2 > 0) {
                playTurn(move);
            } else {
                break;
            }
        }
        
        if (health1 > 0 && health2 > 0) {
            System.out.println("Let's fight again!");
        } else if (health1 <= 0) {
            System.out.println("Right side wins!");
        } else if (health2 <= 0) {
            System.out.println("Left side wins!");
        }
    }
    
    public static void main(String[] args) {
        AlphabetWar game = new AlphabetWar(10);
        // Define a series of moves for the game, including an invalid character 'x'
        char[] moves = {'w', 'z', 'p', 'x', 'b', 'm', 'q', 'd'};
        game.startGame(moves);
        System.out.println();
        AlphabetWar game1 = new AlphabetWar(10);
        char[] moves1 = {'w', 'z', 'p', 'm', 'b', 'q', 's', 'd'};
        game1.startGame(moves1);
        System.out.println();
        AlphabetWar game2 = new AlphabetWar(10);
        char[] moves2 = {'w', 'z', 'w', 'z', 'w', 'z'};
        game.startGame(moves2);
    }
}
