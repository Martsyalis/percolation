/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

public class Percolation {

    private final int nSquared;
    private final int n;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF backwash;
    private int countOpen = 0;
    private boolean[] open;
    private boolean percolationPossible = false;

    public Percolation(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException(
                    "argument " + a + " has to be an integer greater then 0");
        }
        n = a;
        nSquared = n * n;
        uf = new WeightedQuickUnionUF(nSquared + 2);
        backwash = new WeightedQuickUnionUF(nSquared + 2);
        open = new boolean[nSquared + 2];
        // loop for virtual hook up
        for (int i = 1; i <= n; i++) {
            uf.union(0, i); // connect top virtual
            backwash.union(0, i); // connect top virtual to backwash;
            uf.union(nSquared + 1, nSquared + 1 - i); // connect bottom virtual
        }
    }

    public void open(int row, int col) {
        validate(row);
        validate(col);
        if (!percolationPossible) {
            percolationPossible = true;
        }
        int id = getId(row, col);
        if (open[id]) {
            return;
        }
        open[id] = true;
        countOpen++;
        int up = getId(row - 1, col);
        int down = getId(row + 1, col);
        int left = getId(row, col - 1);
        int right = getId(row, col + 1);
        if (row > 1 && open[up]) {
            uf.union(id, up);
            backwash.union(id, up);
        }
        if (row < n && open[down]) {
            uf.union(id, down);
            backwash.union(id, down);
        }
        if (col > 1 && open[left]) {
            uf.union(id, left);
            backwash.union(id, left);
        }
        if (col < n && open[right]) {
            uf.union(id, right);
            backwash.union(id, right);
        }

    }

    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);
        return open[getId(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row);
        validate(col);
        if (!isOpen(row, col)) return false;
        boolean ufFull = uf.find(getId(row, col)) == uf.find(0);
        boolean backwashFull = backwash.find(getId(row, col)) == uf.find(0);
        return ufFull && backwashFull;
    }

    public int numberOfOpenSites() {
        return countOpen;
    }

    public boolean percolates() {
        return percolationPossible && uf.find(0) == uf.find(nSquared + 1);
    }

    private int getId(int row, int col) {
        return (col + (row - 1) * n);
    }

    private void validate(int p) {
        if (p < 1 || p > n) {
            throw new IllegalArgumentException("column or raw is outside its prescribed range");
        }
    }

    public static void main(String[] args) {
        Percolation a = new Percolation(3);
        a.open(1, 3);
        a.open(2, 3);
        a.open(3, 3);
        System.out.println(a.percolates());
        a.open(1, 3);
        a.open(1, 2);
        System.out.println(a.isFull(1, 2));
        System.out.println(a.isFull(1, 1));
    }
}
