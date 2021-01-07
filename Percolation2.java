/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

public class Percolation2 {

    private int[] parent;

    private final int n;

    private final int[] sizeRank;

    private final int[] reverseSizeRank;

    private int countOpen = 0;

    public Percolation2(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException(
                    "argument " + a + " has to be an integer greater then 0");
        }
        n = a;
        int nSquared = n * n;
        parent = new int[nSquared];
        sizeRank = new int[nSquared];
        reverseSizeRank = new int[nSquared];

        for (int i = 0; i < nSquared; i++) {
            if (i < n) {
                sizeRank[i] = 1000;
                reverseSizeRank[i] = 0;

            }
            if (i > (n * (n - 1))) {
                sizeRank[i] = 0;
                reverseSizeRank[i] = 1000;
            }
            else {
                sizeRank[i] = 0;
            }
        }
    }

    public void open(int row, int col) {
        validate(row);
        validate(col);
        int id = getId(row, col);
        int up = getId(row - 1, col);
        int down = getId(row + 1, col);
        int left = getId(row, col - 1);
        int right = getId(row, col + 1);
        // move this check higher up in the function;
        if (parent[id - 1] == 0) {
            parent[id - 1] = id;
            countOpen++;
            if (row > 1 && parent[up - 1] != 0) {
                union(id, up);
            }
            if (row < n && parent[down - 1] != 0) {
                union(id, down);
            }
            if (col > 1 && parent[left - 1] != 0) {
                union(id, left);
            }
            if (col < n && parent[right - 1] != 0) {
                union(id, right);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);
        int id = getId(row, col);
        return parent[id - 1] != 0;
    }

    public boolean isFull(int row, int col) {
        validate(row);
        validate(col);
        int id = getId(row, col);
        if (parent[id - 1] == 0) return false;
        int root = findRoot(id);
        // System.out.println("roots in isFull " + root);
        if (parent[root - 1] == 0) return false;
        return root <= n;
    }

    public int numberOfOpenSites() {
        return countOpen;
    }

    public boolean percolates() {
        for (int i = 1; i <= n; i++) {
            if (isFull(n, i)) {
                return true;
            }
        }
        return false;
    }

    private int getId(int row, int col) {
        return (col + (row - 1) * n);
    }

    private void union(int a, int b) {
        int rootA = findRoot(a);
        int rootB = findRoot(b);
        if (rootA == rootB) return;
        if (sizeRank[rootA - 1] > sizeRank[rootB - 1]) parent[rootB - 1] = rootA;
        if (sizeRank[rootA - 1] < sizeRank[rootB - 1]) parent[rootA - 1] = rootB;
        if (sizeRank[rootA - 1] == sizeRank[rootB - 1]) {
            sizeRank[rootA - 1]++;
            parent[rootB - 1] = rootA;
        }
    }

    private int findRoot(int a) {
        while (parent[a - 1] != a) {
            parent[a - 1] = parent[parent[a - 1] - 1];
            a = parent[a - 1];
        }
        return a;
    }

    private void validate(int p) {
        if (p < 1 || p > n) {
            throw new IllegalArgumentException("column or raw is outside its prescribed range");
        }
    }

    // main method is empty
    public static void main(String[] args) {
        // empty
    }
}
