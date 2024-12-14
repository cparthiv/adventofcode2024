import java.io.IOException;
import java.util.*;

public class Day10 {
    public static void main(String[] args) {

        // Represent map as a 2D integer array
        List<String> lines;
        try {
            lines = FileReaderUtil.readAllLines("src/Inputs/Day10.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int rows = lines.size();
        int cols = lines.getFirst().length();
        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            char[] chars = lines.get(i).toCharArray();
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = chars[j] - '0';
            }
        }

        // Identify trailheads
        List<int[]> trailheads = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) trailheads.add(new int[]{i, j});
            }
        }

        int totalScore = 0;
        for (int[] trailhead : trailheads) {
            Set<String> reachableNines = findReachableNines(matrix, trailhead[0], trailhead[1]);
            totalScore += reachableNines.size();
        }
        System.out.println("Total score: " + totalScore);

        // Calculate rating
        long totalRating = 0;
        for (int[] trailhead : trailheads) {
            totalRating += findRatingForTrailhead(matrix, trailhead[0], trailhead[1]);
        }
        System.out.println("Total rating: " + totalRating);
    }

    public static Set<String> findReachableNines(int[][] map, int startRow, int startCol) {
        int[] dR = {0, 0, 1, -1};
        int[] dC = {1, -1, 0, 0};
        int rows = map.length;
        int cols = map[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        visited[startRow][startCol] = true;
        queue.add(new int[]{startRow, startCol});
        Set<String> nines = new HashSet<>();

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currRow = curr[0];
            int currCol = curr[1];
            int currentHeight = map[currRow][currCol];

            if (currentHeight == 9) {
                nines.add(currRow + "," + currCol);
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int nextRow = currRow + dR[i];
                int nextCol = currCol + dC[i];

                // Check conditions
                if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) continue;
                if (!visited[nextRow][nextCol] && map[nextRow][nextCol] == currentHeight + 1) {
                    visited[nextRow][nextCol] = true;
                    queue.add(new int[]{nextRow, nextCol});
                }
            }
        }

        return nines;
    }

    public static long findRatingForTrailhead(int[][] map, int startRow, int startCol) {
        int rows = map.length;
        int cols = map[0].length;
        int[] dR = {0, 0, 1, -1};
        int[] dC = {1, -1, 0, 0};

        // ways[r][c] will store how many distinct paths lead to cell (r,c)
        long[][] ways = new long[rows][cols];
        ways[startRow][startCol] = 1;

        // We'll use a queue for BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});

        boolean[][] visited = new boolean[rows][cols];
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];
            int h = map[r][c];

            for (int i = 0; i < 4; i++) {
                int nr = r + dR[i];
                int nc = c + dC[i];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;

                if (map[nr][nc] == h + 1) {
                    long oldWays = ways[nr][nc];
                    ways[nr][nc] += ways[r][c];

                    // If previously no ways existed and now we have some, enqueue
                    // Or if it was never visited before
                    if (!visited[nr][nc] || oldWays == 0) {
                        visited[nr][nc] = true;
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }

        // Now sum the ways to all '9' cells
        long rating = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 9) {
                    rating += ways[i][j];
                }
            }
        }

        return rating;
    }
}
