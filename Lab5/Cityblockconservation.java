import java.util.Scanner;

interface Waterconservationsystem {
    public int calculatetrappedwater(int[] blockheights);
}

abstract class Rainyseasonconservation implements Waterconservationsystem {
    public abstract int calculatetrappedwater(int[] blockheights);
}

public class Cityblockconservation extends Rainyseasonconservation {
    public int calculatetrappedwater(int[] blockheights) {
        int n = blockheights.length;
        if (n == 0) {
            return 0;
        }

        // Arrays to store the maximum height to the left and right of each block
        int[] leftmax = new int[n];
        int[] rightmax = new int[n];

        // Fill the leftmax array
        leftmax[0] = blockheights[0];
        for (int i = 1; i < n; i++) {
            leftmax[i] = Math.max(leftmax[i - 1], blockheights[i]);
        }

        // Fill the rightmax array
        rightmax[n - 1] = blockheights[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightmax[i] = Math.max(rightmax[i + 1], blockheights[i]);
        }

        // Calculate the water trapped at each block
        int trappedwater = 0;
        for (int i = 0; i < n; i++) {
            trappedwater += Math.min(leftmax[i], rightmax[i]) - blockheights[i];
        }

        return trappedwater;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of blocks:");
        int n = scanner.nextInt();

        int[] blockheights = new int[n];

        System.out.println("Enter the block heights:");
        for (int i = 0; i < n; i++) {
            blockheights[i] = scanner.nextInt();
        }

        Cityblockconservation c = new Cityblockconservation();

        System.out.println("Trapped water: " + c.calculatetrappedwater(blockheights));
        scanner.close();
    }
}
