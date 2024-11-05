**Credit Card Validator**
The program takes the user input for credit card no. with minimum of 8 digits and maximum of 9 digits. Then it validates the card number based on several steps. We didn't use a switch-case in the provided code because switch-case is typically used when you have multiple discrete conditions or values to handle. In this case, the validation process follows a sequential series of steps (from Step a to Step f), where each step depends on the result of the previous one. There aren't specific conditions or cases that would vary for each step, so a linear control flow (like if-else or direct method calls) is more appropriate.

![image](https://github.com/user-attachments/assets/11a1e4c9-ba3c-453a-816a-7dd18882bb1a)


**Alphabet War Game**
his Java program simulates a game called "Alphabet War," where two players take turns using characters that represent different attack strengths. Each player has a set of letters:

    Player 1's letters: 'w', 'p', 'b', 's' (with strengths 4, 3, 2, and 1 respectively).
    Player 2's letters: 'm', 'q', 'd', 'z' (also with strengths 4, 3, 2, and 1 respectively).

Key Components:

    Constructor: Initializes both players' health.
    playTurn Method: Handles each turn's attack, checking whose turn it is and applying damage to the other player.
    isValidMove Method: Ensures only allowed characters are used, skipping any invalid moves without affecting health.
    startGame Method: Takes an array of moves, applying each move until a player’s health reaches zero.

Game Flow:

Players alternate moves, with each valid character reducing the opponent's health based on its strength. The game announces the winner when one player's health reaches zero.


![image](https://github.com/user-attachments/assets/2088ee1e-3bd4-433a-93a3-cec2953f9892)
