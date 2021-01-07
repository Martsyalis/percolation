import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double C_I_RANGE = 1.96;
    private final int width;
    private final int t;
    private final double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(final int n, final int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "both arguments have to be an integer greater then 0");
        }

        width = n;
        t = trials;
        thresholds = new double[trials];

        for (int i = 0; i < t; i++) {
            Percolation p = new Percolation(width);

            while (!p.percolates()) {
                openRandomClosed(p);
            }
            thresholds[i] = p.numberOfOpenSites() / (double) (width * width);
        }
    }

    private void openRandomClosed(Percolation p) {
        boolean closed = false;
        while (!closed) {
            int random1 = StdRandom.uniform(1, width + 1);
            int random2 = StdRandom.uniform(1, width + 1);
            if (!p.isOpen(random1, random2)) {
                closed = true;
                p.open(random1, random2);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double top = C_I_RANGE * stddev();
        double bottom = Math.sqrt(t);
        return mean() - (top / bottom);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double top = C_I_RANGE * stddev();
        double bottom = Math.sqrt(t);
        return mean() + (top / bottom);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        double[] ci = new double[2];
        PercolationStats p = new PercolationStats(n, t);
        ci[0] = p.confidenceLo();
        ci[1] = p.confidenceHi();
        System.out.println("mean() = " + p.mean());
        System.out.println("stddev() = " + p.stddev());
        System.out
                .println("95% confidence interval = " + "[" + p.confidenceLo() + ", " + p
                        .confidenceHi() + ']');
    }
}
