import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Coinpattern {

    // RecursiveTask that computes the number of ways to make 'sum' using coins[index..end].
    static class CoinChangeTask extends RecursiveTask<Long> {
        private final int[] coins;
        private final int index;
        private final int sum;

        public CoinChangeTask(int[] coins, int index, int sum) {
            this.coins = coins;
            this.index = index;
            this.sum = sum;
        }

        @Override
        protected Long compute() {
            // Base cases
            if (sum == 0) {
                // Found a valid combination
                return 1L;
            }
            if (sum < 0 || index >= coins.length) {
                // No valid combination
                return 0L;
            }

            // 1) Fork a task to include the current coin (sum - coins[index])
            CoinChangeTask includeTask = new CoinChangeTask(coins, index, sum - coins[index]);
            includeTask.fork();

            // 2) Compute excluding the current coin (index + 1)
            CoinChangeTask excludeTask = new CoinChangeTask(coins, index + 1, sum);
            long excludeResult = excludeTask.compute();

            // 3) Join the result from the "include" subtask
            long includeResult = includeTask.join();

            // Total combinations = include + exclude
            return includeResult + excludeResult;
        }
    }

    // Helper method to use ForkJoinPool
    public static long countWaysConcurrent(int[] coins, int sum) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new CoinChangeTask(coins, 0, sum));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Prompt user for number of coins
        System.out.print("Enter the number of different coin denominations (N): ");
        int N = sc.nextInt();

        // Prompt user for coin denominations
        int[] coins = new int[N];
        System.out.println("Enter the coin denominations (space-separated):");
        for (int i = 0; i < N; i++) {
            coins[i] = sc.nextInt();
        }

        // Prompt user for the target sum
        System.out.print("Enter the target sum: ");
        int sum = sc.nextInt();

        sc.close();

        // Calculate ways
        long ways = countWaysConcurrent(coins, sum);
        System.out.println("Number of ways to make " + sum + " with the given coins: " + ways);
    }
}
