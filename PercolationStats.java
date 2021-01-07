import java.util.Random;

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
        Random rand = new Random();

        boolean closed = false;
        while (!closed) {
            int random1 = rand.nextInt(width) + 1;
            int random2 = rand.nextInt(width) + 1;
            if (!p.isOpen(random1, random2)) {
                closed = true;
                p.open(random1, random2);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double sum = 0.0;
        for(int i = 0; i < thresholds.length ; i++){
            sum += thresholds[i];
        }
        return sum / thresholds.length;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double standardDeviation = 0.0;
        double calculatedMean = mean();
        for(double num: thresholds) {
            standardDeviation += Math.pow(num - calculatedMean, 2);
        }
        return standardDeviation;
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
        System.out.println("mean() = " + p.mean());
        System.out.println("stddev() = " + p.stddev());
        System.out
                .println("95% confidence interval = " + p.confidenceLo() + ", " + p
                        .confidenceHi());
    }
}
